package karpushinkirill.gr303.myapplication.model;

import androidx.annotation.NonNull;

public class GraphItem {
    public int id;
    public String name;

    public GraphItem(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
