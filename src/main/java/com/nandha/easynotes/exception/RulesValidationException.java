package com.nandha.easynotes.exception;

public class RulesValidationException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public RulesValidationException(String msg) {
		super(msg);
	}
	
	public RulesValidationException(Throwable t) {
		super(t);
	}

	public RulesValidationException(String msg, Throwable t) {
		super(msg, t);
	}
}
