package net.kaminari.packers;

import java.util.ArrayList;
import java.util.TreeMap;

import net.kaminari.IAckCallback;
import net.kaminari.IMarshal;
import net.kaminari.Packet;
import net.kaminari.Ref;

public class ImmediatePacker extends Packer<Packet, IData> {


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
	}

	@Override
	public void add(IMarshal marshal, Short opcode, IData data, IAckCallback callback) {
		Packet packet = Packet.make(opcode, callback);
		data.pack(marshal, packet);
		add(packet);
	}

	@Override
	public void process(IMarshal marshal, Short blockId, Ref<Short> remaining, TreeMap<Integer, ArrayList<Packet>> byBlock) {
		for (PendingData<Packet> pnd : pending) {
			if (!isPending(pnd.blocks, blockId, false)) {
				continue;
			}
			
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
		}
	}


}
