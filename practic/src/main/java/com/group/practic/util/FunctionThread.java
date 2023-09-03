package com.group.practic.util;

import java.util.function.Function;


public class FunctionThread<A, R> extends Thread {

    private Function<A, R> function;

    private A argument;

    private R result;


    public FunctionThread(Function<A, R> function, A argument) {
        super();
        this.function = function;
        this.argument = argument;
    }


    @Override
    public void run() {
        result = function.apply(argument);
    }


    public R get() {
        return result;
    }

}
