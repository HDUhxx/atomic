package com.ohos.trebleshot.ui;

import com.ohos.trebleshot.utils.Interrupter;

/**
 * created by: veli
 * date: 16/04/18 22:41
 */
public interface UITask
{
    void updateTaskStarted(final Interrupter interrupter);

    void updateTaskStopped();
}
