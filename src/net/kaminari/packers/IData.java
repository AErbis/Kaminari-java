package net.kaminari.packers;

import net.kaminari.IMarshal;
import net.kaminari.Packet;

public interface IData {
	void pack(IMarshal marshal, Packet packet);
	int size(IMarshal marshal);
}
