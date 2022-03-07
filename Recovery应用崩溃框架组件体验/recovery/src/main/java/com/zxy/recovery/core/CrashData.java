package com.zxy.recovery.core;

import java.io.Serializable;

/**
 * ApplicationLoader
 *
 * @author:zhengxiaoyong
 * @since 2021-04-06
 */
public class CrashData implements Serializable {
    /**
     * 崩溃时间
     */
    public long crashTime;

    /**
     * 崩溃次数
     */
    public int crashCount;

    /**
     * 是否重启
     */
    public boolean shouldRestart;

    private CrashData() {
    }

    /**
     * newInstance
     *
     * @return CrashData
     */
    public static CrashData newInstance() {
        return new CrashData();
    }

    /**
     * time
     *
     * @param time
     * @return CrashData
     */
    public CrashData time(long time) {
        this.crashTime = time;
        return this;
    }

    /**
     * count
     *
     * @param count
     * @return CrashData
     */
    public CrashData count(int count) {
        this.crashCount = count;
        return this;
    }

    /**
     * restart
     *
     * @param restart
     * @return CrashData
     */
    public CrashData restart(boolean restart) {
        this.shouldRestart = restart;
        return this;
    }

    @Override
    public String toString() {
        return "CrashData{"
                + "crashCount="
                + crashCount
                + ", crashTime="
                + crashTime
                + ", shouldRestart="
                + shouldRestart
                +
                '}';
    }
}
