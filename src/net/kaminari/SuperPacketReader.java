package net.kaminari;

import java.util.ArrayList;

public class SuperPacketReader<PQ extends IProtocolQueues> {
	private Buffer buffer;
	private int ackEnd;
	private boolean hasAcks;
	
	public SuperPacketReader(byte[] data) {
		buffer = new Buffer(data);
	}
	
	public Short length() {
		return buffer.readShort(0);
	}
	
	public Short id() {
		return buffer.readShort(2);
	}
	
	public Short timestamp() {
		return buffer.readShort(4);
	}
	
	public ArrayList<Short> getAcks() {
		ArrayList<Short> acks = new ArrayList<Short>();
		ackEnd = Short.BYTES * 2;
		int numAcks = Byte.toUnsignedInt(buffer.readByte(ackEnd));
		hasAcks = numAcks != 0;
		ackEnd += Byte.BYTES;
		
		for (int i = 0; i < numAcks; ++i) {
			Short ack = buffer.readShort(ackEnd);
			acks.add(ack);
			ackEnd += Short.BYTES;
		}
		
		return acks;
	}
	
	public boolean hasData() {
		return buffer.readByte(ackEnd) != 0;
	}
	
	public boolean isPingPacket() {
		return !hasAcks && !hasData();
	}
	
	public <T extends IBaseClient> void handlePackets(Protocol<PQ> protocol, IHandlePacket handler, T client) {
		int numBlocks = Byte.toUnsignedInt(buffer.readByte(ackEnd));
		int blockPos = ackEnd + Byte.BYTES;
		
		int remaining = 500 - blockPos;
		for (int i = 0; i < numBlocks; ++i) {
			Short blockId = buffer.readShort(blockPos);
			int numPackets = Byte.toUnsignedInt(buffer.readByte(blockPos + Short.BYTES));
			if (numPackets == 0) {
				return;
			}
			
			blockPos += Short.BYTES + Byte.BYTES;
			remaining -= Short.BYTES + Byte.BYTES;
			
			for (int j = 0; j < numPackets && remaining > 0; ++j) {
				PacketReader packet = new PacketReader(new Buffer(buffer, blockPos, Packet.dataStart));
				int length = Byte.toUnsignedInt(packet.getLength());
				blockPos += length;
				remaining -= length;
				
				if (length < Packet.dataStart || remaining < 0) {
					return;
				}
				
				if (protocol.resolve(packet, blockId)) {
					if (!handler.handlePacket(packet, client)) {
						client.handlingError();
					}
				}
			}
		}
	}
}
