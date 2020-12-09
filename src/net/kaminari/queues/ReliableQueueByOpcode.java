package net.kaminari.queues;

import net.kaminari.packers.IData;
import net.kaminari.packers.IPacker;

public class ReliableQueueByOpcode<P extends IPacker<PacketWithOpcode, IData>> extends ReliableQueueBase<P, PacketWithOpcode, IData> {

	public ReliableQueueByOpcode(P packer) {
		super(packer);
	}

}
