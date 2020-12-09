package net.kaminari;

public class Optional<T> {
	private T value;
	private boolean empty;
	
	Optional() {
		empty = true;
	}
	
	public boolean hasValue() {
		return !empty;
	}
	
	public T getValue() {
		return value;
	}
	
	public void setValue(T value) {
		assert empty : "Optional already has value";
		this.value = value;
	}
}
