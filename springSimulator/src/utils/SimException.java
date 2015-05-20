package utils;

public class SimException extends Exception{
	private static final long serialVersionUID = 1L;

	public SimException(String message){
		super(message);
	}
	
	public SimException(String message, Throwable e){
		super(message, e);
	}
}
