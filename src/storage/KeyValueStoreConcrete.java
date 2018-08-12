package storage;

import java.util.HashMap;
import java.util.Map;


/**
 * Concrete class, implementing the KeyValueStore interface
 * This class maintains most-recently used elements in memory
 * and writes least-recently used elements back to disk in CSV
 * format
*/

public class KeyValueStoreConcrete<K,V> implements KeyValueStore<K,V> {

	private static final int DEFAULT_MEMORY_SIZE = 1000;
	private static final String FILEPATH = "./db.csv";
	private int capacity;
	private int size;
	private Node head;
	private Node tail;
	private Map<K, Node<K,V> > map;
	private FileHandler f;

	public KeyValueStoreConcrete(int capacity) {
		System.out.println("Initiating the key-value store...");
		if(capacity > 0) {
			this.capacity = capacity;
		}
		else {
			System.out.println("Invalid capacity provided, so using default value");
			this.capacity = DEFAULT_MEMORY_SIZE;
		}
		this.size = 0;
		this.head = null;
		this.tail = null;
		this.map = new HashMap<>();
		this.f = new CSVFileHandler();
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

		String ans = f.read(FILEPATH, key);
		return (V) ans;
	}

	/**
	* Method to add or update the key/value pair
	* in the store
	*/
	public void put(K key, V value) {
		if(capacity == 0) {
			// memory based key-value store should be initialized with
			// a valid capacity in memory
			System.out.println("Please specify capacity for the key-value store");
			return;
		}

		if(head == null) {
			head = new Node<K,V>(key, value);
			tail = head;
			map.put(key, head);
			size++;
			return;
		}

		if(map.containsKey(key)){
			if(map.get(key).previous == null) {
				map.get(key).value = value;
			}
			else {
				map.get(key).previous.next = map.get(key).next;
				if(map.get(key).next != null) {
					map.get(key).next.previous = map.get(key).previous;
				}

				map.get(key).next = head;
				head.previous = map.get(key);
				map.get(key).previous = null;
				head = map.get(key);
				head.value = value;
				map.put(key, head);
			}
		}
		else {
			if(size < capacity) size++;
			else {
				Node temp = tail;
				tail = tail.previous;
				if(tail != null) tail.next = null;
				map.remove(temp.key);

				// move the least recently used data to
				// the disk
				f.write(FILEPATH, temp);
			}

			Node node = new Node<K,V>(key, value);
			node.next = head;
			head.previous = node;
			head = node;
			map.put(key, head);
		}

		return;
	}
}
