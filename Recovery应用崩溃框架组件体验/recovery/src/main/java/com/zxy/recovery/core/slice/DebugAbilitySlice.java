/*
 * Copyright (C) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zxy.recovery.core.slice;

import com.zxy.recovery.ResourceTable;
import com.zxy.recovery.core.RecoveryAbility;
import com.zxy.recovery.core.RecoveryStore;
import com.zxy.recovery.tools.LogUtil;
import com.zxy.recovery.tools.RecoveryUtil;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.agp.components.Image;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.agp.components.Component;
import ohos.agp.utils.Color;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.dialog.ToastDialog;
import ohos.agp.window.service.WindowManager;
import ohos.bundle.IBundleManager;
import ohos.utils.IntentConstants;
import ohos.utils.net.Uri;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * DebugAbilitySlice
 *
 * @author:wjt
 * @since 2021-04-06
 */
public class DebugAbilitySlice extends AbilitySlice {
    private static final String PERMISSION = "ohos.permission.WRITE_MEDIA";
    private static final int MY_PERMISSIONS_REQUEST_WRITE_MEDIA = 100;
    private RecoveryStore.ExceptionData mExceptionData;
    private String mStackTrace;
    private String mCause;
    private Text mExceptionTypeTv;
    private Text mClassNameTv;
    private Text mMethodNameTv;
    private Text mLineNumberTv;
    private Text mStackTraceTv;
    private Text mCauseTv;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        getWindow().addFlags(WindowManager.LayoutConfig.INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().setStatusBarColor(Color.getIntColor("#212121"));
        super.setUIContent(ResourceTable.Layout_recovery_layout_debug);
        initView();

        mExceptionData = intent.getSequenceableParam(RecoveryStore.EXCEPTION_DATA);
        mCause = intent.getStringParam(RecoveryStore.EXCEPTION_CAUSE);
        mStackTrace = intent.getStringParam(RecoveryStore.STACK_TRACE);

        initData();
    }

    private void initView() {
        mExceptionTypeTv = (Text) findComponentById(ResourceTable.Id_tv_type);
        mClassNameTv = (Text) findComponentById(ResourceTable.Id_tv_class_name);
        mMethodNameTv = (Text) findComponentById(ResourceTable.Id_tv_method_name);
        mLineNumberTv = (Text) findComponentById(ResourceTable.Id_tv_line_number);
        mStackTraceTv = (Text) findComponentById(ResourceTable.Id_tv_stack_trace);
        mCauseTv = (Text) findComponentById(ResourceTable.Id_tv_cause);

        Image imageBack = (Image) findComponentById(ResourceTable.Id_image_title_bar_back);
        Text textSave = (Text) findComponentById(ResourceTable.Id_text_title_bar_right);
        imageBack.setVisibility(Component.VISIBLE);
        textSave.setText("SAVE");
        imageBack.setClickedListener(component -> terminateAbility());
        textSave.setClickedListener(component -> {
            requestPermission();
        });
    }

    private void requestPermission() {
        if (verifySelfPermission(PERMISSION) == IBundleManager.PERMISSION_GRANTED) {
            boolean isSuccess = saveCrashData();
            showToast(isSuccess ? "Save success!" : "Save failed!");
        } else {
            if (canRequestPermission(PERMISSION)) {
                requestPermissionsFromUser(
                        new String[]{PERMISSION}, MY_PERMISSIONS_REQUEST_WRITE_MEDIA);
            } else {
                CommonDialog commonDialog = new CommonDialog(this);
                Component dialogLayout = LayoutScatter.getInstance(this).parse(ResourceTable.Layout_dialog_layout,
                        null, false);
                Text textContent = (Text) dialogLayout.findComponentById(ResourceTable.Id_text_dialog_layout_content);
                Button btnCancel = (Button) dialogLayout.findComponentById(ResourceTable.Id_btn_dialog_layout_cancel);
                Button btnConfirm = (Button) dialogLayout.findComponentById(ResourceTable.Id_btn_dialog_layout_confirm);
                textContent.setText("此功能为了记录报错信息，需要到开启\"存储\"权限");
                btnConfirm.setText("开启");
                btnCancel.setClickedListener(component1 -> {
                    commonDialog.destroy();
                });
                btnConfirm.setClickedListener(component1 -> {
                    Intent mIntent = new Intent();
                    Operation operation = new Intent.OperationBuilder()
                            .withAction(IntentConstants.ACTION_APPLICATION_DETAILS_SETTINGS)
                            .withUri(Uri.parse("package:" + getBundleName()))
                            .build();
                    mIntent.setOperation(operation);
                    startAbilityForResult(mIntent, MY_PERMISSIONS_REQUEST_WRITE_MEDIA);
                    commonDialog.destroy();
                });
                commonDialog.setContentCustomComponent(dialogLayout);
                commonDialog.setTransparent(true);
                commonDialog.show();
            }
        }
    }

    @Override
    protected void onAbilityResult(int requestCode, int resultCode, Intent resultData) {
        super.onAbilityResult(requestCode, resultCode, resultData);
        if (MY_PERMISSIONS_REQUEST_WRITE_MEDIA == requestCode) {
            requestPermission();
        }
    }

    private void initData() {
        if (mExceptionData != null) {
            String type = mExceptionData.getType() == null ? "" : mExceptionData.getType();
            String name = mExceptionData.getClassName() == null ? "" : mExceptionData.getClassName();

            mExceptionTypeTv.setText(String.format(getString(ResourceTable.String_recovery_exception_type),
                    type.substring(type.lastIndexOf('.') + 1)));

            mClassNameTv.setText(String.format(getString(ResourceTable.String_recovery_class_name),
                    name.substring(name.lastIndexOf('.') + 1)));

            mMethodNameTv.setText(String.format(getString(ResourceTable.String_recovery_method_name),
                    mExceptionData.getMethodName()));

            mLineNumberTv.setText(String.format(getString(ResourceTable.String_recovery_line_number),
                    mExceptionData.getLineNumber()));
        }
        mCauseTv.setText(String.valueOf(mCause));
        mStackTraceTv.setText(String.valueOf(mStackTrace));
    }

    /**
     * 显示Toast
     *
     * @param text 显示的文字
     */
    private void showToast(String text) {
        Component toastLayout = LayoutScatter.getInstance(this)
                .parse(ResourceTable.Layout_layout_toast, null, false);
        Text toastText = (Text) toastLayout.findComponentById(ResourceTable.Id_text_msg_toast);
        toastText.setText(text);
        new ToastDialog(this)
                .setComponent(toastLayout)
                .setTransparent(true)
                .show();
    }

    /**
     * 保存报错信息
     * @return 是否保存成功，true：成功，false：失败
     */
    private boolean saveCrashData() {
        String date = RecoveryUtil.getDateFormat().format(new Date(System.currentTimeMillis()));
        File dir = new File(getExternalFilesDir(null) + File.separator + RecoveryAbility.DEFAULT_CRASH_FILE_DIR_NAME);
        if (!dir.exists()) {
            boolean isSucceed = dir.mkdirs();
            if (!isSucceed) {
                return false;
            }
        }
        File file = new File(dir, date + ".txt");
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            writer.write(System.lineSeparator() + "Exception:"
                    + System.lineSeparator()
                    + (mExceptionData == null ? null : mExceptionData.toString())
                    + System.lineSeparator()
                    + System.lineSeparator());
            writer.write("Cause:" + System.lineSeparator()
                    + mCause + System.lineSeparator()
                    + System.lineSeparator());
            writer.write("StackTrace:"
                    + System.lineSeparator()
                    + mStackTrace
                    + System.lineSeparator()
                    + System.lineSeparator());
            writer.flush();
        } catch (IOException e) {
            LogUtil.error("DebugAbilitySlice", "IOException");
            return false;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    LogUtil.error("DebugAbilitySlice", "IOException");
                }
            }
        }
        return true;
    }
}
