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

package com.example.doodle;

import static ohos.agp.window.service.WindowManager.LayoutConfig.MARK_FULL_SCREEN;

import com.example.doodle.core.ColorChangedListener;
import com.example.doodle.core.IDoodle;
import com.example.doodle.core.IDoodleColor;
import com.example.doodle.core.IDoodleItem;
import com.example.doodle.core.IDoodleItemListener;
import com.example.doodle.core.IDoodlePen;
import com.example.doodle.core.IDoodleSelectableItem;
import com.example.doodle.core.IDoodleShape;
import com.example.doodle.core.IDoodleTouchDetector;
import com.example.doodle.core.ISetingshoworhide;
import com.example.doodle.dialog.ColorPickerView;
import com.example.doodle.dialog.DialogController;
import com.example.doodle.imagepicker.ImageSelectorView;
import com.example.doodle.util.DrawUtil;
import com.example.doodle.util.ImageUtils;
import com.example.doodle.util.KeyboardUtils;
import com.example.doodle.util.LogUtil;
import com.example.doodle.util.StatusBarUtil;
import com.example.doodle.util.Toast;
import com.example.doodle.util.Util;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorProperty;
import ohos.agp.animation.AnimatorScatter;
import ohos.agp.animation.AnimatorValue;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.AttrHelper;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Image;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Slider;
import ohos.agp.components.StackLayout;
import ohos.agp.components.Text;
import ohos.agp.components.TextField;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.PixelMapElement;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.TextTool;
import ohos.agp.window.dialog.CommonDialog;
import ohos.app.Context;
import ohos.bundle.AbilityInfo;
import ohos.data.rdb.ValuesBucket;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.media.image.ImagePacker;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.Size;
import ohos.media.photokit.metadata.AVStorage;
import ohos.multimodalinput.event.KeyEvent;
import ohos.multimodalinput.event.TouchEvent;
import ohos.utils.PacMap;
import ohos.utils.net.Uri;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 涂鸦界面，根据DoodleView的接口，提供页面交互
 * （这边代码和ui比较粗糙，主要目的是告诉大家DoodleView的接口具体能实现什么功能，实际需求中的ui和交互需另提别论）
 *
 * @since 2021-04-29
 */
public class DoodleAbility extends Ability implements Component.ClickedListener,
    KeyboardUtils.SoftKeyboardToggleListener, ISetingshoworhide {
    /**
     * 日志TAG
     */
    public static final String TAG = "Doodle";
    /**
     * 默认马赛克大小
     */
    public static final int DEFAULT_MOSAIC_SIZE = 20;
    /**
     * 默认仿制大小
     */
    public static final int DEFAULT_COPY_SIZE = 20;
    /**
     * 默认文字大小
     */
    public static final int DEFAULT_TEXT_SIZE = 18;
    /**
     * 默认贴图大小
     */
    public static final int DEFAULT_BITMAP_SIZE = 80;
    /**
     * 出现错误
     */
    public static final int RESULT_ERROR = -111;
    /**
     * KEY_PARAMS
     */
    public static final String KEY_PARAMS = "key_doodle_params";
    /**
     * KEY_IMAGE_PATH
     */
    public static final String KEY_IMAGE_PATH = "key_image_path";
    /**
     * 图片保存路径
     */
    public static final String DCIM_PATH = "/storage/emulated/0/DCIM/";
    /**
     * 默认贴图大小
     */
    public static final int DELAY_MSG_TIME = 500;
    /**
     * Start of user-defined activity results.
     */
    public static final int RESULT_OK = -1;

    private String mSavePath;

    private StackLayout mStackLayout;
    private IDoodle mDoodle;
    private DoodleView mDoodleView;

//    private Text mPaintSizeView;

    private Image mBtnHidePanel;
    private Component mSettingsPanel;
    private Component mSelectedEditContainer;
    private Text mItemScaleTextView;
    private Component mBtnColor;
    private Component mColorContainer;
    private Slider mEditSizeSeekBar;
    private Component mShapeContainer;
    private Component mPenContainer;
    private Component mSizeContainer;
    private Component mBtnUndo;
    private Component mMosaicMenu;
    private Component mEditBtn;
    private Component mRedoBtn;
    private List<Object> componentlist;

    private Animator mViewShowAnimation;
    private Animator mViewHideAnimation; // view隐藏和显示时用到的渐变动画
    private AnimatorScatter mViewShowScatter;
    private AnimatorScatter mViewHideScatter;

    private DoodleParams mDoodleParams;

    // 触摸屏幕超过一定时间才判断为需要隐藏设置面板
//    private Runnable mHideDelayRunnable;

    // 触摸屏幕超过一定时间才判断为需要显示设置面板
//    private Runnable mShowDelayRunnable;

    private DoodleOnTouchGestureListener mTouchGestureListener;

    // 保存每个画笔对应的最新大小
    private Map<IDoodlePen, Float> mPenSizeMap = new HashMap<>();

    private final static int mDefMosaicVar = -1;
    private int mMosaicLevel = mDefMosaicVar;
    private Button mBtnMosaicL1;
    private Button mBtnMosaicL2;
    private Button mBtnMosaicL3;

    private DataAbilityHelper mHelper;
    private CommonDialog mSelectImageDialog;
    private EventHandler mEventHandler;
    private Runnable task;
    private TextField textField;
    private DirectionalLayout dirbottom;
    private boolean isHow = false;
    private boolean isHowcoro = false;

    private TextField sizeView;

    /**
     * 启动涂鸦界面
     *
     * @param ability
     * @param params 涂鸦参数
     * @param requestCode startActivityForResult的请求码
     * @see DoodleParams
     */
    public static void startAbilityForResult(Ability ability, DoodleParams params, int requestCode) {
        Intent intent = new Intent();
        intent.setParam(DoodleAbility.KEY_PARAMS, params);
        Operation operation = new Intent.OperationBuilder()
            .withDeviceId("")
            .withBundleName("com.example.doodle")
            .withAbilityName("com.example.doodlelib.DoodleAbility")
            .build();
        intent.setOperation(operation);
        ability.startAbilityForResult(intent, requestCode);
    }

    /**
     * 启动涂鸦界面
     *
     * @param activity
     * @param imagePath 　图片路径
     * @param savePath 　保存路径
     * @param isDir 　保存路径是否为目录
     * @param requestCode 　startActivityForResult的请求码
     */
    @Deprecated
    public static void startAbilityForResult(Ability activity, String imagePath,
                                             String savePath, boolean isDir, int requestCode) {
        DoodleParams params = new DoodleParams();
        params.mImagePath = imagePath;
        params.mSavePath = savePath;
        params.mSavePathIsDir = isDir;
        startAbilityForResult(activity, params, requestCode);
    }

    @Override
    public void onSaveAbilityState(PacMap outState) {
        super.onSaveAbilityState(outState);
        // outState.putPacMap(KEY_PARAMS, mDoodleParams);
    }

    @Override
    public void onRestoreAbilityState(PacMap inState) {
        super.onRestoreAbilityState(inState);
        // Optional<PacMap> pacMap = inState.getPacMap(KEY_PARAMS);
        // mDoodleParams=pacMap.;
    }

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        StatusBarUtil.setStatusBarTranslucent(getWindow(), true, false);
        componentlist = new ArrayList<>();

        // 涂鸦参数
        DoodleParams params = new DoodleParams();
        params.mIsFullScreen = true;

        // 图片路径
        // 初始画笔大小
        params.mPaintUnitSize = DoodleView.DEFAULT_SIZE;

        // 画笔颜色
        params.mPaintColor = Color.RED.getValue();

        // 是否支持缩放item
        params.mSupportScaleItem = true;
        mDoodleParams = params;
      /*  if (mDoodleParams == null) {
            mDoodleParams = getIntent().getSerializableParam(KEY_PARAMS);
            LogUtil.e(TAG, "mDoodleParams is null!");
            this.terminateAbility();
            return;
        }*/
        if (mDoodleParams.mIsFullScreen) {
            getWindow().addWindowFlags(MARK_FULL_SCREEN);
        }

        mHelper = DataAbilityHelper.creator(getContext());
        Uri uri = intent.getUri();
        if (uri == null) {
            LogUtil.e(TAG, "mImage uri is null!");
            this.terminateAbility();
            return;
        }
        FileDescriptor filedesc = null;
        try {
            filedesc = mHelper.openFile(uri, "r");
        } catch (DataAbilityRemoteException | FileNotFoundException e) {
            e.getMessage();
        }
        final int count = 3;
        ImageSource.DecodingOptions decodingOpts = new ImageSource.DecodingOptions();
        decodingOpts.desiredSize = new Size(Util.getScreenWidth(this) / count, Util.getScreenHeight(this) / count);
        ImageSource imageSource = ImageSource.create(filedesc, null);
        PixelMap pixelMap = imageSource.createThumbnailPixelmap(decodingOpts, true);

        if (pixelMap == null) {
            LogUtil.e(TAG, "bitmap is null!");
            this.terminateAbility();
            return;
        }

        // 隐藏标题栏在xml设置
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setUIContent(ResourceTable.Layout_doodle_layout);
        mStackLayout = (StackLayout) findComponentById(ResourceTable.Id_doodle_container);

        /*
        Whether or not to optimize drawing, it is suggested to open,
        which can optimize the drawing speed and performance.
        Note: When item is selected for editing after opening, it will be drawn at the top level,
         and not at the corresponding level until editing is completed.
        是否优化绘制，建议开启，可优化绘制速度和性能.
        注意：开启后item被选中编辑时时会绘制在最上面一层，直到结束编辑后才绘制在相应层级
         */
        mDoodle = mDoodleView = new DoodleViewWrapper(this, pixelMap, mDoodleParams.mOptimizeDrawing, new IDoodleListener() {
            @Override
            public void onSaved(IDoodle doodle, PixelMap pixelMap, Runnable callback) { // 保存图片为jpg格式
                Intent intent = new Intent();
                String picName = String.valueOf(System.currentTimeMillis());
                intent.setUriAndType(saveImage(picName, pixelMap), null);
                mSavePath = DCIM_PATH + picName + ".jpg";
                intent.setParam(KEY_IMAGE_PATH, mSavePath);
                setResult(RESULT_OK, intent);
                terminateAbility();
            }

            public void onError(int i, String msg) {
                setResult(RESULT_ERROR, null);
                terminateAbility();
            }

            @Override
            public void onReady(IDoodle doodle, int width, int height) {
                mEditSizeSeekBar.setMaxValue(Math.min(width, height));
                float size = mDoodleParams.mPaintUnitSize > 0 ? mDoodleParams.mPaintUnitSize
                    * mDoodle.getUnitSize() : 0;
                if (size <= 0) {
                    size = mDoodleParams.mPaintPixelSize > 0 ? mDoodleParams.mPaintPixelSize : mDoodle.getSize();
                }

                // 设置初始值
                mDoodle.setSize(size);
                // 选择画笔
                mDoodle.setPen(DoodlePen.BRUSH);
                mDoodle.setShape(DoodleShape.HAND_WRITE);
                mDoodle.setColor(new DoodleColor(mDoodleParams.mPaintColor));
                if (mDoodleParams.mZoomerScale <= 0) {
                    findComponentById(ResourceTable.Id_btn_zoomer).setVisibility(Component.HIDE);
                }
                mDoodle.setZoomerScale(mDoodleParams.mZoomerScale);
                mTouchGestureListener.setSupportScaleItem(mDoodleParams.mSupportScaleItem);

                // 每个画笔的初始值
                mPenSizeMap.put(DoodlePen.BRUSH, mDoodle.getSize());
                mPenSizeMap.put(DoodlePen.MOSAIC, DEFAULT_MOSAIC_SIZE * mDoodle.getUnitSize());
                mPenSizeMap.put(DoodlePen.COPY, DEFAULT_COPY_SIZE * mDoodle.getUnitSize());
                mPenSizeMap.put(DoodlePen.ERASER, mDoodle.getSize());
                mPenSizeMap.put(DoodlePen.TEXT, DEFAULT_TEXT_SIZE * mDoodle.getUnitSize());
                mPenSizeMap.put(DoodlePen.BITMAP, DEFAULT_BITMAP_SIZE * mDoodle.getUnitSize());
            }
        });
        mDoodleView.setIsetshoworhide(this);
        mTouchGestureListener = new DoodleOnTouchGestureListener(mDoodleView,
            new DoodleOnTouchGestureListener.ISelectionListener() {
                // save states before being selected
                IDoodlePen mLastPen = null;
                IDoodleColor mLastColor = null;
                Float mSize = null;

                IDoodleItemListener mIDoodleItemListener = new IDoodleItemListener() {
                    @Override
                    public void onPropertyChanged(int property) {
                        if (mTouchGestureListener.getSelectedItem() == null) {
                            return;
                        }
                        if (property == IDoodleItemListener.PROPERTY_SCALE) {
                            mItemScaleTextView.setText(
                                (int) (mTouchGestureListener.getSelectedItem().getScale() * 100 + 0.5f) + "%");
                        }
                    }
                };

                @Override
                public void onSelectedItem(IDoodle doodle, IDoodleSelectableItem selectableItem, boolean selected) {
                    if (selected) {
                        if (mLastPen == null) {
                            mLastPen = mDoodle.getPen();
                        }
                        if (mLastColor == null) {
                            mLastColor = mDoodle.getColor();
                        }
                        if (mSize == null) {
                            mSize = mDoodle.getSize();
                        }
                        mDoodleView.setEditMode(true);
                        mDoodle.setPen(selectableItem.getPen());
                        mDoodle.setColor(selectableItem.getColor());
                        mDoodle.setSize(selectableItem.getSize());
                        mEditSizeSeekBar.setProgressValue((int) selectableItem.getSize());
                        mSelectedEditContainer.setVisibility(Component.VISIBLE);
                        mSizeContainer.setVisibility(Component.VISIBLE);
                        mItemScaleTextView.setText((int) (selectableItem.getScale() * 100 + 0.5f) + "%");
                        selectableItem.addItemListener(mIDoodleItemListener);
                    } else {
                        selectableItem.removeItemListener(mIDoodleItemListener);

                        if (mTouchGestureListener.getSelectedItem() == null) { // nothing is selected. 当前没有选中任何一个item
                            if (mLastPen != null) {
                                mDoodle.setPen(mLastPen);
                                mLastPen = null;
                            }
                            if (mLastColor != null) {
                                mDoodle.setColor(mLastColor);
                                mLastColor = null;
                            }
                            if (mSize != null) {
                                mDoodle.setSize(mSize);
                                mSize = null;
                            }
                            mSelectedEditContainer.setVisibility(Component.HIDE);
                        }
                    }
                }

                @Override
                public void onCreateSelectableItem(IDoodle doodle, float x, float y) {
                    if (mDoodle.getPen() == DoodlePen.TEXT) {
                        createDoodleText(null, x, y);
                    } else if (mDoodle.getPen() == DoodlePen.BITMAP) {
                        if (mSelectImageDialog == null) {
                            createDoodleBitmap(null, x, y);
                        } else {
                            if (!mSelectImageDialog.isShowing()) {
                                createDoodleBitmap(null, x, y);
                            }
                        }
                    }
                }
            }) {
            @Override
            public void setSupportScaleItem(boolean supportScaleItem) {
                super.setSupportScaleItem(supportScaleItem);
                if (supportScaleItem) {
                    mItemScaleTextView.setVisibility(Component.VISIBLE);
                } else {
                    mItemScaleTextView.setVisibility(Component.HIDE);
                }
            }
        };

        IDoodleTouchDetector detector = new DoodleTouchDetector(getApplicationContext(), mTouchGestureListener);
        mDoodleView.setDefaultTouchDetector(detector);

        mDoodle.setIsDrawableOutside(mDoodleParams.mIsDrawableOutside);
        mStackLayout.addComponent(mDoodleView, ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_PARENT);
        mDoodle.setDoodleMinScale(mDoodleParams.mMinScale);
        mDoodle.setDoodleMaxScale(mDoodleParams.mMaxScale);
        initView();
    }

    private boolean canChangeColor(IDoodlePen pen) {
        return pen != DoodlePen.ERASER
            && pen != DoodlePen.BITMAP
            && pen != DoodlePen.COPY
            && pen != DoodlePen.MOSAIC;
    }

    // 添加文字
    private void createDoodleText(final DoodleText doodleText, final float x, final float y) {
        if (isTerminating()) {
            return;
        }

        componentlist = DialogController.showInputTextDialog(this, doodleText == null ? null : doodleText.getText(), new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                String text = (component.getTag() + "").trim();
                if (TextTool.isNullOrEmpty(text)) {
                    return;
                }
                if (doodleText == null) {
                    IDoodleSelectableItem item = new DoodleText(mDoodle, text, mDoodle.getSize(), mDoodle.getColor().copy(), x, y);
                    mDoodle.addItem(item);
                    mTouchGestureListener.setSelectedItem(item);
                } else {
                    doodleText.setText(text);
                }
                mDoodle.refresh();
            }
        }, new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {

            }
        });
        textField = (TextField) componentlist.get(0);
        componentlist.get(1);
        final int dirbottomIndex = 2;
        dirbottom = (DirectionalLayout) componentlist.get(dirbottomIndex);
        KeyboardUtils.addKeyboardToggleListener(mBtnColor, new KeyboardUtils.SoftKeyboardToggleListener() {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible) {
                final int bottom = 280;
                if (isVisible) {
                    isHow = true;
                } else {
                    isHow = false;
                }
                if (isHow) {
                    dirbottom.setMarginBottom(AttrHelper.vp2px(bottom, getContext()));
                } else {
                    dirbottom.setMarginBottom(AttrHelper.vp2px(0, getContext()));
                }
            }
        });
        task = new Runnable() {
            @Override
            public void run() {
                // 待执行的操作，由开发者定义
                textField.requestFocus();
                textField.simulateClick();
            }
        };
        mEventHandler = new EventHandler(EventRunner.getMainEventRunner());
        mEventHandler.postTask(task, DELAY_MSG_TIME, EventHandler.Priority.IMMEDIATE); // 延时250ms后立即处理
    }

    // 添加贴图
    private void createDoodleBitmap(final DoodlePixelMap doodlePixelMap, final float x, final float y) {
        mSelectImageDialog = DialogController.showSelectImageDialog(this, new ImageSelectorView.ImageSelectorListener() {
            @Override
            public void onCancel() {
                mSelectImageDialog.destroy();
            }

            @Override
            public void onEnter(Uri uri) {
                FileDescriptor filedesc = null;
                final int count = 3;
                try {
                    filedesc = mHelper.openFile(uri, "r");
                } catch (DataAbilityRemoteException | FileNotFoundException e) {
                    e.getMessage();
                }
                ImageSource.DecodingOptions decodingOpts = new ImageSource.DecodingOptions();
                decodingOpts.desiredSize = new Size(Util.getScreenWidth(getContext()) / count,
                    Util.getScreenHeight(getContext()) / count);
                ImageSource imageSource = ImageSource.create(filedesc, null);
                PixelMap pixelMap = imageSource.createThumbnailPixelmap(decodingOpts, true);
                if (doodlePixelMap == null) {
                    IDoodleSelectableItem item = new DoodlePixelMap(mDoodle, pixelMap, mDoodle.getSize(), x, y);
                    mDoodle.addItem(item);
                    mTouchGestureListener.setSelectedItem(item);

                } else {
                    doodlePixelMap.setPixelMap(pixelMap);
                }
                mDoodle.refresh();
            }
        });
    }

    //++++++++++++++++++以下为一些初始化操作和点击监听+++++++++++++++++++++++++++++++++++++++++
    private void initView() {
        findComponentById(ResourceTable.Id_btn_pen_hand).setClickedListener(this);
        findComponentById(ResourceTable.Id_btn_pen_mosaic).setClickedListener(this);
        findComponentById(ResourceTable.Id_btn_pen_copy).setClickedListener(this);
        findComponentById(ResourceTable.Id_btn_pen_eraser).setClickedListener(this);
        findComponentById(ResourceTable.Id_btn_pen_text).setClickedListener(this);
        findComponentById(ResourceTable.Id_btn_pen_bitmap).setClickedListener(this);
        findComponentById(ResourceTable.Id_doodle_btn_brush_edit).setClickedListener(this);
        findComponentById(ResourceTable.Id_btn_undo).setClickedListener(this);
        findComponentById(ResourceTable.Id_btn_zoomer).setClickedListener(this);
        findComponentById(ResourceTable.Id_btn_set_color_container).setClickedListener(this);
        findComponentById(ResourceTable.Id_doodle_btn_hide_panel).setClickedListener(this);
        findComponentById(ResourceTable.Id_doodle_btn_finish).setClickedListener(this);
        findComponentById(ResourceTable.Id_doodle_btn_back).setClickedListener(this);
        findComponentById(ResourceTable.Id_doodle_btn_rotate).setClickedListener(this);
        findComponentById(ResourceTable.Id_doodle_selectable_edit).setClickedListener(this);
        findComponentById(ResourceTable.Id_doodle_selectable_remove).setClickedListener(this);
        findComponentById(ResourceTable.Id_doodle_selectable_top).setClickedListener(this);
        findComponentById(ResourceTable.Id_doodle_selectable_bottom).setClickedListener(this);
        findComponentById(ResourceTable.Id_btn_hand_write).setClickedListener(this);
        findComponentById(ResourceTable.Id_btn_arrow).setClickedListener(this);
        findComponentById(ResourceTable.Id_btn_line).setClickedListener(this);
        findComponentById(ResourceTable.Id_btn_holl_circle).setClickedListener(this);
        findComponentById(ResourceTable.Id_btn_fill_circle).setClickedListener(this);
        findComponentById(ResourceTable.Id_btn_holl_rect).setClickedListener(this);
        findComponentById(ResourceTable.Id_btn_fill_rect).setClickedListener(this);
        mBtnMosaicL1 = (Button) findComponentById(ResourceTable.Id_btn_mosaic_level1);
        mBtnMosaicL1.setClickedListener(this);
        mBtnMosaicL2 = (Button) findComponentById(ResourceTable.Id_btn_mosaic_level2);
        mBtnMosaicL2.setClickedListener(this);
        mBtnMosaicL3 = (Button) findComponentById(ResourceTable.Id_btn_mosaic_level3);
        mBtnMosaicL3.setClickedListener(this);
        findComponentById(ResourceTable.Id_btn_redo).setClickedListener(this);

        mBtnUndo = findComponentById(ResourceTable.Id_btn_undo);
        mBtnUndo.setLongClickedListener(new Component.LongClickedListener() {
            @Override
            public void onLongClicked(Component component) {
                if (!(DoodleParams.getDialogInterceptor() != null
                    && DoodleParams.getDialogInterceptor().onShow(DoodleAbility.this, mDoodle,
                    DoodleParams.DialogType.CLEAR_ALL))) {
                    DialogController.showEnterCancelDialog(DoodleAbility.this,
                        getString(ResourceTable.String_doodle_clear_screen),
                        getString(ResourceTable.String_doodle_cant_undo_after_clearing),
                        component1 -> mDoodle.clear(), null);
                }
            }
        });
        mSelectedEditContainer = findComponentById(ResourceTable.Id_doodle_selectable_edit_container);
        mSelectedEditContainer.setVisibility(Component.HIDE);
        mItemScaleTextView = (Text) findComponentById(ResourceTable.Id_item_scale);
        mItemScaleTextView.setLongClickedListener(component -> {
            if (mTouchGestureListener.getSelectedItem() != null) {
                mTouchGestureListener.getSelectedItem().setScale(1);
            }
        });

        mSettingsPanel = findComponentById(ResourceTable.Id_doodle_panel);

        mBtnHidePanel = (Image) findComponentById(ResourceTable.Id_doodle_btn_hide_panel);

//        mPaintSizeView = (Text) findComponentById(ResourceTable.Id_paint_size_text);
        mShapeContainer = findComponentById(ResourceTable.Id_shape_container);
        mPenContainer = findComponentById(ResourceTable.Id_pen_container);
        mSizeContainer = findComponentById(ResourceTable.Id_size_container);
        mMosaicMenu = findComponentById(ResourceTable.Id_mosaic_menu);
        mEditBtn = findComponentById(ResourceTable.Id_doodle_selectable_edit);
        mRedoBtn = findComponentById(ResourceTable.Id_btn_redo);

        mBtnColor = findComponentById(ResourceTable.Id_btn_set_color);
        mColorContainer = findComponentById(ResourceTable.Id_btn_set_color_container);
        mEditSizeSeekBar = (Slider) findComponentById(ResourceTable.Id_doodle_seekbar_size);
        mEditSizeSeekBar.setValueChangedListener(new Slider.ValueChangedListener() {
            @Override
            public void onProgressUpdated(Slider slider, int progress, boolean isVar) {
                LogUtil.d(TAG, "onProgressUpdated=" + progress);
                if (progress == 0) {
                    mEditSizeSeekBar.setProgressValue(1);
                    return;
                }
                if ((int) mDoodle.getSize() == progress) {
                    return;
                }
                mDoodle.setSize(progress);
                if (mTouchGestureListener.getSelectedItem() != null) {
                    mTouchGestureListener.getSelectedItem().setSize(progress);
                }
            }

            @Override
            public void onTouchStart(Slider slider) {
            }

            @Override
            public void onTouchEnd(Slider slider) {
            }
        });
        mDoodleView.setExtTouchEventListener(new Component.TouchEventListener() {
            @Override
            public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
                // 隐藏设置面板
                if (!mBtnHidePanel.isSelected()
                    && mDoodleParams.mChangePanelVisibilityDelay > 0) {
                    switch (touchEvent.getAction()) {
                        case TouchEvent.PRIMARY_POINT_DOWN:
//                        mSettingsPanel.removeCallbacks(mHideDelayRunnable);
//                        mSettingsPanel.removeCallbacks(mShowDelayRunnable);
                            //触摸屏幕超过一定时间才判断为需要隐藏设置面板
//                        mSettingsPanel.postDelayed(mHideDelayRunnable, mDoodleParams.mChangePanelVisibilityDelay);
//                            postTask(mHideDelayRunnable, mDoodleParams.mChangePanelVisibilityDelay);
                            break;
                        case TouchEvent.CANCEL:
                        case TouchEvent.PRIMARY_POINT_UP:
//                        mSettingsPanel.removeCallbacks(mHideDelayRunnable);
//                        mSettingsPanel.removeCallbacks(mShowDelayRunnable);
                            //离开屏幕超过一定时间才判断为需要显示设置面板
//                        mSettingsPanel.postDelayed(mShowDelayRunnable, mDoodleParams.mChangePanelVisibilityDelay);
//                            postTask(mShowDelayRunnable, mDoodleParams.mChangePanelVisibilityDelay);
                            break;
                    }
                }
                return false;
            }
        });

//         长按标题栏显示原图 Component的事件
        findComponentById(ResourceTable.Id_doodle_txt_title).setTouchEventListener(new Component.TouchEventListener() {
            @Override
            public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
                switch (touchEvent.getAction() & 0xff) {
                    case TouchEvent.PRIMARY_POINT_DOWN:
                        component.setPressState(true);
                        mDoodle.setShowOriginal(true);
                        break;
                    case TouchEvent.PRIMARY_POINT_UP:
                    case TouchEvent.CANCEL:
                        component.setPressState(false);
                        mDoodle.setShowOriginal(false);
                        break;
                }
                return true;
            }
        });

        mViewShowScatter = AnimatorScatter.getInstance(getContext());
        mViewShowAnimation = mViewShowScatter.parse(ResourceTable.Animation_component_show_animator);

        mViewHideScatter = AnimatorScatter.getInstance(getContext());
        mViewHideAnimation = mViewHideScatter.parse(ResourceTable.Animation_component_show_animator);


//        mHideDelayRunnable = new Runnable() {
//            public void run() {
//                hideView(mSettingsPanel);
//            }
//
//        };
//        mShowDelayRunnable = new Runnable() {
//            public void run() {
//                showView(mSettingsPanel);
//            }
//        };

        // 处理菜单按钮事件穿透的问题
        // 横竖屏bug暂时改false
//        findComponentById(ResourceTable.Id_st_menu).setTouchEventListener(new Component.TouchEventListener() {
//            @Override
//            public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
//                return true;
//            }
//        });
        findComponentById(ResourceTable.Id_st_menu_left).setTouchEventListener(new Component.TouchEventListener() {
            @Override
            public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
                return true;
            }
        });
        findComponentById(ResourceTable.Id_st_menu_right).setTouchEventListener(new Component.TouchEventListener() {
            @Override
            public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
                return true;
            }
        });
        mMosaicMenu.setTouchEventListener(new Component.TouchEventListener() {
            @Override
            public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
                return true;
            }
        });
        mSelectedEditContainer.setTouchEventListener(new Component.TouchEventListener() {
            @Override
            public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
                return true;
            }
        });
        mBtnMosaicL1.setTouchEventListener(new Component.TouchEventListener() {
            @Override
            public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
                return true;
            }
        });
        mBtnMosaicL2.setTouchEventListener(new Component.TouchEventListener() {
            @Override
            public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
                return true;
            }
        });
        mBtnMosaicL3.setTouchEventListener(new Component.TouchEventListener() {
            @Override
            public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
                return true;
            }
        });
        mSelectedEditContainer.setTouchEventListener(new Component.TouchEventListener() {
            @Override
            public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
                return true;
            }
        });
    }

    private ValueAnimator mRotateAnimator;
    private ColorChangedListener mColorChangedListener = new ColorChangedListener() {
        @Override
        public void colorChanged(int color, int size) {
            mDoodle.setColor(new DoodleColor(color));
            mDoodle.setSize(size);
        }

        @Override
        public void colorChanged(Element color, int size) {
            PixelMap pixelMap = ImageUtils.getBitmapFromDrawable(color);
            mDoodle.setColor(new DoodleColor(pixelMap));
            mDoodle.setSize(size);
        }
    };

    @Override
    public void onClick(Component component) {
        if (component.getId() == ResourceTable.Id_btn_pen_hand) {
            mDoodle.setPen(DoodlePen.BRUSH);
        } else if (component.getId() == ResourceTable.Id_btn_pen_mosaic) {
            mDoodle.setPen(DoodlePen.MOSAIC);
        } else if (component.getId() == ResourceTable.Id_btn_pen_copy) {
            mDoodle.setPen(DoodlePen.COPY);
        } else if (component.getId() == ResourceTable.Id_btn_pen_eraser) {
            mDoodle.setPen(DoodlePen.ERASER);
        } else if (component.getId() == ResourceTable.Id_btn_pen_text) {
            mDoodle.setPen(DoodlePen.TEXT);
        } else if (component.getId() == ResourceTable.Id_btn_pen_bitmap) {
            mDoodle.setPen(DoodlePen.BITMAP);
        } else if (component.getId() == ResourceTable.Id_doodle_btn_brush_edit) {
            mDoodleView.setEditMode(!mDoodleView.isEditMode());
        } else if (component.getId() == ResourceTable.Id_btn_undo) {
            mDoodle.undo();
        } else if (component.getId() == ResourceTable.Id_btn_zoomer) {
            mDoodleView.enableZoomer(!mDoodleView.isEnableZoomer());
        } else if (component.getId() == ResourceTable.Id_btn_set_color_container) {
            DoodleColor color = null;
            if (mDoodle.getColor() instanceof DoodleColor) {
                color = (DoodleColor) mDoodle.getColor();
            }
            if (color == null) {
                return;
            }
            if (!(DoodleParams.getDialogInterceptor() != null
                && DoodleParams.getDialogInterceptor().onShow(DoodleAbility.this, mDoodle, DoodleParams.DialogType.COLOR_PICKER))) {
                boolean fullScreen = (getWindow().getLayoutConfig().get().flags & MARK_FULL_SCREEN) != 0; // 待验证
                initColorPickerDialog(mDoodleView, mColorChangedListener, mBtnColor.getBackgroundElement(), Math.min(mDoodleView.getWidth(), mDoodleView.getHeight()));
            }
        } else if (component.getId() == ResourceTable.Id_doodle_btn_hide_panel) {
//                mSettingsPanel.removeCallbacks(mHideDelayRunnable); 待处理
//                mSettingsPanel.removeCallbacks(mShowDelayRunnable); 待处理
            boolean selected = mBtnHidePanel.isSelected();
            mBtnHidePanel.setSelected(!selected);
            if (!mBtnHidePanel.isSelected()) {
                showView(mSettingsPanel);
                mBtnHidePanel.setPixelMap(ResourceTable.Media_doodle_hide_panel);
            } else {
                hideView(mSettingsPanel);
                mBtnHidePanel.setPixelMap(ResourceTable.Media_doodle_hide_panel_pressed);
            }
        } else if (component.getId() == ResourceTable.Id_doodle_btn_finish) {
            mDoodle.save();
        } else if (component.getId() == ResourceTable.Id_doodle_btn_back) {
            if (mDoodle.getAllItem() == null || mDoodle.getItemCount() == 0) {
                terminateAbility();
                return;
            }
            if (!(DoodleParams.getDialogInterceptor() != null
                && DoodleParams.getDialogInterceptor().onShow(DoodleAbility.this, mDoodle, DoodleParams.DialogType.SAVE))) {
                DialogController.showMsgDialog(DoodleAbility.this, getString(ResourceTable.String_doodle_saving_picture), null, getString(ResourceTable.String_doodle_cancel),
                    getString(ResourceTable.String_doodle_save), new Component.ClickedListener() {
                        @Override
                        public void onClick(Component component) {
                            mDoodle.save();
                        }
                    }, new Component.ClickedListener() {
                        @Override
                        public void onClick(Component component) {
                            terminateAbility();
                        }
                    });
            }
        } else if (component.getId() == ResourceTable.Id_doodle_btn_rotate) {
            // 旋转图片
            if (mRotateAnimator == null) {
                mRotateAnimator = new ValueAnimator();
                mRotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(AnimatorValue animatorValue, float fraction, Object animatedValue) {
                        float value = (float) animatedValue;
                        mDoodle.setDoodleRotation((int) value);
                    }
                });
                mRotateAnimator.setDuration(250);
            }
            if (mRotateAnimator.isRunning()) {
                return;
            }
            mRotateAnimator.setIntValues(mDoodle.getDoodleRotation(), mDoodle.getDoodleRotation() + 90);
            mRotateAnimator.start();
        } else if (component.getId() == ResourceTable.Id_doodle_selectable_edit) {
            if (mTouchGestureListener.getSelectedItem() instanceof DoodleText) {
                createDoodleText((DoodleText) mTouchGestureListener.getSelectedItem(), -1, -1);
            } else if (mTouchGestureListener.getSelectedItem() instanceof DoodlePixelMap) {
                createDoodleBitmap((DoodlePixelMap) mTouchGestureListener.getSelectedItem(), -1, -1);
            }
        } else if (component.getId() == ResourceTable.Id_doodle_selectable_remove) {
            mDoodle.removeItem(mTouchGestureListener.getSelectedItem());
            mTouchGestureListener.setSelectedItem(null);
        } else if (component.getId() == ResourceTable.Id_doodle_selectable_top) {
            mDoodle.topItem(mTouchGestureListener.getSelectedItem());
        } else if (component.getId() == ResourceTable.Id_doodle_selectable_bottom) {
            mDoodle.bottomItem(mTouchGestureListener.getSelectedItem());
        } else if (component.getId() == ResourceTable.Id_btn_hand_write) {
            mDoodle.setShape(DoodleShape.HAND_WRITE);
        } else if (component.getId() == ResourceTable.Id_btn_arrow) {
            mDoodle.setShape(DoodleShape.ARROW);
        } else if (component.getId() == ResourceTable.Id_btn_line) {
            mDoodle.setShape(DoodleShape.LINE);
        } else if (component.getId() == ResourceTable.Id_btn_holl_circle) {
            mDoodle.setShape(DoodleShape.HOLLOW_CIRCLE);
        } else if (component.getId() == ResourceTable.Id_btn_fill_circle) {
            mDoodle.setShape(DoodleShape.FILL_CIRCLE);
        } else if (component.getId() == ResourceTable.Id_btn_holl_rect) {
            mDoodle.setShape(DoodleShape.HOLLOW_RECT);
        } else if (component.getId() == ResourceTable.Id_btn_fill_rect) {
            mDoodle.setShape(DoodleShape.FILL_RECT);
        } else if (component.getId() == ResourceTable.Id_btn_mosaic_level1) {
            if (component.isSelected()) {
                return;
            }

            mMosaicLevel = DoodlePath.MOSAIC_LEVEL_1;
            mDoodle.setColor(DoodlePath.getMosaicColor(mDoodle, mMosaicLevel));
            component.setSelected(true);
            mMosaicMenu.findComponentById(ResourceTable.Id_btn_mosaic_level2).setSelected(false);
            mMosaicMenu.findComponentById(ResourceTable.Id_btn_mosaic_level3).setSelected(false);
            if (mTouchGestureListener.getSelectedItem() != null) {
                mTouchGestureListener.getSelectedItem().setColor(mDoodle.getColor().copy());
            }
        } else if (component.getId() == ResourceTable.Id_btn_mosaic_level2) {
            if (component.isSelected()) {
                return;
            }

            mMosaicLevel = DoodlePath.MOSAIC_LEVEL_2;
            mDoodle.setColor(DoodlePath.getMosaicColor(mDoodle, mMosaicLevel));
            component.setSelected(true);
            mMosaicMenu.findComponentById(ResourceTable.Id_btn_mosaic_level1).setSelected(false);
            mMosaicMenu.findComponentById(ResourceTable.Id_btn_mosaic_level3).setSelected(false);
            if (mTouchGestureListener.getSelectedItem() != null) {
                mTouchGestureListener.getSelectedItem().setColor(mDoodle.getColor().copy());
            }
        } else if (component.getId() == ResourceTable.Id_btn_mosaic_level3) {
            if (component.isSelected()) {
                return;
            }
            mMosaicLevel = DoodlePath.MOSAIC_LEVEL_3;
            mDoodle.setColor(DoodlePath.getMosaicColor(mDoodle, mMosaicLevel));
            component.setSelected(true);
            mMosaicMenu.findComponentById(ResourceTable.Id_btn_mosaic_level1).setSelected(false);
            mMosaicMenu.findComponentById(ResourceTable.Id_btn_mosaic_level2).setSelected(false);
            if (mTouchGestureListener.getSelectedItem() != null) {
                mTouchGestureListener.getSelectedItem().setColor(mDoodle.getColor().copy());
            }
        } else if (component.getId() == ResourceTable.Id_btn_redo) {
            if (!mDoodle.redo(1)) {
                mRedoBtn.setVisibility(Component.HIDE);
            }
        }
    }

    private void initColorPickerDialog(IDoodle iDoodle, ColorChangedListener colorChangedListener, Element element, int maxSize) {
        int height = Util.vp2px(this, 200);
        int width = Util.vp2px(this, 200);
        CommonDialog dialog = new CommonDialog(this);
        Component viewGroup = LayoutScatter.getInstance(this).parse(ResourceTable.Layout_doodle_color_selector_dialog, null, true);
        dialog.setTransparent(true);
        dialog.setContentCustomComponent(viewGroup);
        sizeView = (TextField) viewGroup.findComponentById(ResourceTable.Id_doodle_txtview_size);
        Slider seekBar = (Slider) viewGroup.findComponentById(ResourceTable.Id_doodle_color_seekbar_size);
        DirectionalLayout colorselector = (DirectionalLayout) viewGroup.findComponentById(ResourceTable.Id_doodle_color_selector_container);
        DirectionalLayout bottomcolor = (DirectionalLayout) viewGroup.findComponentById(ResourceTable.Id_dooble_bottom_color);


        KeyboardUtils.addKeyboardToggleListener(mBtnColor, new KeyboardUtils.SoftKeyboardToggleListener() {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible) {

                if (isVisible) {
                    isHowcoro = true;
                } else {
                    isHowcoro = false;
                }
                if (isHowcoro) {
                    colorselector.setMarginBottom(AttrHelper.vp2px(130, getContext()));
                    bottomcolor.setMarginBottom(AttrHelper.vp2px(280, getContext()));
                } else {
                    colorselector.setMarginBottom(AttrHelper.vp2px(0, getContext()));
                    bottomcolor.setMarginBottom(AttrHelper.vp2px(50, getContext()));
                }

            }
        });
        seekBar.setValueChangedListener(new Slider.ValueChangedListener() {
            @Override
            public void onProgressUpdated(Slider slider, int progress, boolean b) {
                if (progress == 0) {
                    slider.setProgressValue(1);
                    return;
                }
                if (sizeView != null) {
                    sizeView.setText("" + progress);
                }
//                sizeView.setSelection(sizeView.getText().toString().length()); 无对应API
            }

            @Override
            public void onTouchStart(Slider slider) {

            }

            @Override
            public void onTouchEnd(Slider slider) {

            }
        });
        seekBar.setMaxValue(maxSize);
        seekBar.setProgressValue((int) iDoodle.getSize());
        final ColorPickerView colorPickerView = new ColorPickerView(this, Color.BLACK.getValue(), height, width, null);
        if (element instanceof PixelMapElement) {
            colorPickerView.setelement((PixelMapElement) element);
        } else if (element instanceof ShapeElement) {
            colorPickerView.setColor(new Color(((ShapeElement) element).getRgbColors()[0].asArgbInt()));
        }
        ComponentContainer container = (ComponentContainer) viewGroup.findComponentById(ResourceTable.Id_doodle_color_selector_container);
        container.addComponent(colorPickerView, 0, new ComponentContainer.LayoutConfig(height, width));
        ComponentContainer shaderContainer = (ComponentContainer) viewGroup.findComponentById(ResourceTable.Id_doodle_shader_container);
        Component.ClickedListener listener = new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                Image image = (Image) component;
                colorPickerView.setelement(new PixelMapElement(image.getPixelMap()));
            }
        };
        for (int i = 0; i < shaderContainer.getChildCount(); i++) {
            shaderContainer.getComponentAt(i).setClickedListener(listener);
        }
        viewGroup.findComponentById(ResourceTable.Id_doodle_txtview_reduce).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                seekBar.setProgressValue(Math.max(1, seekBar.getProgress() - 1));
            }
        });

        viewGroup.findComponentById(ResourceTable.Id_doodle_txtview_add).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                seekBar.setProgressValue(Math.min(seekBar.getMax(), seekBar.getProgress() + 1));
            }
        });
        viewGroup.findComponentById(ResourceTable.Id_dialog_enter_btn_01).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                dialog.destroy();
            }
        });
        viewGroup.findComponentById(ResourceTable.Id_dialog_enter_btn_02).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if (colorPickerView.getelement() != null) {
                    colorChangedListener.colorChanged(colorPickerView.getelement(), seekBar.getProgress());
                } else {
                    colorChangedListener.colorChanged(colorPickerView.getColor(), seekBar.getProgress());
                }
                dialog.destroy();
            }
        });
        if (sizeView != null) {
            sizeView.addTextObserver(new Text.TextObserver() {
                @Override
                public void onTextUpdated(String text, int start, int before, int count) {
                    try {
                        int p = Integer.parseInt(text);
                        if (p <= 0) {
                            p = 1;
                        }
                        if (p == seekBar.getProgress()) {
                            return;
                        }
                        seekBar.setProgressValue(p);
                        sizeView.setText("" + seekBar.getProgress());
//                    sizeView.setSelection(sizeView.getText().toString().length());  缺失或未找到API
                    } catch (Exception e) {
                    }
                }
            });
        }
        setSwipeToDismiss(false);  // 向右滑动不可退出
        DrawUtil.assistActivity(getWindow()); // 具体逻辑待实现
        dialog.show();
    }

    /**
     * onKeyDown
     *
     * @param keyCode
     * @param event
     * @return 是否消费事件
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEY_BACK) {
            if (mDoodleView.isEditMode()) {
                mDoodleView.setEditMode(false);
                return true;
            }
        }

        return DoodleAbility.super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() { // 返回键监听
        findComponentById(ResourceTable.Id_doodle_btn_back).simulateClick();
    }

    private void showView(Component component) {
        if (component.getVisibility() == Component.VISIBLE) {
            return;
        }
        if (mViewShowAnimation instanceof AnimatorProperty) {
            AnimatorProperty animatorProperty = (AnimatorProperty) mViewShowAnimation;
            animatorProperty.setTarget(component);
            animatorProperty.alpha(1);
            animatorProperty.start();
        }
        component.setVisibility(Component.VISIBLE);
    }

    private void hideView(Component component) {
        if (component.getVisibility() != Component.VISIBLE) {
            return;
        }
        if (mViewHideAnimation instanceof AnimatorProperty) {
            AnimatorProperty animatorProperty = (AnimatorProperty) mViewHideAnimation;
            animatorProperty.setTarget(component);
            animatorProperty.alpha(0);
            animatorProperty.start();
        }
        component.setVisibility(Component.HIDE);
    }

    @Override
    public void onToggleSoftKeyboard(boolean isVisible) {
    }

    @Override
    public void show() {
        // 隐藏设置面板
        if (!mBtnHidePanel.isSelected()
            && mDoodleParams.mChangePanelVisibilityDelay > 0) {
            showView(mSettingsPanel);
        }
    }

    @Override
    public void hide() {
        // 隐藏设置面板
        if (!mBtnHidePanel.isSelected()
            && mDoodleParams.mChangePanelVisibilityDelay > 0) {
            hideView(mSettingsPanel);
        }
    }

    /**
     * 包裹DoodleView，监听相应的设置接口，以改变UI状态
     *
     * @since 2021-04-29
     */
    private class DoodleViewWrapper extends DoodleView {

        public DoodleViewWrapper(Context context, PixelMap bitmap, boolean optimizeDrawing, IDoodleListener listener) {
            super(context, bitmap, optimizeDrawing, listener);
        }

        private Map<IDoodlePen, Integer> mBtnPenIds = new HashMap<>();

        {
            mBtnPenIds.put(DoodlePen.BRUSH, ResourceTable.Id_btn_pen_hand);
            mBtnPenIds.put(DoodlePen.MOSAIC, ResourceTable.Id_btn_pen_mosaic);
            mBtnPenIds.put(DoodlePen.COPY, ResourceTable.Id_btn_pen_copy);
            mBtnPenIds.put(DoodlePen.ERASER, ResourceTable.Id_btn_pen_eraser);
            mBtnPenIds.put(DoodlePen.TEXT, ResourceTable.Id_btn_pen_text);
            mBtnPenIds.put(DoodlePen.BITMAP, ResourceTable.Id_btn_pen_bitmap);
        }

        @Override
        public void setPen(IDoodlePen pen) {
            IDoodlePen oldPen = getPen();
            super.setPen(pen);

            mMosaicMenu.setVisibility(Component.HIDE);
            mEditBtn.setVisibility(Component.HIDE); // edit btn
            if (pen == DoodlePen.BITMAP || pen == DoodlePen.TEXT) {
                mEditBtn.setVisibility(Component.VISIBLE); // edit btn
                mShapeContainer.setVisibility(Component.HIDE);
                if (pen == DoodlePen.BITMAP) {
                    mColorContainer.setVisibility(Component.HIDE);
                } else {
                    mColorContainer.setVisibility(VISIBLE);
                }
            } else if (pen == DoodlePen.MOSAIC) {
                mMosaicMenu.setVisibility(VISIBLE);
                mShapeContainer.setVisibility(VISIBLE);
                mColorContainer.setVisibility(Component.HIDE);
            } else {
                mShapeContainer.setVisibility(VISIBLE);
                if (pen == DoodlePen.COPY || pen == DoodlePen.ERASER) {
                    mColorContainer.setVisibility(Component.HIDE);
                } else {
                    mColorContainer.setVisibility(VISIBLE);
                }
            }
            setSingleSelected(mBtnPenIds.values(), mBtnPenIds.get(pen));

            if (mTouchGestureListener.getSelectedItem() == null) {
                mPenSizeMap.put(oldPen, getSize()); // save
                Float size = mPenSizeMap.get(pen); // restore
                if (size != null) {
                    mDoodle.setSize(size);
                }
                if (isEditMode()) {
                    mShapeContainer.setVisibility(Component.HIDE);
                    mColorContainer.setVisibility(Component.HIDE);
                    mMosaicMenu.setVisibility(Component.HIDE);
                }
            } else {
                mShapeContainer.setVisibility(Component.HIDE);
                return;
            }

            if (pen == DoodlePen.BRUSH) {
                Element colorBg = mBtnColor.getBackgroundElement();
                if (colorBg instanceof ShapeElement) {
                    RgbColor[] rgbColors = ((ShapeElement) colorBg).getRgbColors();
                    if (rgbColors.length > 0) {
                        mDoodle.setColor(new DoodleColor(rgbColors[0].asArgbInt()));
                    }
                } else {
                    mDoodle.setColor(new DoodleColor(((PixelMapElement) colorBg).getPixelMap()));
                }
            } else if (pen == DoodlePen.MOSAIC) {
                if (mMosaicLevel <= 0) {
                    mMosaicMenu.findComponentById(ResourceTable.Id_btn_mosaic_level2).simulateClick();
                } else {
                    mDoodle.setColor(DoodlePath.getMosaicColor(mDoodle, mMosaicLevel));
                }
            } else if (pen == DoodlePen.COPY) {

            } else if (pen == DoodlePen.ERASER) {

            } else if (pen == DoodlePen.TEXT) {
                Element colorBg = mBtnColor.getBackgroundElement();
                if (colorBg instanceof ShapeElement) {
                    RgbColor[] rgbColors = ((ShapeElement) colorBg).getRgbColors();
                    if (rgbColors.length > 0) {
                        mDoodle.setColor(new DoodleColor(rgbColors[0].asArgbInt()));
                    }
                } else {
                    mDoodle.setColor(new DoodleColor(((PixelMapElement) colorBg).getPixelMap()));
                }
            } else if (pen == DoodlePen.BITMAP) {
                Element colorBg = mBtnColor.getBackgroundElement();
                if (colorBg instanceof ShapeElement) {
                    RgbColor[] rgbColors = ((ShapeElement) colorBg).getRgbColors();
                    if (rgbColors.length > 0) {
                        mDoodle.setColor(new DoodleColor(rgbColors[0].asArgbInt()));
                    }
                } else {
                    mDoodle.setColor(new DoodleColor(((PixelMapElement) colorBg).getPixelMap()));
                }
            }
        }

        private Map<IDoodleShape, Integer> mBtnShapeIds = new HashMap<>();

        {
            mBtnShapeIds.put(DoodleShape.HAND_WRITE, ResourceTable.Id_btn_hand_write);
            mBtnShapeIds.put(DoodleShape.ARROW, ResourceTable.Id_btn_arrow);
            mBtnShapeIds.put(DoodleShape.LINE, ResourceTable.Id_btn_line);
            mBtnShapeIds.put(DoodleShape.HOLLOW_CIRCLE, ResourceTable.Id_btn_holl_circle);
            mBtnShapeIds.put(DoodleShape.FILL_CIRCLE, ResourceTable.Id_btn_fill_circle);
            mBtnShapeIds.put(DoodleShape.HOLLOW_RECT, ResourceTable.Id_btn_holl_rect);
            mBtnShapeIds.put(DoodleShape.FILL_RECT, ResourceTable.Id_btn_fill_rect);

        }

        @Override
        public void setShape(IDoodleShape shape) {
            super.setShape(shape);
            setSingleSelected(mBtnShapeIds.values(), mBtnShapeIds.get(shape));
        }

        Text mPaintSizeView = (Text) DoodleAbility.this.findComponentById(ResourceTable.Id_paint_size_text);

        @Override
        public void setSize(float paintSize) {
            super.setSize(paintSize);
            mEditSizeSeekBar.setProgressValue((int) paintSize);
            mPaintSizeView.setText("" + (int) paintSize + "");

            if (mTouchGestureListener.getSelectedItem() != null) {
                mTouchGestureListener.getSelectedItem().setSize(getSize());
            }
        }

        @Override
        public void setColor(IDoodleColor color) {
            IDoodlePen pen = getPen();
            super.setColor(color);

            DoodleColor doodleColor = null;
            if (color instanceof DoodleColor) {
                doodleColor = (DoodleColor) color;
            }
            if (doodleColor != null
                && canChangeColor(pen)) {
                if (doodleColor.getType() == DoodleColor.Type.COLOR) {
                    ShapeElement colorShapeElement = new ShapeElement();
                    colorShapeElement.setRgbColor(RgbColor.fromArgbInt(doodleColor.getColor()));
                    mBtnColor.setBackground(colorShapeElement);
                } else if (doodleColor.getType() == DoodleColor.Type.BITMAP) {
                    mBtnColor.setBackground(new PixelMapElement(doodleColor.getBitmap()));
                }

                if (mTouchGestureListener.getSelectedItem() != null) {
                    mTouchGestureListener.getSelectedItem().setColor(getColor().copy());
                }
            }

            if (doodleColor != null && pen == DoodlePen.MOSAIC
                && doodleColor.getLevel() != mMosaicLevel) {
                switch (doodleColor.getLevel()) {
                    case DoodlePath.MOSAIC_LEVEL_1:
                        DoodleAbility.this.findComponentById(ResourceTable.Id_btn_mosaic_level1).simulateClick();
                        break;
                    case DoodlePath.MOSAIC_LEVEL_2:
                        DoodleAbility.this.findComponentById(ResourceTable.Id_btn_mosaic_level2).simulateClick();
                        break;
                    case DoodlePath.MOSAIC_LEVEL_3:
                        DoodleAbility.this.findComponentById(ResourceTable.Id_btn_mosaic_level3).simulateClick();
                        break;
                }
            }
        }

        @Override
        public void enableZoomer(boolean enable) {
            super.enableZoomer(enable);
            DoodleAbility.this.findComponentById(ResourceTable.Id_btn_zoomer).setSelected(enable);
            if (enable) {
                Toast.show(DoodleAbility.this, "x" + mDoodleParams.mZoomerScale, Toast.LENGTH_SHORT);
            }
        }

        @Override
        public boolean undo() {
            mTouchGestureListener.setSelectedItem(null);
            boolean res = super.undo();
            if (getRedoItemCount() > 0) {
                mRedoBtn.setVisibility(VISIBLE);
            } else {
                mRedoBtn.setVisibility(Component.HIDE);
            }
            return res;
        }

        @Override
        public void clear() {
            super.clear();
            mTouchGestureListener.setSelectedItem(null);
            mRedoBtn.setVisibility(Component.HIDE);
        }

        @Override
        public void addItem(IDoodleItem item) {
            super.addItem(item);
            if (getRedoItemCount() > 0) {
                mRedoBtn.setVisibility(VISIBLE);
            } else {
                mRedoBtn.setVisibility(Component.HIDE);
            }
        }

        Component mBtnEditMode = DoodleAbility.this.findComponentById(ResourceTable.Id_doodle_btn_brush_edit);
        Boolean mLastIsDrawableOutside = null;

        @Override
        public void setEditMode(boolean editMode) {
            if (editMode == isEditMode()) {
                return;
            }

            super.setEditMode(editMode);
            mBtnEditMode.setSelected(editMode);
            if (editMode) {
                Toast.show(DoodleAbility.this, ResourceTable.String_doodle_edit_mode, Toast.LENGTH_SHORT);
                mLastIsDrawableOutside = mDoodle.isDrawableOutside(); // save
                mDoodle.setIsDrawableOutside(true);
                mPenContainer.setVisibility(Component.HIDE);
                mShapeContainer.setVisibility(Component.HIDE);
                mSizeContainer.setVisibility(Component.HIDE);
                mColorContainer.setVisibility(Component.HIDE);
                mBtnUndo.setVisibility(Component.HIDE);
                mMosaicMenu.setVisibility(Component.HIDE);
            } else {
                if (mLastIsDrawableOutside != null) { // restore
                    mDoodle.setIsDrawableOutside(mLastIsDrawableOutside);
                }
                mTouchGestureListener.center(); // center picture
                if (mTouchGestureListener.getSelectedItem() == null) { // restore
                    setPen(getPen());
                }
                mTouchGestureListener.setSelectedItem(null);
                mPenContainer.setVisibility(VISIBLE);
                mSizeContainer.setVisibility(VISIBLE);
                mBtnUndo.setVisibility(VISIBLE);
            }
        }

        private void setSingleSelected(Collection<Integer> ids, int selectedId) {
            for (int id : ids) {
                if (id == selectedId) {
                    DoodleAbility.this.findComponentById(id).setSelected(true);
                } else {
                    DoodleAbility.this.findComponentById(id).setSelected(false);
                }
            }
        }
    }

    private Uri saveImage(String fileName, PixelMap pixelMap) {
        final int quality = 90;
        Uri uri = null;
        try {
            ValuesBucket valuesBucket = new ValuesBucket();
            valuesBucket.putString(AVStorage.Images.Media.DISPLAY_NAME, fileName);
            valuesBucket.putString("relative_path", "DCIM/");
            valuesBucket.putString(AVStorage.Images.Media.MIME_TYPE, "image/" + "jpeg");
            // 应用独占
            valuesBucket.putInteger("is_pending", 1);
            DataAbilityHelper helper = DataAbilityHelper.creator(getContext());
            int id = helper.insert(AVStorage.Images.Media.EXTERNAL_DATA_ABILITY_URI, valuesBucket);

            uri = Uri.appendEncodedPathToUri(AVStorage.Images.Media.EXTERNAL_DATA_ABILITY_URI, String.valueOf(id));
            FileDescriptor fd = helper.openFile(uri, "w");
            ImagePacker imagePacker = ImagePacker.create();
            ImagePacker.PackingOptions packingOptions = new ImagePacker.PackingOptions();
            OutputStream outputStream = new FileOutputStream(fd);
            packingOptions.quality = quality;
            boolean isResult = imagePacker.initializePacking(outputStream, packingOptions);
            if (isResult) {
                isResult = imagePacker.addImage(pixelMap);
                if (isResult) {
                    long dataSize = imagePacker.finalizePacking();
                }
            }
            outputStream.flush();
            outputStream.close();
            valuesBucket.clear();
            // 解除独占
            valuesBucket.putInteger("is_pending", 0);
            helper.update(uri, valuesBucket, null);
            return uri;
        } catch (Exception e) {
            e.getMessage();
        }
        return uri;
    }

    @Override
    protected void onOrientationChanged(AbilityInfo.DisplayOrientation displayOrientation) {
        super.onOrientationChanged(displayOrientation);
    }

    @Override
    public void onBackground() {
        if (dirbottom != null)
            dirbottom.setMarginBottom(AttrHelper.vp2px(0, getContext()));
        super.onBackground();
    }
}
