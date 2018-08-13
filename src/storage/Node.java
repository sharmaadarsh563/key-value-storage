package storage;

/**
 * Doubly Linked-List node structure. This structure
 * will be used while maintaining data in memory
 * and writing data to the disk using FileHandler
*/

public class Node<K,V> {
	K key;
	V value;
	Node<K,V> previous;
	Node<K,V> next;

	public Node(Node<K,V> p, Node<K,V> n, K k, V v) {
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