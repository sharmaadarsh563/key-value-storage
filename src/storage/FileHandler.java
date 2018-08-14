package storage;


/**
 * File handler class (interface) for reading and writing
 * files in different format
*/

public interface FileHandler<K> {

	public void write(String filePath, Node node);
	public String read(String filePath, K key);
	public void clean(String directoryPath);
}
