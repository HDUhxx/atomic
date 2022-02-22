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
import com.example.doodle.DoodleOnTouchGestureListenerScaleText;
import com.example.doodle.DoodlePen;
import com.example.doodle.DoodleShape;
import com.example.doodle.DoodleText;
import com.example.doodle.DoodleTouchDetector;
import com.example.doodle.DoodleTouchDetectorScaleText;
import com.example.doodle.DoodleView;
import com.example.doodle.IDoodleListener;
import com.example.doodle.ScaleGestureDetectorApi27;
import com.example.doodle.core.IDoodle;
import com.example.doodle.core.IDoodleItem;
import com.example.doodle.core.IDoodleSelectableItem;
import com.example.doodle.dialog.DialogController;
import com.example.doodle.util.KeyboardUtils;
import com.example.doodle.util.LogUtil;
import com.example.doodle.util.Toast;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.agp.components.AttrHelper;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.StackLayout;
import ohos.agp.components.TextField;
import ohos.agp.utils.Color;
import ohos.agp.utils.TextTool;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.global.resource.NotExistException;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * fuqiangping
 *
 * @since 2021-04-22
 */
public class ScaleGestureItemDemoAbility extends Ability {
    private final static String mTAG = "ScaleGestureItemDemoAbility";
    private DoodleView doodleView;
    private DoodleOnTouchGestureListenerScaleText touchGestureListener;
    private List<Object> mComponentlist;
    private EventHandler mEventHandler;
    private Runnable task;
    private TextField textField;
//    private CommonDialog commonDialog;
    private DirectionalLayout dirbottom;
    private boolean mIsShow = false;
    private StackLayout mStackLayout;
    private final static int mBottomBtnIndex = 2;
    private final static int mBottomMargin = 280;
    private final static int mDelayTime = 500;
    private final static int mDoodleSizeParm = 40;
    private final static int mDoodleX = 10;
    private final static int mDoodleYParm = 2;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_scale_gesture_item);
        mComponentlist = new ArrayList<>();

        // step 1
        PixelMap pixelMap = getPixelMapFromResource(ResourceTable.Media_thelittleprince2);
        mStackLayout = (StackLayout) findComponentById(ResourceTable.Id_doodle_container);
        doodleView = new DoodleView(this, pixelMap, new IDoodleListener() {
            @Override
            public void onSaved(IDoodle doodle, PixelMap doodlePixelMap, Runnable callback) {
                Toast.show(getContext(), "onSaved");
            }

            @Override
            public void onReady(IDoodle doodle, int width, int height) {
                doodle.setSize(mDoodleSizeParm * doodle.getUnitSize());
                IDoodleSelectableItem item = new DoodleText(doodle, "Hello, world", doodle.getSize(),
                    doodle.getColor(), mDoodleX, doodleView.getPixelMap().getImageInfo().size.height / mDoodleYParm);
                touchGestureListener.setSelectedItem(item);
                doodle.addItem(item);
            }
        });

        // step 2
        touchGestureListener = new ScaleItemOnTouchGestureListener(doodleView,
            new DoodleOnTouchGestureListenerScaleText.ISelectionListener() {
                @Override
                public void onSelectedItem(IDoodle doodle, IDoodleSelectableItem selectableItem, boolean isSelected) {
                }

                @Override
                public void onCreateSelectableItem(final IDoodle doodle, final float x, final float y) {
                    if (doodle.getPen() != DoodlePen.TEXT) {
                        return;
                    }
                    setInputTextDialog(doodle, x, y);
                    textField = (TextField) mComponentlist.get(0);
//                    commonDialog = (CommonDialog) mComponentlist.get(1);
                    dirbottom = (DirectionalLayout) mComponentlist.get(mBottomBtnIndex);
                    task = new Runnable() {
                        @Override
                        public void run() {
                            // 待执行的操作，由开发者定义
                            textField.requestFocus();
                            textField.simulateClick();
                            dirbottom.setMarginBottom(AttrHelper.vp2px(mBottomMargin, getContext()));
                        }
                    };
                    KeyboardUtils.addKeyboardToggleListener(mStackLayout, new KeyboardUtils.SoftKeyboardToggleListener() {
                        @Override
                        public void onToggleSoftKeyboard(boolean isVisible) {
                            if (isVisible) {
                                mIsShow = true;
                            } else {
                                mIsShow = false;
                            }
                            if (mIsShow) {
                                dirbottom.setMarginBottom(AttrHelper.vp2px(mBottomMargin, getContext()));
                            } else {
                                dirbottom.setMarginBottom(AttrHelper.vp2px(0, getContext()));
                            }
                        }
                    });
                    mEventHandler = new EventHandler(EventRunner.getMainEventRunner());
                    mEventHandler.postTask(task, mDelayTime, EventHandler.Priority.IMMEDIATE); // 延时250ms后立即处理
                }
            });
        DoodleTouchDetectorScaleText touchDetector = new DoodleTouchDetectorScaleText(this, touchGestureListener);
        doodleView.setDefaultTouchDetector(touchDetector);

        // step 3
        doodleView.setPen(DoodlePen.TEXT);
        doodleView.setShape(DoodleShape.HAND_WRITE);
        doodleView.setColor(new DoodleColor(Color.RED.getValue()));

        // step 4
        ComponentContainer container = (ComponentContainer) findComponentById(ResourceTable.Id_doodle_container);
        container.addComponent(doodleView);
    }

    private void setInputTextDialog(final IDoodle doodle, final float x, final float y) {
        mComponentlist = DialogController.showInputTextDialog(ScaleGestureItemDemoAbility.this,
            null, new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    String text = (component.getTag() + "").trim();
                    if (TextTool.isNullOrEmpty(text)) {
                        return;
                    }
                    IDoodleSelectableItem item = new DoodleText(doodle, text,
                        doodle.getSize(), doodle.getColor().copy(), x, y);
                    doodle.addItem(item);
                    touchGestureListener.setSelectedItem(item);
                    doodle.refresh();
                }
            }, new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                }
            });
    }

    /**
     * Change selected item's size by scale gesture.
     * Note that this class extends DoodleOnTouchGestureListener and does not affect the original gesture logic.
     *
     * @since 2021-04-29
     * 注意，这里继承了DoodleOnTouchGestureListener，不影响原有的手势逻辑
     */
    private static class ScaleItemOnTouchGestureListener extends DoodleOnTouchGestureListenerScaleText {
        ScaleItemOnTouchGestureListener(DoodleView doodle, ISelectionListener listener) {
            super(doodle, listener);
        }

        @Override
        public boolean onScale(ScaleGestureDetectorApi27 detector) {
            if (getSelectedItem() != null) {
                IDoodleItem item = getSelectedItem();
                item.setSize(item.getSize() * detector.getScaleFactor());
                return true;
            } else {
                return super.onScale(detector);
            }
        }
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
            LogUtil.i(mTAG, "IOException");
        } catch (NotExistException e) {
            LogUtil.i(mTAG, "NotExistException");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LogUtil.i(mTAG, "inputStream IOException");
                }
            }
        }
        return pixelMap;
    }

    @Override
    public void onBackground() {
        if (dirbottom != null) {
            dirbottom.setMarginBottom(AttrHelper.vp2px(0, getContext()));
        }
        super.onBackground();
    }
}
