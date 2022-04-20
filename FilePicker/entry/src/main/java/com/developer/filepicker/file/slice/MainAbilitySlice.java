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

package com.developer.filepicker.file.slice;

import com.developer.filepicker.file.FileListAdapter;
import com.developer.filepicker.file.ListItem;
import com.developer.filepicker.file.ResourceTable;
import com.developer.filepicker.model.DialogConfigs;
import com.developer.filepicker.model.DialogProperties;
import com.developer.filepicker.view.FilePickerDialog;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Checkbox;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.ListContainer;
import ohos.agp.components.RadioContainer;
import ohos.agp.components.Text;
import ohos.agp.components.TextField;
import ohos.agp.utils.Color;
import ohos.agp.window.dialog.ToastDialog;
import ohos.agp.window.service.WindowManager;
import ohos.app.Context;
import ohos.app.Environment;
import ohos.data.resultset.ResultSet;
import ohos.global.resource.RawFileEntry;
import ohos.global.resource.Resource;
import ohos.global.resource.ResourceManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/** MainAbilitySlice
 *
 * @author ljx
 * @since 2021-07-012
 */
public class MainAbilitySlice extends AbilitySlice {
    private static final int CAPACITY = 1024;
    private static final int CONSTANT = -1;
    private FilePickerDialog dialog;
    private ArrayList<ListItem> listItem;
    private FileListAdapter mFileListAdapter;
    private DialogProperties properties;
    private ListContainer fileList;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        getWindow().setInputPanelDisplayType(WindowManager.LayoutConfig.INPUT_ADJUST_PAN);
        WindowManager.getInstance().getTopWindow().get().setStatusBarColor(new Color(Color.getIntColor("#3F51B5")).getValue());
        super.setUIContent(ResourceTable.Layout_ability_main);
        initView();
        apply();
        initFilePickerDialog();

        getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        setFile(Environment.DIRECTORY_MUSIC,"resources/rawfile/qnzl.mp3", "qnzl.mp3");
        setFile(Environment.DIRECTORY_PICTURES,"resources/rawfile/ic_directory_parent.png", "ic_directory_parent.png");
        setFile(Environment.DIRECTORY_PICTURES,"resources/rawfile/icon.png", "icon.png");
        setFile(Environment.DIRECTORY_DOCUMENTS,"resources/rawfile/ic_type_folder.png", "ic_type_folder.png");
        setFile(Environment.DIRECTORY_DOWNLOADS,"resources/rawfile/ic_type_file.png", "ic_type_file.png");
        setFile(Environment.DIRECTORY_DOWNLOADS,"resources/rawfile/ic_type_folder.png", "ic_type_folder.png");
        setFile(Environment.DIRECTORY_DOWNLOADS,"resources/rawfile/test1.zip", "test1.zip");
        setFile(Environment.DIRECTORY_DOWNLOADS,"resources/rawfile/test2.jpg", "test2.jpg");
        setFile(Environment.DIRECTORY_DOWNLOADS,"resources/rawfile/test2.rar", "test2.rar");
    }

    private void setFile(String type, String rawfile, String name) {
        ResourceManager resManager = getContext().getResourceManager();
        RawFileEntry rawFileEntry = resManager.getRawFileEntry(rawfile);
        Resource resource = null;
        OutputStream outputStream = null;
        try {
            resource = rawFileEntry.openRawFile();
            File file = new File(getExternalFilesDir(type), name);
            outputStream = new FileOutputStream(file);
            int index;
            byte[] bytes = new byte[CAPACITY];
            while ((index = resource.read(bytes)) != CONSTANT) {
                outputStream.write(bytes, 0, index);
                outputStream.flush();
            }
        } catch (IOException e) {
            new ToastDialog(this).setText(e.getMessage()).show();
        } finally {
            try {
                resource.close();
                outputStream.close();
            } catch (Exception e) {
                new ToastDialog(this).setText(e.getMessage()).show();
            }
        }
    }

    private void initView() {
        listItem = new ArrayList<>();
        fileList = (ListContainer) findComponentById(ResourceTable.Id_listView);
        mFileListAdapter = new FileListAdapter(listItem, getContext());
        fileList.setItemProvider(mFileListAdapter);
        properties = new DialogProperties(this);
        RadioContainer modeRadio = (RadioContainer) findComponentById(ResourceTable.Id_modeRadio);
        modeRadio.mark(0);
        modeRadio.setMarkChangedListener((radioContainer, index) -> {
            switch (index) {
                case 0:
                    properties.selection_mode = DialogConfigs.SINGLE_MODE;
                    break;
                case 1:
                    properties.selection_mode = DialogConfigs.MULTI_MODE;
                    break;
            }
        });
        RadioContainer typeRadio = (RadioContainer) findComponentById(ResourceTable.Id_typeRadio);
        typeRadio.mark(0);
        typeRadio.setMarkChangedListener((radioContainer, index) -> {
            switch (index) {
                case 0:
                    properties.selection_type = DialogConfigs.FILE_SELECT;
                    break;
                case 1:
                    properties.selection_type = DialogConfigs.DIR_SELECT;
                    break;
                case 2:
                    properties.selection_type = DialogConfigs.FILE_AND_DIR_SELECT;
                    break;
            }
        });
    }

    private void apply() {
        TextField extension = (TextField) findComponentById(ResourceTable.Id_extensions);
        TextField root = (TextField) findComponentById(ResourceTable.Id_root);
        TextField offset = (TextField) findComponentById(ResourceTable.Id_offset);
        Checkbox show_hidden_files = (Checkbox) findComponentById(ResourceTable.Id_show_hidden_files);
        Text apply = (Text) findComponentById(ResourceTable.Id_apply);
        apply.setClickedListener((view) -> {
            String fextension = extension.getText();
            if (fextension.length() > 0) {
                int commas = countCommas(fextension);
                String[] exts = new String[commas + 1];
                StringBuffer buff = new StringBuffer();
                int i = 0;
                for (int j = 0; j < fextension.length(); j++) {
                    if (fextension.charAt(j) == ',') {
                        exts[i] = buff.toString();
                        buff = new StringBuffer();
                        i++;
                    } else {
                        buff.append(fextension.charAt(j));
                    }
                }
                exts[i] = buff.toString();
                properties.extensions = exts;
            } else {
                properties.extensions = null;
            }
            String foffset = root.getText();
            if (foffset.length() > 0 || !foffset.equals("")) {
                properties.root = new File(foffset);
            } else {
                properties.root = new File(properties.getPath());
            }
            String fset = offset.getText();
            if (fset.length() > 0 || !fset.equals("")) {
                properties.offset = new File(fset);
            } else {
                properties.offset = new File(properties.getPath());
            }
            properties.show_hidden_files = show_hidden_files.isChecked();
            properties.error_dir = new File(properties.getPath());
            if (dialog != null) {
                dialog.setProperties(properties);
            }
        });
    }

    private void initFilePickerDialog() {
        Text showDialog = (Text) findComponentById(ResourceTable.Id_show_dialog);
        showDialog.setClickedListener(v -> {
            dialog = new FilePickerDialog(this, properties);
            dialog.setTitle("Select a File");
            dialog.setPositiveBtnName("SELECT");
            dialog.setNegativeBtnName("CANCEL");


            dialog.setProperties(properties);


            try {
                dialog.onStart();
            } catch (IOException e) {
                e.printStackTrace();
            }
            dialog.show();

            dialog.setDialogSelectionListener((files) -> {
                int size = listItem.size();
                listItem.clear();
                mFileListAdapter.notifyDataSetItemRangeRemoved(0, size);
                for (String path : files) {
                    File file = new File(path);
                    ListItem item = new ListItem();
                    item.setName(file.getName());
                    try {
                        item.setPath(file.getCanonicalPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    listItem.add(item);
                }
                mFileListAdapter.notifyDataSetItemRangeInserted(0, listItem.size());
                fileList.setHeight(listItem.size() * 260);
            });
        });
    }

    private int countCommas(String fextension) {
        int count = 0;
        for (char ch : fextension.toCharArray()) {
            if (ch == ',') count++;
        }
        return count;
    }

    @Override
    protected void onBackPressed() {
        if (dialog != null){
            if (!dialog.isShowing()) {
                super.onBackPressed();
            }
            dialog.destroy();
        }
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}