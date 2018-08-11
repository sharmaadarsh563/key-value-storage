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

		public Node(K k, V v) {
			this.key = k;
			this.value = v;
			this.previous = null;
			this.next = null;
		}
	}

	private Node head;
	private Node tail;
	private Map<K, Node> map;

	public KeyValueStoreConcrete(int capacity) {
		System.out.println("Initiating the key-value store...");
		this.capacity = capacity;
		this.size = 0;
		this.head = null;
		this.tail = null;
		this.map = new HashMap<>();
	}

	/**
	* Method to get the key/value pair
	* from the memory with a fallback
	* on the store (disk)
	*/
	public V get(K key) {
		if(map.containsKey(key)) {
			return map.get(key).value;
		}

		// READ FROM THE DISK: // NEEDS TO BE IMPLEMENTTED
		V ans = null;
		return ans;
	}

	/**
	* Method to add or update the key/value pair
	* in the store
	*/
	public void put(K key, V value) {
		if(capacity == 0) return;

		if(head == null) {
			head = new Node(key, value);
			tail = head;
			map.put(key, head);
			size++;
			return;
		}

		// NEEDS TO BE IMPLEMENTTED

		return;
	}
}
