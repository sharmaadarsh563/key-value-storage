import java.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import storage.*;

public class KeyValueStoreTest {

	public static void main(String args[]) throws IOException{
		System.out.println("Initiating the templates...");

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try {
			System.out.println("Enter the capacity of the key-value store:");

			int cp = Integer.parseInt(reader.readLine());

			KeyValueStore store = new KeyValueStoreConcrete<Integer, String>(cp);
			store.put(1, "adarsh");
			store.put(2, "arpit");
			store.put(3, "uddeshya");
			store.put(4, "blahblah");
			store.put(5, "asjdhasjdhs");
			System.out.println("\n"+store.get(5));
			System.out.println("\n"+store.get(4));
			System.out.println("\n"+store.get(3));
			System.out.println("\n"+store.get(2));
			System.out.println("\n"+store.get(1));
		}
		catch (NumberFormatException e) {
			System.out.println("\nExiting...");
			System.out.println("Error: " + e);
		}
		catch (Exception e) {
			System.out.println("Error: " + e);
		}
		finally {
			if (reader != null) {
				reader = null;
			}
		}
	}
}
