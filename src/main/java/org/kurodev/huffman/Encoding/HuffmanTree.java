package org.kurodev.huffman.Encoding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class HuffmanTree {
    private static final Logger logger = LoggerFactory.getLogger(HuffmanTree.class);
    private static final Node EMPTY_NODE = new Node();

    private Map<Integer, Character> tree = new TreeMap<>();

    public HuffmanTree(Map<Integer, Character> tree) {
        this.tree = tree;
    }

    public static HuffmanTree constructTree(String input) {
        logger.info("Computing char frequency");
        final List<Node> nodes = new ArrayList<>();
        input.chars().forEach(c -> {
            if (nodes.stream().anyMatch(value -> value.getSymbol() == c)) return;

            long total = input.chars().filter(value -> value == c).count();
            double frequency = (double) total / (double) input.length();
            nodes.add(new Node(frequency, (char) c));
        });
        logger.info("Constructing tree");

        while (nodes.size() > 1) {
            nodes.sort(Comparator.comparingDouble(Node::getFrequency));
            constructNewBranch(nodes);
        }

        return new HuffmanTree(generateTree(nodes.getFirst()));
    }

    private static Map<Integer, Character> generateTree(Node root) {
        final Map<Integer, Character> tree = new TreeMap<>();
        generateTree(root, 0, tree);
        return tree;
    }

    private static void generateTree(Node root, int depth, Map<Integer, Character> tree) {
        if (root.isRootNode()) {
            if (tree.containsKey(depth - 1)) {
                tree.put(-depth, root.getSymbol());
            } else {
                tree.put(depth - 1, root.getSymbol());
            }
            return;
        }

        List<Node> parents = Arrays.stream(root.getParents())
                .sorted(Comparator.comparingDouble(Node::getFrequency)
                        .thenComparing(Node::getSymbol, Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();
        for (int i = 0; i < parents.size(); i++) {
            generateTree(parents.get(i), depth + 1, tree);

        }
    }

    private static void constructNewBranch(List<Node> nodes) {
        Node[] children = new Node[2];
        for (int childIndex = 0; childIndex < children.length; childIndex++) {
            children[childIndex] = getNode(nodes);
        }
        nodes.add(new Node(children));
    }

    private static Node getNode(List<Node> nodes) {
        if (nodes.isEmpty()) return EMPTY_NODE;
        return nodes.removeFirst();
    }

    public static String getTableString(Map<Integer, Character> table) {
        StringBuilder sb = new StringBuilder();

        int maxLeadingZeros = table.keySet().stream().map(Math::abs).max(Integer::compareTo).orElse(1);

        table.entrySet()
                .stream()
                .sorted(Comparator.comparingInt(symbol -> Math.abs(symbol.getKey())))
                .forEach(entry -> {
                    char letter = entry.getValue();
                    int leadingZeros = entry.getKey();
                    boolean justZeros = leadingZeros < 0;

                    String zeros = "0".repeat(Math.abs(leadingZeros));
                    String padding = " ".repeat(maxLeadingZeros - Math.abs(leadingZeros));

                    // Format each row as "Letter | 0001"
                    sb.append(letter)
                            .append(" | ")
                            .append(padding)
                            .append(leadingZeros < 0 ? " " : "")
                            .append(zeros)
                            .append(justZeros ? "" : "1")
                            .append(System.lineSeparator());
                });

        return sb.toString();
    }

    @Override
    public String toString() {
        return "HuffmanTree:\n%s".formatted(getTableString(tree));
    }
}
