package net.kaminari;

public interface IBroadcastOperation<T> {
	void onCandidate(T pq);
}
