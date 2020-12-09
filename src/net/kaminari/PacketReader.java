package net.kaminari;

public class PacketReader extends Packet {
	public PacketReader(Buffer buffer) {
		super(buffer);
	}
	
	public long timestamp() {
		return 0;
	}
	
	public int bytesRead() {
		return buffer.getPosition();
	}
	
	public int bufferSize() {
		return buffer.getSize();
	}
}
