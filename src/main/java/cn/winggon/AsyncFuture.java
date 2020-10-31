package cn.winggon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * 一个简陋的异步工具嗯
 *
 * Created by winggonLee on 2020/10/31
 */
public class AsyncFuture {

    private List<CompletableFuture> queue = new ArrayList<>();

    public static AsyncFuture create(){
        return new AsyncFuture();
    }

    public void run(Runnable runnable) {
        queue.add(CompletableFuture.runAsync(runnable));
    }

    public <T> T supply(Supplier<T> supplier, T obj) {
        queue.add(CompletableFuture.runAsync(() -> {
            T result = supplier.get();
            if (obj instanceof Collection) {
                ((Collection) obj).addAll((Collection) result);
            } else if (obj instanceof Map) {
                ((Map) obj).putAll((Map) result);
            } else if (obj instanceof StringBuilder) {
                ((StringBuilder) obj).append(result);
            } else {
                throw new RuntimeException("类型不对呀");
            }
        }));
        return obj;
    }

    public void await() {
        final CompletableFuture[] arr = new CompletableFuture[0];
        CompletableFuture.allOf(queue.toArray(arr)).join();
        queue.clear();
    }
}
