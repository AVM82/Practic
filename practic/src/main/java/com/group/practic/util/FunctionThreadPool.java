package com.group.practic.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;


public class FunctionThreadPool<A, R> {

    private Function<A, R> function;

    private ExecutorService es;

    private long sleepTimeMs = 1000;


    public FunctionThreadPool(Function<A, R> function) {
        this.function = function;
    }


    public FunctionThreadPool(Function<A, R> function, long sleepTimeMs) {
        this.function = function;
        this.sleepTimeMs = sleepTimeMs > 10 || sleepTimeMs < this.sleepTimeMs ? sleepTimeMs
                : this.sleepTimeMs;
    }


    public Collection<R> execute(Collection<A> arguments) {
        es = Executors.newFixedThreadPool(arguments.size());
        Collection<FunctionThread<A, R>> pool = createPool(arguments);
        pool.forEach(es::execute);
        while (!es.isTerminated()) {
            try {
                Thread.sleep(sleepTimeMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            es.shutdown();
        }
        return getResults(pool);
    }


    private Collection<FunctionThread<A, R>> createPool(Collection<A> arguments) {
        Collection<FunctionThread<A, R>> result = new ArrayList<>(arguments.size());
        arguments.forEach(a -> result.add(new FunctionThread<>(function, a)));
        return result;
    }


    private Collection<R> getResults(Collection<FunctionThread<A, R>> pool) {
        Collection<R> results = new ArrayList<>(pool.size());
        pool.forEach(t -> results.add(t.get()));
        return results;
    }

}
