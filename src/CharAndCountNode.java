
public class CharAndCountNode implements Comparable<CharAndCountNode> {
	public char character;
	public int count;

	public CharAndCountNode leftChild;
	public CharAndCountNode rightChild;

	public CharAndCountNode(char character, int count) {
		super();
		this.character = character;
		this.count = count;
	}

	@Override
	public int compareTo(CharAndCountNode pair) {
		return ((Integer) this.count).compareTo((Integer) pair.count);
	}

}
