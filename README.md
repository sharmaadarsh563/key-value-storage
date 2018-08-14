# Key-Value Store

This is the in-memory disk-backed key-value storage. If the memory gets full, the least recently used data is moved to the disk, making it available to perform the read operation. The data on disk is arranged in a file-based system, sharding the file using a hash function, and keeping the size of each file as small as possible so as to optimize read/write operation. To further improve the performance, the buffering (of default size) has also been introduced while reading/writing.

## Assumptions
* Our disk is mechanical hard drive
* Access pattern consists of significant number of read operations. Read operation consists of the following steps:
```
Step 1: A read from the HashMap corresponding to a key,
Step 2: An update in the Doubly LinkedList, making value as most recently used
```

## Design Choices

### Data Structures
* Doubly linked-list, tracking the most and least recently used key-value pair
* HashMap, returning the value corresponding to a key with O(1) complexity
* Queue, to improve the performance of read operation by delaying the Operation 2

### Design Patterns
* Singleton pattern, restricting the instantiation of key-value storage class

### Run the program

A step by step series of examples that tell you how to get a development env running

Say what the step will be

```
Give the example
```

And repeat

```
until finished
```

End with an example of getting some data out of the system or using it for a little demo

## Running the tests

Explain how to run the automated tests for this system

### Break down into end to end tests

Explain what these tests test and why

```
Give an example
```

### And coding style tests

Explain what these tests test and why

```
Give an example
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
