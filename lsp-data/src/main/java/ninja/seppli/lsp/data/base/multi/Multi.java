package ninja.seppli.lsp.data.base.multi;

import java.util.Objects;
import java.util.function.Function;

/**
 * An class which can hold either T1 or T2. It is used to parse json with
 * multiple types per one field
 *
 * @author sebi
 *
 * @param <T1> the first possible type
 * @param <T2> the second possible type
 */
public class Multi<T1, T2> {
	/**
	 * type 1
	 */
	private T1 t1;
	/**
	 * type 2
	 */
	private T2 t2;


	/**
	 * private Constructor for {@link #ofT1(Object)} and {@link #ofT2(Object)} One
	 * of the parameter has to be null
	 *
	 * @param t1 first type object
	 * @param t2 second type object
	 */
	private Multi(T1 t1, T2 t2) {
		if (t1 != null && t2 != null) {
			throw new IllegalArgumentException("t1 or t2 has to be null");
		}
		if (t1 == null && t2 == null) {
			throw new NullPointerException("t1 or t2 are both null");
		}
		this.t1 = t1;
		this.t2 = t2;
	}

	/**
	 * Returns the type 1 or null if t2 is present
	 *
	 * @return the type 1 object or null
	 */
	public T1 getT1() {
		return t1;
	}

	/**
	 * Returns the type 2 or null if t1 is present
	 *
	 * @return the type 2 object or null
	 */
	public T2 getT2() {
		return t2;
	}


	/**
	 * Checks if t1 is present
	 *
	 * @return if t1 is present
	 */
	public boolean isT1Present() {
		return t1 != null;
	}

	/**
	 * Checks if t2 is present
	 *
	 * @return if t2 is present
	 */
	public boolean isT2Present() {
		return !isT1Present();
	}

	/**
	 * Mapps the Multi to a "normal" type. Depending on which type is present one or
	 * the other function is called
	 *
	 * @param <T>        the "normal" type to which is mapped
	 * @param t1Function the function for when t1 is present
	 * @param t2Function the function for when t2 is present
	 * @return the normal converted type
	 */
	public <T> T mapToObj(Function<T1, T> t1Function, Function<T2, T> t2Function) {
		if (isT1Present()) {
			return t1Function.apply(t1);
		} else {
			return t2Function.apply(t2);
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(t1, t2);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Multi)) {
			return false;
		}
		Multi<?, ?> other = (Multi<?, ?>) obj;
		return Objects.equals(t1, other.t1) && Objects.equals(t2, other.t2);
	}

	@Override
	public String toString() {
		return "Multi [ " + mapToObj(Object::toString, Object::toString) + " ]";
	}

	/**
	 * Factory method for Multi with type t1 active
	 *
	 * @param <T1> the Type 1
	 * @param <T2> the type 2
	 * @param t1   the value of type 1
	 * @return a new multi object with the type 1 present
	 */
	public static <T1, T2> Multi<T1, T2> ofT1(T1 t1) {
		return new Multi<T1, T2>(t1, null);
	}

	/**
	 * Factory method for Multi with type t2 active
	 *
	 * @param <T1> the Type 1
	 * @param <T2> the type 2
	 * @param t2   the value of type 2
	 * @return a new multi object with the type 2 present
	 */
	public static <T1, T2> Multi<T1, T2> ofT2(T2 t2) {
		return new Multi<T1, T2>(null, t2);
	}

}
