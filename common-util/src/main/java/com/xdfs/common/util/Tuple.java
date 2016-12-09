package com.xdfs.common.util;

import java.util.*;

/**
 * Created by xyy on 16-12-4.
 */

public class Tuple<K, V> {

    public Tuple(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    private K key;
    private V value;
}
