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
package com.lzh.nonview.router.route;


import com.lzh.nonview.router.activityresult.ActivityResultCallback;
import com.lzh.nonview.router.tools.Constants;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;

/**
 * Base on the IBaseRoute, This interface provided some methods
 */
public interface IActivityRoute extends IBaseRoute<IActivityRoute> {

    /**
     *  Create intent by  that parsed by uri
     *
     * @param context
     * @return Intent
     */
    Intent createIntent(Ability context);

    /**
     * requestCode
     *
     * @param requestCode
     * @return IActivityRoute
     */
    IActivityRoute requestCode(int requestCode);

    /**
     * resultCallback
     *
     * @param callback
     * @return IActivityRoute
     */
    IActivityRoute resultCallback(ActivityResultCallback callback);

    /**
     * setOptions
     *
     * @param options
     * @return IActivityRoute
     */
    IActivityRoute setOptions(IntentParams options);

    /**
     * setAnim
     *
     * @param enterAnim enter animation
     * @param exitAnim exit animation
     * @return IActivityRoute
     */
    IActivityRoute setAnim(int enterAnim, int exitAnim);

    /**
     * addFlags
     *
     * @param flag
     * @return IActivityRoute
     */
    IActivityRoute addFlags(int flag);

    /**
     * open
     *
     * @param fragment Fraction
     */
    void open(Fraction fragment);

    /**
     * EmptyActivityRoute
     */
    class EmptyActivityRoute extends EmptyBaseRoute<IActivityRoute> implements IActivityRoute {

        public EmptyActivityRoute(InternalCallback internal) {
            super(internal);
        }

        @Override
        public Intent createIntent(Ability context) {
            internal.invoke(context);
            return new Intent();
        }

        @Override
        public IActivityRoute requestCode(int requestCode) {
            internal.getExtras().setRequestCode(requestCode);
            return this;
        }

        @Override
        public IActivityRoute resultCallback(ActivityResultCallback callback) {
            internal.getExtras().putValue(Constants.KEY_RESULT_CALLBACK, callback);
            return this;
        }

        @Override
        public IActivityRoute setOptions(IntentParams options) {
            internal.getExtras().putValue(Constants.KEY_ACTIVITY_OPTIONS, options);
            return this;
        }

        @Override
        public IActivityRoute setAnim(int enterAnim, int exitAnim) {
            internal.getExtras().setInAnimation(enterAnim);
            internal.getExtras().setOutAnimation(exitAnim);
            return this;
        }

        @Override
        public IActivityRoute addFlags(int flag) {
            internal.getExtras().addFlags(flag);
            return this;
        }

        @Override
        public void open(Fraction fragment) {
            internal.invoke(fragment.getFractionAbility());
        }
    }
}
