package fr.hugman.mubble.splatoon.client.ink;

import com.mojang.blaze3d.vertex.VertexConsumer;
import fr.hugman.mubble.splatoon.world.level.ink.InkGrid;
import fr.hugman.mubble.splatoon.world.level.ink.InkStyle;
import fr.hugman.mubble.splatoon.world.level.ink.InkSurfaces;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FaceInfo;
import net.minecraft.client.model.geom.builders.UVPair;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.BlockStateModelSet;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Bakes ink into the mesh of the chunk section holding it, so that drawn ink costs nothing more than the blocks under
 * it once the section is built.
 *
 * <p>Each row of cells sharing a style and a depth becomes a single quad, tinted with the color of its ink and
 * textured with the sprite of its type: {@code <namespace>:block/ink/<path>} in the block atlas. Any texture placed
 * there by a resource pack is picked up, animated ones included, and unknown types fall back on the normal ink.
 *
 * <p>Coated blocks are drawn a second time, right over themselves, in the color of their ink: the quads of their own
 * model, with their own texture, which keeps the shape of every blade of grass. Textures meant to be tinted (grass,
 * leaves, vines...) take the color of the ink fully.
 *
 * @author Hugman
 * @since v4.0.0
 */
public final class InkMesher {
    /**
     * How far ink floats above the surface it is painted on, to stay clear of it in the depth buffer.
     */
    private static final float OFFSET = 1.0F / 512.0F;

    private static final Direction[] DIRECTIONS = Direction.values();

    private InkMesher() {
    }

    public static void mesh(SectionPos sectionPos, BlockAndTintGetter region, Function<ChunkSectionLayer, VertexConsumer> layers, BlockStateModelSet models) {
        var section = ClientInk.section(sectionPos.asLong());
        if (section == null) {
            return;
        }
        meshFaces(section.faces(), region, () -> layers.apply(ChunkSectionLayer.CUTOUT));
        if (!section.coats().isEmpty()) {
            meshCoats(section.coats(), region, layers, models);
        }
    }

    private static void meshCoats(Long2ObjectMap<InkStyle> coats, BlockAndTintGetter region, Function<ChunkSectionLayer, VertexConsumer> layers, BlockStateModelSet models) {
        var random = RandomSource.createThreadLocalInstance(0L);
        List<BlockStateModelPart> parts = new ArrayList<>();
        var lighting = region.cardinalLighting();

        for (var entry : coats.long2ObjectEntrySet()) {
            var pos = BlockPos.of(entry.getLongKey());
            var state = region.getBlockState(pos);
            if (state.isAir()) {
                continue;
            }
            // the same parts the block itself was drawn with
            random.setSeed(state.getSeed(pos));
            parts.clear();
            models.get(state).collectParts(random, parts);

            var offset = state.getOffset(pos);
            float x = SectionPos.sectionRelative(pos.getX()) + (float) offset.x;
            float y = SectionPos.sectionRelative(pos.getY()) + (float) offset.y;
            float z = SectionPos.sectionRelative(pos.getZ()) + (float) offset.z;
            int color = ARGB.opaque(entry.getValue().color());
            int light = LightCoordsUtil.getLightCoords(region, pos);

            for (var part : parts) {
                for (int d = -1; d < DIRECTIONS.length; d++) {
                    var cullFace = d < 0 ? null : DIRECTIONS[d];
                    if (cullFace != null && !Block.shouldRenderFace(state, region.getBlockState(pos.relative(cullFace)), cullFace)) {
                        continue;
                    }
                    for (var quad : part.getQuads(cullFace)) {
                        var material = quad.materialInfo();
                        var consumer = layers.apply(material.layer());
                        int shaded = material.shade() ? ARGB.scaleRGB(color, lighting.byFace(quad.direction())) : color;
                        var normal = quad.direction().getUnitVec3f();
                        for (int vertex = 0; vertex < 4; vertex++) {
                            var position = quad.position(vertex);
                            long uv = quad.packedUV(vertex);
                            consumer.addVertex(x + position.x(), y + position.y(), z + position.z(), shaded,
                                    UVPair.unpackU(uv), UVPair.unpackV(uv), OverlayTexture.NO_OVERLAY, light,
                                    normal.x(), normal.y(), normal.z());
                        }
                    }
                }
            }
        }
    }

    private static void meshFaces(List<ClientInk.Face> faces, BlockAndTintGetter region, Supplier<VertexConsumer> output) {
        if (faces.isEmpty()) {
            return;
        }
        var atlas = Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.BLOCKS);
        VertexConsumer consumer = null;

        for (var face : faces) {
            var pos = face.pos();
            var side = face.side();
            var ink = face.ink();
            var surfaces = InkSurfaces.of(region.getBlockState(pos));
            if (surfaces.surfaces(side) == 0L) {
                continue;
            }
            if (consumer == null) {
                consumer = output.get();
            }

            var palette = ink.palette();
            var sprites = new TextureAtlasSprite[palette.size()];
            var colors = new int[palette.size()];
            float shade = region.cardinalLighting().byFace(side);
            for (int i = 0; i < palette.size(); i++) {
                var style = palette.get(i);
                sprites[i] = sprite(atlas, style.type());
                colors[i] = ARGB.scaleRGB(ARGB.opaque(style.color()), shade);
            }
            int frontLight = LightCoordsUtil.getLightCoords(region, pos.relative(side));
            int ownLight = LightCoordsUtil.getLightCoords(region, pos);

            float x = SectionPos.sectionRelative(pos.getX());
            float y = SectionPos.sectionRelative(pos.getY());
            float z = SectionPos.sectionRelative(pos.getZ());

            for (int v = 0; v < InkGrid.SIZE; v++) {
                int u = 0;
                while (u < InkGrid.SIZE) {
                    int cell = InkGrid.cell(u, v);
                    int index = ink.paletteIndex(cell);
                    float depth = surfaces.depth(side, cell);
                    if (index == 0 || Float.isNaN(depth)) {
                        u++;
                        continue;
                    }
                    // merge the rest of the row as long as it looks the same
                    int end = u + 1;
                    while (end < InkGrid.SIZE
                            && ink.paletteIndex(InkGrid.cell(end, v)) == index
                            && surfaces.depth(side, InkGrid.cell(end, v)) == depth) {
                        end++;
                    }
                    quad(consumer, x, y, z, side,
                            (float) u / InkGrid.SIZE, (float) v / InkGrid.SIZE,
                            (float) end / InkGrid.SIZE, (float) (v + 1) / InkGrid.SIZE,
                            depth, sprites[index - 1], colors[index - 1], depth == 0.0F ? frontLight : ownLight);
                    u = end;
                }
            }
        }
    }

    private static TextureAtlasSprite sprite(TextureAtlas atlas, Identifier type) {
        var sprite = atlas.getSprite(type.withPrefix("block/ink/"));
        if (sprite == atlas.missingSprite() && !type.equals(InkStyle.NORMAL)) {
            return sprite(atlas, InkStyle.NORMAL);
        }
        return sprite;
    }

    private static void quad(VertexConsumer consumer, float x, float y, float z, Direction side,
                             float u0, float v0, float u1, float v1, float depth,
                             TextureAtlasSprite sprite, int color, int light) {
        var axis = side.getAxis();
        float w = side.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 1.0F - depth + OFFSET : depth - OFFSET;
        var min = switch (axis) {
            case X -> new Vector3f(w, v0, u0);
            case Y -> new Vector3f(u0, w, v0);
            case Z -> new Vector3f(u0, v0, w);
        };
        var max = switch (axis) {
            case X -> new Vector3f(w, v1, u1);
            case Y -> new Vector3f(u1, w, v1);
            case Z -> new Vector3f(u1, v1, w);
        };
        var normal = side.getUnitVec3f();
        var faceInfo = FaceInfo.fromFacing(side);

        for (int vertex = 0; vertex < 4; vertex++) {
            var position = faceInfo.getVertexInfo(vertex).select(min, max);
            float u = (float) InkGrid.u(axis, position.x(), position.y(), position.z());
            float v = (float) InkGrid.v(axis, position.x(), position.y(), position.z());
            consumer.addVertex(
                    x + position.x(), y + position.y(), z + position.z(),
                    color,
                    sprite.getU(u), sprite.getV(axis == Direction.Axis.Y ? v : 1.0F - v),
                    OverlayTexture.NO_OVERLAY, light,
                    normal.x(), normal.y(), normal.z()
            );
        }
    }
}
