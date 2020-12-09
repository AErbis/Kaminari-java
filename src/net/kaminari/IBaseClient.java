package net.kaminari;

public interface IBaseClient {
	boolean hasPendingSuperPackets();
	short firstSuperPacketId();
	byte[] popPendingSuperPacket();
	
	void disconnect();
	void handlingError();
}
