package fr.hugman.mubble.world.arcade.move;

/**
 * One number a move reads from the profile it runs under.
 * <p>
 * Moves never hold a tuning constant of their own: whatever they need is declared as one of these,
 * and the profile gives it a value. The default only stands in for a profile that leaves the
 * parameter out, which is why the shipped profiles spell every one of them out.
 *
 * @param name         key of the parameter in the {@code params} object of the move
 * @param defaultValue value used when the profile does not give one
 * @param min          lowest value a profile may give
 * @param max          highest value a profile may give
 * @param unit         what the number is measured in, for the documentation
 */
public record MoveParam(String name, double defaultValue, double min, double max, String unit) {
    public static MoveParam of(String name, double defaultValue, double min, double max, String unit) {
        return new MoveParam(name, defaultValue, min, max, unit);
    }

    /** A length in blocks. */
    public static MoveParam blocks(String name, double defaultValue) {
        return of(name, defaultValue, 0.0D, 64.0D, "blocks");
    }

    /** A speed in blocks per tick. */
    public static MoveParam speed(String name, double defaultValue) {
        return of(name, defaultValue, 0.0D, 16.0D, "blocks/tick");
    }

    /** A duration in ticks. */
    public static MoveParam ticks(String name, double defaultValue) {
        return of(name, defaultValue, 0.0D, 1200.0D, "ticks");
    }

    /** A plain factor. */
    public static MoveParam factor(String name, double defaultValue) {
        return of(name, defaultValue, 0.0D, 16.0D, "factor");
    }

    /** An angle in degrees. */
    public static MoveParam degrees(String name, double defaultValue) {
        return of(name, defaultValue, 0.0D, 360.0D, "degrees");
    }

    public boolean accepts(double value) {
        return value >= this.min && value <= this.max && !Double.isNaN(value);
    }
}
