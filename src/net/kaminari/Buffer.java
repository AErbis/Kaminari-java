package net.kaminari;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class Buffer {
	
	public long id;
	public short opcode;
	
	public byte[] body;
	public int offset;
	public int index;
	
	public boolean alreadySent = false;
	

	public Buffer() {
		body = new byte[500];
		offset = 0;
		index = 5;
	}

	public Buffer(Buffer other) {
		body = other.body.clone();
		offset = other.offset;
		index = other.index;
	}

	public Buffer(Buffer other, int offset, int index) {
		body = other.body;
		this.offset = offset;
		this.index = index;
	}
	
	public Buffer(byte[] data) {
		body = data;
		offset = 0;
		index = 0;
	}
	
	public int getPosition() {
		return index;
	}

	public void reset() {
		offset = 0;
		index = 0;
	}
	
	public int getSize(){
		return body.length;
	}
	
	public void write(byte _data) {
		body[offset + index++] = _data;
	}
	
	public void write(int position, byte _data) {
		body[offset + position] = _data;
	}
	
	public void write(int position, short _data) {
		byte[] bytes = ByteBuffer.allocate(Short.BYTES)
				.order(ByteOrder.LITTLE_ENDIAN)
				.putShort(_data).array();
		body[offset + position] = bytes[0];
		body[offset + position + 1] = bytes[1];
	}
	
	public void setOpcode(short _opcode) {
		opcode = _opcode;
	}
	
	public void write(short _data) {
		write(ByteBuffer.allocate(Short.BYTES)
				.order(ByteOrder.LITTLE_ENDIAN)
				.putShort(_data).array());
	}
	
	public void write(int _data) {
		write(ByteBuffer.allocate(Integer.BYTES)
				.order(ByteOrder.LITTLE_ENDIAN)
				.putInt(_data).array());
	}
	
	public void write(float _data) {
		write(ByteBuffer.allocate(Float.BYTES)
				.order(ByteOrder.LITTLE_ENDIAN)
				.putFloat(_data).array());
	}
	
	public void write(long _data) {
		write(ByteBuffer.allocate(Long.BYTES)
				.order(ByteOrder.LITTLE_ENDIAN)
				.putLong(_data).array());
	}
	
	public void write(double _data) {
		write(ByteBuffer.allocate(Double.BYTES)
				.order(ByteOrder.LITTLE_ENDIAN)
				.putDouble(_data).array());
	}
	
	public void write(boolean _data) {
		write((byte)(_data ? 1 : 0));
	}
	
	public void write(String _data) {
		write((byte)_data.length());
		for (int i = 0; i < _data.length(); ++i)
		{
			write((byte)_data.charAt(i));
		}
	}
	
	public void write(byte[] _data) {
		for (byte b : _data) {
			body[offset + index++] = b;
		}
	}

	public char readCharacter() {
		return (char)readByte(index++);
	}
	
	public byte readByte() {
		return readByte(index++);
	}
	
	public byte readByte(int pos) {
		byte toReturn = body[offset + pos];
		return toReturn;
	}
	
	public short readShort() {
		short toReturn = readShort(index);
		index += 2;
		return toReturn;
	}
	
	public short readShort(int pos) {
	    short MASK = 0xFF;
	    int result = 0;
	    	  result = (int)body[offset + pos] & MASK;
	    	  result = result + (((int)body[offset + pos + 1] & MASK) << 8);
		return (short)result;
	}
	
	public int readInteger() {
		int toReturn = readInteger(index);
		index += 4;
		return toReturn;
	}
	
	public int readInteger(int pos) {
	    int MASK = 0xFF;
	    int result = 0;   
	        result = (int)body[offset + pos++] & MASK;
	        result = result + (((int)body[offset + pos++] & MASK) << 8);
	        result = result + (((int)body[offset + pos++] & MASK) << 16);
	        result = result + (((int)body[offset + pos] & MASK) << 24); 
	    return result;
	}
	
	public float readFloat() {
		float toReturn = ByteBuffer.allocate(Float.BYTES)
				.order(ByteOrder.LITTLE_ENDIAN)
				.put(Arrays.copyOfRange(body, offset + index, offset + index + Float.BYTES))
				.getFloat(0);
		index += Float.BYTES;
		return toReturn;
	}
	
	public long readLong() {
		long toReturn = ByteBuffer.allocate(Long.BYTES)
				.order(ByteOrder.LITTLE_ENDIAN)
				.put(Arrays.copyOfRange(body, offset + index, offset + index + Long.BYTES))
				.getLong(0);
		index += Long.BYTES;
		return toReturn;
	}
	
	public double readDouble() {
		return Double.longBitsToDouble(readLong());
	}
	
	public boolean readBoolean() {
		return body[offset + index++] == (byte)1; 
	}
	
	public String readString() {
		String str = new String();
		int len = (int)readByte();
		for (int i = 0; i < len; ++i)
		{
			str += (char)readByte();
		}
		return str;
	}
	
	public byte peekByte() {
		return readByte(index);
	}
	
	public void write(Packet packet) {
		Buffer other = packet.getData();
		for (int i = 0; i < other.getPosition(); ++i) {
			body[offset + index++] = other.body[i];
		}
	}
	
	public void move(int position) {
		this.index = position;
	}
	


}
