package net.kaminari.queues;

import java.util.ArrayList;
import java.util.TreeMap;

import net.kaminari.IAckCallback;
import net.kaminari.IMarshal;
import net.kaminari.Packet;
import net.kaminari.Ref;
import net.kaminari.packers.IData;
import net.kaminari.packers.IPacker;

public class UnreliableQueue<P extends IPacker<T, IData>, T> {
	private P packer;
	private Short maxRetries = 0;
	
	public UnreliableQueue(P packer) {
		this.packer = packer;
	}
	
	public UnreliableQueue(P packer, Short maxRetries) {
		this.packer = packer;
		this.maxRetries = maxRetries;
	}
	
	public void add(IMarshal marshal, Short opcode, IData data, IAckCallback callback) {
		packer.add(marshal, opcode, data, callback);
	}
	
	public void add(Packet packet) {
		packer.add(packet);
	}
	
	public void process(IMarshal marshal, Short blockId, Ref<Short> remaining, TreeMap<Integer, ArrayList<Packet>> byBlock) {
		packer.process(marshal, blockId, remaining, byBlock);
		
		if (maxRetries == 0) {
			packer.clear();
		}
		else {
			packer.removeByCount(maxRetries);
		}
	}
	
	public void ack(Short blockId) {
		if (maxRetries > 0) {
			packer.ack(blockId);	
		}
	}
	
	public void clear() {
		packer.clear();
	}
}

