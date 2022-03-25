/**
 * A simple class that represents a pair of items
 * @author Nat Schwartzenberger
 * @version 2/8/2022
 *
 * @param <E>
 * @param <T>
 */
public class Pair <E,T>{
	private E key; 
	private T value;
	
	/**
	 * Stores a pair of items
	 * @param key the first item of the pair
	 * @param value the second item of the pair
	 */
	public Pair(E key, T value) {
		this.key = key;
		this.value = value;
	}
	
	/**
	 * Gets the first item of the pair
	 * @return the first item of the pair
	 */
	public E getKey() {
		return this.key;
	}
	
	/**
	 * Gets the second item of the pair
	 * @return the second item of the pair
	 */
	public T getValue() {
		return this.value;
	}
	
  
	
}
