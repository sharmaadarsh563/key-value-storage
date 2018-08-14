package storage;


/**
 * The interface to KeyValueStore, which will be implentated
 * by other concrete classes based on the choice of the algorithm
 * , the underlying data-structures, and the data-types
 */

public interface KeyValueStore<K,V> {

	public V get(K key);
	public void put(K key, V value);
}
