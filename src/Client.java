import java.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import storage.*;

public class Client {

	public static void main(String args[]) throws IOException{
		System.out.println("Initiating the templates...");

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try {
			System.out.println("Enter the capacity of the key-value store: ");

			int cp = Integer.parseInt(reader.readLine());

			KeyValueStore<Integer,String> store = KeyValueStoreConcrete.getInstance(cp);

			System.out.println("Please provide valid operators: get, put, or quit");

			while(true) {
				String[] lineArr = reader.readLine().split(" ");
				if(lineArr[0].equals("put")) {
					store.put(Integer.parseInt(lineArr[1]), lineArr[2]);
				}
				else if(lineArr[0].equals("get")) {
					System.out.println("value =>" + store.get(Integer.parseInt(lineArr[1])));
				}
				else if(lineArr[0].equals("quit")) {
					System.out.println("Quiting...");
					break;
				}
				else {
					System.out.println("Invalid operator provided");
					break;
				}
			}
		}
		catch (NumberFormatException e) {
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
