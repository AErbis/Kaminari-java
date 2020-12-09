package net.kaminari.queues;

import net.kaminari.packers.IData;
import net.kaminari.packers.IPacker;

public class ReliableQueue<P extends IPacker<T, IData>, T> extends ReliableQueueBase<P, T, IData> {

	public ReliableQueue(P packer) {
		super(packer);
	}

}
