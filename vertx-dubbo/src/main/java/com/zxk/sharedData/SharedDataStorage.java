package com.zxk.sharedData;

import com.zxk.vertx.standard.StandardVertxUtil;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.shareddata.AsyncMap;

import java.util.function.BiConsumer;

import static com.zxk.vertx.util.ConstantUtil.SHARED_DATA_STORAGE;

/**
 * author xiaokangzheng
 * date 2018/3/22 下午6:57
 */
public class SharedDataStorage {
    private final Vertx vertx;

    public SharedDataStorage() {
        this.vertx = StandardVertxUtil.getStandardVertx();
    }

    private Future<AsyncMap<String, SharedDataSystemSourceInfo>> container() {
        Future<AsyncMap<String, SharedDataSystemSourceInfo>> fu = Future.future();
       // vertx.sharedData().<String, SharedDataSystemSourceInfo>getAsyncMap(SHARED_DATA_STORAGE, fu);
        return fu;
    }

    protected <T> Future<T> container(BiConsumer<Future<T>, AsyncMap<String, SharedDataSystemSourceInfo>> codeExecutor) {
        Future<T> fu = Future.future();
        return container().compose(container -> codeExecutor.accept(fu, container), fu);
    }
}
