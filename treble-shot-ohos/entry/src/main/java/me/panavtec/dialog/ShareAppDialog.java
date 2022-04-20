package me.panavtec.dialog;

import me.panavtec.config.AppConfig;
import me.panavtec.title.ResourceTable;
import me.panavtec.title.hmutils.FileUtils;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.LayoutScatter;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.utils.net.Uri;

import java.io.File;

public class ShareAppDialog extends CommonDialog
{
    public static final String EXTRA_FILENAME_LIST = "extraFileNames";

    public ShareAppDialog(final Context context)
    {
        super(context);

        Component componentDlg = LayoutScatter.getInstance(context)
                .parse(ResourceTable.Layout_layout_app_share,null, false);
        setContentCustomComponent(componentDlg);
        setAutoClosable(true);

        componentDlg.findComponentById(ResourceTable.Id_app_share_cancel).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                ShareAppDialog.this.hide();
            }
        });

        componentDlg.findComponentById(ResourceTable.Id_app_share_apk).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                shareAsApk(context);
                ShareAppDialog.this.hide();
            }
        });

        componentDlg.findComponentById(ResourceTable.Id_app_share_aslink).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                shareAsLink(context);
                ShareAppDialog.this.hide();
            }
        });

    }

    private void shareAsApk(final Context context)
    {
        new EventHandler(EventRunner.getMainEventRunner()).postTask(new Runnable()
        {
            @Override
            public void run()
            {
                try {
                    String fileName = "TrebleShot.hap";
                    File storageDirectory = FileUtils.getApplicationDirectory(context.getApplicationContext());
                    File codeFile = new File(context.getBundleCodePath());
                    String newFileName = FileUtils.getUniqueFileName(storageDirectory, fileName, true);
                    File cloneFileTarget = new File(storageDirectory, newFileName);
                    cloneFileTarget.createNewFile();
                    FileUtils.copy(context, codeFile, cloneFileTarget);

                    try {
//                        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.SEND");
                        intent.setType("application/octet-stream");
                        intent.setParam("android.intent.extra.SUBJECT", context.getString(ResourceTable.String_butn_share));
                        intent.setParam(EXTRA_FILENAME_LIST, newFileName);
                        intent.setParam("android.intent.extra.STREAM", Uri.getUriFromFile(cloneFileTarget));
                        intent.addFlags(Intent.FLAG_NOT_OHOS_COMPONENT);
                        context.startAbility(intent, 0);
                    } catch (IllegalArgumentException e) {
                        new ToastDialog(context).setText(context.getString(ResourceTable.String_mesg_providerNotAllowedError)).show();
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void shareAsLink(final Context context)
    {
        new EventHandler(EventRunner.getMainEventRunner()).postTask(new Runnable()
        {
            @Override
            public void run()
            {
                try {
                    String textToShare = context.getString(ResourceTable.String_text_linkTrebleshot,
                            AppConfig.URI_GOOGLE_PLAY);
//                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.SEND");
                    intent.setType("text/plain");
                    intent.setParam("android.intent.extra.SUBJECT", context.getString(ResourceTable.String_butn_share));
                    intent.setParam("android.intent.extra.TEXT", textToShare);
                    intent.addFlags(Intent.FLAG_NOT_OHOS_COMPONENT);
                    context.startAbility(intent,0);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
