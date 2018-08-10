package storage;


/**
 * The interface to KeyValueStore, which will be implentated
 * by other concrete classes based on the choice of the algorithm
 * andthe underlying data-structures
 */

public interface KeyValueStore<K,V> {

	public V get(K key);
	public void put(K key, V value);
}
