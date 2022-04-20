package com.developer.filepicker.view;

import com.developer.filepicker.ResourceTable;
import com.developer.filepicker.controller.DialogSelectionListener;
import com.developer.filepicker.controller.NotifyItemChecked;
import com.developer.filepicker.controller.adapters.FileListAdapter;
import com.developer.filepicker.model.DialogConfigs;
import com.developer.filepicker.model.DialogProperties;
import com.developer.filepicker.model.FileListItem;
import com.developer.filepicker.model.MarkedItemList;
import com.developer.filepicker.utils.ExtensionFilter;
import com.developer.filepicker.utils.Utility;
import com.developer.filepicker.widget.MaterialCheckbox;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.ListContainer;
import ohos.agp.components.Text;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.dialog.ToastDialog;
import ohos.agp.window.service.DisplayAttributes;
import ohos.agp.window.service.DisplayManager;
import ohos.app.Context;
import ohos.bundle.IBundleManager;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author akshay sunil masram
 */
@SuppressWarnings("unused")
public class FilePickerDialog extends CommonDialog implements ListContainer.ItemClickedListener {

    private static final int WIDTH = 320;
    private static final int HEIGHT = 600;
    private static final int RADIUS = 5;
    public static final int EXTERNAL_READ_PERMISSION_GRANT = 112;
    private final Context context;
    private ListContainer listView;
    private Text dname, dir_path, title;
    private DialogProperties properties;
    private DialogSelectionListener callbacks;
    private ArrayList<FileListItem> internalList;
    private ExtensionFilter filter;
    private FileListAdapter mFileListAdapter;
    private Button select;
    private String titleStr = null;
    private String positiveBtnNameStr = null;
    private String negativeBtnNameStr = null;

    public FilePickerDialog(Context context) {
        super(context);
        this.context = context;
        properties = new DialogProperties(context);
        filter = new ExtensionFilter(properties);
        internalList = new ArrayList<>();
        initView();
    }

    public FilePickerDialog(Context context, DialogProperties properties) {
        super(context);
        this.context = context;
        this.properties = properties;
        filter = new ExtensionFilter(properties);
        internalList = new ArrayList<>();
        initView();
    }

    private void initView() {
        Component component = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_dialog_main,
                null, false);
        setContentCustomComponent(component);
        setAutoClosable(false);
        setSize(vp2px(context, WIDTH), vp2px(context, HEIGHT));
        setAlignment(LayoutAlignment.CENTER);
        setCornerRadius(vp2px(context, RADIUS));
        listView = (ListContainer) component.findComponentById(ResourceTable.Id_fileList);
        select = (Button) component.findComponentById(ResourceTable.Id_select);
        int size = MarkedItemList.getFileCount();
        if (size == 0) {
            select.setEnabled(false);
            try {
                select.setTextColor(new Color(context.getResourceManager().getElement(ResourceTable.Color_bt_pink).getColor()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        dname = (Text) component.findComponentById(ResourceTable.Id_dname);
        title = (Text) component.findComponentById(ResourceTable.Id_title);
        dir_path = (Text) component.findComponentById(ResourceTable.Id_dir_path);
        Button cancel = (Button) component.findComponentById(ResourceTable.Id_cancel);
        if (negativeBtnNameStr != null) {
            cancel.setText(negativeBtnNameStr);
        }
        select.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                String[] paths = MarkedItemList.getSelectedPaths();
                if (callbacks != null) {
                    callbacks.onSelectedFilePaths(paths);
                }
                hide();
            }
        });
        cancel.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                hide();
            }
        });
        mFileListAdapter = new FileListAdapter(internalList, context, properties);
        mFileListAdapter.setNotifyItemCheckedListener(new NotifyItemChecked() {
            @Override
            public void notifyCheckBoxIsClicked() {
                positiveBtnNameStr = positiveBtnNameStr == null ?
                        "Select" : positiveBtnNameStr;
                int size = MarkedItemList.getFileCount();
                if (size == 0) {
                    select.setEnabled(false);
                    try {
                        select.setTextColor(new Color(context.getResourceManager().getElement(ResourceTable.Color_bt_pink).getColor()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    select.setText(positiveBtnNameStr);
                } else {
                    select.setEnabled(true);
                    try {
                        select.setTextColor(new Color(context.getResourceManager().getElement(ResourceTable.Color_bt_red).getColor()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String button_label = positiveBtnNameStr + " (" + size + ") ";
                    select.setText(button_label);
                }
                if (properties.selection_mode == DialogConfigs.SINGLE_MODE) {
                    mFileListAdapter.notifyDataChanged();
                }
            }
        });
        listView.setItemProvider(mFileListAdapter);
        setTitle();
    }

    public void setTitle(CharSequence titleStr) {
        if (titleStr != null) {
            this.titleStr = titleStr.toString();
        } else {
            this.titleStr = null;
        }
        setTitle();
    }

    private void setTitle() {
        if (title == null || dname == null) {
            return;
        }
        if (titleStr != null) {
            if (title.getVisibility() == Component.INVISIBLE) {
                title.setVisibility(Component.VISIBLE);
            }
            title.setText(titleStr);
            if (dname.getVisibility() == Component.VISIBLE) {
                dname.setVisibility(Component.INVISIBLE);
            }
        } else {
            if (title.getVisibility() == Component.VISIBLE) {
                title.setVisibility(Component.INVISIBLE);
            }
            if (dname.getVisibility() == Component.INVISIBLE) {
                dname.setVisibility(Component.VISIBLE);
            }
        }
    }

    public void onStart() throws IOException {
        positiveBtnNameStr = (
                positiveBtnNameStr == null ?
                        "Select" :
                        positiveBtnNameStr
        );
        select.setText(positiveBtnNameStr);
        if (Utility.checkStorageAccessPermissions(context)) {
            File currLoc;
            internalList.clear();
            if (properties.offset.isDirectory() && validateOffsetPath()) {
                currLoc = new File(properties.offset.getCanonicalPath());
                FileListItem parent = new FileListItem();
                parent.setFilename("..");
                parent.setDirectory(true);
                parent.setLocation(Objects.requireNonNull(currLoc.getParentFile())
                        .getCanonicalPath());
                parent.setTime(currLoc.lastModified());
                internalList.add(parent);
            } else if (properties.root.exists() && properties.root.isDirectory()) {
                currLoc = new File(properties.root.getCanonicalPath());
            } else {
                currLoc = new File(properties.error_dir.getCanonicalPath());
            }
            dname.setText(currLoc.getName());
            dir_path.setText(currLoc.getCanonicalPath());

            setTitle();
            internalList = Utility.prepareFileListEntries(internalList, currLoc, filter,
                    properties.show_hidden_files);
            listView.setItemProvider(mFileListAdapter);
            mFileListAdapter.setData(internalList);
            listView.setItemClickedListener(this);
        }
    }

    private boolean validateOffsetPath() throws IOException {
        String offset_path = properties.offset.getCanonicalPath();
        String root_path = properties.root.getCanonicalPath();
        return !offset_path.equals(root_path) && offset_path.contains(root_path);
    }

    @Override
    public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {
        if (internalList.size() > i) {
            FileListItem fitem = internalList.get(i);
            if (fitem.isDirectory()) {
                if (new File(fitem.getLocation()).canRead()) {
                    File currLoc = new File(fitem.getLocation());
                    dname.setText(currLoc.getName());
                    setTitle();
                    try {
                        dir_path.setText(currLoc.getCanonicalPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    internalList.clear();
                    if (!currLoc.getName().equals(properties.root.getName())) {
                        FileListItem parent = new FileListItem();
                        parent.setFilename("..");
                        parent.setDirectory(true);
                        try {
                            parent.setLocation(Objects.requireNonNull(currLoc
                                    .getParentFile()).getCanonicalPath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        parent.setTime(currLoc.lastModified());
                        internalList.add(parent);
                    }
                    internalList = Utility.prepareFileListEntries(internalList, currLoc, filter,
                            properties.show_hidden_files);
                    mFileListAdapter.notifyDataChanged();
                } else {
                    new ToastDialog(context).setText("Directory cannot be accessed").show();
                }
            } else {
                MaterialCheckbox fmark = (MaterialCheckbox) component.findComponentById(ResourceTable.Id_file_mark);
                fmark.simulateClick();
            }
        }
    }

    public DialogProperties getProperties() {
        return properties;
    }

    public void setProperties(DialogProperties properties) {
        this.properties = properties;
        filter = new ExtensionFilter(properties);
    }

    public void setDialogSelectionListener(DialogSelectionListener callbacks) {
        this.callbacks = callbacks;
    }

    public void setPositiveBtnName(CharSequence positiveBtnNameStr) {
        if (positiveBtnNameStr != null) {
            this.positiveBtnNameStr = positiveBtnNameStr.toString();
        } else {
            this.positiveBtnNameStr = null;
        }
    }

    public void setNegativeBtnName(CharSequence negativeBtnNameStr) {
        if (negativeBtnNameStr != null) {
            this.negativeBtnNameStr = negativeBtnNameStr.toString();
        } else {
            this.negativeBtnNameStr = null;
        }
    }

    public void markFiles(List<String> paths) throws IOException {
        if (paths != null && paths.size() > 0) {
            if (properties.selection_mode == DialogConfigs.SINGLE_MODE) {
                File temp = new File(paths.get(0));
                switch (properties.selection_type) {
                    case DialogConfigs.DIR_SELECT:
                        if (temp.exists() && temp.isDirectory()) {
                            FileListItem item = new FileListItem();
                            item.setFilename(temp.getName());
                            item.setDirectory(temp.isDirectory());
                            item.setMarked(true);
                            item.setTime(temp.lastModified());
                            item.setLocation(temp.getCanonicalPath());
                            MarkedItemList.addSelectedItem(item);
                        }
                        break;

                    case DialogConfigs.FILE_SELECT:
                        if (temp.exists() && temp.isFile()) {
                            FileListItem item = new FileListItem();
                            item.setFilename(temp.getName());
                            item.setDirectory(temp.isDirectory());
                            item.setMarked(true);
                            item.setTime(temp.lastModified());
                            item.setLocation(temp.getCanonicalPath());
                            MarkedItemList.addSelectedItem(item);
                        }
                        break;

                    case DialogConfigs.FILE_AND_DIR_SELECT:
                        if (temp.exists()) {
                            FileListItem item = new FileListItem();
                            item.setFilename(temp.getName());
                            item.setDirectory(temp.isDirectory());
                            item.setMarked(true);
                            item.setTime(temp.lastModified());
                            item.setLocation(temp.getCanonicalPath());
                            MarkedItemList.addSelectedItem(item);
                        }
                        break;
                }
            } else {
                for (String path : paths) {
                    switch (properties.selection_type) {
                        case DialogConfigs.DIR_SELECT:
                            File temp = new File(path);
                            if (temp.exists() && temp.isDirectory()) {
                                FileListItem item = new FileListItem();
                                item.setFilename(temp.getName());
                                item.setDirectory(temp.isDirectory());
                                item.setMarked(true);
                                item.setTime(temp.lastModified());
                                item.setLocation(temp.getCanonicalPath());
                                MarkedItemList.addSelectedItem(item);
                            }
                            break;
                        case DialogConfigs.FILE_SELECT:
                            temp = new File(path);
                            if (temp.exists() && temp.isFile()) {
                                FileListItem item = new FileListItem();
                                item.setFilename(temp.getName());
                                item.setDirectory(temp.isDirectory());
                                item.setMarked(true);
                                item.setTime(temp.lastModified());
                                item.setLocation(temp.getCanonicalPath());
                                MarkedItemList.addSelectedItem(item);
                            }
                            break;

                        case DialogConfigs.FILE_AND_DIR_SELECT:
                            temp = new File(path);
                            if (temp.exists() && (temp.isFile() || temp.isDirectory())) {
                                FileListItem item = new FileListItem();
                                item.setFilename(temp.getName());
                                item.setDirectory(temp.isDirectory());
                                item.setMarked(true);
                                item.setTime(temp.lastModified());
                                item.setLocation(temp.getCanonicalPath());
                                MarkedItemList.addSelectedItem(item);
                            }
                            break;
                    }
                }
            }
        }
    }

    @Override
    public void show() {
        if (!Utility.checkStorageAccessPermissions(context)) {
            // 应用未被授予权限
            if (context.canRequestPermission("ohos.permission.WRITE_MEDIA")) {
                // 是否可以申请弹框授权(首次申请或者用户未选择禁止且不再提示)
                context.requestPermissionsFromUser(
                        new String[]{"ohos.permission.WRITE_MEDIA"}, 1);
            }
        } else {
            super.show();
            positiveBtnNameStr = positiveBtnNameStr == null ?
                    "Select" : positiveBtnNameStr;
            select.setText(positiveBtnNameStr);
            int size = MarkedItemList.getFileCount();
            if (size == 0) {
                select.setText(positiveBtnNameStr);
            } else {
                String button_label = positiveBtnNameStr + " (" + size + ") ";
                select.setText(button_label);
            }
        }
    }

    @Override
    public void hide() {
        super.hide();
        MarkedItemList.clearSelectionList();
        internalList.clear();
    }

    @Override
    public void destroy() {
        // currentDirName is dependent on dname
        String currentDirName = dname.getText().toString();
        if (internalList.size() > 0) {
            FileListItem fitem = internalList.get(0);
            File currLoc = new File(fitem.getLocation());
            if (currentDirName.equals(properties.root.getName()) ||
                    !currLoc.canRead()) {

                MarkedItemList.clearSelectionList();
                internalList.clear();
                super.destroy();

            } else {
                dname.setText(currLoc.getName());
                try {
                    dir_path.setText(currLoc.getCanonicalPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                internalList.clear();
                if (!currLoc.getName().equals(properties.root.getName())) {
                    FileListItem parent = new FileListItem();
                    parent.setFilename("..");
                    parent.setDirectory(true);
                    try {
                        parent.setLocation(Objects.requireNonNull(currLoc.getParentFile())
                                .getCanonicalPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    parent.setTime(currLoc.lastModified());
                    internalList.add(parent);
                }
                internalList = Utility.prepareFileListEntries(internalList, currLoc, filter,
                        properties.show_hidden_files);
                mFileListAdapter.notifyDataChanged();
            }
            setTitle();
        } else {
            MarkedItemList.clearSelectionList();
            internalList.clear();
            super.destroy();
        }
    }

    private int vp2px(Context context, float vp) {
        DisplayAttributes attributes = DisplayManager.getInstance().getDefaultDisplay(context).get().getAttributes();
        return (int) (attributes.densityPixels * vp);
    }
}
