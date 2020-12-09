package net.kaminari;

public interface IHandlePacket {
	<T extends IBaseClient> boolean handlePacket(PacketReader packet, T client);
}
