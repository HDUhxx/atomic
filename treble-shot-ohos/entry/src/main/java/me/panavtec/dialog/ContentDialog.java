package me.panavtec.dialog;

import me.panavtec.title.ResourceTable;
import me.panavtec.title.hmutils.TextUtils;
import ohos.agp.components.Component;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.CommonDialog;
import ohos.app.Context;

/**
 * created by: veli
 * date: 4/8/19 9:16 AM
 */
public class ContentDialog extends CommonDialog {
    private final Text teContent;
    final Text teCancel;
    final Text teNo;
    final Text teYes;

    public ContentDialog(Context context, Component.ClickedListener yesListener, Component.ClickedListener noListener) {
        super(context);
        Component component = LayoutScatter.getInstance(context)
                .parse(ResourceTable.Layout_layout_guide_dialog, null, false);
        setContentCustomComponent(component);


        Text title = (Text) component.findComponentById(ResourceTable.Id_teTitle);
        teContent = (Text) component.findComponentById(ResourceTable.Id_teContent);
        teCancel = (Text) component.findComponentById(ResourceTable.Id_teCancel);
        teNo = (Text) component.findComponentById(ResourceTable.Id_teNo);
        teYes = (Text) component.findComponentById(ResourceTable.Id_teYes);

        title.setText("连接向导");
        teContent.setText("你在另一个设备上刚配置好了吗？");
        teCancel.setText("取消");
        teNo.setText("否");
        teYes.setText("是");
        setAutoClosable(true);
        setTransparent(true);
        setAlignment(LayoutAlignment.HORIZONTAL_CENTER);
        teCancel.setClickedListener(component1 -> destroy());
        teYes.setClickedListener(component12 -> {
            yesListener.onClick(component12);
            destroy();
        });
        teNo.setClickedListener(component13 -> {
            noListener.onClick(component13);
            destroy();
        });
    }

    public ContentDialog setContent(String content) {
        if (!TextUtils.isEmpty(content)) teContent.setText(content);
        return this;
    }

    public ContentDialog setCancel(String cancel) {
        if (!TextUtils.isEmpty(cancel)) teCancel.setText(cancel);
        return this;
    }

    public ContentDialog setTeNo(String noStr) {
        if (!TextUtils.isEmpty(noStr)) teNo.setText(noStr);
        return this;
    }

    public ContentDialog setTeYes(String yesStr) {
        if (!TextUtils.isEmpty(yesStr)) teYes.setText(yesStr);
        return this;
    }

}
