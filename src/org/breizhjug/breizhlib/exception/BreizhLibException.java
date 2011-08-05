package org.breizhjug.breizhlib.exception;


public class BreizhLibException extends RuntimeException {

    public BreizhLibException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public BreizhLibException(String detailMessage) {
		super(detailMessage);
	}

	public BreizhLibException(Throwable throwable) {
		super(throwable);
	}
}
