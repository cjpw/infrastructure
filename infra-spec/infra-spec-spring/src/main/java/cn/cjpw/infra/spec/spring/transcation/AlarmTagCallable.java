package cn.cjpw.infra.spec.spring.transcation;

import java.util.concurrent.Callable;

public class AlarmTagCallable<T> implements Callable<T>, AlarmTag {

    private Callable<T> callable;
    private String sourceTid;
    private String runnerTag;

    AlarmTagCallable(Callable<T> callable, String sourceTid, String runnerTag) {
        this.callable = callable;
        this.sourceTid = sourceTid;
        this.runnerTag = runnerTag;
    }

    public Callable<T> getCallable() {
        return callable;
    }

    @Override
    public String getSourceTid() {
        return sourceTid;
    }

    @Override
    public String getRunnerTag() {
        return runnerTag;
    }

    @Override
    public T call() throws Exception {
        return callable.call();
    }
}