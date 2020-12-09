package net.kaminari;

public interface IProtocol<PQ extends IProtocolQueues> {
	public boolean read(IBaseClient client, SuperPacket<PQ> superpacket, IHandlePacket handler);
	public Buffer update(IBaseClient client, SuperPacket<PQ> superpacket);
}