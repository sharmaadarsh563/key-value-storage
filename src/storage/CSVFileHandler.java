package storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Concrete file handler class for reading and writing
 * files in CSV format. This handler uses BufferedReader
 * and BufferedWriter for reading and writing, respectively.
 * The unbuffered I/O stream would have caused each read or
 * write request to be handled by the underlying OS.
 *
 * Note: BufferedWriter would cause this key-value store to be eventually
 *       consistent, as it buffers the print operation before writing
 *       it to the file.
*/

public class CSVFileHandler implements FileHandler<Integer> {

	public CSVFileHandler() {}

	public void write(String filePath, Node node) {
		File file = new File(filePath);

		try {

			FileWriter ofile = new FileWriter(file, true);
			BufferedWriter out = new BufferedWriter(ofile);

			out.write(String.valueOf(node.key) + ":" + String.valueOf(node.value));
			out.newLine();
			out.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String read(String filePath, Integer key) {
		File file = new File(filePath);

		String result = "Value does not exist";

		if (!file.exists()) {
			return result;
		}

		System.out.println("Reading the file...");

		try {

			FileReader ifile = new FileReader(file);
			BufferedReader in = new BufferedReader(ifile);

			String currLine = "";

			while ((currLine = in.readLine()) != null) {
				String[] currLineArray = currLine.split(":");
				if(currLineArray[0].equals(key.toString())) {
					result = currLineArray[1];
				}
			}

			in.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}
}