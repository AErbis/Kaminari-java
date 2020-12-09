package net.kaminari;

public interface IMarshal extends IHandlePacket {
	<T> int size(Class<T> cls);
}
