package com.group.practic.util;

import java.util.function.Consumer;

public class AsyncThread<A> extends Thread {

    private Consumer<A> procedure;

    private A argument;

    public AsyncThread(ThreadGroup threadGroup,
                       String threadName,
                       Consumer<A> procedure, A argument) {
        super(threadGroup, threadName);
        this.procedure = procedure;
        this.argument = argument;
    }


    @Override
    public void run() {
        procedure.accept(argument);
    }

}