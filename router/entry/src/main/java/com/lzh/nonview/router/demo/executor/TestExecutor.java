package com.lzh.nonview.router.demo.executor;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 动作
 *
 * @since 2021-03-20
 */
public class TestExecutor implements Executor {
    ExecutorService pool = Executors.newSingleThreadExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setName("action_annotation_executor");
            return thread;
        }
    });

    @Override
    public void execute(Runnable runnable) {
        pool.execute(runnable);
    }
}
