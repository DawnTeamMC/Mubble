package fr.hugman.mubble.test.gametest.arcade;

import com.mojang.authlib.GameProfile;
import fr.hugman.mubble.network.protocol.common.custom.ArcadeCorrectionPayload;
import fr.hugman.mubble.network.protocol.common.custom.ArcadeInputPayload;
import fr.hugman.mubble.test.gametest.support.TestFlight;
import fr.hugman.mubble.world.arcade.ArcadeController;
import fr.hugman.mubble.world.arcade.ArcadePrediction;
import fr.hugman.mubble.world.arcade.ArcadeProfile;
import fr.hugman.mubble.world.arcade.sim.ArcadeInputFrame;
import fr.hugman.mubble.world.arcade.sim.MoveResult;
import io.netty.channel.embedded.EmbeddedChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ClientboundDisconnectPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.protocol.game.ServerboundAcceptTeleportationPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerLoadedPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

/**
 * A client of the arcade layer, connected to the test server through a real {@link Connection}.
 * <p>
 * The server side is an actual survival {@link ServerPlayer}, placed in the player list like any
 * other: every packet goes through the vanilla handlers, its speed check, its "moved wrongly" check
 * and its floating kick. The client side is a stand-in player stepping the shared simulation through
 * {@link ArcadePrediction}, the very code the game client runs, and sending what the client would
 * send, in the order it would send it.
 */
public final class ArcadeTestClient {
    private final GameTestHelper helper;
    private final ServerPlayer server;
    private final Player self;
    private final Connection connection;
    private final EmbeddedChannel channel;
    private final List<String> events = new ArrayList<>();

    private Vec3 lastSent;
    private boolean lastOnGround;
    private boolean lastHorizontalCollision;
    private int positionReminder;

    private int teleports;
    private int corrections;
    private int rejections;
    private int motions;
    private boolean kicked;

    private ArcadeTestClient(GameTestHelper helper, ServerPlayer server, Player self, Connection connection, EmbeddedChannel channel) {
        this.helper = helper;
        this.server = server;
        this.self = self;
        this.connection = connection;
        this.channel = channel;
    }

    /**
     * Joins the server as a survival player standing at the center of {@code pos}, in structure-relative
     * coordinates, with every move of {@code profile} forced on both sides, or without the arcade layer
     * when {@code profile} is {@code null}.
     */
    public static ArcadeTestClient join(GameTestHelper helper, BlockPos pos, @Nullable ResourceKey<ArcadeProfile> profile) {
        var level = helper.getLevel();
        var minecraftServer = level.getServer();
        var uuid = UUID.randomUUID();
        var cookie = CommonListenerCookie.createInitial(new GameProfile(uuid, "arcade-" + uuid.toString().substring(0, 8)), false);
        var player = new ServerPlayer(minecraftServer, level, cookie.gameProfile(), cookie.clientInformation());
        var connection = new Connection(PacketFlow.SERVERBOUND);
        var channel = new EmbeddedChannel(connection);
        minecraftServer.getPlayerList().placeNewPlayer(connection, player, cookie);
        player.setGameMode(GameType.SURVIVAL);
        new ServerboundPlayerLoadedPacket().handle(player.connection);

        var self = helper.makeMockPlayer(GameType.SURVIVAL);
        var client = new ArcadeTestClient(helper, player, self, connection, channel);
        var at = helper.absoluteVec(Vec3.atBottomCenterOf(pos));
        player.connection.teleport(at.x, at.y, at.z, 0.0F, 0.0F);
        client.receive();
        client.teleports = 0;
        client.events.clear();

        if (profile != null) {
            ArcadeTestKit.force(player, profile);
            ArcadeTestKit.force(self, profile);
        }
        return client;
    }

    /** The player as the server has it. */
    public ServerPlayer server() {
        return this.server;
    }

    /** The player as the client has it. */
    public Player self() {
        return this.self;
    }

    public ArcadeController controller() {
        return ArcadeController.of(this.self);
    }

    public ArcadeController serverController() {
        return ArcadeController.of(this.server);
    }

    /**
     * One tick of the client then of the connection on the server: the packets of the server are read,
     * the player steps, the step and the move are sent, and the server ticks the player.
     */
    public void tick(ArcadeInputFrame frame) {
        this.receive();
        if (this.kicked) {
            return;
        }
        var controller = this.controller();
        controller.tick();
        this.self.setOldPosAndRot();
        var step = ArcadePrediction.step(this.self, controller, frame);
        new ServerboundCustomPayloadPacket(step.payload()).handle(this.server.connection);
        this.sendPosition();
        this.server.connection.tick();
    }

    /**
     * One tick of a client lying about its step: it reports, and moves, {@code extraX} blocks further
     * along x than the simulation took it.
     */
    public void tickTampered(ArcadeInputFrame frame, double extraX) {
        this.receive();
        if (this.kicked) {
            return;
        }
        var controller = this.controller();
        controller.tick();
        this.self.setOldPosAndRot();
        var payload = ArcadePrediction.step(this.self, controller, frame).payload();
        var result = payload.result();
        var forged = new MoveResult(result.dx() + extraX, result.dy(), result.dz(), result.onGround(), result.horizontalCollision(), result.verticalCollision());
        this.self.setPos(this.self.position().add(extraX, 0.0D, 0.0D));
        new ServerboundCustomPayloadPacket(new ArcadeInputPayload(payload.frame(), payload.from(), forged, payload.fingerprint(), payload.impulse())).handle(this.server.connection);
        this.sendPosition();
        this.server.connection.tick();
    }

    /** Stands in for a client that does not run the arcade layer: it only reports {@code position}. */
    public void tickVanilla(Vec3 position, boolean onGround) {
        this.receive();
        if (this.kicked) {
            return;
        }
        this.self.setPos(position);
        this.self.setOnGround(onGround);
        this.sendPosition();
        this.server.connection.tick();
    }

    /** Sends the position the way the game client does: when it moved, and at least once a second. */
    private void sendPosition() {
        var position = this.self.position();
        this.positionReminder++;
        boolean move = this.lastSent == null || position.distanceToSqr(this.lastSent) > Mth.square(2.0E-4D) || this.positionReminder >= 20;
        boolean onGround = this.self.onGround();
        boolean horizontalCollision = this.self.horizontalCollision;
        if (move) {
            new ServerboundMovePlayerPacket.PosRot(position, this.self.getYRot(), this.self.getXRot(), onGround, horizontalCollision).handle(this.server.connection);
            this.lastSent = position;
            this.positionReminder = 0;
        } else if (onGround != this.lastOnGround || horizontalCollision != this.lastHorizontalCollision) {
            new ServerboundMovePlayerPacket.StatusOnly(onGround, horizontalCollision).handle(this.server.connection);
        }
        this.lastOnGround = onGround;
        this.lastHorizontalCollision = horizontalCollision;
    }

    /** Reads every packet the server sent since the last call, acting on the ones a client acts on. */
    public void receive() {
        Object message;
        while ((message = this.channel.readOutbound()) != null) {
            if (message instanceof Packet<?> packet) {
                this.handle(packet);
            }
        }
        if (!this.kicked && !this.connection.isConnected()) {
            this.kicked = true;
            this.events.add("disconnected");
        }
    }

    private void handle(Packet<?> packet) {
        if (packet instanceof ClientboundBundlePacket bundle) {
            bundle.subPackets().forEach(this::handle);
        } else if (packet instanceof ClientboundPlayerPositionPacket position) {
            // what the vanilla client does: take the position, accept the teleport, report back
            this.teleports++;
            this.events.add("teleported to " + position.change().position());
            var current = new PositionMoveRotation(this.self.position(), this.self.getDeltaMovement(), this.self.getYRot(), this.self.getXRot());
            var target = PositionMoveRotation.calculateAbsolute(current, position.change(), position.relatives());
            this.self.setPos(target.position());
            this.self.setDeltaMovement(target.deltaMovement());
            this.self.setYRot(target.yRot());
            this.self.setXRot(target.xRot());
            this.self.setOldPosAndRot();
            this.controller().markReset();
            new ServerboundAcceptTeleportationPacket(position.id()).handle(this.server.connection);
            new ServerboundMovePlayerPacket.PosRot(target.position(), target.yRot(), target.xRot(), false, false).handle(this.server.connection);
            this.lastSent = target.position();
        } else if (packet instanceof ClientboundSetEntityMotionPacket motion) {
            if (motion.id() == this.server.getId()) {
                this.motions++;
                this.self.setDeltaMovement(motion.movement());
            }
        } else if (packet instanceof ClientboundCustomPayloadPacket custom && custom.payload() instanceof ArcadeCorrectionPayload correction) {
            if (correction.rejected()) {
                this.rejections++;
                this.events.add("rejected at step " + correction.tick());
            } else {
                this.corrections++;
                this.events.add("corrected at step " + correction.tick());
            }
            ArcadePrediction.correct(this.self, this.controller(), correction);
        } else if (packet instanceof ClientboundDisconnectPacket disconnect) {
            this.kicked = true;
            this.events.add("kicked: " + disconnect.reason().getString());
        }
    }

    /** Leaves the server, so that the player does not linger in the level after the test. */
    public void leave() {
        TestFlight.reset(this.server);
        var players = this.helper.getLevel().getServer().getPlayerList();
        if (players.getPlayer(this.server.getUUID()) == this.server) {
            players.remove(this.server);
        }
    }

    /** Runs {@code command} as the server, with {@code @self} standing for this player. */
    public void serverCommand(String command) {
        var server = this.helper.getLevel().getServer();
        server.getCommands().performPrefixedCommand(server.createCommandSourceStack(), command.replace("@self", this.server.getScoreboardName()));
    }

    public int teleports() {
        return this.teleports;
    }

    public int corrections() {
        return this.corrections;
    }

    public int rejections() {
        return this.rejections;
    }

    public int motions() {
        return this.motions;
    }

    public boolean kicked() {
        return this.kicked;
    }

    /** What happened on the connection, for the message of a failed assertion. */
    public String events() {
        return this.events.isEmpty() ? "nothing" : String.join(", ", this.events);
    }

    /** Fails the test if the server pushed back on the movement in any way. */
    public void assertUndisturbed(String what) {
        this.receive();
        this.helper.assertFalse(this.kicked, what + ": the player was kicked (" + this.events() + ")");
        this.helper.assertValueEqual(this.teleports, 0, what + ": teleports by the server (" + this.events() + ")");
        this.helper.assertValueEqual(this.rejections, 0, what + ": rejected steps (" + this.events() + ")");
        this.helper.assertTrue(this.server.position().distanceTo(this.self.position()) < 1.0E-6D,
                what + ": the server has the player at " + this.server.position() + ", the client at " + this.self.position());
    }
}
