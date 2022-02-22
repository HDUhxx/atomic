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

package com.example.doodle.dialog;

import com.example.doodle.ResourceTable;
import com.example.doodle.imagepicker.ImageSelectorView;
import com.example.doodle.util.DrawUtil;
import com.example.doodle.util.KeyboardUtils;
import com.example.doodle.util.StatusBarUtil;

import ohos.aafwk.ability.Ability;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.agp.components.TextField;
import ohos.agp.utils.Color;
import ohos.agp.utils.TextTool;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.service.WindowManager;
import ohos.utils.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * fuqiangping
 *
 * @since 2021-04-29
 */
public class DialogController {
    private static Component view;

    private DialogController() {
    }

    /**
     * 显示dialog
     *
     * @param activity
     * @param title
     * @param msg
     * @param enterClick
     * @param cancelClick
     * @return dialog
     */
    public static CommonDialog showEnterCancelDialog(Ability activity, String title,
                                                     String msg, final Component.ClickedListener enterClick,
                                                     final Component.ClickedListener cancelClick) {
        return showMsgDialog(activity, title, msg, activity.getString(ResourceTable.String_doodle_cancel),
            activity.getString(ResourceTable.String_doodle_enter), enterClick, cancelClick);
    }

    /**
     * 显示dialog
     *
     * @param activity
     * @param title
     * @param msg
     * @param btn01
     * @param btn02
     * @param enterClick
     * @param cancelClick
     * @return dialog
     */
    public static CommonDialog showEnterDialog(Ability activity, String title, String msg,
                                               String btn01, String btn02, final Component.ClickedListener enterClick,
                                               final Component.ClickedListener cancelClick) {
        return showMsgDialog(activity, title, msg, null,
            activity.getString(ResourceTable.String_doodle_enter), enterClick, null);
    }

    /**
     * 显示dialog
     *
     * @param ability
     * @param title
     * @param msg
     * @param btn01
     * @param btn02
     * @param enterClick
     * @param cancelClick
     * @return dialog
     */
    public static CommonDialog showMsgDialog(Ability ability, String title, String msg, String btn01,
                                             String btn02, final Component.ClickedListener enterClick,
                                             final Component.ClickedListener cancelClick) {
        final CommonDialog dialog = getDialog(ability);
        dialog.getWindow().setInputPanelDisplayType(WindowManager.LayoutConfig.INPUT_ADJUST_RESIZE);
        StatusBarUtil.setStatusBarTranslucent(dialog.getWindow(), true, false);
        dialog.siteRemovable(true);
        dialog.setSwipeToDismiss(true); // 退出DIalog的方式目前只提供了此api 暂替代setCanceledOnTouchOutside
        dialog.setTransparent(true);

        final CommonDialog finalDialog = dialog;
        view = LayoutScatter.getInstance(ability).parse(ResourceTable.Layout_doodle_dialog, null, true);
        dialog.setContentCustomComponent(view);
        checkMsg(title, msg, btn01, btn02);
        Component.ClickedListener clickedListener = new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if (component.getId() == ResourceTable.Id_dialog_bg) {
                    finalDialog.destroy();
                } else if (component.getId() == ResourceTable.Id_dialog_enter_btn_02) {
                    finalDialog.destroy();
                    if (enterClick != null) {
                        enterClick.onClick(component);
                    }
                } else if (component.getId() == ResourceTable.Id_dialog_enter_btn_01) {
                    finalDialog.destroy();
                    if (cancelClick != null) {
                        cancelClick.onClick(component);
                    }
                }
            }
        };
        view.findComponentById(ResourceTable.Id_dialog_bg).setClickedListener(clickedListener);
        view.findComponentById(ResourceTable.Id_dialog_enter_btn_01).setClickedListener(clickedListener);
        view.findComponentById(ResourceTable.Id_dialog_enter_btn_02).setClickedListener(clickedListener);
        dialog.show();
        return dialog;
    }

    private static void checkMsg(String title, String msg, String btn01, String btn02) {
        if (TextTool.isNullOrEmpty(title)) {
            view.findComponentById(ResourceTable.Id_dialog_title).setVisibility(Component.HIDE);
            view.findComponentById(ResourceTable.Id_dialog_list_title_divider).setVisibility(Component.HIDE);
        } else {
            Text text = (Text) view.findComponentById(ResourceTable.Id_dialog_title);
            text.setText(title);
        }

        if (TextTool.isNullOrEmpty(msg)) {
            view.findComponentById(ResourceTable.Id_dialog_enter_msg).setVisibility(Component.HIDE);
        } else {
            Text titleView = (Text) view.findComponentById(ResourceTable.Id_dialog_enter_msg);
            titleView.setText(msg);
        }

        if (TextTool.isNullOrEmpty(btn01)) {
            view.findComponentById(ResourceTable.Id_dialog_enter_btn_01).setVisibility(Component.HIDE);
        } else {
            Text text = (Text) view.findComponentById(ResourceTable.Id_dialog_enter_btn_01);
            text.setText(btn01);
        }

        if (TextTool.isNullOrEmpty(btn02)) {
            view.findComponentById(ResourceTable.Id_dialog_enter_btn_02).setVisibility(Component.HIDE);
        } else {
            Text text = (Text) view.findComponentById(ResourceTable.Id_dialog_enter_btn_02);
            text.setText(btn02);
        }
    }

    /**
     * 显示dialog
     *
     * @param ability
     * @param text
     * @param enterClick
     * @param cancelClick
     * @return dialog
     */
    public static List<Object> showInputTextDialog(Ability ability, final String text,
                                                   final Component.ClickedListener enterClick,
                                                   final Component.ClickedListener cancelClick) {
        final int enterBtnColor1 = 0xffb3b3b3;
        final int enterBtnColor2 = 0xff232323;
        boolean isFullScreen = (ability.getWindow().getLayoutConfig().get()
            .flags & WindowManager.LayoutConfig.MARK_FULL_SCREEN) != 0;
        final CommonDialog dialog = getDialog(ability);
        List<Object> componentlist = new ArrayList<>();
        dialog.getWindow().setInputPanelDisplayType(WindowManager.LayoutConfig.INPUT_ADJUST_RESIZE); // 待验证
        dialog.setTransparent(true);
        ComponentContainer container = (ComponentContainer) LayoutScatter.getInstance(ability)
            .parse(ResourceTable.Layout_doodle_create_text, null, true);
        dialog.setContentCustomComponent(container);

        if (isFullScreen) {
            DrawUtil.assistActivity(dialog.getWindow());
        }
        final TextField textField = (TextField) container.findComponentById(ResourceTable.Id_doodle_selectable_edit);
        final Component cancelBtn = container.findComponentById(ResourceTable.Id_doodle_text_cancel_btn);
        final Text enterBtn = (Text) container.findComponentById(ResourceTable.Id_doodle_text_enter_btn);
        final DirectionalLayout dirbottom = (DirectionalLayout) container
            .findComponentById(ResourceTable.Id_doodle_dir_bottom);
        container.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                textField.clearFocus();
                KeyboardUtils.hideSoftInput();
                dialog.destroy();
            }
        });
        textField.addTextObserver(new Text.TextObserver() {
            @Override
            public void onTextUpdated(String s, int start, int before, int count) {
                String text = (textField.getText() + "").trim();
                if (text == null || text.length() == 0) {
                    enterBtn.setEnabled(false);
                    enterBtn.setTextColor(new Color(enterBtnColor1));
                } else {
                    enterBtn.setEnabled(true);
                    enterBtn.setTextColor(new Color(enterBtnColor2));
                }
            }
        });
        textField.setText(text == null ? "" : text);
        cancelBtn.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                textField.clearFocus();
                KeyboardUtils.hideSoftInput();
                dialog.destroy();
                if (cancelClick != null) {
                    cancelClick.onClick(cancelBtn);
                }
            }
        });
        enterBtn.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                textField.clearFocus();
                KeyboardUtils.hideSoftInput();
                dialog.destroy();
                if (enterClick != null) {
                    enterBtn.setTag((textField.getText() + "").trim());
                    enterClick.onClick(enterBtn);
                }
            }
        });
        dialog.show();
        componentlist.add(textField);
        componentlist.add(dialog);
        componentlist.add(dirbottom);
        return componentlist;
    }

    /**
     * 显示dialog
     *
     * @param ability
     * @param listener
     * @return dialog
     */
    public static CommonDialog showSelectImageDialog(Ability ability,
                                                     final ImageSelectorView.ImageSelectorListener listener) {
        final int columnCount = 4;
        final CommonDialog dialog = getDialog(ability);
        dialog.getWindow().setInputPanelDisplayType(WindowManager.LayoutConfig.INPUT_ADJUST_RESIZE);
        dialog.setTransparent(true);
        ComponentContainer container = (ComponentContainer) LayoutScatter.getInstance(ability)
            .parse(ResourceTable.Layout_doodle_create_bitmap, null, true);
        dialog.setContentCustomComponent(container);
        container.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                dialog.destroy();
            }
        });
        ComponentContainer selectorContainer = (ComponentContainer) container
            .findComponentById(ResourceTable.Id_doodle_image_selector_container);
        ImageSelectorView selectorView = new ImageSelectorView(ability, false, 1, null,
            new ImageSelectorView.ImageSelectorListener() {
                @Override
                public void onCancel() {
                    dialog.destroy();
                    if (listener != null) {
                        listener.onCancel();
                    }
                }

                @Override
                public void onEnter(Uri uri) {
                    dialog.destroy();
                    if (listener != null) {
                        listener.onEnter(uri);
                    }
                }
            });
        selectorView.setColumnCount(columnCount);
        selectorContainer.addComponent(selectorView);
        dialog.show();
        return dialog;
    }

    private static CommonDialog getDialog(Ability ability) {
        // 判断是否全屏的方法？
//        boolean fullScreen = (ability.getWindow().getLayoutConfig().get()
//            .flags & WindowManager.LayoutConfig.MARK_FULL_SCREEN) != 0;
//        int flags = ability.getWindow().getLayoutConfig().get().flags;
        CommonDialog dialog = null;
        // 需处理Dialog全屏和非全屏的主题设置
        dialog = new CommonDialog(ability);
        return dialog;
    }
}

