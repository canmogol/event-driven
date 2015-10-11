package com.lambstat.module.jetty.data;

import javafx.collections.transformation.FilteredList;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ParamMap<K extends String, V extends Param<K, Object>> extends LinkedHashMap<K, V> {

    public ParamMap() {
    }

    public void addParam(V param) {
        this.put(param.getKey(), param);
    }

    public List<Param<K, Object>> getParamList() {
        return new ArrayList<Param<K, Object>>(this.values());
    }

    public Object getValue(K k) {
        return ((Param) this.get(k)).getValue();
    }
}
