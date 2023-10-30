package com.group.practic.entity;

public interface NewInstanceCreatable<R, T> {

    T create(R entity);
    
}
