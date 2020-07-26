package com.hugman.mubble.util.entry;

public abstract class Entry<E> {
	protected E value;
	protected String name;

	protected Entry(String name) {
		this.name = name;
	}

	protected abstract E register();

	protected String getName() {
		return name;
	}

	public E getValue() {
		return value;
	}
}
