package com.example.movieboxoffice.entity;

import lombok.Data;

@Data
public class Response<T> {

    private int code;

    private String message;

    private T data;
    // 默认构造函数
    public Response() {}

    // 成功状态的构造函数
    public Response(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 成功状态的简单构造函数
    public Response(int code, T data) {
        this(code, "success", data);
    }

    // 失败状态的构造函数
    public Response(int code, String message) {
        this.code = code;
        this.message = message;
    }


    // 成功响应的静态方法
    public static <T> Response<T> success(T data) {
        return new Response<>(200, "操作成功", data);
    }

    public static <T> Response<T> success(String message, T data) {
        return new Response<>(200, message, data);
    }

    // 错误响应的静态方法
    public static <T> Response<T> error(int errorCode, String errorMessage) {
        return new Response<>(errorCode, errorMessage, null);
    }

    public static <T> Response<T> error(String errorMessage) {
        return error(500, errorMessage); // 假设500为通用错误码
    }


}

