package net.kaminari.packers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import net.kaminari.Constants;
import net.kaminari.IAckCallback;
import net.kaminari.IMarshal;
import net.kaminari.Overflow;
import net.kaminari.Packet;
import net.kaminari.Ref;

public class OrderedPacker extends Packer<Packet, IData> {
	protected boolean hasNewPacket;
	protected Short lastBlock;
	
	
	@Override
	public void onAck(ArrayList<PendingData<Packet>> toBeRemoved) {
		// Do nothing on purpose
	}

	@Override
	public void onClear() {
		// Do nothing on purpose
	}

	@Override
	public void add(Packet packet) {
		pending.add(new PendingData<Packet>(packet));
		hasNewPacket = true;
	}

	@Override
	public void add(IMarshal marshal, Short opcode, IData data, IAckCallback callback) {
		Packet packet = Packet.make(opcode, callback);
		data.pack(marshal, packet);
		add(packet);
	}

	@Override
	public void process(IMarshal marshal, Short blockId, Ref<Short> remaining, TreeMap<Integer, ArrayList<Packet>> byBlock) {
		if (!isPending(blockId)) {
			return;
		}
		
		int numInserted = 0;
		for (PendingData<Packet> pnd : reverse(pending)) {
			Integer actualBlock = (int)getActualBlock(pnd.blocks, blockId);
			Short size = (short)pnd.data.getSize();
			
			if (byBlock.containsKey(actualBlock)) {
				if (size > remaining.value) {
					break;
				}
				
				byBlock.get(actualBlock).add(pnd.data);
			}
			else {
				size = (short) (size + 4);
				if (size > remaining.value) {
					break;
				}
				
				byBlock.put(actualBlock, new ArrayList<Packet>());
				byBlock.get(actualBlock).add(pnd.data);
			}
			
			pnd.blocks.add(blockId);
			remaining.value = (short) (remaining.value - size);
			++numInserted;
		}
		
		if (numInserted > 0) {
			hasNewPacket = false;
			lastBlock = blockId;
		}
	}
	
	protected boolean isPending(Short blockId) {
		if (pending.size() == 0) {
			return false;
		}
		
		return hasNewPacket ||
				Overflow.sub(blockId, lastBlock) >= Constants.ResendThreshold;
	}
	
	static <T> List<T> reverse(final List<T> list) {
	    final List<T> result = new ArrayList<>(list);
	    Collections.reverse(result);
	    return result;
	}
}
