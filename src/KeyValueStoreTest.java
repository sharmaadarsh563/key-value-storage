import java.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import storage.*;

public class KeyValueStoreTest {

	public static void main(String args[]) throws IOException{
		System.out.println("Initiating the templates...");

		// System.out.println("Enter 'q' to quit");

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try {
			System.out.println("Enter the capacity of the key-value store:");

			int cp = Integer.parseInt(reader.readLine());

			KeyValueStore store = new KeyValueStoreConcrete<Integer, String>(cp);
		}
		catch (NumberFormatException e) {
			System.out.println("\nExiting...");
			System.out.println("Error: You provided the invalid input");
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
