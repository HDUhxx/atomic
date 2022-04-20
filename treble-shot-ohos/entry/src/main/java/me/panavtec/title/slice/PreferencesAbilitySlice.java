package me.panavtec.title.slice;

import com.ohos.trebleshot.utils.AppUtils;
import me.panavtec.dialog.AlertDialog;
import me.panavtec.dialog.EditTextPreferenceDialog;
import me.panavtec.title.ResourceTable;
import ohos.aafwk.content.Intent;
import ohos.agp.components.AbsButton;
import ohos.agp.components.Checkbox;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.data.DatabaseHelper;
import ohos.data.preferences.Preferences;

import static com.ohos.trebleshot.config.AppConfig.PREFERENCES_FILE_NAME;
import static me.panavtec.title.hmutils.HMUtils.restartApp;
import static me.panavtec.title.hmutils.HmAddConstant.HM_SHAREPERENCE_NAME;


public class PreferencesAbilitySlice extends BaseSlice implements Component.ClickedListener, AbsButton.CheckedStateChangedListener {
    Text txt_titile;
    final int[] check_box_ids = new int[]{ResourceTable.Id_check_xsxtyy,ResourceTable.Id_check_zqd,ResourceTable.Id_check_kfzms,ResourceTable.Id_check_nsd,
            ResourceTable.Id_check_stop_app_stop_service,ResourceTable.Id_check_jzlst,ResourceTable.Id_check_qr_xry,
            ResourceTable.Id_check_wifi_xry,ResourceTable.Id_check_xry,ResourceTable.Id_check_font_setting,
            ResourceTable.Id_check_amoled,ResourceTable.Id_check_dark_theme};
    //Map<String,Integer> keyWord2Id = new HashMap<String,Integer>();
    public static final String KEY_TEXT_STORAGE_PATH = "text_storagePath";
    public static final String KEY_DEVICE_NAME = "device_name";
    public static final int DIR_SELECT_CODE = 1000;
    Preferences preferences;
    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_slice_preferences);
        preferences = AppUtils.getPreferences(getContext());
        setBack();
        initView();

    }

    private void initView(){
        txt_titile = (Text) findComponentById(ResourceTable.Id_txt_title);
        txt_titile.setText("设置");
        findComponentById(ResourceTable.Id_ima_send).setVisibility(Component.HIDE);
        for(int id:check_box_ids){
            Checkbox checkbox = ((Checkbox)findComponentById(id));
            String keyword = checkbox.getText();
            boolean marked = preferences.getBoolean(keyword,false);
            checkbox.setChecked(marked);
            checkbox.setCheckedStateChangedListener(this);
        }

        findComponentById(ResourceTable.Id_ima_more).setClickedListener(component -> new AlertDialog(PreferencesAbilitySlice.this)
                .setTitle(ResourceTable.String_ques_resetToDefault)
                .setMessage(ResourceTable.String_text_resetPreferencesToDefaultSummary)
                .setNegativeButton(ResourceTable.String_butn_cancel, null)
                .setPositiveButton(ResourceTable.String_butn_proceed, component1 -> {
                                // TODO: 10/7/18 This will cause two seperate sync operations to start
                    DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                    databaseHelper.deletePreferences(PREFERENCES_FILE_NAME);
                    databaseHelper.deletePreferences(HM_SHAREPERENCE_NAME);
                    restartApp(this);
                }).show());

        findComponentById(ResourceTable.Id_ly_notification_setting).setClickedListener(this);
        findComponentById(ResourceTable.Id_ly_device).setClickedListener(this);
        findComponentById(ResourceTable.Id_ly_cclj).setClickedListener(this);
    }

    @Override
    public void onClick(Component component) {
        switch (component.getId()){
            case ResourceTable.Id_ly_notification_setting:
                Intent intent = new Intent();
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                intent.setFlags(Intent.FLAG_ABILITY_NEW_MISSION);
                intent.setParam("android.provider.extra.APP_PACKAGE",getAbilityInfo().bundleName);
                startAbility(intent);
                break;
            case ResourceTable.Id_ly_device:
                String deviceName = preferences.getString(KEY_DEVICE_NAME,"");
                new EditTextPreferenceDialog(getContext())
                        .setDefaultContent(deviceName)
                        .setOnDialogClicked(new EditTextPreferenceDialog.OnDialogClickedListener() {
                    @Override
                    public void onConfirm(String content) {
                        preferences.putString(KEY_DEVICE_NAME,content);
                    }

                    @Override
                    public void onCancel() {

                    }
                }).show();
                break;
            case ResourceTable.Id_ly_cclj:
                String path = preferences.getString(KEY_TEXT_STORAGE_PATH,"/storage/emulated/0");
                Intent intent1 = new Intent();
                intent1.setParam("path",path);
                presentForResult(new SetFilePathAbilitySlice(),intent1,DIR_SELECT_CODE);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onResult(int requestCode, Intent resultIntent) {
        if(requestCode==DIR_SELECT_CODE){
            try {
                String path = resultIntent.getStringParam("path");
                preferences.putString(KEY_TEXT_STORAGE_PATH,path);
                preferences.flush();
            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onCheckedChanged(AbsButton absButton, boolean b) {
        Checkbox checkbox = (Checkbox)absButton;
        preferences.putBoolean(checkbox.getText(),b);
        preferences.flush();
    }
}
