package storage;

import java.util.HashMap;
import java.util.Map;


/**
 * Concrete class, implementing the KeyValueStore interface
 * This class maintains most-recently used elements in memory
 * and writes least-recently used elements back to disk in CSV
 * format
*/

public class KeyValueStoreConcrete implements KeyValueStore<Integer,String> {

	private static final int DEFAULT_MEMORY_SIZE = 1000;
	private static final int DEFAULT_FILE_SIZE = 10000;
	private static KeyValueStoreConcrete uniqueInstance;
	private int capacity;
	private int size;
	private Node<Integer,String> head;
	private Node<Integer,String> tail;
	private Map<Integer, Node<Integer,String> > map;
	private FileHandler<Integer> f;

	private KeyValueStoreConcrete(int capacity) {
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
	* Method to get the same instance,
	* restricting the initiation of multiple
	* instances of the same class
	*/
	public static synchronized KeyValueStoreConcrete getInstance(int capacity) {
		if (uniqueInstance == null) {
			uniqueInstance = new KeyValueStoreConcrete(capacity);
		}
		return uniqueInstance;
	}

	/**
	* Method to get the key/value pair
	* from the memory with a fallback
	* on the store (disk)
	*/
	public String get(Integer key) {
		if(map.containsKey(key)) {
			return map.get(key).value;
		}

		String shard = String.valueOf(key%DEFAULT_FILE_SIZE);
		String ans = f.read("./db/" + shard + ".csv", key);
		return ans;
	}

	/**
	* Method to add or update the key/value pair
	* in the store
	*/
	public void put(Integer key, String value) {
		if(capacity == 0) {
			// memory based key-value store should be initialized with
			// a valid capacity in memory
			System.out.println("Please specify capacity for the key-value store");
			return;
		}

		if(head == null) {
			head = new Node<Integer,String>(key, value);
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
				map.get(key).previous = null;
				head.previous = map.get(key);
				head = map.get(key);
				head.value = value;
				map.put(key, head);
			}
		}
		else {
			if(size < capacity) size++;
			else {
				Node<Integer,String> temp = tail;
				tail = tail.previous;
				if(tail != null) tail.next = null;
				map.remove(temp.key);

				// move the least recently used data to
				// the disk
				String shard = String.valueOf(temp.key%DEFAULT_FILE_SIZE);
				f.write("./db/" + shard + ".csv", temp);
			}

			Node<Integer,String> node = new Node<Integer,String>(key, value);
			node.next = head;
			head.previous = node;
			head = node;
			map.put(key, head);
			if(tail == null) {
				tail = head;
			}
		}

		return;
	}
}
