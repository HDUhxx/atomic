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
 *
 */

package com.example.doodledemo;

import com.example.doodle.DoodleColor;
import com.example.doodle.DoodleOnTouchGestureListener;
import com.example.doodle.DoodleShape;
import com.example.doodle.DoodleTouchDetector;
import com.example.doodle.DoodleView;
import com.example.doodle.IDoodleListener;
import com.example.doodle.core.IDoodle;
import com.example.doodle.core.IDoodleItem;
import com.example.doodle.core.IDoodlePen;
import com.example.doodle.util.LogUtil;
import com.example.doodle.util.Toast;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.render.PixelMapHolder;
import ohos.agp.render.Texture;
import ohos.agp.utils.Matrix;
import ohos.global.resource.NotExistException;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;

import java.io.IOException;
import java.io.InputStream;

/**
 * fuqiangping
 *
 * @since 2021-04-22
 */
public class MosaicDemo extends Ability implements Component.ClickedListener {
    private static final String TAG = "MosaicDemo";
    private DoodleView doodleView;
    private final static int mDoodleSizeParm = 30;
    private final static int mMosaicLevel1 = 5;
    private final static int mMosaicLevel2 = 20;
    private final static int mMosaicLevel3 = 50;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_ability_mosaic_demo);
        findComponentById(ResourceTable.Id_btn_mosaic_x1).setClickedListener(this);
        findComponentById(ResourceTable.Id_btn_mosaic_x2).setClickedListener(this);
        findComponentById(ResourceTable.Id_btn_mosaic_x3).setClickedListener(this);

        // step 1
        PixelMap pixelMap = getPixelMapFromResource(ResourceTable.Media_thelittleprince2);
        doodleView = new DoodleView(this, pixelMap, new IDoodleListener() {
            @Override
            public void onSaved(IDoodle doodle, PixelMap doodlePixelMap, Runnable callback) {
                Toast.show(MosaicDemo.this, "onSaved", Toast.LENGTH_SHORT);
            }

            @Override
            public void onReady(IDoodle doodle, int width, int height) {
                doodle.setSize(mDoodleSizeParm * doodle.getUnitSize());
            }
        });

        // step 2
        DoodleOnTouchGestureListener touchGestureListener = new DoodleOnTouchGestureListener(doodleView, null);
        DoodleTouchDetector touchDetector = new DoodleTouchDetector(this, touchGestureListener);
        doodleView.setDefaultTouchDetector(touchDetector);

        // step 3
        doodleView.setPen(new MosaicPen());
        doodleView.setShape(DoodleShape.HAND_WRITE);

        // setColor
        findComponentById(ResourceTable.Id_btn_mosaic_x3).simulateClick(); // see setMosaicLevel(View view)

        // step 4
        ComponentContainer container = (ComponentContainer) findComponentById(ResourceTable.Id_doodle_container);
        container.addComponent(doodleView);
    }

    private DoodleColor getMosaicColor(int level) {
        Matrix matrix = new Matrix();
        matrix.setScale(1f / level, 1f / level);
        PixelMap.InitializationOptions options = new PixelMap.InitializationOptions();
        options.editable = true;
        PixelMap temp = PixelMap.create(doodleView.getPixelMap(), options);
        Canvas tempCanvas = new Canvas(new Texture(temp));
        tempCanvas.setMatrix(matrix);
        tempCanvas.drawPixelMapHolder(new PixelMapHolder(doodleView.getPixelMap()), 0, 0, new Paint());
        PixelMap mosaicBitmap = temp;

        matrix.reset();
        matrix.setScale(level, level);
        DoodleColor doodleColor = new DoodleColor(mosaicBitmap, matrix);
        doodleColor.setLevel(level);
        return doodleColor;
    }

    /**
     * 通过图片ID返回PixelMap
     *
     * @param resourceId 图片的资源ID
     * @return 图片的PixelMap
     */
    private PixelMap getPixelMapFromResource(int resourceId) {
        InputStream inputStream = null;
        PixelMap pixelMap = null;
        try {
            // 创建图像数据源ImageSource对象
            inputStream = getResourceManager().getResource(resourceId);
            ImageSource.SourceOptions srcOpts = new ImageSource.SourceOptions();
            srcOpts.formatHint = "image/png";
            ImageSource imageSource = ImageSource.create(inputStream, srcOpts);

            // 设置图片参数
            ImageSource.DecodingOptions decodingOptions = new ImageSource.DecodingOptions();
            pixelMap = imageSource.createPixelmap(decodingOptions);
            return pixelMap;
        } catch (IOException e) {
            LogUtil.i(TAG, "IOException");
        } catch (NotExistException e) {
            LogUtil.i(TAG, "NotExistException");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LogUtil.i(TAG, "inputStream IOException");
                }
            }
        }
        return pixelMap;
    }

    @Override
    public void onClick(Component component) {
        if (component.getId() == ResourceTable.Id_btn_mosaic_x1) {
            doodleView.setColor(getMosaicColor(mMosaicLevel1));
        } else if (component.getId() == ResourceTable.Id_btn_mosaic_x2) {
            doodleView.setColor(getMosaicColor(mMosaicLevel2));
        } else if (component.getId() == ResourceTable.Id_btn_mosaic_x3) {
            doodleView.setColor(getMosaicColor(mMosaicLevel3));
        }
    }

    /**
     * 虽然这里设置新的画笔不是必要的，但是基于设计的规范应该这样做。马赛克画笔在概念上不同于其他画笔，
     *
     * @since 2021-04-14
     */
    private static class MosaicPen implements IDoodlePen {
        @Override
        public void config(IDoodleItem doodleItem, Paint paint) {
        }

        @Override
        public void drawHelpers(Canvas canvas, IDoodle doodle) {
        }

        @Override
        public IDoodlePen copy() {
            return this;
        }
    }
}
