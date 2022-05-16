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
package com.lxj.xpopup.util;

import ohos.agp.render.Path;

public class MyPath extends Path {

    private float nowPointX = 0;
    private float nowPointY = 0;

    public MyPath() {
    }

    public MyPath(Path path) {
        super(path);
    }

    @Override
    public void moveTo(float xCoor, float yCoor) {
        super.moveTo(xCoor, yCoor);
        nowPointX = xCoor;
        nowPointY = yCoor;
    }

    @Override
    public void lineTo(float xCoor, float yCoor) {
        super.lineTo(xCoor, yCoor);
        nowPointX = xCoor;
        nowPointY = yCoor;
    }

    @Override
    public void quadTo(float xCoor0, float yCoor0, float xCoor1, float yCoor1) {
        super.quadTo(xCoor0, yCoor0, xCoor1, yCoor1);
        nowPointX = xCoor1;
        nowPointY = yCoor1;
    }

    @Override
    public void cubicTo(float x1, float y1, float x2, float y2, float x3, float y3) {
        super.cubicTo(x1, y1, x2, y2, x3, y3);
        nowPointX = x3;
        nowPointY = y3;
    }

    /**
     * 添加基于相对位置的三次贝塞尔曲线
     *
     * @param x1 第二个点的x坐标
     * @param y1 第二个点的y坐标
     * @param x2 第三个点的x坐标
     * @param y2 第三个点的y坐标
     * @param x3 第四个点的x坐标
     * @param y3 第四个点的y坐标
     */
    public void rCubicTo(float x1, float y1, float x2, float y2, float x3, float y3) {
        cubicTo(x1 + nowPointX, y1 + nowPointY,
                x2 + nowPointX, y2 + nowPointY,
                x3 + nowPointX, y3 + nowPointY);
    }

    @Override
    public void reset() {
        super.reset();
        nowPointX = 0;
        nowPointY = 0;
    }
}
