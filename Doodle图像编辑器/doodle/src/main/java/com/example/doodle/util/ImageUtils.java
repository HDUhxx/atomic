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

import ohos.agp.components.element.Element;
import ohos.agp.components.element.PixelMapElement;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.render.Canvas;
import ohos.agp.utils.Matrix;
import ohos.media.image.ImageException;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Rect;
import ohos.media.image.common.Size;
import ohos.media.photokit.metadata.AVStorage;
import ohos.utils.net.Uri;

/**
 * fuqiangping
 *
 * @since 2021-04-29
 */
public class ImageUtils {
    private static final PixelFormat PIXEL_CONFIG = PixelFormat.ARGB_8888;
    private static final int COLORDRAWABLE_DIMENSION = 2;

    // 系统数据库存放图片的路径
    private static final Uri STORAGE_URI = AVStorage.Images.Media.EXTERNAL_DATA_ABILITY_URI;

    private ImageUtils() {
    }

    /**
     * 旋转图片
     *
     * @param pixelMap
     * @param degree
     * @param isRecycle
     * @return PixelMap
     */
    public static PixelMap rotate(PixelMap pixelMap,
                                  int degree, boolean isRecycle) {
        final int var = 2;
        Matrix matrix = new Matrix();
        Size size = pixelMap.getImageInfo().size;
        matrix.setRotate(degree, (float) size.width / var,
            (float) size.height / var);
        Rect rect = new Rect();
        rect.cropRect(0, 0, size.width,
            size.height);
        PixelMap bm1 = null;
        try {
            bm1 = PixelMap.create(pixelMap, rect, null);
            if (isRecycle) {
                pixelMap.release();
                return bm1;
            }
        } catch (OutOfMemoryError ex) {
            ex.getMessage();
        }
        return bm1;
    }

    /**
     * PixelMap
     *
     * @param element
     * @return PixelMap
     */
    // 部分api未确认，已注释掉，先跳过，后续完善或者找其他解决方案
    public static PixelMap getBitmapFromDrawable(Element element) {
        PixelMap pixelMap = null;
        if (element == null) {
            return pixelMap;
        }

        if (element instanceof PixelMapElement) {
            return ((PixelMapElement) element).getPixelMap();
        }
        try {
            PixelMap.InitializationOptions initializationOptions = new PixelMap.InitializationOptions();
            initializationOptions.pixelFormat = PIXEL_CONFIG;
            if (element instanceof ShapeElement) {
                initializationOptions.size.width = COLORDRAWABLE_DIMENSION;
                initializationOptions.size.height = COLORDRAWABLE_DIMENSION;
            }
            Canvas canvas = new Canvas();
            element.drawToCanvas(canvas);
            return pixelMap;
        } catch (ImageException e) {
            e.getMessage();
            return pixelMap;
        }
    }
}
