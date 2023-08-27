package com.mayu.practice.base;

import java.util.HashMap;

public class ResponseMap extends HashMap<String, Object> {
    public static final String CODE_FIELD = "resCode";
    public static final String MESSAGE_FIELD = "resDesc";
    public static final String DATA_FIELD = "resData";
    public static final String SALT_FIELD = "salt";

    public ResponseMap() {
    }

    public ResponseMap(String resCode, String resDesc, Object resData) {
        this.put(CODE_FIELD, resCode);
        this.put(MESSAGE_FIELD, resDesc);
        this.put(DATA_FIELD, resData);
    }

    /**
     * define success response map on default code and message
     *
     * @return this
     */
    public static ResponseMap success() {
        return new ResponseMap(ResCode.SUCCESS, ResMessage.SUCCESS, null);
    }

    /**
     * define success response map on default code and message
     *
     * @param data data
     * @return this
     */
    public static ResponseMap success(Object data) {
        return new ResponseMap(ResCode.SUCCESS, ResMessage.SUCCESS, data);
    }

    /**
     * define fail response map on default code and message
     *
     * @return this
     */
    public static ResponseMap fail() {
        return new ResponseMap(ResCode.FAIL, ResMessage.FAIL, null);
    }

    /**
     * define fail response map on default code
     *
     * @param message ""
     * @return this
     */
    public static ResponseMap fail(String message) {
        return new ResponseMap(ResCode.FAIL, message, null);
    }

    /**
     * define fail response map
     *
     * @param code    ""
     * @param message ""
     * @return this
     */
    public static ResponseMap fail(String code, String message) {
        return new ResponseMap(code, message, null);
    }

    /**
     * @param key   key
     * @param value value
     * @return this
     */
    public ResponseMap putting(String key, Object value) {
        this.put(key, value);
        return this;
    }
}
