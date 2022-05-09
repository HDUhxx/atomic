/**
 * Copyright 2016 Harish Sridharan
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cooltechworks.scratchview.demo;

import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorValue;
import ohos.agp.components.Component;
import ohos.agp.components.Text;

/**
 * Created by Harish Sridharan 06/04/2016
 *
 * Original Source available at : https://gist.github.com/cooltechworks/618318af2af753e3f683
 */
public class FlipAnimator extends AnimatorValue {

    /**
     * Flip From Component - the component which is being shown before the animation.
     */
    private Text fromComponent;

    /**
     * Flip To Component - the component which should be shown after the animation.
     */
    private RotateText toComponent;

    private boolean visibilitySwapped;

    /**
     * Creates a 3D flip animation between two components.
     *
     * @param fromComponent First component in the transition.
     * @param toComponent Second component in the transition.
     */
    public FlipAnimator(Text fromComponent, RotateText toComponent) {
        this.fromComponent = fromComponent;
        this.toComponent = toComponent;

        setDuration(500);
        setCurveType(Animator.CurveType.ACCELERATE_DECELERATE);
    }

    public void applyTransformation(float interpolatedTime) {
        if (!visibilitySwapped) {
            fromComponent.setVisibility(Component.HIDE);
            toComponent.setVisibility(Component.VISIBLE);

            visibilitySwapped = true;
        }

        toComponent.setFraction(interpolatedTime);
    }
}