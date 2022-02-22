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

import com.example.doodle.DoodleAbility;
import com.example.doodle.imagepicker.ImageSelectorActivity;
import com.example.doodle.util.LogUtil;
import com.example.doodle.util.Toast;
import com.example.doodle.util.Util;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.Text;
import ohos.agp.utils.TextTool;
import ohos.agp.window.dialog.ToastDialog;
import ohos.bundle.AbilityInfo;
import ohos.bundle.IBundleManager;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.Size;
import ohos.security.SystemPermission;
import ohos.utils.net.Uri;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;

/**
 * fuqiangping
 *
 * @since 2021-04-22
 */
public class MainAbility extends Ability {
    private static final int PERMISSION_REQUEST_INTERACTIVE = 1;
    private static final int REQ_CODE_SELECT_IMAGE = 100;
    private static final int REQ_CODE_DOODLE = 101;
    private final static int mDuration = 20000;
    private Text mPath;
    private Image mSaveImg;
    private DataAbilityHelper mHelper;
    private boolean mIsOpenImageSelector = false;
    private final static String mPermmReadMedia = "ohos.permission.READ_MEDIA";

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_ability_main);
        requestPermissionsFromUser(new String[]{SystemPermission.READ_USER_STORAGE,
            SystemPermission.WRITE_USER_STORAGE, SystemPermission.READ_MEDIA, SystemPermission.WRITE_MEDIA,
            SystemPermission.MEDIA_LOCATION}, PERMISSION_REQUEST_INTERACTIVE);
        mSaveImg = (Image) findComponentById(ResourceTable.Id_img);
        findComponentById(ResourceTable.Id_btn_select_image).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                checkPermission();
            }
        });
        findComponentById(ResourceTable.Id_btn_guide).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                Intent intent = new Intent();
                Operation operation = new Intent.OperationBuilder()
                    .withDeviceId("").withBundleName(getBundleName())
                    .withAbilityName("com.example.doodledemo.guide.DoodleGuideAbility").build();
                intent.setOperation(operation);
                startAbility(intent);
            }
        });
        findComponentById(ResourceTable.Id_btn_mosaic).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                Intent intent = new Intent();
                Operation operation = new Intent.OperationBuilder()
                    .withDeviceId("").withBundleName(getBundleName())
                    .withAbilityName("com.example.doodledemo.MosaicDemo").build();
                intent.setOperation(operation);
                startAbility(intent);
            }
        });
        findComponentById(ResourceTable.Id_btn_scale_gesture).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                Intent intent = new Intent();
                Operation operation = new Intent.OperationBuilder()
                    .withDeviceId("").withBundleName(getBundleName())
                    .withAbilityName("com.example.doodledemo.ScaleGestureItemDemoAbility")
                    .build();
                intent.setOperation(operation);
                startAbility(intent);
            }
        });
        mPath = (Text) findComponentById(ResourceTable.Id_img_path);
    }

    private void checkPermission() {
        if (verifySelfPermission(mPermmReadMedia) != IBundleManager.PERMISSION_GRANTED) {
            // 应用未被授予权限
            if (canRequestPermission(mPermmReadMedia)) {
                // 是否可以申请弹框授权(首次申请或者用户未选择禁止且不再提示)
                requestPermissionsFromUser(
                    new String[]{mPermmReadMedia}, PERMISSION_REQUEST_INTERACTIVE);
            }
        } else {
            if (!mIsOpenImageSelector) {
                Intent intent = new Intent();
                intent.setParam(ImageSelectorActivity.KEY_IS_MULTIPLE_CHOICE, false);
                intent.setParam(ImageSelectorActivity.KEY_MAX_COUNT, Integer.MAX_VALUE);
                Operation operation = new Intent.OperationBuilder()
                    .withDeviceId("")
                    .withBundleName(getBundleName())
                    .withAbilityName("com.example.doodle.imagepicker.ImageSelectorActivity")
                    .build();
                intent.setOperation(operation);
                mIsOpenImageSelector = !mIsOpenImageSelector;
                startAbilityForResult(intent, REQ_CODE_SELECT_IMAGE);
            }
        }
    }

    @Override
    public void onRequestPermissionsFromUserResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsFromUserResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_INTERACTIVE) {
            if (!(grantResults.length > 0 && grantResults[0] == IBundleManager.PERMISSION_GRANTED)) {
                Toast.showLong(this, "请前往“设置”授予“存储访问权限”");
            }
        }
    }

    @Override
    protected void onOrientationChanged(AbilityInfo.DisplayOrientation displayOrientation) {
        super.onOrientationChanged(displayOrientation);
    }

    @Override
    protected void onAbilityResult(int requestCode, int resultCode, Intent resultData) {
        super.onAbilityResult(requestCode, resultCode, resultData);
        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            mIsOpenImageSelector = false;
            if (resultData == null) {
                return;
            }
            if (resultCode == ImageSelectorActivity.RESULT_OK) {
                Intent intent = new Intent();
                Uri uri = resultData.getUri();
                intent.setUriAndType(uri, null);
                Operation operation = new Intent.OperationBuilder()
                    .withDeviceId("")
                    .withBundleName(getBundleName())
                    .withAbilityName("com.example.doodle.DoodleAbility")
                    .build();
                intent.setOperation(operation);
                startAbilityForResult(intent, REQ_CODE_DOODLE);
            }
        } else if (requestCode == REQ_CODE_DOODLE) {
            if (resultData == null) {
                return;
            }
            if (resultCode == DoodleAbility.RESULT_OK) {
                Uri uri = resultData.getUri();
                String path = resultData.getStringParam(DoodleAbility.KEY_IMAGE_PATH);
                if (TextTool.isNullOrEmpty(path)) {
                    return;
                }
                showSavePic(uri);
                mPath.setText(path);
                LogUtil.d("onAbilityResult", "path=" + path);
            } else if (resultCode == DoodleAbility.RESULT_ERROR) {
                ToastDialog toastDialog = new ToastDialog(this);
                toastDialog.setText("error").setAlignment(1).setDuration(mDuration).show();
            }
        }
    }

    private void showSavePic(Uri uri) {
        if (mHelper == null) {
            mHelper = DataAbilityHelper.creator(getContext());
        }
        FileDescriptor filedesc = null;
        try {
            filedesc = mHelper.openFile(uri, "r");
        } catch (DataAbilityRemoteException | FileNotFoundException e) {
            e.getMessage();
        }
        ImageSource.DecodingOptions decodingOpts = new ImageSource.DecodingOptions();
        decodingOpts.desiredSize = new Size(Util.getScreenWidth(this), Util.getScreenHeight(this));
        ImageSource imageSource = ImageSource.create(filedesc, null);
        PixelMap pixelMap = imageSource.createThumbnailPixelmap(decodingOpts, true);
        mSaveImg.setPixelMap(pixelMap);
    }
}
