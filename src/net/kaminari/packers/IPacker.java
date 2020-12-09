package net.kaminari.packers;

import java.util.ArrayList;
import java.util.TreeMap;

import net.kaminari.IAckCallback;
import net.kaminari.IMarshal;
import net.kaminari.Packet;
import net.kaminari.Ref;

public interface IPacker<T, D extends IData>  {
	void onAck(ArrayList<PendingData<T>> toBeRemoved);
	void onClear();
	void add(Packet packet);
	void add(IMarshal marshal, Short opcode, D data, IAckCallback callback);
	void process(IMarshal marshal, Short blockId, Ref<Short> remaining, TreeMap<Integer, ArrayList<Packet>> byBlock);
	void ack(Short blockId);
	void clear();
	boolean isPending(ArrayList<Short> blocks, Short blockId, boolean force);
	Short getActualBlock(ArrayList<Short> blocks, Short blockId);
	Short newBlockCost(Short blockId, TreeMap<Integer, ArrayList<Packet>> byBlock);
	void removeByCount(Short count);
}
