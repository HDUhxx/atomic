package com.ohos.trebleshot.utils;

/**
 * created by: Veli
 * date: 11.02.2018 19:37
 */

public abstract class InterruptAwareJob
{
    abstract protected void onRun();

    protected void run(Interrupter interrupter)
    {
        onRun();
        interrupter.removeClosers();
    }
}
