package org.kurodev.huffman.Encoding;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class Node {
    private final double frequency;
    private final Node[] parents;
    private final Character symbol;

    private Node(double frequency, Character symbol, Node[] parents) {
        this.frequency = frequency;
        this.symbol = symbol;
        this.parents = parents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Double.compare(frequency, node.frequency) == 0 && Objects.equals(symbol, node.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(frequency, symbol);
    }

    public Node(double frequency, char symbol) {
        this(frequency, symbol, new Node[0]);
    }

    public Node(Node... parents) {
        this(Arrays.stream(parents).mapToDouble(node -> node.frequency).sum(), null, parents);
    }

    public Character getSymbol() {
        return symbol;
    }

    public double getFrequency() {
        return frequency;
    }

    public boolean isRootNode() {
        return parents.length == 0;
    }

    @Override
    public String toString() {
        String parentSymbols = Arrays.stream(parents)
                .map(Node::getSymbol)
                .map(sym -> sym == null ? "?" : sym.toString())
                .collect(Collectors.joining(","));

        return String.format("{freq=%.2f, sym=%s, parents=[%s]}",
                frequency,
                symbol == null ? "?" : symbol,
                parentSymbols.isEmpty() ? "-" : parentSymbols);
    }

    public Node[] getParents() {
        return parents;
    }
}
