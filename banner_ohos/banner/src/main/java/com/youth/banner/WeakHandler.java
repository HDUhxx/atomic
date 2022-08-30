/*
 *    Copyright 2021 youth5201314
 *    Copyright 2021 Institute of Software Chinese Academy of Sciences, ISRC

 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.youth.banner;


import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;

import java.lang.ref.WeakReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@SuppressWarnings("unused")
public class WeakHandler {
    private final EventHandler mExec;
    private Lock mLock = new ReentrantLock();
    @SuppressWarnings("ConstantConditions")
    final ChainedRef mRunnables = new ChainedRef(mLock, null);



    public WeakHandler( EventRunner eventRunner) {
        mExec = new EventHandler(eventRunner);
    }

//    /**
//     * Use the provided {@link Looper} instead of the default one and take a callback
//     * interface in which to handle messages.
//     *
//     * @param looper The looper, must not be null.
//     * @param callback The callback interface in which to handle messages, or null.
//     */
//    public WeakHandler(@NonNull Looper looper, @NonNull Handler.Callback callback) {
//        mCallback = callback;
//        mExec = new ExecHandler(looper, new WeakReference<>(callback));
//    }

    /**
     * Causes the Runnable r to be added to the message queue.
     * The runnable will be run on the thread to which this handler is
     * attached.
     *
     * @param r The Runnable that will be executed.
     *
     * @return Returns true if the Runnable was successfully placed in to the
     *         message queue.  Returns false on failure, usually because the
     *         looper processing the message queue is exiting.
     */
    public final void post( Runnable r) {
         mExec.postTask(wrapRunnable(r));
    }



    public final void postAtTime(Runnable r, long uptimeMillis) {
         mExec.postTimingTask(wrapRunnable(r), uptimeMillis);
    }


    public final void  postAtTime(Runnable r, Object token, long uptimeMillis) {
        mExec.postTimingTask(wrapRunnable(r),  uptimeMillis, (EventHandler.Priority) token);
    }

    /**
     * Causes the Runnable r to be added to the message queue, to be run
     * after the specified amount of time elapses.
     * The runnable will be run on the thread to which this handler
     * is attached.
     *
     * @param r The Runnable that will be executed.
     * @param delayMillis The delay (in milliseconds) until the Runnable
     *        will be executed.
     *
     * @return Returns true if the Runnable was successfully placed in to the
     *         message queue.  Returns false on failure, usually because the
     *         looper processing the message queue is exiting.  Note that a
     *         result of true does not mean the Runnable will be processed --
     *         if the looper is quit before the delivery time of the message
     *         occurs then the message will be dropped.
     */
    public final void postDelayed(Runnable r, long delayMillis) {
         mExec.postTask(wrapRunnable(r),delayMillis);
    }

    /**
     * Posts a message to an object that implements Runnable.
     * Causes the Runnable r to executed on the next iteration through the
     * message queue. The runnable will be run on the thread to which this
     * handler is attached.
     * <b>This method is only for use in very special circumstances -- it
     * can easily starve the message queue, cause ordering problems, or have
     * other unexpected side-effects.</b>
     *
     * @param r The Runnable that will be executed.
     *
     * @return Returns true if the message was successfully placed in to the
     *         message queue.  Returns false on failure, usually because the
     *         looper processing the message queue is exiting.
     */
    public final void postAtFrontOfQueue(Runnable r) {
         mExec.postTask(wrapRunnable(r));
    }

    /**
     * Remove any pending posts of Runnable r that are in the message queue.
     */
    public final void removeCallbacks(Runnable r) {
        final WeakRunnable runnable = mRunnables.remove(r);
        if (runnable != null) {
            mExec.removeTask(runnable);
        }
    }

    /**
     * Remove any pending posts of Runnable <var>r</var> with Object
     * <var>token</var> that are in the message queue.  If <var>token</var> is null,
     * all callbacks will be removed.
     */
    public final void removeCallbacks(Runnable r, Object token) {
        final WeakRunnable runnable = mRunnables.remove(r);
        if (runnable != null) {
            mExec.removeEvent(0,runnable);
        }
    }

    /**
     * Pushes a message onto the end of the message queue after all pending messages
     * before the current time. It will be received in callback,
     * in the thread attached to this handler.
     *
     * @return Returns true if the message was successfully placed in to the
     *         message queue.  Returns false on failure, usually because the
     *         looper processing the message queue is exiting.
     */
    public final void sendMessage(InnerEvent msg) {
         mExec.sendEvent(msg);
    }

    /**
     * Sends a Message containing only the what value.
     *
     * @return Returns true if the message was successfully placed in to the
     *         message queue.  Returns false on failure, usually because the
     *         looper processing the message queue is exiting.
     */
    public final void sendEmptyMessage(int what) {
         mExec.sendEvent(what);
    }


    public final void sendEmptyMessageDelayed(int what, long delayMillis) {
         mExec.sendEvent(what, delayMillis);
    }


    public final void sendEmptyMessageAtTime(int what, long uptimeMillis) {
         mExec.sendTimingEvent(what, uptimeMillis);
    }

    /**
     * Enqueue a message into the message queue after all pending messages
     * before (current time + delayMillis). You will receive it in
     * callback, in the thread attached to this handler.
     *
     * @return Returns true if the message was successfully placed in to the
     *         message queue.  Returns false on failure, usually because the
     *         looper processing the message queue is exiting.  Note that a
     *         result of true does not mean the message will be processed -- if
     *         the looper is quit before the delivery time of the message
     *         occurs then the message will be dropped.
     */
    public final void sendMessageDelayed(InnerEvent msg, long delayMillis) {
         mExec.sendEvent(msg, delayMillis);
    }


    public void sendMessageAtTime(InnerEvent msg, long uptimeMillis) {
         mExec.sendTimingEvent(msg, uptimeMillis);
    }

    /**
     * Enqueue a message at the front of the message queue, to be processed on
     * the next iteration of the message loop.  You will receive it in
     * callback, in the thread attached to this handler.
     * <b>This method is only for use in very special circumstances -- it
     * can easily starve the message queue, cause ordering problems, or have
     * other unexpected side-effects.</b>
     *
     * @return Returns true if the message was successfully placed in to the
     *         message queue.  Returns false on failure, usually because the
     *         looper processing the message queue is exiting.
     */
    public final void sendMessageAtFrontOfQueue(InnerEvent msg) {
         mExec.sendEvent(msg);
    }

    /**
     * Remove any pending posts of messages with code 'what' that are in the
     * message queue.
     */
    public final void removeMessages(int what) {
        mExec.removeEvent(what);
    }

    /**
     * Remove any pending posts of messages with code 'what' and whose obj is
     * 'object' that are in the message queue.  If <var>object</var> is null,
     * all messages will be removed.
     */
    public final void removeMessages(int what, Object object) {
        mExec.removeEvent(what, object);
    }

    /**
     * Remove any pending posts of callbacks and sent messages whose
     * <var>obj</var> is <var>token</var>.  If <var>token</var> is null,
     * all callbacks and messages will be removed.
     */
    public final void removeCallbacksAndMessages(Object token) {
        mExec.removeAllEvent();
    }

    /**
     * Check if there are any pending posts of messages with code 'what' in
     * the message queue.
     */
    public final boolean hasMessages(int what) {
        return mExec.hasInnerEvent(what);
    }

    /**
     * Check if there are any pending posts of messages with code 'what' and
     * whose obj is 'object' in the message queue.
     */
    public final boolean hasMessages(int what, Object object) {
        return mExec.hasInnerEvent( object);
    }

    public final EventRunner geteventrunner() {
        return mExec.getEventRunner();
    }

    private WeakRunnable wrapRunnable( Runnable r) {
        //noinspection ConstantConditions
        if (r == null) {
            throw new NullPointerException("Runnable can't be null");
        }
        final ChainedRef hardRef = new ChainedRef(mLock, r);
        mRunnables.insertAfter(hardRef);
        return hardRef.wrapper;
    }

//    private static class ExecHandler extends EventHandler {
////        private final WeakReference<Handler.Callback> mCallback;
//
//        ExecHandler() {
//            mCallback = null;
//        }
//
//        ExecHandler(WeakReference<Handler.Callback> callback) {
//            mCallback = callback;
//        }
//
//        ExecHandler(Looper looper) {
//            super(looper);
//            mCallback = null;
//        }
//
//        ExecHandler(Looper looper, WeakReference<Handler.Callback> callback) {
//            super(looper);
//            mCallback = callback;
//        }
//
//        public ExecHandler(EventRunner eventRunner) {
//        }
//
//        @Override
////        public void handleMessage(@NonNull Message msg) {
////            if (mCallback == null) {
////                return;
////            }
////            final Handler.Callback callback = mCallback.get();
////            if (callback == null) { // Already disposed
////                return;
////            }
////            callback.handleMessage(msg);
////        }
////    }

    static class WeakRunnable implements Runnable {
        private final WeakReference<Runnable> mDelegate;
        private final WeakReference<ChainedRef> mReference;

        WeakRunnable(WeakReference<Runnable> delegate, WeakReference<ChainedRef> reference) {
            mDelegate = delegate;
            mReference = reference;
        }

        @Override
        public void run() {
            final Runnable delegate = mDelegate.get();
            final ChainedRef reference = mReference.get();
            if (reference != null) {
                reference.remove();
            }
            if (delegate != null) {
                delegate.run();
            }
        }
    }

    static class ChainedRef {

        ChainedRef next;

        ChainedRef prev;

        final Runnable runnable;

        final WeakRunnable wrapper;


        Lock lock;

        public ChainedRef(Lock lock, Runnable r) {
            this.runnable = r;
            this.lock = lock;
            this.wrapper = new WeakRunnable(new WeakReference<>(r), new WeakReference<>(this));
        }

        public WeakRunnable remove() {
            lock.lock();
            try {
                if (prev != null) {
                    prev.next = next;
                }
                if (next != null) {
                    next.prev = prev;
                }
                prev = null;
                next = null;
            } finally {
                lock.unlock();
            }
            return wrapper;
        }

        public void insertAfter( ChainedRef candidate) {
            lock.lock();
            try {
                if (this.next != null) {
                    this.next.prev = candidate;
                }

                candidate.next = this.next;
                this.next = candidate;
                candidate.prev = this;
            } finally {
                lock.unlock();
            }
        }


        public WeakRunnable remove(Runnable obj) {
            lock.lock();
            try {
                ChainedRef curr = this.next; // Skipping head
                while (curr != null) {
                    if (curr.runnable == obj) { // We do comparison exactly how Handler does inside
                        return curr.remove();
                    }
                    curr = curr.next;
                }
            } finally {
                lock.unlock();
            }
            return null;
        }
    }
}