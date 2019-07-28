package com.wildbeeslabs.jentle.algorithms.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 通用的枚举类型
 * Created by hisen on 17-8-26.
 * E-mail: hisenyuan@gmail.com
 */
@Getter
@RequiredArgsConstructor
public enum CommonEnum {
    LOGIN_FAILED(10001, "登录失败,账号或者密码错误"),
    LOGIN_SUCCESS(10002, "登录成功"),
    LOGIN_LOCKED(10003, "账号被锁定,请重置密码"),
    REQUEST_SUCCESS(10003, "请求成功"),
    REQUEST_FAILED(10004, "请求成功"),
    JWT_SECRET(10005, "XX#$%()(#*!()!KL<><MQLMNQNQJQK sdfkjsdrow32234545df>?N<:{LWPW_hisen"),
    JWT_PAYLOAD(10006, "payload"),
    JWT_MAXAGE(10007, "3600000");// 60 * 60 * 1000 = 3600000

    private final int code;
    private final String msg;
}
