package net.kaminari.packers;

import java.util.ArrayList;
import java.util.TreeMap;

import net.kaminari.IAckCallback;
import net.kaminari.IMarshal;
import net.kaminari.Packet;
import net.kaminari.Ref;

public class MergePacker<G extends IHasDataVector<D>, D extends IHasId> extends Packer<D, D> {
	private Class<G> cls;
	private Short opcode;
	
	MergePacker(Class<G> cls, Short opcode) {
		this.cls = cls;
		this.opcode = opcode;
	}
	
	@Override
	public void onAck(ArrayList<PendingData<D>> toBeRemoved) {
		// Do nothing on purpose
	}

	@Override
	public void onClear() {
		// Do nothing on purpose
	}

	@Override
	public void add(Packet packet) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(IMarshal marshal, Short opcode, D data, IAckCallback callback) {
		pending.add(new PendingData<D>(data));
	}

	@Override
	public void process(IMarshal marshal, Short blockId, Ref<Short> remaining, TreeMap<Integer, ArrayList<Packet>> byBlock) {
		if (pending.size() == 0) {
			return;
		}
		
		try {
			G global = cls.newInstance();
			global.initialize();
			Short size = (short) (6 + 2 + newBlockCost(blockId, byBlock));
			
			for (PendingData<D> pnd : pending) {
				if (isPending(pnd.blocks, blockId, false)) {
					break;
				}
				
				Short nextSize = (short)(size + pnd.data.size(marshal));
				int nextSizeUInt = Short.toUnsignedInt(nextSize);
				int remainingUInt = Short.toUnsignedInt(remaining.value);
				if (Integer.compareUnsigned(nextSizeUInt, remainingUInt) > 0) {
					break;
				}
				
				size = nextSize;
				global.getData().add(pnd.data);
				pnd.blocks.add(blockId);
			}
			
			if (global.getData().size() == 0) {
				return;
			}
			
			Packet packet = Packet.make(opcode);
			global.pack(marshal, packet);
			remaining.value = (short) (remaining.value - size);
			
			Integer key = (int)blockId;
			if (byBlock.containsKey(key)) {
				byBlock.get(key).add(packet);
			}
			else {
				byBlock.put(key, new ArrayList<Packet>());
				byBlock.get(key).add(packet);
			}
			
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
