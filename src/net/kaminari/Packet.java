package net.kaminari;

public class Packet {
	
	public IAckCallback onAcked;
	public static final int dataStart = Byte.BYTES * 2 + Short.BYTES + Byte.BYTES;
	
	protected Buffer buffer;
	
    public static Packet make(int opcode)
    {
        return new Packet(opcode);
    }

    // FIXME(gpascualg): Packets with ack callbacks
    public static Packet make(int opcode, IAckCallback onAcked)
    {
        Packet packet = Packet.make(opcode);
        packet.onAcked = onAcked;
        return packet;
    }
	
	public Packet(int opcode) {
		buffer = new Buffer();
		buffer.write(2, (short)opcode);
	}
	
	public Packet(Buffer buffer) {
		this.buffer = buffer;
	}
	
	public Buffer getData() {
		return buffer;
	}

	public byte getLength() {
		return this.buffer.readByte(0);
	}
	
	public Short getOpcode() {
		return this.buffer.readShort(2);
	}
	
	public byte getId() {
		return buffer.readByte(1);
	}
	
	public byte getOffset() {
		return this.buffer.readByte(5);
	}
	
	public byte getSize() {
		return (byte)buffer.getPosition();
	}

	public void finish(byte counter) {
		buffer.write(0, (byte)buffer.getPosition());
		buffer.write(1, counter);
	}
}
