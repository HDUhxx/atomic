package me.panavtec.dialog;

import me.panavtec.title.ResourceTable;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.agp.window.dialog.CommonDialog;
import ohos.app.Context;

public class AlertDialog extends CommonDialog
{
    private final Component mComponentDlg;
    private final Context mContext;
    private Component.ClickedListener mRightButtonListener;
    private Component.ClickedListener mLeftButtonListener;


    public AlertDialog(final Context context)
    {
        super(context);
        mContext = context;
        mComponentDlg = LayoutScatter.getInstance(context)
                .parse(ResourceTable.Layout_layout_alert_dlg,null, false);
        setContentCustomComponent(mComponentDlg);
        setAutoClosable(true);

        mComponentDlg.findComponentById(ResourceTable.Id_alert_dlg_left_button).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(mLeftButtonListener != null) mLeftButtonListener.onClick(component);
                AlertDialog.this.hide();
            }
        });

        mComponentDlg.findComponentById(ResourceTable.Id_alert_dlg_right_button).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(mRightButtonListener != null) mRightButtonListener.onClick(component);
                AlertDialog.this.hide();
            }
        });
        
    }

    public AlertDialog setTitle(int msgId) {
        Text text = (Text) mComponentDlg.findComponentById(ResourceTable.Id_alert_dlg_title);
        text.setText(msgId);
        return this;
    }


    public AlertDialog setTitle(String msgStr) {
        Text text = (Text) mComponentDlg.findComponentById(ResourceTable.Id_alert_dlg_title);
        text.setText(msgStr);
        return this;
    }

    public AlertDialog setMessage(String string) {
        Text text = (Text) mComponentDlg.findComponentById(ResourceTable.Id_alert_dlg_message);
        text.setText(string);
        return this;
    }
    
    public AlertDialog setMessage(int stringId) {
        Text text = (Text) mComponentDlg.findComponentById(ResourceTable.Id_alert_dlg_message);
        text.setText(stringId);
        return this;
    }

    public AlertDialog setNegativeButton(int string, Component.ClickedListener listener) {
        Button button = (Button) mComponentDlg.findComponentById(ResourceTable.Id_alert_dlg_left_button);
        button.setText(mContext.getString(string));
        mLeftButtonListener = listener;
        return this;
    }

    public AlertDialog setPositiveButton(int string, Component.ClickedListener listener) {
        Button button = (Button) mComponentDlg.findComponentById(ResourceTable.Id_alert_dlg_right_button);
        button.setText(mContext.getString(string));
        mRightButtonListener = listener;
        return this;
    }
    
}
