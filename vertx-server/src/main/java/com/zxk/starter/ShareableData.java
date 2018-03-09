package com.zxk.starter;

import io.vertx.core.shareddata.Shareable;
import io.vertx.ext.web.Router;

/**
 * author xiaokangzheng
 * date 2018/3/7 上午11:04
 */
public class ShareableData implements Shareable {

    private Router mainRouter;

    public ShareableData(Router mainRouter) {
        this.mainRouter = mainRouter;
    }

    public Object getData() {
        return mainRouter;
    }
}
