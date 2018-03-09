package com.zxk.starter.register;

import java.util.Date;

/**
 * Created by better on 15/9/16.
 */
public class RegisterInfo {
    public static final String COMMAND = "command";

    /**
     * (必选)
     * <p/>
     * 事件总线地址, HTTP请求的目标地址, 也是应用的监听地址.
     * 该地址既可以为具体的事件总线地址, 也可以是一个带参数的地址模板.
     */
    private String address;

    /**
     * (必选)
     * <p/>
     * HTTP请求的URL.
     */
    private String uri;

    /**
     * [可选]
     * <p/>
     * 事件总线地址对应的行为, 由应用负责处理.
     */
    private String action;

    private String className;

    private String dispatcher; //分发器

    private Date time; //注册时间

    //无参构造函数，用于从JSON反序列化
    public RegisterInfo() {
    }

    public RegisterInfo(String address, String uri, String method) {
        this.address = address;
        this.uri = uri;
    }

    /**
     * @return {@link #address}
     */
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(String dispatcher) {
        this.dispatcher = dispatcher;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    /**
     * @return 注册信息的hashcode.
     */
    @Override
    public int hashCode() {
        int result = uri != null ? uri.hashCode() : 0;
        result = 31 * result + (action != null ? action.hashCode() : 0);
        return result;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return "RegisterInfo{" +
                "action='" + action + '\'' +
                ", address='" + address + '\'' +
                ", uri='" + uri + '\'' +
                ", dispatcher='" + dispatcher + '\'' +
                ", time=" + time +
                '}';
    }
}
