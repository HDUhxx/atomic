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

package com.example.doodle.imagepicker;

import com.example.doodle.ResourceTable;
import com.example.doodle.util.ToastsUtils;
import com.example.doodle.util.Util;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.ListContainer;
import ohos.agp.components.TableLayoutManager;
import ohos.agp.components.Text;
import ohos.data.resultset.ResultSet;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.Size;
import ohos.media.photokit.metadata.AVStorage;
import ohos.utils.net.Uri;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * fuqiangping
 *
 * @since 2021-04-29
 */
public class ImageSelectorActivity extends Ability {
    /**
     * KEY_PATH_LIST
     */
    public static final String KEY_PATH_LIST = "key_path";

    /**
     * WHAT_REFRESH_PATH_LIST
     */
    public static final int WHAT_REFRESH_PATH_LIST = 1;

    /**
     * KEY_IS_MULTIPLE_CHOICE
     */
    public static final String KEY_IS_MULTIPLE_CHOICE = "key_is_multiple_choice";

    /**
     * KEY_MAX_COUNT
     */
    public static final String KEY_MAX_COUNT = "key_max_count";

    /**
     * REQ_CODE_DOODLE
     */
    public static final int REQ_CODE_DOODLE = 101;
    /**
     * RESULT_OK
     */
    public static final int RESULT_OK = 1;

    // 每次查询数据库的数量
    private static final int CURSOR_COUNT = 36;
    private static final int GRID_COUNT = 3;
    private static final int MINUS_INDEX = -1;
    /**
     * RESULT_OK
     */
    private ListContainer mGridView;
    private int mCursorPosition = MINUS_INDEX; // 当前在数据库查找位置

    private EventHandler mHandler;

    private boolean mIsFinishSearchImage = false; // 是否扫描完了所有图片
    private boolean mIsScanning = false; // 正在扫描
//    private boolean mIsMultipleChoice = false; // 是否多选
//    private int mMaxCount = Integer.MAX_VALUE; // 最多可选数量,超过最大数时点击不会选中.多选时次变量才生效
    private ImageSelectorProvider mProvider;
    private ArrayList<PixelMap> mList = new ArrayList<>();
    private HashMap<PixelMap, Uri> mUriMap = new HashMap<>();
    private int mImageHeight;
    private int mImagesetWidth;
    private int mSelectIndex = MINUS_INDEX;
    private Button btnback;
    private Button btnenter;
    private DataAbilityHelper mHelper;
    private Text mLoadTxt;
    private ListContainer.ItemClickedListener mItemClickedListener = new ListContainer.ItemClickedListener() {
        @Override
        public void onItemClicked(ListContainer listContainer, Component component, int psotion, long l) {
            if (mSelectIndex == psotion) {
                mSelectIndex = MINUS_INDEX;
                btnenter.setText("确定(0)");
                mProvider.setType(true);
            } else {
                mSelectIndex = psotion;
                btnenter.setText("确定(1)");
                mProvider.setType(false);
            }

            mProvider.setSlecitem(psotion);
            mProvider.notifyDataChanged();
        }
    };

    /**
     * ForResult
     *
     * @param requestCode
     * @param ability
     * @param pathList
     * @param isMultipleChoice
     */
    public static void startabilityForResult(int requestCode, Ability ability,
                                             ArrayList<String> pathList, boolean isMultipleChoice) {
        startabilityForResult(requestCode, ability, pathList, isMultipleChoice, Integer.MAX_VALUE);
    }

    /**
     * ForResult
     *
     * @param requestCode
     * @param ability
     * @param pathList
     * @param isMultipleChoice
     * @param maxCount
     */
    public static void startabilityForResult(int requestCode, Ability ability, ArrayList<String> pathList,
                                             boolean isMultipleChoice, int maxCount) {
        Intent intent = new Intent();
        intent.setParam(ImageSelectorActivity.KEY_IS_MULTIPLE_CHOICE, isMultipleChoice);
        intent.setParam(ImageSelectorActivity.KEY_MAX_COUNT, maxCount);
        intent.setParam(ImageSelectorActivity.KEY_PATH_LIST, pathList);

        // 指定待启动FA的bundleName和abilityName
        Operation operation = new Intent.OperationBuilder()
            .withDeviceId("")
            .withBundleName("com.example.doodle")
            .withAbilityName("com.example.doodlelib.imageselector.ImageSelectorAbility")
            .build();
        intent.setOperation(operation);

        // 通过AbilitySlice的startAbility接口实现启动另一个页面
        ability.startAbilityForResult(intent, requestCode);
    }

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_doodle_layout_image_selector);
        mGridView = (ListContainer) findComponentById(ResourceTable.Id_doodle_list_image);
        mLoadTxt = (Text) findComponentById(ResourceTable.Id_loading);
        btnback = (Button) findComponentById(ResourceTable.Id_btn_back);
        btnback.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                terminateAbility();
            }
        });
        btnenter = (Button) findComponentById(ResourceTable.Id_btn_enter);
        btnenter.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if (mSelectIndex == MINUS_INDEX) {
                    new ToastsUtils.Builder(getContext())
                        .setToastText("请选择图片")
                        .build().show();
                    return;
                } else {
                    if (mSelectIndex >= 0 && mList.size() > 0) {
                        Intent intent = new Intent();
                        PixelMap pixelMap = mList.get(mSelectIndex);
                        intent.setUriAndType(mUriMap.get(pixelMap), null);
                        setResult(RESULT_OK, intent);
                        terminateAbility();
                    }
                }
            }
        });
        mHelper = DataAbilityHelper.creator(getContext());
        mImageHeight = Util.getScreenWidth(this) / GRID_COUNT;
        mImagesetWidth = Util.getScreenWidth(this) / GRID_COUNT;
        EventRunner eventRunner = EventRunner.current();
        if (eventRunner == null) {
            return;
        }
        mHandler = new ScanImgHandler(eventRunner);
        initRecycleView();
        mGridView.setItemClickedListener(mItemClickedListener);
        scanImageData();
    }

    private void initRecycleView() {
        TableLayoutManager manager = new TableLayoutManager();
        manager.setColumnCount(GRID_COUNT);
        mGridView.setLayoutManager(manager);
        mGridView.setScrolledListener(new ScrollListener());
    }

    /**
     * 滑动监听
     *
     * @since 2021-04-29
     */
    private class ScrollListener implements ListContainer.ScrolledListener {
        final static int pageItemCount = 10;

        @Override
        public void onContentScrolled(Component component, int i, int i1, int i2, int i3) {
            final int childCount = ((ListContainer) component).getChildCount();
            if (childCount > 0) {
                Component lastChild = ((ListContainer) component).getComponentAt(childCount - 1);
//                ImageSelectorProvider outerAdapter = (ImageSelectorProvider) ((ListContainer) component)
//                    .getItemProvider();
                int lastVisible = ((ListContainer) component).getIndexForComponent(lastChild);
                if (lastVisible + pageItemCount >= mList.size() && !mIsFinishSearchImage && !mIsScanning) {
                    scanImageData();
                }
            }
        }
    }

    /**
     * scanImageData
     */
    public void scanImageData() {
        if (mIsFinishSearchImage || mIsScanning) {
            return;
        }
        mIsScanning = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    ------------------
//                    DataAbilityPredicates predicates = new DataAbilityPredicates();
//                    predicates.orderByDesc(AVStorage.Images.Media.DATE_MODIFIED);
                    ResultSet result = mHelper.query(AVStorage.Images.Media.EXTERNAL_DATA_ABILITY_URI, null, null);
                    result.goToRow(mCursorPosition); // 从上一次的扫描位置继续扫描
                    int index = 0;
                    while (result.goToNextRow() && index < CURSOR_COUNT) {
                        index++;
                        int mediaId = result.getInt(result.getColumnIndexForName(AVStorage.Images.Media.ID));
                        Uri uri = Uri.appendEncodedPathToUri(AVStorage.Images.Media.EXTERNAL_DATA_ABILITY_URI,
                            "" + mediaId);
                        FileDescriptor filedesc = null;
                        try {
                            filedesc = mHelper.openFile(uri, "r");
                        } catch (DataAbilityRemoteException | FileNotFoundException e) {
                            e.getMessage();
                        }
                        ImageSource.DecodingOptions decodingOpts = new ImageSource.DecodingOptions();
                        decodingOpts.desiredSize = new Size(mImagesetWidth, mImageHeight);
                        if (filedesc != null && filedesc.valid()) {
                            ImageSource imageSource = ImageSource.create(filedesc, null);
                            PixelMap pixelMap = imageSource.createThumbnailPixelmap(decodingOpts, true);
                            mList.add(pixelMap);
                            mUriMap.put(pixelMap, uri);
                        }
                    }
//                    Collections.reverse(mList);
                    mCursorPosition += index;
                    mIsScanning = false;
                    if (index < CURSOR_COUNT) { // 扫描完了所有图片
                        mIsFinishSearchImage = true;
                    }
                    result.close();
                    mHandler.sendEvent(WHAT_REFRESH_PATH_LIST);
                } catch (DataAbilityRemoteException e) {
                    e.getCause();
                }
            }
        }).start();
    }

    /**
     * ScanImgHandler
     *
     * @since 2021-04-29
     */
    class ScanImgHandler extends EventHandler {
        ScanImgHandler(EventRunner runner) throws IllegalArgumentException {
            super(runner);
        }

        @Override
        protected void processEvent(InnerEvent event) {
            super.processEvent(event);
            if (event == null) {
                return;
            }
            switch (event.eventId) {
                case WHAT_REFRESH_PATH_LIST:
                    if (mList.size() > 0 && mLoadTxt.getVisibility() == Component.VISIBLE) {
                        mLoadTxt.setVisibility(Component.HIDE);
                    }
                    if (mProvider == null) {
                        mProvider = new ImageSelectorProvider(mList, ImageSelectorActivity.this);
                        mGridView.setItemProvider(mProvider);
                    } else {
                        mProvider.refreshPathList(mList);
                    }
                    break;
                default:
                    break;
            }
        }

        /**
         * distributeEvent
         *
         * @param event
         */
        @Override
        public void distributeEvent(InnerEvent event) {
            super.distributeEvent(event);
        }
    }
}