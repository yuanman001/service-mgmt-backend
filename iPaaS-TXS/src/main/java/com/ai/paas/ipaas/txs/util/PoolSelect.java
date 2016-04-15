package com.ai.paas.ipaas.txs.util;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Pool Select
 * <p/>
 * Created by gaoht on 15/4/28.
 */
public class PoolSelect<T> {

    private AtomicInteger seeds = new AtomicInteger();

    public T next(List<T> pools) {
        if (pools == null || pools.size() < 1) {
            return null;
        }
        int seed;
        int oldSeed;
        int size = pools.size();
        do {
            oldSeed = seeds.get();
            seed = oldSeed + 1;
            if (oldSeed > size) {
                seed = 0;
            }
        } while (!seeds.compareAndSet(oldSeed, seed));
        return pools.get(seed % size);
    }
}
