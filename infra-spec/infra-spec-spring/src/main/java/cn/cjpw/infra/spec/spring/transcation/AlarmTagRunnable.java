package cn.cjpw.infra.spec.spring.transcation;

public class AlarmTagRunnable implements Runnable, AlarmTag {

    private Runnable runnable;
    private String sourceTid;
    private String runnerTag;

    AlarmTagRunnable(Runnable runnable, String sourceTid, String runnerTag) {
        this.runnable = runnable;
        this.sourceTid = sourceTid;
        this.runnerTag = runnerTag;
    }

    @Override
    public void run() {
        runnable.run();
    }

    public Runnable getRunnable() {
        return runnable;
    }

    @Override
    public String getSourceTid() {
        return sourceTid;
    }

    @Override
    public String getRunnerTag() {
        return runnerTag;
    }
}