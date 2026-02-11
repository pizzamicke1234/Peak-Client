package peak.managers.misc;

public class StopWatch {
    private long lastMS = System.currentTimeMillis();

    public void reset() {
        lastMS = System.currentTimeMillis();
    }

    public boolean hasTimeElapsed(long time) {
        return System.currentTimeMillis() - lastMS >= time;
    }

    // Das ist die Methode, die in deinem Rise-Source "finished" hieß
    public boolean finished(long delay) {
        return System.currentTimeMillis() - lastMS >= delay;
    }

    public long getElapsedTime() {
        return System.currentTimeMillis() - lastMS;
    }
}