package storage;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;


/**
 * Concrete class, implementing the KeyValueStore interface,
 * storing the key-value pair of date-type Integer and String.
 * This class maintains most-recently used elements in memory
 * and writes least-recently used elements back to the disk in CSV
 * format. Write happens in a traditional append-only mechanism,
 * where key-value pair gets appended to end of the file - this might
 * create duplicate keys to be present in the file. To avert this, a
 * background thread is run which cleans up the files periodically without
 * affecting the read/write operation.
*/

public class KeyValueStoreConcrete implements KeyValueStore<Integer,String> {

	private static final int DEFAULT_MEMORY_SIZE = 1000;
	private static final int DEFAULT_FILE_SIZE = 10000;
	private static KeyValueStoreConcrete uniqueInstance;
	private static DaemonThread dt;
	private static Queue< Node<Integer,String> > queue;
	private int capacity;
	private int size;
	private Node<Integer,String> head;
	private Node<Integer,String> tail;
	private Map< Integer,Node<Integer,String> > map;
	private FileHandler<Integer> f;

	private KeyValueStoreConcrete(int capacity) {
		if(capacity > 0) {
			this.capacity = capacity;
		}
		else {
			System.out.println("Invalid capacity provided, so using default value");
			this.capacity = DEFAULT_MEMORY_SIZE;
		}
		size = 0;
		head = null;
		tail = null;
		map = new HashMap<>();
		queue = new LinkedList<>();
		f = new CSVFileHandler();

		// start the thread in the background, which would
		// clean-up the files
		dt = new DaemonThread("CleanUpFilesThread", f, this);
		dt.start();
	}

	/**
	* Thread-safe static method to get the same instance,
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
	* Shutdown this instance by stopping the threads
	*/
	public static void shutDown() {
		dt.stop();
		System.out.println("Shutting down...");
	}

	/**
	* Get the queue size
	*/
	public static int getQueueSize() {
		return queue.size();
	}

	/**
	* remove element from queue
	*/
	public static Node<Integer,String> removeQueueElement() {
		return queue.remove();
	}

	/**
	* add element to the queue
	*/
	public static void addQueueElement(Node<Integer,String> node) {
		queue.add(node);
	}

	/**
	* Method to get the key/value pair
	* from the memory with a fallback
	* on the store (disk)
	*/
	public String get(Integer key) {
		String ans = "";

		// For a memory (cache) hit, get the value
		// in O(1) complexity
		if(map.containsKey(key)) {
			ans = map.get(key).value;
		}
		// For a memory (cache) miss, load the value from file
		// or the disk
		else {
			// figure out the filename based on the shard index
			String shardIndex = String.valueOf(key%DEFAULT_FILE_SIZE);

			// load the value from file
			ans = f.read("./db/" + shardIndex + ".csv", key);
		}

		// push this pair into queue, the background daemon thread
		// will make it most recently used element
		// This is being done to improve the performance of read
		// operation
		queue.add(new Node<Integer,String>(key, ans));

		return ans;
	}

	/**
	* Method to add or update the key/value pair
	* in the store
	*/
	public void put(Integer key, String value) {
		// memory based key-value store should be initialized with
		// a valid capacity in memory
		if(capacity == 0) {
			System.out.println("Please specify capacity for the key-value store");
			return;
		}

		// if doubly linked-list is empty then create a node and make an
		// entry into the Map too
		if(head == null) {
			head = new Node<Integer,String>(key, value);
			tail = head;
			map.put(key, head);
			size++;
			return;
		}

		// if key exist in the Map, then return it, while making it
		// most recently used key-value pair
		if(map.containsKey(key)){
			// if key if already most recently used, then do nothing
			if(map.get(key).previous == null) {
				map.get(key).value = value;
			}
			// else, move it to the head of the doubly linked-list,
			// making it the most recently used key-value pair
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
		// if key doesn't exist in the Map, then add it
		else {
			// if memory isn't full, then make room for this key
			if(size < capacity) size++;
			// else move the least recently used key-value pair back to
			// disk
			else {
				Node<Integer,String> temp = tail;
				tail = tail.previous;
				if(tail != null) tail.next = null;
				map.remove(temp.key);

				// move the least recently used key-value pair to
				// the disk
				String shard = String.valueOf(temp.key%DEFAULT_FILE_SIZE);
				f.write("./db/" + shard + ".csv", temp);
			}

			// and make this key as most recently used
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
