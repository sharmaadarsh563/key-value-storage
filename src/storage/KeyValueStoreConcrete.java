package storage;

import java.util.HashMap;
import java.util.Map;

public class KeyValueStoreConcrete<K,V> implements KeyValueStore<K,V> {

	private int capacity;
	private int size;

	class Node {
		K key;
		V value;
		Node previous;
		Node next;

		public Node(Node p, Node n, K k, V v) {
			this.key = k;
			this.value = v;
			this.previous = p;
			this.next = n;
		}
	}

	private Node head;
	private Node tail;
	private Map<K, Node> map;

	public KeyValueStoreConcrete(int capacity) {
		this.size = 0;
		this.capacity = capacity;
		this.map = new HashMap<>();
	}

	/**
	* Method to get the key/value pair
	* from the memory with a fallback
	* on the store (disk)
	*/
	public V get(K key) {
		// NEEDS TO BE IMPLEMENTED
		V ans = null;
		return ans;
	}

	/**
	* Method to add or update the key/value pair
	* in the store
	*/
	public void put(K key, V value) {
		// NEEDS TO BE IMPLEMENTED
	}
}
