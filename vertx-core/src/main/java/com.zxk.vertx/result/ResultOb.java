package com.zxk.vertx.result;

import io.vertx.core.json.Json;

/**
 *  相应消息类型
 * @ProjectName:    [vertx-core]
 * @Package:        [org.rayeye.vertx.result]
 * @ClassName:      [ResultOb]
 * @Description:    [相应消息类型]
 * @Author:         [Neil.Zhou]
 * @CreateDate:     [2017/9/20 13:34]
 * @UpdateUser:     [Neil.Zhou]
 * @UpdateDate:     [2017/9/20 13:34]
 * @UpdateRemark:   [相应消息]
 * @Version:        [1.0]
 * <p>Copyright: Copyright (c) 2017/9/20</p>
 *
 */
public class ResultOb<T> {
    private String code="200" ;//状态
    private String msg="SUCCESS";//消息
    private T data;

    public T getData() {
        return data;
    }

    public ResultOb setData(T data) {
        this.data = data;
        return this;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public ResultOb setMsg(String msg) {
        this.msg = msg;
        return this;
    }
    @Override
    public String toString(){
        return Json.encode(this);
    }

    public static ResultOb build(){
        return new ResultOb();
    }
}
