package net.kaminari;

public class Unsigned {
	public static boolean ge(Short x, Short y) {
		int unsignedX = Short.toUnsignedInt(x);
		int unsignedY = Short.toUnsignedInt(y);
		return Integer.compareUnsigned(unsignedX, unsignedY) > 0;
	}
	
	public static boolean geq(Short x, Short y) {
		int unsignedX = Short.toUnsignedInt(x);
		int unsignedY = Short.toUnsignedInt(y);
		return Integer.compareUnsigned(unsignedX, unsignedY) >= 0;
	}
	
	public static boolean le(Short x, Short y) {
		int unsignedX = Short.toUnsignedInt(x);
		int unsignedY = Short.toUnsignedInt(y);
		return Integer.compareUnsigned(unsignedX, unsignedY) < 0;
	}
	
	public static boolean leq(Short x, Short y) {
		int unsignedX = Short.toUnsignedInt(x);
		int unsignedY = Short.toUnsignedInt(y);
		return Integer.compareUnsigned(unsignedX, unsignedY) <= 0;
	}
}
