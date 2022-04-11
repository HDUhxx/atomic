/*
 * Copyright (C) 2017 Haoge
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lzh.nonview.router.executors;

import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;

import java.util.concurrent.Executor;

/**
 * 放到主线程执行
 */
public class MainThreadExecutor implements Executor{

    private EventHandler mainHandler = new EventHandler(EventRunner.getMainEventRunner());

    @Override
    public void execute(Runnable command) {
        //Looper.myLooper(), 获取当前进程的looper对象 Looper.getMainLooper()  获取主线程的Looper对象
       if( EventRunner.current()== EventRunner.getMainEventRunner()){
           command.run();
       }else{
           //在当前ui线程执行
           mainHandler.postSyncTask(command);
       }

    }
}
