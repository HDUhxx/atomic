package me.panavtec.dialog;

import me.panavtec.title.ResourceTable;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.LayoutScatter;
import ohos.agp.window.dialog.CommonDialog;
import ohos.app.Context;
import ohos.utils.net.Uri;

public class DevSurveyDialog extends CommonDialog
{
    public DevSurveyDialog(final Context context)
    {
        super(context);

        Component componentDlg = LayoutScatter.getInstance(context)
                .parse(ResourceTable.Layout_layout_dev_survey,null, false);
        setContentCustomComponent(componentDlg);
        setAutoClosable(true);

        componentDlg.findComponentById(ResourceTable.Id_butn_temp_later).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                DevSurveyDialog.this.hide();
            }
        });

        componentDlg.findComponentById(ResourceTable.Id_butn_temp_doIt).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                doIt(context);
                DevSurveyDialog.this.hide();
            }
        });
    }

    private void doIt(final Context context)
    {
        Intent intent = new Intent().setUri(Uri.parse(
                "https://docs.google.com/forms/d/e/1FAIpQLScmwX923MACmHvZTpEyZMDCxRQjrd8b67u9p9MOjV1qFVp-_A/viewform?usp=sf_link"));
//                             Intent.ACTION_VIEW
        context.startAbility(intent, 0);

    }
}
