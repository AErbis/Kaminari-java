package net.kaminari.packers;

import java.util.ArrayList;

public interface IHasDataVector<T> extends IData {
	void initialize();
	ArrayList<T> getData();
}
