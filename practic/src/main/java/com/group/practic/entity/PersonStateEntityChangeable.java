package com.group.practic.entity;

public interface PersonStateEntityChangeable<R, T> {
    
    boolean match(R entity);
    
    T update(R entity);

}
