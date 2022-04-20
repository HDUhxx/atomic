package me.panavtec.dialog;

import com.ohos.trebleshot.object.NetworkDevice;
import me.panavtec.title.ResourceTable;
import me.panavtec.title.hminterface.DeviceActionInterface;
import ohos.agp.components.Component;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.agp.window.dialog.CommonDialog;
import ohos.app.Context;

public class DeviceSetDialog extends CommonDialog
{
    private final Component mComponentDlg;
    private Component.ClickedListener mRightButtonListener;
    private Component.ClickedListener mLeftButtonListener;
    private DeviceActionInterface actionInterface;

    public void setActionInterface(DeviceActionInterface actionInterface) {
        this.actionInterface = actionInterface;
    }

    public DeviceSetDialog(final Context context, NetworkDevice device)
    {
        super(context);

        mComponentDlg = LayoutScatter.getInstance(context)
                .parse(ResourceTable.Layout_dialog_setinfo,null, false);
        Text devName = (Text) mComponentDlg.findComponentById(ResourceTable.Id_dev_name);
        Text devNickName = (Text) mComponentDlg.findComponentById(ResourceTable.Id_dev_nick_name);
        Text devVersion = (Text) mComponentDlg.findComponentById(ResourceTable.Id_dev_version);
        devName.setText(device.nickname);
        devNickName.setText(device.brand+" "+device.model);
        devVersion.setText(device.versionName);
        setContentCustomComponent(mComponentDlg);
        setAutoClosable(true);

        mComponentDlg.findComponentById(ResourceTable.Id_dev_update).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                actionInterface.update(device);
                DeviceSetDialog.this.hide();
            }
        });

        mComponentDlg.findComponentById(ResourceTable.Id_dev_remove).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                actionInterface.remove(device);
                DeviceSetDialog.this.hide();
            }
        });
        mComponentDlg.findComponentById(ResourceTable.Id_dev_close).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                actionInterface.close(device);
                DeviceSetDialog.this.hide();
            }
        });

    }

    public DeviceSetDialog setTitle(int msgId) {
        Text text = (Text) mComponentDlg.findComponentById(ResourceTable.Id_alert_dlg_title);
        text.setText(msgId);
        return this;
    }

    public DeviceSetDialog setMessage(String string) {
        Text text = (Text) mComponentDlg.findComponentById(ResourceTable.Id_alert_dlg_message);
        text.setText(string);
        return this;
    }

    public DeviceSetDialog setMessage(int stringId) {
        Text text = (Text) mComponentDlg.findComponentById(ResourceTable.Id_alert_dlg_message);
        text.setText(stringId);
        return this;
    }

    public DeviceSetDialog setNegativeButton(int string, Component.ClickedListener listener) {
        mLeftButtonListener = listener;
        return this;
    }

    public DeviceSetDialog setPositiveButton(int string, Component.ClickedListener listener) {
        mRightButtonListener = listener;
        return this;
    }
}
