package net.kaminari;

public class Overflow {
	public static boolean le(Byte xs, Byte ys) 
	{
		int x = Byte.toUnsignedInt(xs);
		int y = Byte.toUnsignedInt(ys);
		int threshold = 255 / 2;
		int comparison = Integer.compareUnsigned(x, y);
		return 
				(comparison < 0 && (y - x) < threshold) ||
				(comparison > 0 && (x - y) > threshold);
	}
	
	public static boolean leq(Byte xs, Byte ys) 
	{
		int x = Byte.toUnsignedInt(xs);
		int y = Byte.toUnsignedInt(ys);
		int threshold = 255 / 2;
		int comparison = Integer.compareUnsigned(x, y);
		return 
				(comparison <= 0 && (y - x) < threshold) ||
				(comparison > 0 && (x - y) > threshold);
	}
	
	public static boolean le(Short xs, Short ys) 
	{
		int x = Short.toUnsignedInt(xs);
		int y = Short.toUnsignedInt(ys);
		int threshold = 65535 / 2;
		int comparison = Integer.compareUnsigned(x, y);
		return 
				(comparison < 0 && (y - x) < threshold) ||
				(comparison > 0 && (x - y) > threshold);
	}
	
	public static boolean leq(Short xs, Short ys) 
	{
		int x = Short.toUnsignedInt(xs);
		int y = Short.toUnsignedInt(ys);
		int threshold = 65535 / 2;
		int comparison = Integer.compareUnsigned(x, y);
		return 
				(comparison <= 0 && (y - x) < threshold) ||
				(comparison > 0 && (x - y) > threshold);
	}
	
	public static boolean le(Integer x, Integer y) 
	{
		int threshold = (int)(4294967295L / 2 - 1);
		int comparison = Integer.compareUnsigned(x, y);
		return 
				(comparison < 0 && (y - x) < threshold) ||
				(comparison > 0 && (x - y) > threshold);
	}
	
	public static boolean leq(Integer x, Integer y) 
	{
		int threshold = (int)(4294967295L / 2 - 1);
		int comparison = Integer.compareUnsigned(x, y);
		return 
				(comparison <= 0 && (y - x) < threshold) ||
				(comparison > 0 && (x - y) > threshold);
	}
	
	public static boolean le(Long x, Long y) 
	{
		long threshold = 9223372036854775806L;
		int comparison = Long.compareUnsigned(x, y);
		return 
				(comparison <= 0 && (y - x) < threshold) ||
				(comparison > 0 && (x - y) > threshold);
	}
	
	public static boolean leq(Long x, Long y) 
	{
		long threshold = 9223372036854775806L;
		int comparison = Long.compareUnsigned(x, y);
		return 
				(comparison <= 0 && (y - x) < threshold) ||
				(comparison > 0 && (x - y) > threshold);
	}

	public static boolean ge(Byte xs, Byte ys) 
	{
		int x = Byte.toUnsignedInt(xs);
		int y = Byte.toUnsignedInt(ys);
		int threshold = 255 / 2;
		int comparison = Integer.compareUnsigned(x, y);
		return 
				(comparison > 0 && (x - y) < threshold) ||
				(comparison < 0 && (y - x) > threshold);
	}

	public static boolean geq(Byte xs, Byte ys) 
	{
		int x = Byte.toUnsignedInt(xs);
		int y = Byte.toUnsignedInt(ys);
		int threshold = 255 / 2;
		int comparison = Integer.compareUnsigned(x, y);
		return 
				(comparison >= 0 && (x - y) < threshold) ||
				(comparison < 0 && (y - x) > threshold);
	}
	
	public static boolean ge(Short xs, Short ys) 
	{
		int x = Short.toUnsignedInt(xs);
		int y = Short.toUnsignedInt(ys);
		int threshold = 65535 / 2;
		int comparison = Integer.compareUnsigned(x, y);
		return 
				(comparison > 0 && (x - y) < threshold) ||
				(comparison < 0 && (y - x) > threshold);
	}
	
	public static boolean geq(Short xs, Short ys) 
	{
		int x = Short.toUnsignedInt(xs);
		int y = Short.toUnsignedInt(ys);
		int threshold = 65535 / 2;
		int comparison = Integer.compareUnsigned(x, y);
		return 
				(comparison >= 0 && (x - y) < threshold) ||
				(comparison < 0 && (y - x) > threshold);
	}
	
	public static boolean ge(Integer x, Integer y) 
	{
		int threshold = (int)(4294967295L / 2 - 1);
		int comparison = Integer.compareUnsigned(x, y);
		return 
				(comparison > 0 && (x - y) < threshold) ||
				(comparison < 0 && (y - x) > threshold);
	}
	
	public static boolean geq(Integer x, Integer y) 
	{
		int threshold = (int)(4294967295L / 2 - 1);
		int comparison = Integer.compareUnsigned(x, y);
		return 
				(comparison >= 0 && (x - y) < threshold) ||
				(comparison < 0 && (y - x) > threshold);
	}
	
	public static boolean ge(Long x, Long y) 
	{
		long threshold = 9223372036854775806L;
		int comparison = Long.compareUnsigned(x, y);
		return 
				(comparison > 0 && (x - y) < threshold) ||
				(comparison < 0 && (y - x) > threshold);
	}
	
	public static boolean geq(Long x, Long y) 
	{
		long threshold = 9223372036854775806L;
		int comparison = Long.compareUnsigned(x, y);
		return 
				(comparison >= 0 && (x - y) < threshold) ||
				(comparison < 0 && (y - x) > threshold);
	}
	
	public static Short sub(Short xs, Short ys) 
	{
		int x = Short.toUnsignedInt(xs);
		int y = Short.toUnsignedInt(ys);
		int comparison = Integer.compareUnsigned(x, y);
		return (short)(
				(comparison >= 0) ? (x - y) : (65535 - y + x));
	}
}
