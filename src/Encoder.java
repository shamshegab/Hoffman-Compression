
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.BitSet;
import java.util.Hashtable;
import java.util.PriorityQueue;
import java.util.Set;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Shams Sherif
 */
public class Encoder {

	public static final char END_OF_HEADER = '>';
	public static final char END_OF_oneHEADER = '^';

	public void encodeFolder(String path) throws IOException {
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		String encoded = "";
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				System.out.println("Starting Compressing");
				System.out.println("File number " + i);
				String filepath = path + listOfFiles[i].getName();
				System.out.println("Getting characterTable.....");
				Hashtable<Character, String> charTable = getChartable(filepath);
				System.out.println("");
				System.out.println("translating.....");
				String res = translate(filepath, charTable);
				Set<Character> keys = charTable.keySet();

				encoded += res;

				System.out.println("writing in header....");
				writeHeader(res.length(), charTable, path + "compressedFile.txt", listOfFiles.length - i,
						listOfFiles[i].getName());

			}
		}
		System.out.println("converting....");
		byte bytes[] = convertToBytes(encoded);

		writeBytes(bytes, path + "compressedFile.txt");
		System.out.println("Compressed");

	}

	public void encodeSingle(String file) throws IOException {
		System.out.println("staring compressing");
		System.out.println("Getting charcterTable......");
		Hashtable<Character, String> charTable = getChartable(file);
		System.out.println("Translating.......");
		String encoded = translate(file, charTable);
		System.out.println("Converting......");
		int count = 0;
		count = encoded.length();
		// System.out.println(encoded);
		byte bytes[] = convertToBytes(encoded);
		System.out.println("writing to File........");
		writeHeader(count, charTable, "compressedFile.txt");
		writeBytes(bytes, "compressedFile.txt");
		System.out.println("Compressed");

	}

	private String translate(String file, Hashtable<Character, String> charTable)
			throws FileNotFoundException, IOException {

		File f = new File(file);
		String encoded = "";
		int count = 0;
		StringBuilder sb = new StringBuilder("");

		try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
			String line = reader.readLine();
			while ((line != null)) {
				for (char character : line.toCharArray()) {
					// System.out.println(character+"
					// "+charTable.get(character));
					sb.append(charTable.get(character));

				}

				if ((line = reader.readLine()) != null) {
					sb.append(charTable.get('\n'));
				}

			}

		}
		encoded = sb.toString();
		return encoded;
	}

	private byte[] convertToBytes(String encoded) {
		BitSet bitset = new BitSet(encoded.length());
		for (int i = 0; i < encoded.length(); i++) {
			if (encoded.charAt(i) == '1') {
				bitset.set(i);
			}
		}

		// System.out.println(bitset);
		byte[] bytes = new byte[(bitset.length() / 8 + 1)];
		for (int i = 0; i < bitset.length(); i++) {
			if (bitset.get(i)) {
				bytes[i / 8] |= 1 << (7 - i % 8);
			}
		}

		return bytes;
	}

	private void writeBytes(byte[] bytes, String path) throws FileNotFoundException, IOException {
		for (int i = 0; i < bytes.length; i++) {
			String s1 = String.format("%8s", Integer.toBinaryString(bytes[i] & 0xFF)).replace(' ', '0');

		}
		try {

			FileOutputStream output = new FileOutputStream(path, true);
			try {
				output.write(bytes);
			} finally {
				output.close();
			}

			// fos.write(bytes, "true");
		} catch (Exception e) {
			System.out.println("error");
		}
	}

	private void writeHeader(int count, Hashtable<Character, String> charTable, String path) throws IOException {
		Set<Character> keys = charTable.keySet();
		File Header = new File(path);
		FileWriter out = new FileWriter(Header, false);
		BufferedWriter wr = new BufferedWriter(out);
		wr.write(count + "\n");
		for (Character key : keys) {
			wr.write(key + "\n" + charTable.get(key) + "\n");
			// System.out.println(key + "::: " + charTable.get(key));
		}
		wr.write(END_OF_HEADER);// end of header

		wr.close();
		out.close();
	}

	private Hashtable<Character, String> getChartable(String filepath) {
		FileManager manager = new FileManager();
		try {
			manager.readTextFileAndCountCharacters(filepath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PriorityQueue<CharAndCountNode> heap = manager.getHeap();
		HuffmanTree tree = new HuffmanTree();
		Hashtable<Character, String> charTable = tree.createHuffmanTree(heap);

		// tree.printAllCharactersWithCodes();
		return charTable;
	}

	private void writeHeader(int count, Hashtable<Character, String> charTable, String string, int index,
			String filename) throws IOException {
		Set<Character> keys = charTable.keySet();
		File Header = new File(string);
		FileWriter out = new FileWriter(Header, true);
		BufferedWriter wr = new BufferedWriter(out);

		wr.write(count + "\n");
		wr.write(filename + "\n");
		for (Character key : keys) {
			wr.write(key + "\n" + charTable.get(key) + "\n");
			// System.out.println(key + "::: " + charTable.get(key));
		}
		if (index > 1) {
			wr.write(END_OF_oneHEADER);
		} else {
			wr.write(END_OF_HEADER);
		}
		// end of header

		wr.close();
		out.close();
	}

}
