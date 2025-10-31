package com.aston.domashka;

import java.util.Objects;

public class MyHashMap<K, V> {

    private static class Entry<K, V> {
        final K key;
        V value;
        Entry<K, V> next;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private static final int DEFAULT_CAPACITY = 16;
    private Entry<K, V>[] table;
    private int size = 0;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = new Entry[DEFAULT_CAPACITY];
    }

    private int index(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    public void put(K key, V value) {
        int index = index(key);
        Entry<K, V> current = table[index];

        if (current == null) {
            table[index] = new Entry<>(key, value);
            size++;
            return;
        }

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                current.value = value;
                return;
            }
            if (current.next == null) {
                current.next = new Entry<>(key, value);
                size++;
                return;
            }
            current = current.next;
        }
    }

    public V get(K key) {
        int index = index(key);
        Entry<K, V> current = table[index];

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    public void remove(K key) {
        int index = index(key);
        Entry<K, V> current = table[index];
        Entry<K, V> prev = null;

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return;
            }
            prev = current;
            current = current.next;
        }
    }

    public int size() {
        return size;
    }

    public static void main(String[] args) {
        MyHashMap<String, Integer> map = new MyHashMap<>();

        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        System.out.println("B -> " + map.get("B")); // 2

        map.put("B", 99);
        System.out.println("B -> " + map.get("B")); // 99

        map.remove("A");
        System.out.println("A -> " + map.get("A")); // null

        System.out.println("Size: " + map.size()); // 2
    }
}
