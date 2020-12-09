package net.kaminari.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketTimeoutException;
import net.kaminari.Buffer;
import net.kaminari.IBaseClient;
import net.kaminari.IMarshal;
import net.kaminari.IProtocol;
import net.kaminari.IProtocolQueues;
import net.kaminari.SuperPacket;

public abstract class Client implements IBaseClient {
	private List<byte[]> pendingPackets;
	private IMarshal marshal;
	private IProtocol<IProtocolQueues> protocol;
	private SuperPacket<IProtocolQueues> superPacket;

	public Client(IMarshal marshal, IProtocol<IProtocolQueues> protocol, IProtocolQueues queues) {
		pendingPackets = Collections.synchronizedList(new ArrayList<byte[]>());
		this.marshal = marshal;
		this.protocol = protocol;
		this.superPacket = new SuperPacket<IProtocolQueues>(queues);
	}
	
	public void updateInputs() {
		// TODO: Populate pendingPackets somewhere
		protocol.read(this, superPacket, marshal);
	}
	
	public void updateOutputs() throws IOException {
		Buffer buffer = protocol.update(this, superPacket);
		if (buffer != null) {
			send(buffer);
		}
	}
	
	public void tryReceive() throws IOException, SocketTimeoutException {
		 // TODO(gpascualg): Magic numbers
		byte[] data = new byte[500];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		receive(packet);
		pendingPackets.add(data);
	}
	
	public IProtocolQueues getSender() {
		return superPacket.getQueues();
	}
	
	@Override
	public void disconnect() {
		
	}

	@Override
	public boolean hasPendingSuperPackets() {
		return !pendingPackets.isEmpty();
	}

	@Override
	public short firstSuperPacketId() {
		// HACK(gpascualg): A big hack here...
		return (new Buffer(pendingPackets.get(0))).readShort(2);
	}

	@Override
	public byte[] popPendingSuperPacket() {
		byte[] data = pendingPackets.get(0);
		pendingPackets.remove(0);
		return data;
	}
	
	// ABSTRACT METHODS LEFT TO IMPLEMENTATION
	protected abstract void send(Buffer buffer) throws IOException;
	protected abstract void receive(DatagramPacket packet) throws IOException, SocketTimeoutException;
}
