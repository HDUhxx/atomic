/*
 * Copyright (C) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.doodle.util;

import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Point;
import ohos.agp.utils.Rect;
import ohos.agp.window.service.Window;

/**
 * fuqiangping
 *
 * @since 2021-04-29
 */
public class DrawUtil {
    private DrawUtil() {
    }

    /**
     * 计算 向量（px,py) 旋转ang角度后的新长度
     *
     * @param px
     * @param py
     * @param ang
     * @param isChLen
     * @param newLen
     * @return double[]
     */
    public static double[] rotateVec(float px, float py, double ang,
                                     boolean isChLen, double newLen) {
        final int defSize = 2;
        double[] mathstr = new double[defSize];

        // 矢量旋转函数，参数含义分别是x分量、y分量、旋转角、是否改变长度�?�新长度
        double vx = px * Math.cos(ang) - py * Math.sin(ang);
        double vy = px * Math.sin(ang) + py * Math.cos(ang);
        if (isChLen) {
            double var = Math.sqrt(vx * vx + vy * vy);
            vx = vx / var * newLen;
            vy = vy / var * newLen;
        }
        mathstr[0] = vx;
        mathstr[1] = vy;
        return mathstr;
    }

    /**
     * drawCircle
     *
     * @param canvas
     * @param cx
     * @param cy
     * @param radius
     * @param paint
     */
    public static void drawCircle(Canvas canvas, float cx, float cy, float radius, Paint paint) {
        canvas.drawCircle(cx, cy, radius, paint);
    }

    /**
     * drawRect
     *
     * @param canvas
     * @param sx
     * @param sy
     * @param dx
     * @param dy
     * @param paint
     */
    public static void drawRect(Canvas canvas, float sx, float sy, float dx, float dy, Paint paint) {
        // 保证　左上角　与　右下角　的对应关系
        if (sx < dx) {
            if (sy < dy) {
                canvas.drawRect(sx, sy, dx, dy, paint);
            } else {
                canvas.drawRect(sx, dy, dx, sy, paint);
            }
        } else {
            if (sy < dy) {
                canvas.drawRect(dx, sy, sx, dy, paint);
            } else {
                canvas.drawRect(dx, dy, sx, sy, paint);
            }
        }
    }

    /**
     * 计算点p2绕p1顺时针旋转的角度
     *
     * @param px1
     * @param py1
     * @param px2
     * @param py2
     * @return 旋转的角度
     */
    public static float computeAngle(float px1, float py1, float px2, float py2) {
        final int rotate90 = 90;
        final int rotate180 = 180;
        final int rotate270 = 270;
        final int rotate360 = 360;
        final int parm = 2;
        float pointX = px2 - px1;
        float pointY = py2 - py1;

        float arc = (float) Math.atan(pointY / pointX);

        float angle = (float) (arc / (Math.PI * parm) * rotate360);
        if (pointX >= 0 && pointY == 0) {
            angle = 0;
        } else if (pointX < 0 && pointY == 0) {
            angle = rotate180;
        } else if (pointX == 0 && pointY > 0) {
            angle = rotate90;
        } else if (pointX == 0 && pointY < 0) {
            angle = rotate270;
        } else if (pointX < 0 && pointY > 0) {
            angle = rotate180 + angle;
        } else if (pointX < 0 && pointY < 0) {
            angle = rotate180 + angle;
        } else if (pointX > 0 && pointY < 0) {
            angle = rotate360 + angle;
        }

        LogUtil.i("LogUtil", "[" + px1 + "," + py1 + "]:[" + px2 + "," + py2 + "] = " + angle);

        return angle;
    }

    /**
     * 顺时针旋转
     *
     * @param coords
     * @param degree
     * @param x
     * @param y
     * @param px
     * @param py
     * @return Point
     */
    public static Point rotatePoint(Point coords, float degree, float x, float y, float px, float py) {
        final int rotate90 = 90;
        final int rotate180 = 180;
        final int rotate270 = 270;
        final int rotate360 = 360;
        if (0 == degree % rotate360) {
            coords.modify(x, y);
            return coords;
        }

        // 角度变成弧度
        float radian = (float) (degree * Math.PI / rotate180);
        coords.modify((float) ((x - px) * Math.cos(radian) - (y - py) * Math.sin(radian) + px),
            (float) ((x - px) * Math.sin(radian) + (y - py) * Math.cos(radian) + py));
        return coords;
    }

    /**
     * assistActivity
     *
     * @param window
     */
    public static void assistActivity(Window window) {
//        new OhosBug5497Workaround(window);
    }

    /**
     * scaleRect
     *
     * @param rect
     * @param scale
     * @param px
     * @param py
     */
    public static void scaleRect(Rect rect, float scale, float px, float py) {
        final float var = 0.5f;
        rect.left = (int) (px - scale * (px - rect.left) + var);
        rect.right = (int) (px - scale * (px - rect.right) + var);
        rect.top = (int) (py - scale * (py - rect.top) + var);
        rect.bottom = (int) (py - scale * (py - rect.bottom) + var);
    }
}
