package com.wzp.nflj.util;


import com.wzp.nflj.enums.ResultEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: zp.wei
 * @DATE: 2020/10/16 16:05
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 156436793662573854L;

    private static Object[] nullArray = {};
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 结果消息
     */
    private String message;
    /**
     * 成功与否
     */
    private Boolean success;

    /**
     * 响应的时间
     */
    private String timestamp;
    /**
     * 成功时响应数据
     */
    private T result;


    /**
     * 请求成功  默认code为0 传入对应的返回结果
     *
     * @param result
     * @param <T>
     * @return
     */
    public static <T> Result<T> ok(T result) {
        return new Result<T>()
                .result(result)
                .success(ResultEnum.RESULT_SUCCESS.getSuccess())
                .code(ResultEnum.RESULT_SUCCESS.getCode())
                .timeStamp()
                .msg(ResultEnum.RESULT_SUCCESS.getMessage());
    }

    public static <T> Result<T> ok() {
        return new Result<T>()
                .result((T) nullArray)
                .success(ResultEnum.RESULT_SUCCESS.getSuccess())
                .code(ResultEnum.RESULT_SUCCESS.getCode())
                .timeStamp()
                .msg(ResultEnum.RESULT_SUCCESS.getMessage());
    }


    /**
     * 系统定义的错误返回结果
     *
     * @param <T>
     * @return
     */
    public static <T> Result<T> error() {
        return error(ResultEnum.BUSINESS_FAIL.getSuccess(), ResultEnum.BUSINESS_FAIL.getCode(), ResultEnum.BUSINESS_FAIL.getMessage());
    }

    /**
     * @param error 传入对应的错误码 自动返回对应错误消息
     * @param <T>
     * @return
     */
    public static <T> Result<T> error(ResultEnum error) {
        return error(error.getSuccess(), error.getCode(), error.getMessage());
    }

    /**
     * 全部自定义消息 与错误码
     *
     * @param code
     * @param message
     * @param <T>
     * @return
     */
    public static <T> Result<T> error(Boolean success, Integer code, String message) {
        Result<T> msg = new Result<>();
        msg.success = success;
        msg.message = message;
        msg.code = code;
        return msg.timeStamp();
    }

    public Result() {

    }

    public Result<T> result(T result) {
        this.result = result;
        return this;
    }

    public Result<T> success(Boolean success) {
        this.success = success;
        return this;
    }

    public Result<T> code(Integer code) {
        this.code = code;
        return this;
    }

    public Result<T> msg(String msg) {
        this.message = msg;
        return this;
    }

    private Result<T> timeStamp() {
        this.timestamp = DateUtil.formatLocalDateTime();
        return this;
    }

}
