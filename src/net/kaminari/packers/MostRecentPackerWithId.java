package net.kaminari.packers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map;


import net.kaminari.IAckCallback;
import net.kaminari.IMarshal;
import net.kaminari.Packet;
import net.kaminari.Ref;
import net.kaminari.queues.PacketWithId;

public class MostRecentPackerWithId extends Packer<PacketWithId, IHasId> {

	private Integer opcode;
	private Map<Long, PendingData<PacketWithId>> idMap = new HashMap<Long, PendingData<PacketWithId>>();
	
	@Override
	public void onAck(ArrayList<PendingData<PacketWithId>> toBeRemoved) {
		for(PendingData<PacketWithId> pending : toBeRemoved) {
			idMap.remove(pending.data.id);
		}
	}

	@Override
	public void onClear() {
		idMap.clear();
	}

	@Override
	public void add(Packet packet) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(IMarshal marshal, Short opcode, IHasId data, IAckCallback callback) {
		
	}
	
	private void add(Packet packet, Long id) {
		if(idMap.containsKey(id)) {
			idMap.get(id).data.packet = packet;
			idMap.get(id).blocks.clear();
		}
		else {
			PendingData<PacketWithId> pending = new PendingData<PacketWithId>(new PacketWithId(packet, id));
		}
	}

	@Override
	public void process(IMarshal marshal, Short blockId, Ref<Short> remaining, TreeMap<Integer, ArrayList<Packet>> byBlock) {
	}

}
