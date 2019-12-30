package com.ayang818.kugga.prepare.niopool;

import java.util.concurrent.*;

/**
 * @author 杨丰畅
 * @description TODO
 * @date 2019/12/19 18:22
 **/
public class HandlerExecutePool {
    private ThreadPoolExecutor threadPoolExecutor;

    public HandlerExecutePool(Integer corePoolSize, Integer maxPoolSize) {
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, 60, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1000), new CustomizeThreadFactory());
    }

    public void execute(Runnable task) {
        threadPoolExecutor.execute(task);
    }

    public <T> Future<T> execute(Callable<T> task) {
        return threadPoolExecutor.submit(task);
    }
}


class CustomizeThreadFactory implements ThreadFactory {
    public Thread newThread(Runnable r) {
        return new Thread("Handler Thread");
    }
}