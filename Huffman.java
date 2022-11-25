import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Huffman {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter: ");
        huffmanCompression(scanner.nextLine());
        scanner.close();
    }
    public static void findFrequency(String text, HashMap<Character, Integer> map) {
        for (int i = 0; i < text.length(); ++i) {
            map.put(text.charAt(i), map.get(text.charAt(i))==null?1:map.get(text.charAt(i)) + 1);
        }
    }
    public static void dfs(Node root, Character chr, String[] path, String current) {
        if (root == null) return;
        if (root.c != null && root.c.equals(chr)) {
            path[0] = current;
            return;
        }
        current += "0";
        dfs(root.left, chr, path, current);
        current = current.substring(0, current.length()-1);
        current += "1";
        dfs(root.right, chr, path, current);
    }
    public static void dfsNormal(Node root) {
        if (root == null) return;
        if (root.c == null)
            System.out.println(root.frequency);
        else
            System.out.println(root.c);
        dfsNormal(root.left);
        dfsNormal(root.right);
    }
    public static void huffmanCompression(String s) {
        if (s.length() < 2) {
            System.out.println("Length must be bigger than 1");
            return;
        }
        System.out.println("Roughly Uncompressed");
        System.out.print("Length:  ");
        int lengthOfBits = (int) Math.floor(Math.log(s.length())) + 1;
        for (int i = 0; i < lengthOfBits * s.length(); ++i) System.out.print("1");
        System.out.println();
        Node root = null;
        HashMap<Character, Integer> map = new HashMap<>();
        HashMap<Character, String> comp = new HashMap<>();
        findFrequency(s, map);
        PriorityQueue<Node> x = new PriorityQueue<>(Comparator.comparingInt(e->e.frequency));
        for (Character key : map.keySet()) {
            Node n = new Node();
            n.c = key;
            n.frequency = map.get(key);
            x.add(n);
        }
        while (!x.isEmpty()) {
            Node n1 = x.poll();
            Node n2 = x.poll();
            Node parent = new Node();
            parent.frequency = n1.frequency + n2.frequency;
            parent.left = n1.frequency <= n2.frequency?n1:n2;
            parent.right = n1.frequency > n2.frequency?n1:n2;
            if (x.size() != 0)
                x.add(parent);
            else
                root = parent;
        }
        for (Character key:map.keySet()) {
            String[] testPath = new String[]{""};
            //dfsNormal(root); //to see the tree
            dfs(root, key, testPath, "");
            System.out.println(key + " : " + testPath[0]);
            comp.put(key, testPath[0]);
        }
        String result = "";
        for (int i = 0; i < s.length(); i++) {
            result += comp.get(s.charAt(i)) + (i!=s.length()-1?",":"");
        }
        System.out.println("Result:  " + result);
        decode(root, result);
    }
    public static Character search(Node root, String rec) {
        if (rec == null) {
            return root.c;
        } else if (rec.charAt(0) == '0') {
            return search(root.left, rec.length()>1?rec.substring(1):null);
        } else {
            return search(root.right, rec.length()>1?rec.substring(1):null);
        }
    }
    public static void decode(Node root, String compressed) {
        String[] tokens = compressed.split(",");
        String result = "";
        for (String token : tokens) {
            result += search(root, token);
        }
        System.out.println("Decoded: " + result);
    }
}

class Node {
    Node left;
    Node right;
    Character c;
    int frequency;

    @Override
    public String toString() {
        return "(" + c + ", " + frequency + ")";
    }
}