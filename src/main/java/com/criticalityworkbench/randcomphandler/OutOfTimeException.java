package com.criticalityworkbench.randcomphandler;

class OutOfTimeException extends RuntimeException{
	public OutOfTimeException() {
		super();
	}

	public OutOfTimeException(String message){
		super(message);
	}

	public OutOfTimeException(String s, Throwable throwable){
		super(s, throwable);
	}

	public OutOfTimeException(Throwable throwable){
		super(throwable);
	}
				
}
