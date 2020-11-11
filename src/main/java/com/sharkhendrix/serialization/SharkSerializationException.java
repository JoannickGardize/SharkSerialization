package com.sharkhendrix.serialization;

public class SharkSerializationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SharkSerializationException(Throwable t) {
		super(t);
	}

	public SharkSerializationException(String message) {
		super(message);
	}
}
