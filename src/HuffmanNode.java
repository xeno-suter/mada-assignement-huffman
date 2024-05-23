package src;

public class HuffmanNode implements Comparable<HuffmanNode> {

    int frequency;
    Character character;
    HuffmanNode left;
    HuffmanNode right;

    public HuffmanNode(int frequency, Character character, HuffmanNode left, HuffmanNode right) {
        this.frequency = frequency;
        this.character = character;
        this.left = left;
        this.right = right;
    }
    
    @Override
    public int compareTo(HuffmanNode o) {
        return Integer.compare(this.frequency, o.frequency);
    }
}
