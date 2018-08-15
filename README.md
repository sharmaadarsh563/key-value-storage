# Key-Value Store

This is the in-memory disk-backed key-value storage. If the memory gets full, the least recently used key-value pair is moved to the disk (using append-only mechanism), making it available for reads. The data on disk is arranged in a file-based system, sharding the file using a hash function, and keeping the size of each file as small as possible so as to optimize read/write operation. To further improve the performance, the synchronized buffering (of default size) has also been introduced while reading/writing. In addition, since data is appended to file, duplicate keys might exist in the file; therefore, a background daemon thread runs periodically to clean-up those files.

## Assumptions

* Our disk is mechanical hard drive.
* Access pattern consists of significant number of read operations. Read operation consists of the following steps:

```
Step 1: A read from the HashMap corresponding to a key.
Step 2: An update in the Doubly LinkedList, making value as most recently used.
```

## Design Choices

### Data Structures
* Doubly linked-list, tracking the most and least recently used key-value pair.
* HashMap, returning the value corresponding to a key in O(1) complexity.
* Queue, to improve the performance of read operation by delaying the Step 2.

### Design Patterns
* Singleton pattern, restricting the instantiation of key-value storage class.

### Run the program

Please follow the instructions below to correctly run this program

Step 1: Clone the repository

```
git clone https://github.com/sharmaadarsh563/key-value-storage.git
```

Step 2: Change permission of run.sh

```
chmod +x run.sh
```

Step 3: Run the program

```
cd src/ && ./run.sh
```

Then follow the instructions

### Example

After running the program, you need to perform the series of get and put operations:

Step 1: Speficy the capacity of the in-memory store:

```
Initiating the templates...
Enter the capacity of the key-value store: 
20
```

Step 2: Set value "adarsh" corresponding to the key "1"

```
put 1 adarsh
```

Step 3: Get value "adarsh" corresponding to the key "1"

```
get 1
value => adarsh
```

Step 3: Quit the program

```
quit
Quiting...
Stopping all threads...
Shutting down...
```


## Future Improvements

### Existing implementation
* Like the read operation, breakdown the write operation in multiple steps and prioritize them to improve the performance.
* Run the separate thread for Queue handling and Files' clean-up.

### New implementation
* Instead of a Doubly linked-list, HashMap, and Queue, maintain a Binary Search Tree (BST) in memory and write it out as SSTables on disk when the size of BST exceeds a threshold. Note that the keys would be written in an sorted order in SSTables.
* Run a background thread to perform merging and compaction on SSTables.

## Authors

* **Adarsh Sharma**
