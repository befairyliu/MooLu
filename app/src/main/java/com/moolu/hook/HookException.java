package com.moolu.hook;

/**
 * Created by Nanan on 2/27/2015.
 */
public class HookException extends Exception{
    private static final long serialVersionUID = 1L;
    public HookException(){
        super();
    }

    public HookException(final String detailMessage, final Throwable throwable) {
        super(detailMessage, throwable);
    }

    public HookException(final String detailMessage) {
        super(detailMessage);
    }

    public HookException(final Throwable throwable) {
        super(throwable);
    }
}
