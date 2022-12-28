package fr.hugman.mubble.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import java.util.function.Function;

public class MubbleCodecs {
	public static final Codec<Float> NONNEGATIVE_FLOAT = rangedFloat(0.0F, Float.MAX_VALUE, v -> "Value must be non-negative: " + v);
	public static final Codec<Long> NONNEGATIVE_LONG = rangedLong(0L, Long.MAX_VALUE, v -> "Value must be non-negative: " + v);

	private static Codec<Long> rangedLong(long min, long max, Function<Long, String> messageFactory) {
		Function<Long, DataResult<Long>> function = createRangeChecker(min, max, messageFactory);
		return Codec.LONG.flatXmap(function, function);
	}

	private static Codec<Float> rangedFloat(float min, float max, Function<Float, String> messageFactory) {
		Function<Float, DataResult<Float>> function = createRangeChecker(min, max, messageFactory);
		return Codec.FLOAT.flatXmap(function, function);
	}

	private static <N extends Number> Function<N, DataResult<N>> createRangeChecker(N min, N max, Function<N, String> messageFactory) {
		return value -> {
			if(((Comparable) value).compareTo(min) >= 0 && ((Comparable) value).compareTo(max) <= 0) {
				return DataResult.success(value);
			}
			return DataResult.error((String) messageFactory.apply(value));
		};
	}
}
