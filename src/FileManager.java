
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.PriorityQueue;
import java.util.Set;

public class FileManager {

	private Hashtable<Character, Integer> charTable = new Hashtable<>();
	private PriorityQueue<CharAndCountNode> heap = new PriorityQueue<>();

	public PriorityQueue<CharAndCountNode> getHeap() {
		return heap;
	}

	public void readTextFileAndCountCharacters(String fileName) throws IOException {
		Path path = Paths.get(fileName);
		try (BufferedReader reader = Files.newBufferedReader(path)) {
			String line = null;
			int numberOfNewLine = -1;
			while ((line = reader.readLine()) != null) {
				countCharacters(line);
				numberOfNewLine++;
			}
			addNewLinesToTable(numberOfNewLine);
		}

		Set<Character> keys = charTable.keySet();
		for (Character key : keys) {
			heap.add(new CharAndCountNode(key, charTable.get(key)));
		}

	}

	private void countCharacters(String line) {
		for (char character : line.toCharArray()) {
			if (charTable.containsKey(character)) {
				charTable.replace(character, charTable.get(character) + 1);
			} else {
				charTable.put(character, 1);

			}
		}
	}

	private void addNewLinesToTable(int number) {
		char newLineCharacter = '\n';

		for (int i = 0; i < number; i++) {
			if (charTable.containsKey(newLineCharacter)) {
				charTable.replace(newLineCharacter, charTable.get(newLineCharacter) + 1);
			} else {
				charTable.put(newLineCharacter, 1);
			}
		}
	}

}
