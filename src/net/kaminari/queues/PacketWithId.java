package net.kaminari.queues;

import net.kaminari.Packet;

public class PacketWithId {
	public Packet packet;
	public Long id;
	
	public PacketWithId(Packet packet, Long id) {
		this.packet = packet;
		this.id = id;
	}
}
