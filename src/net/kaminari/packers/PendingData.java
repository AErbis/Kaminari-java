package net.kaminari.packers;

import java.util.ArrayList;

public class PendingData<T> {
	public T data;
	public ArrayList<Short> blocks;

	public PendingData(T data) {
		this.data = data;
		this.blocks = new ArrayList<Short>();
	}
	
}
