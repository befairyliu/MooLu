package com.moolu.framework.entity;

/**
 * Created by Nanan on 2/26/2015.
 */
public class ResourcePrepareException extends Exception{

    private static final long serialVersionUID = 1L;

    public ResourcePrepareException() {
        super();
    }

    public ResourcePrepareException(final String detailMessage, final Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ResourcePrepareException(final String detailMessage) {
        super(detailMessage);
    }

    public ResourcePrepareException(final Throwable throwable) {
        super(throwable);
    }
}
