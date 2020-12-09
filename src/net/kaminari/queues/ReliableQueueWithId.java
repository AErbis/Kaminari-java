package net.kaminari.queues;

import net.kaminari.packers.IData;
import net.kaminari.packers.IPacker;

public class ReliableQueueWithId<P extends IPacker<PacketWithId, IData>> extends ReliableQueueBase<P, PacketWithId, IData> {

	public ReliableQueueWithId(P packer) {
		super(packer);
	}

}
