package com.zxk.vertx.result;

import io.vertx.core.json.Json;

/**
 *  response响应集合消息
 * @ProjectName:    [vertx-core]
 * @Package:        [org.rayeye.vertx.result]
 * @ClassName:      [ResultList]
 * @Description:    [响应集合消息]
 * @Author:         [Neil.Zhou]
 * @CreateDate:     [2017/9/20 13:34]
 * @UpdateUser:     [Neil.Zhou]
 * @UpdateDate:     [2017/9/20 13:34]
 * @UpdateRemark:   [相应消息]
 * @Version:        [1.0]
 * <p>Copyright: Copyright (c) 2017/9/20</p>
 *
 */
public class ResultList<T> extends ResultOb{
    private int total;     //共多少条
    private int count;     //每页多少条
    private int pagetotal;     //共多少页
    private boolean hasNextPage; //有下一页
    private boolean hasPrePage; //有上一页
    private int nopage;  //第几页

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPagetotal() {
        return pagetotal;
    }

    public void setPagetotal(int pagetotal) {
        this.pagetotal = pagetotal;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public boolean isHasPrePage() {
        return hasPrePage;
    }

    public void setHasPrePage(boolean hasPrePage) {
        this.hasPrePage = hasPrePage;
    }

    public int getNopage() {
        return nopage;
    }

    public void setNopage(int nopage) {
        this.nopage = nopage;
    }

    @Override
    public String toString(){
        return Json.encode(this);
    }
}
