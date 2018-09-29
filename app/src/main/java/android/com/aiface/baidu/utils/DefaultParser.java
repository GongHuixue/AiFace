/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package android.com.aiface.baidu.utils;

import android.com.aiface.baidu.exception.FaceError;
import android.com.aiface.baidu.model.ResponseResult;
import android.com.aiface.baidu.parser.Parser;
import android.util.Log;


import org.json.JSONException;
import org.json.JSONObject;

public class DefaultParser implements Parser<ResponseResult> {

    @Override
    public ResponseResult parse(String json) throws FaceError {
        Log.e("xx", "DefaultParser:" + json);
        try {
            JSONObject jsonObject = new JSONObject(json);

            if (jsonObject.has("error_code")) {
                FaceError error = new FaceError(jsonObject.optInt("error_code"), jsonObject.optString("error_msg"));
                throw error;
            }

            ResponseResult result = new ResponseResult();
            result.setLogId(jsonObject.optLong("log_id"));
            result.setJsonRes(json);

            return result;
        } catch (JSONException e) {
            e.printStackTrace();
            FaceError error = new FaceError(FaceError.ErrorCode.JSON_PARSE_ERROR, "Json parse error:" + json, e);
            throw error;
        }
    }
}
