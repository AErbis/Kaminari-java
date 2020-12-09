package net.kaminari;

public interface IBroadcaster<T> {
	void broadcast(IBroadcastOperation<T> operation);
}
