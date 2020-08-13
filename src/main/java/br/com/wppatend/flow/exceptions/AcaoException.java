package br.com.wppatend.flow.exceptions;

public class AcaoException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1129195015815171024L;

	public AcaoException(String msg) {
		super(msg);
	}
	
	public AcaoException(Throwable t) {
		super(t);
	}
	
	public AcaoException(String msg, Throwable t) {
		super(msg, t);
	}

}
