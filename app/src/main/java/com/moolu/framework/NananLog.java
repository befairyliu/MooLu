package com.moolu.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.helpers.MarkerIgnoringBase;

/**
 * Created by Nanan on 2/3/2015.
 */
public class NananLog extends MarkerIgnoringBase {

    private Logger log = null;
    //for promote core extendability
    protected Class<?> theClass;

    /**
     * Set the log level,disable log when set it to 0,The greater the number
     * The greater the scope.
     */
    private static int LOG_LEVEL = 4;
    private final static int LOG_ERROR = 1;
    private final static int LOG_WARN = 2;
    private final static int LOG_INFO = 3;
    private final static int LOG_DEBUG = 4;
    private final static int LOG_TRACE = 5;

    private NananLog() {};
    public NananLog(Class<?> t){
        if(t == null){
            throw new IllegalArgumentException("owner must not be null");
        }
        this.log = LoggerFactory.getLogger(t);
        //for promote core extendability
        this.theClass = t;
    }

    public static void setLOG_LEVEL(int log_level){
        LOG_LEVEL = log_level;
    }


    @Override
    public boolean isTraceEnabled() {
        if(LOG_LEVEL > LOG_TRACE){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isDebugEnabled() {
        if(LOG_LEVEL > LOG_DEBUG){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isInfoEnabled() {
        if(LOG_LEVEL > LOG_INFO){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isWarnEnabled() {
        if(LOG_LEVEL > LOG_WARN){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isErrorEnabled() {
        if(LOG_LEVEL > LOG_ERROR){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void trace(String format, Object param1, Object param2) {
        if (!this.isTraceEnabled()) {
            return;
        }
        log.trace(format, param1, param2);
    }

    @Override
    public void trace(String format, Object param1) {
        if (!this.isTraceEnabled()) {
            return;
        }
        log.trace(format, param1);
    }

    @Override
    public void trace(String format, Object[] argArray) {
        if (!this.isTraceEnabled()) {
            return;
        }
        log.trace(format, argArray);
        //for promote core extendability
        StringBuilder sb = new StringBuilder();
        for (Object o : argArray) {
            sb.append(((o==null)?null:o.toString()));
            sb.append("\n");
        }
    }

    @Override
    public void trace(String msg, Throwable t) {
        if (!this.isTraceEnabled()) {
            return;
        }
        log.trace(msg, t);

        //for promote core extendability
        StringBuilder sb = new StringBuilder();
        if (t != null) {
            for (StackTraceElement element : t.getStackTrace()) {
                sb.append(((element==null)?null:element.toString()));
                sb.append("\n");
            }
        }
    }

    @Override
    public void trace(String msg) {
        if (!this.isTraceEnabled()) {
            return;
        }
        log.trace(msg);
    }

    @Override
    public void debug(String msg) {
        if (!this.isDebugEnabled()) {
            return;
        }
        log.debug(msg);
    }

    @Override
    public void debug(String msg, Throwable t) {
        if (!this.isDebugEnabled()) {
            return;
        }
        log.debug(msg, t);

        //for promote core extendability
        StringBuilder sb = new StringBuilder();
        if (t != null) {
            for (StackTraceElement element : t.getStackTrace()) {
                sb.append(((element==null)?null:element.toString()));
                sb.append("\n");
            }
        }
    }

    @Override
    public void debug(String format, Object[] argArray) {
        if (!this.isDebugEnabled()) {
            return;
        }
        log.debug(format, argArray);

        //for promote core extendability
        StringBuilder sb = new StringBuilder();
        for (Object o : argArray) {
            sb.append(((o==null)?null:o.toString()));
            sb.append("\n");
        }
    }

    @Override
    public void debug(String format, Object arg1) {
        if (!this.isDebugEnabled()) {
            return;
        }
        log.debug(format, arg1);
    }

    @Override
    public void debug(String format, Object param1, Object param2) {
        if (!this.isDebugEnabled()) {
            return;
        }
        log.debug(format, param1, param2);
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        if (!this.isInfoEnabled()) {
            return;
        }
        log.info(format, arg1, arg2);
    }

    @Override
    public void info(String format, Object arg) {
        if (!this.isInfoEnabled()) {
            return;
        }
        log.info(format, arg);
    }

    @Override
    public void info(String format, Object[] argArray) {
        if (!this.isInfoEnabled()) {
            return;
        }
        log.info(format, argArray);

        //for promote core extendability
        StringBuilder sb = new StringBuilder();
        for (Object o : argArray) {
            sb.append(((o==null)?null:o.toString()));
            sb.append("\n");
        }
    }

    @Override
    public void info(String msg, Throwable t) {
        if (!this.isInfoEnabled()) {
            return;
        }
        log.info(msg, t);

        //for promote core extendability
        StringBuilder sb = new StringBuilder();
        if (t != null) {
            for (StackTraceElement element : t.getStackTrace()) {
                sb.append(((element==null)?null:element.toString()));
                sb.append("\n");
            }
        }
    }

    @Override
    public void info(String msg) {
        if (!this.isInfoEnabled()) {
            return;
        }
        log.info(msg);
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        if (!this.isWarnEnabled()) {
            return;
        }
        log.warn(format, arg1, arg2);
    }

    @Override
    public void warn(String format, Object arg) {
        if (!this.isWarnEnabled()) {
            return;
        }
        log.warn(format, arg);
    }

    @Override
    public void warn(String format, Object[] argArray) {
        if (!this.isWarnEnabled()) {
            return;
        }
        log.warn(format, argArray);

        //for promote core extendability
        StringBuilder sb = new StringBuilder();
        for (Object o : argArray) {
            sb.append(((o==null)?null:o.toString()));
            sb.append("\n");
        }
    }

    @Override
    public void warn(String msg, Throwable t) {
        if (!this.isWarnEnabled()) {
            return;
        }
        log.warn(msg, t);

        //for promote core extendability
        StringBuilder sb = new StringBuilder();
        if (t != null) {
            for (StackTraceElement element : t.getStackTrace()) {
                sb.append(((element==null)?null:element.toString()));
                sb.append("\n");
            }
        }
    }

    @Override
    public void warn(String msg) {
        if (!this.isWarnEnabled()) {
            return;
        }
        log.warn(msg);
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        if (!this.isErrorEnabled()) {
            return;
        }
        log.error(format, arg1, arg2);
    }

    @Override
    public void error(String format, Object arg) {
        if (!this.isErrorEnabled()) {
            return;
        }
        log.error(format, arg);
    }

    @Override
    public void error(String format, Object[] argArray) {
        if (!this.isErrorEnabled()) {
            return;
        }
        log.error(format, argArray);

        //for promote core extendability
        StringBuilder sb = new StringBuilder();
        for (Object o : argArray) {
            sb.append(((o==null)?null:o.toString()));
            sb.append("\n");
        }
    }

    @Override
    public void error(String msg, Throwable t) {
        if (!this.isErrorEnabled()) {
            return;
        }
        log.error(msg, t);

        //for promote core extendability
        StringBuilder sb = new StringBuilder();
        if (t != null) {
            for (StackTraceElement element : t.getStackTrace()) {
                sb.append(((element==null)?null:element.toString()));
                sb.append("\n");
            }
        }
    }

    @Override
    public void error(String msg) {
        if (!this.isErrorEnabled()) {
            return;
        }
        log.error(msg);
    }
}
