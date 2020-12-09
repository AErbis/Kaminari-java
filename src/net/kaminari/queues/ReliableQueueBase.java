package net.kaminari.queues;

import java.util.ArrayList;
import java.util.TreeMap;

import net.kaminari.IAckCallback;
import net.kaminari.IMarshal;
import net.kaminari.Packet;
import net.kaminari.Ref;
import net.kaminari.packers.IData;
import net.kaminari.packers.IPacker;

public class ReliableQueueBase<P extends IPacker<T, D>, T, D extends IData> {
	private P packer;
	
	ReliableQueueBase(P packer) {
		this.packer = packer;
	}
	
	public void add(IMarshal marshal, Short opcode, D data, IAckCallback callback) {
		packer.add(marshal, opcode, data, callback);
	}
	
	public void add(Packet packet) {
		packer.add(packet);
	}
	
	public void process(IMarshal marshal, Short blockId, Ref<Short> remaining, TreeMap<Integer, ArrayList<Packet>> byBlock) {
		packer.process(marshal, blockId, remaining, byBlock);
	}
	
	public void ack(Short blockId) {
		packer.ack(blockId);
	}
	
	public void clear() {
		packer.clear();
	}
}

