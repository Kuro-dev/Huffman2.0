package org.kurodev.huffman.Encoding;

import org.junit.jupiter.api.Test;

import java.util.Map;

class HuffmanTreeTest {

    @Test
    void constructTree() {
        HuffmanTree tree = HuffmanTree.constructTree("Blahahhppppppp");
        System.out.println("Huffmantree for 'Blahahhppppppp'");
        System.out.println(tree);
    }
}