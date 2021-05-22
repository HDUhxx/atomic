package com.example.myapplication_tongqu.slice;

import com.example.myapplication_tongqu.ResourceTable;
import com.example.myapplication_tongqu.guangbo.FabuAbility;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.TextField;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;

public class LoginAbility extends AbilitySlice {
    private Button button1,button2;
    private TextField text_name;
    private TextField text_password;
    private Context context;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_layout_login);

        context =getContext();

        button1 = (Button) findComponentById(ResourceTable.Id_login_btnzhuce);

        button2 = (Button) findComponentById(ResourceTable.Id_login_btn1);
        text_name = (TextField) findComponentById(ResourceTable.Id_text_name);
        text_password = (TextField) findComponentById(ResourceTable.Id_text_password);

        login();
        zhuce();

    }

    private void zhuce(){

        // 为按钮设置点击回调
        button1.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button1!=null){
                    present(new LoginZcAbility(),new Intent());
                }
            }
        });
    };


    private void login(){
        button2.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                String name =  text_name.getText().toString().trim();
                String password = text_password.getText().toString().trim();

                if(button2!=null && name.equals("同区") && password.equals("2019")){
                    present(new FabuAbility(),new Intent());
                    new ToastDialog(context).setText("欢迎登到同区").show();
                }else if(button2!=null && name.equals("蛟龙腾飞") && password.equals("2019")){
                    present(new FabuAbility(),new Intent());
                    new ToastDialog(context).setText("欢迎登到同区").show();
                }else if(button2!=null && name.equals("惠便利") && password.equals("2019")){
                    present(new FabuAbility(),new Intent());
                    new ToastDialog(context).setText("欢迎登到同区").show();
                }
                else {
                    new ToastDialog(context).setText("用户名或密码有误请重新输入").show();
                }
            }
        });

    };
}
