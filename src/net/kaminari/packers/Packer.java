package net.kaminari.packers;

import java.util.ArrayList;
import java.util.TreeMap;

import net.kaminari.Constants;
import net.kaminari.IAckCallback;
import net.kaminari.IMarshal;
import net.kaminari.Overflow;
import net.kaminari.Packet;
import net.kaminari.Ref;

public abstract class Packer<T, D extends IData> implements IPacker<T, D> {
	
	protected ArrayList<PendingData<T>> pending = new ArrayList<PendingData<T>>(25);
	
	@Override
	public abstract void onAck(ArrayList<PendingData<T>> toBeRemoved);

	@Override
	public abstract void onClear();

	@Override
	public abstract void add(Packet packet);

	@Override
	public abstract void add(IMarshal marshal, Short opcode, D data, IAckCallback callback);
	
	@Override
	public abstract void process(IMarshal marshal, Short blockId, Ref<Short> remaining, TreeMap<Integer, ArrayList<Packet>> byBlock);

	@Override
	public void ack(Short blockId) {
		ArrayList<PendingData<T>> toFind = new ArrayList<PendingData<T>>();
		PendingData<T> p;
		for(int i = pending.size() -1; i >= 0; --i) {
			p = pending.get(i);
			for(Short s : p.blocks) {
				if(s == blockId) {
					toFind.add(p);
					pending.remove(i);
					break;
				}
			}
		}
		onAck(toFind);
	}

	@Override
	public void clear() {
		this.onClear();
		pending.clear();
	}

	@Override
	public boolean isPending(ArrayList<Short> blocks, Short blockId, boolean force) {
		if(blocks.size() != 0 && blocks.get(blocks.size() -1) == blockId) {
			return false;
		}
		return force ||
				blocks.size() == 0 ||
				Overflow.sub(blockId, blocks.get(blocks.size() -1)) >= Constants.ResendThreshold;
	}

	@Override
	public Short getActualBlock(ArrayList<Short> blocks, Short blockId) {
		if(blocks.size() != 0) {
			blockId = blocks.get(0);
		}
		return blockId;
	}

	@Override
	public Short newBlockCost(Short blockId, TreeMap<Integer, ArrayList<Packet>> byBlock) {
		if(byBlock.containsKey((int)blockId)) {
			return 0;
		}
		return 4;
	}

	@Override
	public void removeByCount(Short count) {
	}
}
