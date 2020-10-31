package cn.winggon;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * 一个简陋的异步工具嗯
 * <p>
 * Created by winggonLee on 2020/10/31
 */
public class AsyncFuture {

    private List<CompletableFuture> queue = new ArrayList<>();

    public static AsyncFuture create() {
        return new AsyncFuture();
    }

    public void run(Runnable runnable) {
        queue.add(CompletableFuture.runAsync(runnable));
    }

    public <T> T supply(Supplier<T> supplier, T obj) {
        queue.add(CompletableFuture.runAsync(() -> ObjUtils.shallowCopy(supplier.get(), obj)));
        return obj;
    }

    public void await() {
        CompletableFuture.allOf(queue.toArray(new CompletableFuture[0])).join();
        queue.clear();
    }
}
