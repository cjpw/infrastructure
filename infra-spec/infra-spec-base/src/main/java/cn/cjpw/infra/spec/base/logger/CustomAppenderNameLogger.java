package cn.cjpw.infra.spec.base.logger;

import org.slf4j.Logger;
import org.slf4j.MDC;
import org.slf4j.Marker;

/**
 * @author jun.chen1
 * @since 2023/11/13 16:45
 **/
class CustomAppenderNameLogger implements Logger {

    private Logger delegate;

    public CustomAppenderNameLogger(Logger logger){
        this.delegate=logger;
    }

    private void addPrintParam(){
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
        String cl = stackTraceElement.getClassName() + ":" + stackTraceElement.getLineNumber();
        MDC.put("by", cl);
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return delegate.isTraceEnabled();
    }

    @Override
    public void trace(String msg) {
        addPrintParam();
        delegate.trace(msg);
        MDC.remove("by");
    }

    @Override
    public void trace(String format, Object arg) {
        addPrintParam();
        delegate.trace(format, arg);
        MDC.remove("by");
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        addPrintParam();
        delegate.trace(format, arg1, arg2);
        MDC.remove("by");
    }

    @Override
    public void trace(String format, Object... arguments) {
        addPrintParam();
        delegate.trace(format, arguments);
        MDC.remove("by");
    }

    @Override
    public void trace(String msg, Throwable t) {
        addPrintParam();
        delegate.trace(msg, t);
        MDC.remove("by");
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return delegate.isTraceEnabled(marker);
    }

    @Override
    public void trace(Marker marker, String msg) {
        addPrintParam();
        delegate.trace(marker, msg);
        MDC.remove("by");
    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
        addPrintParam();
        delegate.trace(marker, format, arg);
        MDC.remove("by");
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        addPrintParam();
        delegate.trace(marker, format, arg1, arg2);
        MDC.remove("by");
    }

    @Override
    public void trace(Marker marker, String format, Object... argArray) {
        addPrintParam();
        delegate.trace(marker, format, argArray);
        MDC.remove("by");
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        addPrintParam();
        delegate.trace(marker, msg, t);
        MDC.remove("by");
    }

    @Override
    public boolean isDebugEnabled() {
        return delegate.isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        addPrintParam();
        delegate.debug(msg);
        MDC.remove("by");
    }

    @Override
    public void debug(String format, Object arg) {
        addPrintParam();
        delegate.debug(format, arg);
        MDC.remove("by");
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        addPrintParam();
        delegate.debug(format, arg1, arg2);
        MDC.remove("by");
    }

    @Override
    public void debug(String format, Object... arguments) {
        addPrintParam();
        delegate.debug(format, arguments);
        MDC.remove("by");
    }

    @Override
    public void debug(String msg, Throwable t) {
        addPrintParam();
        delegate.debug(msg, t);
        MDC.remove("by");
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        addPrintParam();
        return delegate.isDebugEnabled(marker);
    }

    @Override
    public void debug(Marker marker, String msg) {
        addPrintParam();
        delegate.debug(marker, msg);
        MDC.remove("by");
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        addPrintParam();
        delegate.debug(marker, format, arg);
        MDC.remove("by");
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        addPrintParam();
        delegate.debug(marker, format, arg1, arg2);
        MDC.remove("by");
    }

    @Override
    public void debug(Marker marker, String format, Object... arguments) {
        addPrintParam();
        delegate.debug(marker, format, arguments);
        MDC.remove("by");
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        addPrintParam();
        delegate.debug(marker, msg, t);
        MDC.remove("by");
    }

    @Override
    public boolean isInfoEnabled() {
        return delegate.isInfoEnabled();
    }

    @Override
    public void info(String msg) {
        addPrintParam();
        delegate.info(msg);
        MDC.remove("by");
    }

    @Override
    public void info(String format, Object arg) {
        addPrintParam();
        delegate.info(format, arg);
        MDC.remove("by");
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        addPrintParam();
        delegate.info(format, arg1, arg2);
        MDC.remove("by");
    }

    @Override
    public void info(String format, Object... arguments) {
        addPrintParam();
        delegate.info(format, arguments);
        MDC.remove("by");
    }

    @Override
    public void info(String msg, Throwable t) {
        addPrintParam();
        delegate.info(msg, t);
        MDC.remove("by");
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return delegate.isInfoEnabled(marker);
    }

    @Override
    public void info(Marker marker, String msg) {
        addPrintParam();
        delegate.info(marker, msg);
        MDC.remove("by");
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        addPrintParam();
        delegate.info(marker, format, arg);
        MDC.remove("by");
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        addPrintParam();
        delegate.info(marker, format, arg1, arg2);
        MDC.remove("by");
    }

    @Override
    public void info(Marker marker, String format, Object... arguments) {
        addPrintParam();
        delegate.info(marker, format, arguments);
        MDC.remove("by");
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        addPrintParam();
        delegate.info(marker, msg, t);
        MDC.remove("by");
    }

    @Override
    public boolean isWarnEnabled() {
        return delegate.isWarnEnabled();
    }

    @Override
    public void warn(String msg) {
        addPrintParam();
        delegate.warn(msg);
        MDC.remove("by");
    }

    @Override
    public void warn(String format, Object arg) {
        addPrintParam();
        delegate.warn(format, arg);
        MDC.remove("by");
    }

    @Override
    public void warn(String format, Object... arguments) {
        addPrintParam();
        delegate.warn(format, arguments);
        MDC.remove("by");
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        addPrintParam();
        delegate.warn(format, arg1, arg2);
        MDC.remove("by");
    }

    @Override
    public void warn(String msg, Throwable t) {
        addPrintParam();
        delegate.warn(msg, t);
        MDC.remove("by");
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return delegate.isWarnEnabled(marker);
    }

    @Override
    public void warn(Marker marker, String msg) {
        addPrintParam();
        delegate.warn(marker, msg);
        MDC.remove("by");
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
        addPrintParam();
        delegate.warn(marker, format, arg);
        MDC.remove("by");
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        addPrintParam();
        delegate.warn(marker, format, arg1, arg2);
        MDC.remove("by");
    }

    @Override
    public void warn(Marker marker, String format, Object... arguments) {
        addPrintParam();
        delegate.warn(marker, format, arguments);
        MDC.remove("by");
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        addPrintParam();
        delegate.warn(marker, msg, t);
        MDC.remove("by");
    }

    @Override
    public boolean isErrorEnabled() {
        return delegate.isErrorEnabled();
    }

    @Override
    public void error(String msg) {
        addPrintParam();
        delegate.error(msg);
        MDC.remove("by");
    }

    @Override
    public void error(String format, Object arg) {
        addPrintParam();
        delegate.error(format, arg);
        MDC.remove("by");
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        addPrintParam();
        delegate.error(format, arg1, arg2);
        MDC.remove("by");
    }

    @Override
    public void error(String format, Object... arguments) {
        addPrintParam();
        delegate.error(format, arguments);
        MDC.remove("by");
    }

    @Override
    public void error(String msg, Throwable t) {
        addPrintParam();
        delegate.error(msg, t);
        MDC.remove("by");
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return delegate.isErrorEnabled(marker);
    }

    @Override
    public void error(Marker marker, String msg) {
        addPrintParam();
        delegate.error(marker, msg);
        MDC.remove("by");
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
        addPrintParam();
        delegate.error(marker, format, arg);
        MDC.remove("by");
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        addPrintParam();
        delegate.error(marker, format, arg1, arg2);
        MDC.remove("by");
    }

    @Override
    public void error(Marker marker, String format, Object... arguments) {
        addPrintParam();
        delegate.error(marker, format, arguments);
        MDC.remove("by");
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        addPrintParam();
        delegate.error(marker, msg, t);
        MDC.remove("by");
    }
}
