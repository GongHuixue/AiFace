/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package android.com.aiface.baidu.parser;


import android.com.aiface.baidu.exception.FaceError;

/**
 * JSON解析
 * @param <T>
 */
public interface Parser<T> {
    T parse(String json) throws FaceError;
}
