package com.lambstat.module.jetty.data;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class Param<K, V> implements Serializable {
    private K key;
    private V value;
    private ParamRelation relation = ParamRelation.EQ;

    public Param() {
    }

    public Param(K key) {
        this.key = key;
    }

    public Param(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public Param(K key, V value, ParamRelation relation) {
        this.key = key;
        this.value = value;
        this.relation = relation;
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

    public ParamRelation getRelation() {
        return relation;
    }

    public void setRelation(ParamRelation relation) {
        this.relation = relation;
    }

    public String toString() {
        return "Param{key=" + this.key + ", value=" + this.value +  ", relation=\'" + this.relation + '\'' + '}';
    }
}
