package me.panavtec.title.slice;

import com.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import com.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import com.ohos.trebleshot.config.Keyword;
import com.ohos.trebleshot.coolsocket.CoolSocket;
import com.ohos.trebleshot.object.NetworkDevice;
import com.ohos.trebleshot.object.TextStreamObject;
import com.ohos.trebleshot.service.WorkerService;
import com.ohos.trebleshot.ui.UIConnectionUtils;
import com.ohos.trebleshot.utils.AppUtils;
import com.ohos.trebleshot.utils.CommunicationBridge;
import me.panavtec.title.ConnectionManagerActivity;
import me.panavtec.title.ResourceTable;
import me.panavtec.title.hmutils.HMUtils;
import me.panavtec.title.hmutils.TextUtils;
import me.panavtec.title.hmutils.Toast;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.*;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.dialog.ToastDialog;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.media.image.PixelMap;
import ohos.miscservices.pasteboard.PasteData;
import ohos.miscservices.pasteboard.SystemPasteboard;
import org.json.JSONObject;

import static ohos.agp.components.Component.*;

public class TextEditorAbilitySlice extends BaseSlice {

    public static final String ACTION_EDIT_TEXT = "genonbeta.intent.action.EDIT_TEXT";
    public static final String EXTRA_SUPPORT_APPLY = "extraSupportApply";
    public static final String EXTRA_TEXT_INDEX = "extraText";
    public static final String EXTRA_CLIPBOARD_ID = "clipboardId";

    private TextStreamObject mTextStreamObject;
    private TextField mInputCtrl;


    public static final int MENU_ACTION_SAVE = 0;
    public static final int MENU_ACTION_SHARE = 1;
    public static final int MENU_ACTION_REMOVE = 2;
    public static final int MENU_ACTION_SHOW_AS_QR_CODE = 3;
    public static final int MENU_ACTION_SHARE_TREBLESHOT = 4;

    public static final int REQUEST_CODE_CHOOSE_DEVICE = 0;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_layout_text_editor_activity);
        initView(intent);
    }

    private void initView(Intent intent) {
        setBack();
        mInputCtrl = (TextField) findComponentById(ResourceTable.Id_layout_text_editor_activity_text_text_box);
        findComponentById(ResourceTable.Id_ima_more).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                showActionDialog();
            }
        });

        findComponentById(ResourceTable.Id_ima_send).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                jumpAbility(ConnectionManagerActivity.class);
            }
        });


        if (intent.hasParameter(EXTRA_CLIPBOARD_ID)) {
            try {
                 mTextStreamObject = new TextStreamObject(intent.getLongParam(EXTRA_CLIPBOARD_ID, -1));
                AppUtils.getDatabase(this).reconstruct(mTextStreamObject);
                mInputCtrl.setText(mTextStreamObject.text);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void jumpAbility(Class<?> tclass) {
        Intent intent = new Intent();
        Operation build = new Intent.OperationBuilder()
                .withDeviceId("")
                .withAbilityName(tclass)
                .withBundleName(getBundleName())
                .build();
        intent.setOperation(build);
        startAbilityForResult(intent, 1);
    }


    @Override
    protected void onAbilityResult(int requestCode, int resultCode, Intent resultData) {
        super.onAbilityResult(requestCode, resultCode, resultData);
        if (resultCode == 0) {
            if (requestCode == 1
                    && resultData != null
                    && resultData.hasParameter(ConnectionManagerActivity.EXTRA_DEVICE_ID)
                    && resultData.hasParameter(ConnectionManagerActivity.EXTRA_CONNECTION_ADAPTER)) {
                String deviceId = resultData.getStringParam(ConnectionManagerActivity.EXTRA_DEVICE_ID);
                String connectionAdapter = resultData.getStringParam(ConnectionManagerActivity.EXTRA_CONNECTION_ADAPTER);

                try {
                    NetworkDevice networkDevice = new NetworkDevice(deviceId);
                    NetworkDevice.Connection connection = new NetworkDevice.Connection(deviceId, connectionAdapter);

                    AppUtils.getDatabase(this).reconstruct(networkDevice);
                    AppUtils.getDatabase(this).reconstruct(connection);

                    doCommunicate(networkDevice, connection);
                } catch (Exception e) {
                    Toast.show(this, ResourceTable.String_mesg_somethingWentWrong, Toast.LENGTH_SHORT);
                }
            }
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

    //save Text to DB.
    public void saveText() {
        if (mTextStreamObject == null)
            mTextStreamObject = new TextStreamObject(AppUtils.getUniqueNumber());

        if (mTextStreamObject.date == 0)
            mTextStreamObject.date = System.currentTimeMillis();

        mTextStreamObject.text = mInputCtrl.getText();
        AppUtils.getDatabase(this).publish(mTextStreamObject);
    }


    public boolean onOptionsItemSelected(int actionId) {
        if (actionId == MENU_ACTION_SAVE) {
            saveText();
//            Snackbar.make(findViewById(android.R.id.content), R.string.mesg_textStreamSaved, Snackbar.LENGTH_LONG).show();
//        } else if (actionId == R.id.menu_action_done) {
//            Intent intent = new Intent()
//                    .putExtra(EXTRA_TEXT_INDEX, mInputCtrl.getText().toString());
//
//            setResult(RESULT_OK, intent);
//            finish();
//        } else if (actionId == R.id.menu_action_copy) {
//            ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE))
//                    .setPrimaryClip(ClipData.newPlainText("copiedText", mInputCtrl.getText().toString()));
//
//            createSnackbar(R.string.mesg_textCopiedToClipboard).show();
        } else if (actionId == MENU_ACTION_SHARE) {
            Intent shareIntent = new Intent();
            shareIntent.setAction("android.intent.action.SEND");
            shareIntent.setType("text/plain");
            shareIntent.setParam("android.intent.extra.SUBJECT", getString(ResourceTable.String_butn_share));
            shareIntent.setParam("android.intent.extra.TEXT", mInputCtrl.getText());
            shareIntent.addFlags(Intent.FLAG_NOT_OHOS_COMPONENT);
            startAbility(shareIntent);

//            startActivity(Intent.createChooser(shareIntent, getString(R.string.text_fileShareAppChoose)));
        } else if (actionId == MENU_ACTION_SHARE_TREBLESHOT) {
            Intent intent = new Intent();
            intent.setParam(ConnectionManagerActivity.EXTRA_REQUEST_TYPE, ConnectionManagerActivity.RequestType.RETURN_RESULT.toString());
            intent.setParam(ConnectionManagerActivity.EXTRA_ACTIVITY_SUBTITLE, getString(ResourceTable.String_text_sendText));
            HMUtils.startAbilityForResult(this, intent, ConnectionManagerActivity.class.getName(), "", REQUEST_CODE_CHOOSE_DEVICE);

        } else if (actionId == MENU_ACTION_SHOW_AS_QR_CODE) {
            if (mInputCtrl.length() > 0 && mInputCtrl.length() <= 1200) {
                try {
                    new EventHandler(EventRunner.create("QRCodeGen")).postTask(() -> {
                        PixelMap pixelMap = QRCodeEncoder.syncEncodeQRCode(mInputCtrl.getText(), BGAQRCodeUtil.dp2px(TextEditorAbilitySlice.this, 160));
                        TextEditorAbilitySlice.this.getUITaskDispatcher().asyncDispatch(() -> {
                            //qrImage.setPixelMap(pixelMap);
                        });
                    });

                } catch (Exception e) {
                    Toast.show(this, ResourceTable.String_mesg_somethingWentWrong, Toast.LENGTH_SHORT);
                }
            }
//        } else if (actionId == android.R.id.home) {
//            onBackPressed();
        } else if (actionId == MENU_ACTION_REMOVE) {
            if (mTextStreamObject != null)
                AppUtils.getDatabase(this).remove(mTextStreamObject);

            mTextStreamObject = null;
        }

        return true;
    }

    protected void doCommunicate(final NetworkDevice device, final NetworkDevice.Connection connection) {
        new ToastDialog(this).setText(getString(ResourceTable.String_mesg_communicating)).setDuration(2000).show();

        WorkerService.RunningTask rt = new WorkerService.RunningTask () {

            @Override
            public void onRun() {
                final Component.ClickedListener retryButtonListener = new Component.ClickedListener() {
                    @Override
                    public void onClick(Component component) {
                        doCommunicate(device, connection);
                    }
                };

                CommunicationBridge.connect(AppUtils.getDatabase(TextEditorAbilitySlice.this), true, new CommunicationBridge.Client.ConnectionHandler() {
                    @Override
                    public void onConnect(CommunicationBridge.Client client) {
                        client.setDevice(device);

                        try {
                            final JSONObject jsonRequest = new JSONObject();
                            final CoolSocket.ActiveConnection activeConnection = client.communicate(device, connection);

                            jsonRequest.put(Keyword.REQUEST, Keyword.REQUEST_CLIPBOARD);
                            jsonRequest.put(Keyword.TRANSFER_CLIPBOARD_TEXT, mInputCtrl.getText().toString());

                            activeConnection.reply(jsonRequest.toString());

                            CoolSocket.ActiveConnection.Response response = activeConnection.receive();
                            activeConnection.getSocket().close();

                            JSONObject clientResponse = new JSONObject(response.response);

                            if (clientResponse.has(Keyword.RESULT) && clientResponse.getBoolean(Keyword.RESULT)) {
                                new EventHandler(EventRunner.getMainEventRunner()).postTask(new Runnable() {
                                    @Override
                                    public void run() {
                                        new ToastDialog(TextEditorAbilitySlice.this).setText(getString(ResourceTable.String_mesg_sent)).setDuration(2000).show();
                                    }
                                });
                            }
                            else {
                                UIConnectionUtils.showConnectionRejectionInformation(
                                        TextEditorAbilitySlice.this,
                                        device, clientResponse, retryButtonListener);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            UIConnectionUtils.showUnknownError(TextEditorAbilitySlice.this, retryButtonListener);
                        }
                    }
                });
            }
        };

        rt.setTitle(getString(ResourceTable.String_mesg_communicating));
        rt.setIconRes(ResourceTable.Media_ic_compare_arrows_white_24dp_static);
        WorkerService.getSelf().run(rt);
    }


    private CommonDialog commonDialog;

    public void showActionDialog() {
        if (commonDialog == null) commonDialog = new CommonDialog(this);
        Component component = LayoutScatter.getInstance(this).parse(ResourceTable.Layout_action_dialog_layout, null, false);
        commonDialog.setAlignment(LayoutAlignment.HORIZONTAL_CENTER);
        commonDialog.setAutoClosable(true);

        component.findComponentById(ResourceTable.Id_teSave).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                saveText();
                new ToastDialog(component.getContext()).setText(
                        component.getContext().getString(ResourceTable.String_mesg_textStreamSaved)).show();
                commonDialog.destroy();
            }
        });
        component.findComponentById(ResourceTable.Id_teCopy).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {  //复制到剪切板
                commonDialog.destroy();
                SystemPasteboard pasteboard = SystemPasteboard.getSystemPasteboard(component.getContext());
                if (pasteboard != null) {
                    pasteboard.setPasteData(PasteData.creatPlainTextData(mInputCtrl.getText()));
                    new ToastDialog(component.getContext()).setText(
                            component.getContext().getString(ResourceTable.String_mesg_textCopiedToClipboard)).show();
                }
            }
        });
        component.findComponentById(ResourceTable.Id_teShare).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {  //分享
                Intent shareIntent = new Intent();
                shareIntent.setAction("android.intent.action.SEND");
                shareIntent.setType("text/plain");
                shareIntent.setParam("android.intent.extra.SUBJECT", getString(ResourceTable.String_butn_share));
                shareIntent.setParam("android.intent.extra.TEXT", mInputCtrl.getText());
                shareIntent.addFlags(Intent.FLAG_NOT_OHOS_COMPONENT);
                startAbility(shareIntent);
                commonDialog.destroy();
            }
        });

        component.findComponentById(ResourceTable.Id_teQR).setEnabled(mInputCtrl.getText().length() > 0
                && mInputCtrl.getText().length() <= 1200);

        component.findComponentById(ResourceTable.Id_teQR).setClickedListener(component1 -> {
            commonDialog.destroy();
            showQrDialog();
        });

        component.findComponentById(ResourceTable.Id_teRemove).setVisibility(mTextStreamObject != null ? VISIBLE: HIDE);
        component.findComponentById(ResourceTable.Id_teRemove).setClickedListener(component1 -> {
            if (mTextStreamObject != null)
                AppUtils.getDatabase(this).remove(mTextStreamObject);

            mTextStreamObject = null;
            commonDialog.destroy();
        });

        commonDialog.setContentCustomComponent(component);
        if (!commonDialog.isShowing()) commonDialog.show();
    }


    private void showQrDialog() {
        CommonDialog commonDialog = new CommonDialog(this);
        Component component = LayoutScatter.getInstance(this).parse(ResourceTable.Layout_qr_dialog_layout, null, false);
        commonDialog.setAlignment(LayoutAlignment.BOTTOM);
        commonDialog.setAutoClosable(true);
        commonDialog.setTransparent(true);
        Image image = (Image) component.findComponentById(ResourceTable.Id_imaQr);
        String s = mInputCtrl.getText();
        if (!TextUtils.isEmpty(s)) {
            try {
                new EventHandler(EventRunner.create("QRCodeGen")).postTask(() -> {
                    PixelMap pixelMap = QRCodeEncoder.syncEncodeQRCode(mInputCtrl.getText(), BGAQRCodeUtil.dp2px(TextEditorAbilitySlice.this, 240));
                    TextEditorAbilitySlice.this.getUITaskDispatcher().asyncDispatch(() -> {
                        image.setPixelMap(pixelMap);
                    });
                });

            } catch (Exception e) {
                Toast.show(this, ResourceTable.String_mesg_somethingWentWrong, Toast.LENGTH_SHORT);
            }
        }

        image.setClickedListener(component1 -> {
            commonDialog.destroy();
        });

        commonDialog.setContentCustomComponent(component);
        if (!commonDialog.isShowing()) commonDialog.show();
    }


    @Override
    protected void onBackPressed() {
        boolean hasObject = mTextStreamObject != null;
        boolean hasEntry = mInputCtrl.getText().length() > 0;
        boolean hasSavedText = hasObject && mTextStreamObject.text != null && mTextStreamObject.text.length() > 0;

        if (!hasEntry || (hasSavedText && mTextStreamObject.text.equals(mInputCtrl.getText().toString()))) {
            super.onBackPressed();
        } else {
            exitDialog();
        }
    }


    private void exitDialog() {
        CommonDialog commonDialog = new CommonDialog(getContext());
        Component component = LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_alert_dialog_layout,
                null, false);
        commonDialog.setContentCustomComponent(component);
        commonDialog.setTransparent(true);
        Text titleTV = (Text) component.findComponentById(ResourceTable.Id_alert_dialog_title);
        titleTV.setText("文本保存");
        Text messageTV = (Text) component.findComponentById(ResourceTable.Id_alert_dialog_message);
        messageTV.setText("是否保存文本?");
        Button ok = (Button) component.findComponentById(ResourceTable.Id_alert_dialog_ok);
        ok.setClickedListener(component1 -> {
            saveText();
            commonDialog.remove();
            terminateAbility();
        });
        Button cancle = (Button) component.findComponentById(ResourceTable.Id_alert_dialog_cancle);
        cancle.setClickedListener(component1 -> {
            commonDialog.remove();
            terminateAbility();
        });
        commonDialog.show();
    }

}
