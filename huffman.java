import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Comparator;

//Node which saves each save information
class Node {
	Integer freq;
	char character;
	Node left;
	Node right;
}

//comparator compares frequency of each node, to sort the nodes
class nodeComparator implements Comparator<Node> {
	public int compare(Node a, Node b) {
		return a.freq - b.freq;
	}
}

public class huffman {
	// build huffman code tree
	static Integer huffmanBits = 0;

	public static Node buildTree(HashMap<Character, Integer> freqMap) {
		PriorityQueue<Node> queue = new PriorityQueue<Node>(freqMap.size(), new nodeComparator());
		Iterator mapItr = freqMap.entrySet().iterator();
		// iterate the hashmap and push each node into priority queue, priority value is
		// based on frequency
		while (mapItr.hasNext()) {
			HashMap.Entry map = (HashMap.Entry) mapItr.next();
			Node n = new Node();
			n.character = (char) map.getKey();
			n.freq = (Integer) map.getValue();
			n.left = null;
			n.right = null;
			queue.add(n);
		}
		Node rootNode = null;
		while (queue.size() > 1) {
			Node min1 = queue.poll(); // returns and removes the element from front of the queue
			Node min2 = queue.poll();
			Node minParent = new Node();
			minParent.freq = min1.freq + min2.freq;
			minParent.character = ' ';
			minParent.right = min1;
			minParent.left = min2;
			rootNode = minParent;
			queue.add(minParent);
		}
		return rootNode;
	}

	// print huffman tree
	public static void getCode(Node rootNode, String code) {

		if (rootNode.left == null && rootNode.right == null) {
			System.out.println(rootNode.character + " : " + code);
			// keeps count of bits required for each character in order to display savings
			huffmanBits += code.length() * rootNode.freq;
		} else {
			getCode(rootNode.right, code + '1');
			getCode(rootNode.left, code + '0');
		} // return huffmanBits;
	}

	public static void main(String[] args) {
		try {
			HashMap<Character, Integer> map = new HashMap<Character, Integer>();
			FileReader f = new FileReader(new File("src\\A Tale of Two Cities.txt"));
			BufferedReader br = new BufferedReader(f);
			int c = 0;
			// saving each character with its frequency in hashmap
			while ((c = br.read()) != -1) {
				// checking for ASCII
				if (c >= 31 && c <= 127) {
					char letter = (char) c;
					// generating map with key as character and frequency as the value.
					map.put(letter, (map.getOrDefault(letter, 0) + 1));
				}
			}
			Node rootNode = buildTree(map);
			getCode(rootNode, "");
			Integer fixedBits = 7 * rootNode.freq;
			System.out.println("Bits for fixed length encoding using 7 bit per character : " + fixedBits);
			System.out.println("Bits required for Huffman encoding : " + huffmanBits);
			System.out.println("Bits saved : " + (fixedBits - huffmanBits)); // 7*rode node frequency. As root node gives summation of all frequencies.
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
