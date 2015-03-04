package com.moolu.json.jackson;

/**
 * Created by Nanan on 3/4/2015.
 */
public class ResponseDo<T> {
    public final static	int FAILED_CODE=-1;

    private String message;private T result;
    private int resultCode=1;


    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
}
