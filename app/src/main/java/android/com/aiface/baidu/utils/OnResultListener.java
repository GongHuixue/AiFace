/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package android.com.aiface.baidu.utils;

import android.com.aiface.baidu.exception.FaceError;

public interface OnResultListener<T> {
    void onResult(T result);

    void onError(FaceError error);
}
