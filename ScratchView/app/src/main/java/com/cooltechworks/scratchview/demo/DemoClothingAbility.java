/**
 *
 * Copyright 2016 Harish Sridharan
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cooltechworks.scratchview.demo;

import com.cooltechworks.views.ScratchTextView;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Text;

/**
 * Created by Harish on 10/03/16.
 */
public class DemoClothingAbility extends Ability {

    private Text mScratchTitleView;

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_ability_clothing);

        mScratchTitleView = (Text) findComponentById(ResourceTable.Id_scratch_title_text);
        ScratchTextView scratchTextView = (ScratchTextView) findComponentById(ResourceTable.Id_scratch_view);

        if(scratchTextView != null) {
            scratchTextView.setRevealListener(new ScratchTextView.IRevealListener() {
                @Override
                public void onRevealed(ScratchTextView tv) {
                    showPrice();
                    mScratchTitleView.setText(ResourceTable.String_flat_200_offer);
                }

                @Override
                public void onRevealPercentChangedListener(ScratchTextView stv, float percent) {
                    // on percent reveal.
                }
            });
        }

    }

    /**
     * Reveals the discounted price.
     */
    private void showPrice() {
        Text priceBeforeView = (Text) findComponentById(ResourceTable.Id_price_before_text);
        RotateText priceAfterText = (RotateText) findComponentById(ResourceTable.Id_price_after_text);
        FlipAnimator animator = new FlipAnimator(priceBeforeView, priceAfterText);
        animator.setDuration(800);
        animator.setValueUpdateListener((animatorValue, v) -> {
            animator.applyTransformation(v);
        });
        animator.start();
    }


}
