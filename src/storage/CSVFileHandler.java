package storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

	private Map<Integer,String> map;

	public CSVFileHandler() {
		map = new HashMap<>();
	}

	public void write(String filePath, Node node) {
		File file = new File(filePath);

		try {

			FileWriter ofile = new FileWriter(file, true);

			// synchronous stream -- helps in multi-threading environment
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

			// synchronous stream -- helps in multi-threading environment
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

	public void clean(String directoryPath) {
		File dirObj = new File(directoryPath);

		File[] fileListing = dirObj.listFiles();

		if (fileListing != null) {
			for (File inFileObj: fileListing) {
				try {
					// synchronous stream -- helps in multi-threading environment
					BufferedReader in = new BufferedReader(new FileReader(inFileObj));

					String currLine = "";

					while ((currLine = in.readLine()) != null) {
						String[] currLineArray = currLine.split(":");
						map.put(Integer.valueOf(currLineArray[0]), currLineArray[1]);
					}

					in.close();

					String oldFileName = inFileObj.getName();

					File outFileoObj = new File(directoryPath + oldFileName + ".new");

					// append-only synchronous stream -- helps in multi-threading environment
					BufferedWriter out = new BufferedWriter(new FileWriter(outFileoObj, true));

					for(Map.Entry<Integer, String> entry: map.entrySet()) {
						out.write(String.valueOf(entry.getKey()) + ":" + entry.getValue());
						out.newLine();
					}

					map.clear();

					inFileObj.delete();

					File originalFile = new File(directoryPath + oldFileName);
					outFileoObj.renameTo(originalFile);

					out.close();
				}
				catch (IOException e) {
					System.out.println("Internal error occured");
				}
			}
		}
		else {
			return;
		}

	}

}
