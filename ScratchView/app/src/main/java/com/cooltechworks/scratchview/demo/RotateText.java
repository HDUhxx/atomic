 /*
  * Copyright (c) 2021 Huawei Device Co., Ltd.
  * <p>
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  * <p>
  * http://www.apache.org/licenses/LICENSE-2.0
  * <p>
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package com.cooltechworks.scratchview.demo;

import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.render.ThreeDimView;
import ohos.agp.utils.Color;
import ohos.agp.utils.Rect;
import ohos.app.Context;

public class RotateText extends Text implements Component.DrawTask {
    private float fraction;

    public RotateText(Context context) {
        super(context);
        init();
    }

    public RotateText(Context context, AttrSet attrSet) {
        super(context, attrSet);
        init();
    }

    public RotateText(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        init();
    }

    private void init() {
        addDrawTask(this);
    }

    @Override
    public void onDraw(Component component, Canvas canvas) {
        drawContent(canvas);
    }

    private void drawContent(Canvas canvas) {
        final double radians = Math.PI * fraction;
        float degrees = (float) (180.0 * radians / Math.PI);
        if (fraction >= 0.5f) {
            degrees -= 180f;
        }
        degrees = -degrees;
        ThreeDimView camera = new ThreeDimView();
        Paint paint = new Paint();
        paint.setTextSize(getTextSize());
        paint.setFakeBoldText(true);
        canvas.save();
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        canvas.clipRect(centerX, centerY, centerX, centerY);
        canvas.translate(centerX, centerY);
        camera.rotateY(degrees);
        camera.applyToCanvas(canvas);
        canvas.translate(-centerX, -centerY);
        String content1 = "₹ 1400";
        String content2 = " ₹ 1200";
        int[] textDimens1 = getTextDimensions(content1, paint);
        int[] textDimens2 = getTextDimensions(content2, paint);
        int width1 = textDimens1[0];
        int height1 = textDimens1[1];
        int width2 = textDimens2[0];
        int height2 = textDimens2[1];
        paint.setColor(new Color(Color.getIntColor("#999999")));
        canvas.drawText(paint, content1, centerX - (width1 + width2) / 2, centerY + height1 / 2);
        paint.setColor(new Color(Color.getIntColor("#333333")));
        canvas.drawLine(centerX - (width1 + width2) / 2, centerY, centerX - (width1 + width2) / 2 + width1, centerY, paint);
        paint.setColor(Color.RED);
        canvas.drawText(paint, content2, centerX - (width1 + width2) / 2 + width1, centerY + height2 / 2);
        canvas.restore();
    }

    public void setFraction(float fraction) {
        this.fraction = fraction;
        invalidate();
    }

    private int[] getTextDimensions(String text, Paint paint) {
        Rect bounds = paint.getTextBounds(text);
        int width = bounds.left + bounds.getWidth();
        int height = bounds.bottom + bounds.getHeight();

        return new int[] { width, height};
    }
}
