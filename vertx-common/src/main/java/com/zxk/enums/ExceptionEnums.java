package com.zxk.enums;

/**
 * Created by wangyi on 2016/11/17.
 */
public enum  ExceptionEnums {
//    系统返回
    SUCCESS("S0000","处理成功"),
    ERROR("E10000","系统异常"),

    E10001("E10001","解析渠道密文失败"),
    E10002("E10002","调用PAY失败"),
    E10003("E10003","调用TRADE失败"),
    E10004("E10004","request转化异常"),

    //    HTTP返回
    REQUEST_NULL("H0001","请求对象为空"),REQ_BIZCODE_NULL("H0002","bizCode为空"),
    REQ_CONTEXT_NULL("H0003","context为空"),REQ_SERVICECODE_NULL("H0004","serviceCode为空"),
    REQ_SIGN_NULL("H0005","sign为空"),QUERY_BIZCODE_NULL("H0006","业务线未注册或被禁用"),
    QUERY_SERVICE_CODE_NULL("H0007","接口未注册或被禁用"),IP_NULL("H0020","IP地址不在白名单"),
    BIZ_KEY_NULL("H0008","解密密钥未配置"),UNSIGN_ERROR("H0009","验签失败"),
    DECRYPT_ERROR("H0010","解密失败"),INVOKE_UNIMPL_METHOD("H0011","接口在网关未实现"),
    HANDLER_ERROR("H0012","接口未知异常"),INVOKE_METHOD_ERROR("H0013","接口调用失败"),
    PARAM_DECRYPT_NOT_JSON("H0014","参数解密后不是JSON格式"),PARAM_DONT_MATCH_INTE("H0015","参数格式与接口参数不符"),
    PARAM_ERROR("H0016","参数异常"),PARAM_NOT_JSON("H0017","接口返回不是JSON格式"),
    PARAM_DONT_MATCH("H0018","接口返回格式不正确"),UNKNOW_ERROR("H9999","网关未知异常"),
    BIZ_KEY_MULTIPLE("H0019","多密钥请指定密钥名"),PARAM_PARSE_OBJECT_ERROR("H0020","参数转换请求对象异常"),
    RISK_TYPE_ERROR("H0021","风控回调类型错误"),

    //admin返回
    PARAMS_ERROR("A0001","参数异常"),SAVE_ERROR("A0002","保存数据异常"),
    DEL_ERROR("A0003","删除数据异常"),UPDATE_ERROR("A0004","更新数据异常"),
    SNAME_SCODE_ERROR("A0005","服务名或服务编码已存在"),UNKNOW_DATA("A0006","未找到的数据"),
    BIZ_DETAIL_DUPLICATE("A0007","该业务线已绑定该接口"),BIZ_UNAVAILABLE("A0008","业务线不可用"),
    SERVICE_UNAVAILABLE("A0009","接口不可用"),


//    RPC返回
    HTTP_METHOD_NULL("R0001","Http回调方法为空"),PARAM_NULL("R0002","回调参数为空"),
    NOTIFY_URL_NULL("R0003","回调地址为空"),NOTIFY_BIZCODE_NULL("H0004","bizCode为空"),
    BUILD_REQ_ERROR("R0005","组装回调对象异常"),HTTP_SEND_ERROR("R0006","HTTP发送异常"),
    NOTIFY_URL_ERROR("R0007","回调地址异常");

    public String code;

    public String msg;

    ExceptionEnums(String code, String msg) {
       this.code = code;
        this.msg = msg;
    }
}
