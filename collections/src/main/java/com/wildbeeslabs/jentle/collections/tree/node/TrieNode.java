package com.wildbeeslabs.jentle.collections.tree.node;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class TrieNode {
    private Map<Character, TrieNode> children;
    private boolean endOfWord;

    public TrieNode() {
        this.children = new HashMap<>();
        this.endOfWord = false;
    }
}
