package me.panavtec.dialog;

import me.panavtec.title.ResourceTable;
import ohos.agp.components.*;
import ohos.agp.window.dialog.CommonDialog;
import ohos.app.Context;

public class EditTextPreferenceDialog extends CommonDialog implements Component.ClickedListener {
    final Text title;
    final TextField mTextField;
    final Button confirm;
    final Button cancel;
    OnDialogClickedListener onDialogClickedListener;
    public EditTextPreferenceDialog(Context context) {
        super(context);
        Component componentDlg = LayoutScatter.getInstance(context)
                .parse(ResourceTable.Layout_dialog_edit_text_preference,null, false);
        setContentCustomComponent(componentDlg);
        setAutoClosable(true);

        this.title = (Text) componentDlg.findComponentById(ResourceTable.Id_title);
        this.mTextField = (TextField) componentDlg.findComponentById(ResourceTable.Id_edit_text);
        this.confirm = (Button) componentDlg.findComponentById(ResourceTable.Id_confirm);
        this.confirm.setClickedListener(this);
        this.cancel = (Button) componentDlg.findComponentById(ResourceTable.Id_cancel);
        this.cancel.setClickedListener(this);
    }

    public EditTextPreferenceDialog setTitle(String title){
        this.title.setText(title);
        return this;
    }
    public EditTextPreferenceDialog setDefaultContent(String content){
        this.mTextField.setText(content);
        return this;
    }

    public EditTextPreferenceDialog setOnDialogClicked(OnDialogClickedListener onDialogClicked){
        this.onDialogClickedListener = onDialogClicked;
        return this;
    }

    @Override
    public void onClick(Component component) {
        switch (component.getId()){
            case ResourceTable.Id_confirm:
                if(onDialogClickedListener!=null)
                    onDialogClickedListener.onConfirm(mTextField.getText());
                break;
            case ResourceTable.Id_cancel:
                    if(onDialogClickedListener!=null)
                        onDialogClickedListener.onCancel();
                break;
        }
        hide();
    }

   public interface OnDialogClickedListener{
        void onConfirm(String content);
        void onCancel();
   }
}
