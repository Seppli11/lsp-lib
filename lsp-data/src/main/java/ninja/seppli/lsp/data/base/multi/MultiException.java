package ninja.seppli.lsp.data.base.multi;

import java.io.IOException;

/**
 * An exception with two suppressed exception. It is thrown when both types of a
 * {@link Multi} object couldn't be parsed
 *
 * @author sebi
 *
 */
public class MultiException extends IOException {
	/**
	 * the t1 throwable
	 */
	private Throwable t1;
	/**
	 * the t2 throwable
	 */
	private Throwable t2;

	/**
	 * Constructor
	 *
	 * @param message the messag
	 * @param t1      exception for type 1
	 * @param t2      exception for type 2
	 */
	public MultiException(String message, Throwable t1, Throwable t2) {
		super(message);
		this.t1 = t1;
		this.t2 = t2;
		addSuppressed(t1);
		addSuppressed(t2);
	}

	/**
	 * Returns the throwable t2
	 *
	 * @return the t1 throwable
	 */
	public Throwable getT1() {
		return t1;
	}

	/**
	 * Returns the throwable t1
	 *
	 * @return the t2 throable
	 */
	public Throwable getT2() {
		return t2;
	}

}