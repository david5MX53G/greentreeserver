/**
 * 
 */
package com.greentree.model.exception;

/**
 * This should be thrown when a <code>{@link Block}</code> fails to validate.
 * 
 * @author david5MX53G
 *
 */
public class InvalidBlockException extends Exception {

	/**
	 * This is used by <code>{@link java.io.Serializable}</code> for version control.
	 */
	private static final long serialVersionUID = 4064186132637961172L;

	/**
	 * @param msg sent to the <code>{@link Exception}</code> constructor
	 */
	public InvalidBlockException(String msg) {
		super(msg);
	}

}
