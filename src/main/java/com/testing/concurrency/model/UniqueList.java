package com.testing.concurrency.model;

import java.util.ArrayList;

public class UniqueList<T> extends ArrayList<T> {

    public UniqueList() {
        super();
    }

    /**
     * put the element if absent
     * @param t the element to insert in the list
     * @return true if the element was inserted, false otherwise
     */
    public boolean putIfAbsent(T t) {
        boolean absent = !this.contains(t);
        if (absent) {
            this.add(t);
            return true;
        }
        return false;
    }
}
