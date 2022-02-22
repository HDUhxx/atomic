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
import com.example.doodle.util.Util;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.ListContainer;
import ohos.agp.components.StackLayout;
import ohos.agp.components.TableLayoutManager;
import ohos.agp.components.Text;
import ohos.app.Context;
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
import java.util.HashMap;
import java.util.List;

/**
 * fuqiangping
 *
 * @since 2021-04-29
 */
public class ImageSelectorView extends StackLayout {
    /**
     * WHAT_REFRESH_PATH_LIST
     */
    public static final int WHAT_REFRESH_PATH_LIST = 1;

    // 每次查询数据库的数量
    private static final int CURSOR_COUNT = 36;
    private static final int DEF_COUNT = 3;
    private static final int MINUS_INDEX = -1;
    private int mCount = DEF_COUNT; // 列数
    private ImageSelectorListener mSelectorListener;
//    private Text mPreText;
    private ArrayList<PixelMap> mList = new ArrayList<>();
    private HashMap<PixelMap, Uri> mUriMap = new HashMap<>();
    private int mImageHeight;
    private int mImagesetWidth;
    private DataAbilityHelper mHelper;
    private ImageSelectorProvider mProvider;
    private ListContainer mGridView;
    private Context mContext;
    private boolean mIsFinishSearchImage = false; // 是否扫描完了所有图片
    private boolean mIsScanning = false; // 正在扫描
    private int mCursorPosition = MINUS_INDEX; // 当前在数据库查找位置
    private EventHandler mHandler;
    private Text mLoadTxt;
    private int mGridCount = DEF_COUNT;
    private final static int mLoadTxtMargin = 150;
    private int mSelectIndex = MINUS_INDEX;
    private Button btnback;
    private Button btnenter;
    private ListContainer.ItemClickedListener mItemClickedListener = new ListContainer.ItemClickedListener() {
        @Override
        public void onItemClicked(ListContainer listContainer, Component component, int index, long l) {
            if (mSelectIndex == index) {
                mSelectIndex = MINUS_INDEX;
                btnenter.setText("确定(0)");
                mProvider.setType(true);
            } else {
                mSelectIndex = index;
                btnenter.setText("确定(1)");
                mProvider.setType(false);
            }
            mProvider.setSlecitem(index);
            mProvider.notifyDataChanged();
        }
    };

    /**
     * 构造函数
     *
     * @param context
     * @param isMultipleChoice
     * @param maxCount
     * @param pathList
     * @param listener
     */
    public ImageSelectorView(Context context, boolean isMultipleChoice, int maxCount,
                             final List<String> pathList, ImageSelectorListener listener) {
        super(context);
        this.mContext = context;
        Component view = LayoutScatter.getInstance(getContext())
            .parse(ResourceTable.Layout_doodle_layout_image_selector, null, true);
        addComponent(view, ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_PARENT);
        mLoadTxt = (Text) findComponentById(ResourceTable.Id_loading);
        mLoadTxt.setMarginTop(mLoadTxtMargin);
//        mPreText = (Text) findComponentById(ResourceTable.Id_text_pre_id);
        mGridView = (ListContainer) findComponentById(ResourceTable.Id_doodle_list_image);
        btnback = (Button) findComponentById(ResourceTable.Id_btn_back);
        btnback.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                mSelectorListener.onCancel();
            }
        });
        btnenter = (Button) findComponentById(ResourceTable.Id_btn_enter);
        btnenter.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                Uri uri = null;
                if (mSelectIndex >= 0 && mList.size() > 0) {
                    PixelMap pixelMap = mList.get(mSelectIndex);
                    uri = mUriMap.get(pixelMap);
                }
                if (uri != null) {
                    mSelectorListener.onEnter(uri);
                }
            }
        });
        mSelectorListener = listener;
        mHelper = DataAbilityHelper.creator(getContext());
        mImageHeight = Util.getScreenWidth(context) / mGridCount;
        mImagesetWidth = Util.getScreenWidth(context) / mGridCount;
        EventRunner eventRunner = EventRunner.current();
        if (eventRunner == null) {
            return;
        }
        mHandler = new ImageSelectorView.ScanImgHandler(eventRunner);
        initRecycleView();
        mGridView.setItemClickedListener(mItemClickedListener);
        scanImageData();
    }

    private void initRecycleView() {
        TableLayoutManager manager = new TableLayoutManager();
        manager.setColumnCount(mCount);
        mGridView.setLayoutManager(manager);
        mGridView.setScrolledListener(new ImageSelectorView.ScrollListener());
    }

    /**
     * 滑动监听
     *
     * @since 2021-05-06
     */
    private class ScrollListener implements ListContainer.ScrolledListener {
        @Override
        public void onContentScrolled(Component component, int i, int i1, int i2, int i3) {
            final int pageItemCount = 10;
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
     * 列数
     *
     * @param count
     */
    public void setColumnCount(int count) {
        mCount = count;
    }

    public int getColumnCount() {
        return mGridCount;
    }

    /**
     * 接口
     *
     * @since 2021-04-29
     */
    public interface ImageSelectorListener {
        /**
         * 取消
         */
        void onCancel();

        /**
         * enter
         *
         * @param uri
         */
        void onEnter(Uri uri);
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
                    mCursorPosition += index;
                    mIsScanning = false;
                    if (index < CURSOR_COUNT) { // 扫描完了所有图片
                        mIsFinishSearchImage = true;
                    }
                    result.close();
                    mHandler.sendEvent(WHAT_REFRESH_PATH_LIST);
                } catch (DataAbilityRemoteException e) {
                    e.getMessage();
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
                    if (mLoadTxt.getVisibility() == Component.VISIBLE) {
                        if (mList.size() > 0) {
                            mLoadTxt.setVisibility(Component.HIDE);
                        } else {
                            mLoadTxt.setText(ResourceTable.String_no_pic);
                        }
                    }
                    if (mProvider == null) {
                        mProvider = new ImageSelectorProvider(mList, (Ability) mContext);
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
