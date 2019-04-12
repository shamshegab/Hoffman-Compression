
import java.util.Hashtable;
import java.util.PriorityQueue;
import java.util.Set;

public class HuffmanTree {

	Hashtable<Character, String> charTable = new Hashtable<>();

	public Hashtable<Character, String> getCharTable() {
		return charTable;
	}

	public Hashtable<Character, String> createHuffmanTree(PriorityQueue<CharAndCountNode> heap) {

		CharAndCountNode root = null;
		while (heap.size() > 1) {
			CharAndCountNode minNode1 = heap.peek();

			heap.poll();

			CharAndCountNode minNode2 = heap.peek();
			heap.poll();

			CharAndCountNode newNode = new CharAndCountNode('>', minNode1.count + minNode2.count);
			newNode.leftChild = minNode1;
			newNode.rightChild = minNode2;

			root = newNode;

			heap.add(newNode);

		}

		createCharCode(root, "");

		return charTable;

	}

	public void createCharCode(CharAndCountNode node, String code) {

		if (node.leftChild == null && node.rightChild == null && node.character != '>') {

			charTable.put(node.character, code);

			return;
		}

		createCharCode(node.leftChild, code + "0");
		createCharCode(node.rightChild, code + "1");

	}

	

}
