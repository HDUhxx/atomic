package me.panavtec.title.hmadapter;

import com.bumptech.glide.Glide;
import me.panavtec.title.ResourceTable;
import me.panavtec.title.hminterface.AvatarInterface;
import me.panavtec.title.hminterface.PermissionInterface;
import me.panavtec.title.hmutils.HmSharedPerfrence;
import me.panavtec.title.hmutils.Toast;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.agp.animation.AnimatorProperty;
import ohos.agp.components.*;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;
import ohos.bundle.IBundleManager;
import ohos.data.resultset.ResultSet;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Rect;
import ohos.media.image.common.Size;
import ohos.media.photokit.metadata.AVStorage;
import ohos.utils.net.Uri;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class WelcomeProvider extends PageSliderProvider implements PermissionInterface{

    final Context context;
    PermissionInterface anInterface;
    Image mLayoutWelcomePage3PermOkImage;
    Button mLayoutWelcomePage3RequestButton;
    AvatarInterface avatarInterface;
    Image mLayoutWelcomePage1SplashImage;
    DirectionalLayout mLayoutWelcomePage1Details;
    private Image imageDL;

    public WelcomeProvider(Context context) {
        this.context = context;
    }

    public void setAnInterface(PermissionInterface anInterface) {
        this.anInterface = anInterface;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object createPageInContainer(ComponentContainer componentContainer, int i) {
        componentContainer.removeAllComponents();
        Component cpt;
        switch (i){
            case 0:
                cpt = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_layout_welcome_page_1, null, false);
                mLayoutWelcomePage1SplashImage = (Image) cpt.findComponentById(ResourceTable.Id_layout_welcome_page_1_splash_image);
                mLayoutWelcomePage1Details = (DirectionalLayout) cpt.findComponentById(ResourceTable.Id_layout_welcome_page_1_details);
                componentContainer.addComponent(cpt);
                return cpt;
            case 1:
                cpt = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_layout_welcome_page_3, null, false);
                 mLayoutWelcomePage3PermOkImage = (Image) cpt.findComponentById(ResourceTable.Id_layout_welcome_page_3_perm_ok_image);
                 mLayoutWelcomePage3RequestButton = (Button) cpt.findComponentById(ResourceTable.Id_layout_welcome_page_3_request_button);
                isOrder(mLayoutWelcomePage3PermOkImage, mLayoutWelcomePage3RequestButton);
                mLayoutWelcomePage3RequestButton.setClickedListener(new Component.ClickedListener() {
                    @Override
                    public void onClick(Component component) {
                        doQx2(mLayoutWelcomePage3PermOkImage, mLayoutWelcomePage3RequestButton);
//                        new ToastDialog(context).setText("请前往设置授予媒体读写权限").setDuration(2000).back_header();
                    }
                });
                componentContainer.addComponent(cpt);
                return cpt;
            case 2:
                cpt = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_layout_welcome_page_2, null, false);
                imageDL = (Image) cpt.findComponentById(ResourceTable.Id_image_DL);
//                avatarInterface.loadExiestImage(image_DL);
                loadExiestImage(imageDL);
//                imageDirectionalLayout.setClickedListener(new Component.ClickedListener() {
//                    @Override
//                    public void onClick(Component component) {
//                        doQx();
//                        avatarInterface.updateImage(imageDirectionalLayout);
//                    }
//                });
                Text txShowName = (Text) cpt.findComponentById(ResourceTable.Id_tx_show_name);
                txShowName.setText(getName());
                Text txShowName_ = (Text) cpt.findComponentById(ResourceTable.Id_tx_show_name_);
                txShowName_.setText(String.valueOf(getName().toLowerCase().charAt(0)));
                imageDL.setClickedListener(new Component.ClickedListener() {
                    @Override
                    public void onClick(Component component) {
                        showNameDialog(txShowName, txShowName_);
                    }
                });
                componentContainer.addComponent(cpt);
                return cpt;
//            case 3:
//                cpt = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_layout_welcome_page_4, null, false);
//                componentContainer.addComponent(cpt);
//                return cpt;
            case 3:
                cpt = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_layout_welcome_page_5, null, false);
                componentContainer.addComponent(cpt);
                return cpt;
        }
        return null;

    }

    @Override
    public void destroyPageFromContainer(ComponentContainer componentContainer, int i, Object o) {

    }

    @Override
    public boolean isPageMatchToObject(Component component, Object o) {
        return true;
    }

    public void setAvatarInterface(AvatarInterface avatarInterface) {
        this.avatarInterface = avatarInterface;
    }

    public void doQx2(Image layoutWelcomePage3PermOkImage, Button layoutWelcomePage3RequestButton){
        if (context.verifySelfPermission("ohos.permission.READ_MEDIA") != IBundleManager.PERMISSION_GRANTED) {
            // 应用未被授予权限
            if (context.canRequestPermission("ohos.permission.READ_MEDIA")) {
                // 是否可以申请弹框授权(首次申请或者用户未选择禁止且不再提示)
                context.requestPermissionsFromUser(
                        new String[] { "ohos.permission.READ_MEDIA" } , 3);
            } else {
                // 显示应用需要权限的理由，提示用户进入设置授权
            }
        } else {
            // 权限已被授予
            new ToastDialog(context).setText("媒体读写权限授予成功").setDuration(2000).show();
            layoutWelcomePage3PermOkImage.setVisibility(Component.VISIBLE);
            layoutWelcomePage3RequestButton.setVisibility(Component.HIDE);
        }

    }
    public void isOrder(Image layoutWelcomePage3PermOkImage,Button layoutWelcomePage3RequestButton){
        if (context.verifySelfPermission("ohos.permission.READ_MEDIA") != IBundleManager.PERMISSION_GRANTED) {
            layoutWelcomePage3PermOkImage.setVisibility(Component.HIDE);
            layoutWelcomePage3RequestButton.setVisibility(Component.VISIBLE);
        }else {
            layoutWelcomePage3PermOkImage.setVisibility(Component.VISIBLE);
            layoutWelcomePage3RequestButton.setVisibility(Component.HIDE);
        }
    }

    @Override
    public void isPermission(Boolean isPermis) {
        if (isPermis) {
            mLayoutWelcomePage3PermOkImage.setVisibility(Component.VISIBLE);
            mLayoutWelcomePage3RequestButton.setVisibility(Component.HIDE);
        }else {
            mLayoutWelcomePage3PermOkImage.setVisibility(Component.HIDE);
            mLayoutWelcomePage3RequestButton.setVisibility(Component.VISIBLE);
        }
    }
    private void showNameDialog(Text text, Text text1) {
        CommonDialog commonDialog = new CommonDialog(context);
        Component component = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_input_dialog_with_icon_layout, null, false);
        Text inputDialogTitle = (Text) component.findComponentById(ResourceTable.Id_input_dialog_title);
        TextField inputDialogTextField = (TextField) component.findComponentById(ResourceTable.Id_input_dialog_text_field);
        inputDialogTitle.setText("请输入昵称");
        commonDialog.setContentCustomComponent(component);
        commonDialog.setAutoClosable(true);
        commonDialog.setTransparent(true);
        commonDialog.setAlignment(LayoutAlignment.HORIZONTAL_CENTER);

        inputDialogTextField.setText(text.getText());

        Text txShowName_ = (Text) component.findComponentById(ResourceTable.Id_tx_show_name_);
        txShowName_.setText(text1.getText());

        loadExiestImage((Image) component.findComponentById(ResourceTable.Id_image_DL));

        component.findComponentById(ResourceTable.Id_image_DL).setClickedListener(component12 -> {
            commonDialog.destroy();
            doQx();
            avatarInterface.updateImage(imageDL);
        });

        Button remove = (Button) component.findComponentById(ResourceTable.Id_input_dialog_remove);
        remove.setClickedListener(component11 -> {
            commonDialog.destroy();
            imageDL.setPixelMap(null);
            HmSharedPerfrence sharedPerfrence = HmSharedPerfrence.getInstance(context);
            sharedPerfrence.saveOtherInConFile("avater", "");
            ((ComponentContainer) imageDL.getComponentParent())
                    .findComponentById(ResourceTable.Id_tx_show_name_)
                    .setVisibility(Component.VISIBLE);
        });


        Button ok = (Button) component.findComponentById(ResourceTable.Id_input_dialog_ok2);
        ok.setClickedListener(component1 -> {
            if (inputDialogTextField.getText().trim().length()>0){
            text.setText(inputDialogTextField.getText().trim());
            text1.setText(String.valueOf(inputDialogTextField.getText().trim().toLowerCase().charAt(0)));

//            HmSharedPerfrence perfrence = new HmSharedPerfrence(context);
                HmSharedPerfrence    perfrence = HmSharedPerfrence.getInstance(context);
            perfrence.saveOtherInConFile("device_name",inputDialogTextField.getText().trim());
            commonDialog.destroy();}else {
                Toast.show(context,"请输入昵称");
            }
        });
        Button cancle = (Button) component.findComponentById(ResourceTable.Id_input_dialog_cancle);
        cancle.setClickedListener(component1 -> {
            commonDialog.destroy();
        });
        if (!commonDialog.isShowing()) commonDialog.show();
    }

    public String getName(){
//        String name = new HmSharedPerfrence(context).getOtherInConFile("device_name");
        String name = HmSharedPerfrence.getInstance(context).getOtherInConFile("device_name");
        if (name.length()>0){
            return name;
        }else {
            HmSharedPerfrence.getInstance(context).saveOtherInConFile("device_name","SDK_GPHONE_X86_ARM");
            return "SDK_GPHONE_X86_ARM";
        }

    }

    /**
     * 通过资源ID获取位图对象
     **/
    private PixelMap getPixelMap(int drawableId) {
        InputStream drawableInputStream = null;
        try {
            drawableInputStream = context.getResourceManager().getResource(drawableId);
            ImageSource.SourceOptions sourceOptions = new ImageSource.SourceOptions();
            sourceOptions.formatHint = "image/png";
            ImageSource imageSource = ImageSource.create(drawableInputStream, sourceOptions);
            ImageSource.DecodingOptions decodingOptions = new ImageSource.DecodingOptions();
            decodingOptions.desiredSize = new Size(0, 0);
            decodingOptions.desiredRegion = new Rect(0, 0, 0, 0);
            decodingOptions.desiredPixelFormat = PixelFormat.ARGB_8888;
            return imageSource.createPixelmap(decodingOptions);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                if (drawableInputStream != null){
                    drawableInputStream.close();
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public void loadExiestImage(Image imageDirectionalLayout) {
//        this.imageDirectionalLayout = imageDirectionalLayout;
//        HmSharedPerfrence perfrence = new HmSharedPerfrence(context);
        HmSharedPerfrence perfrence = HmSharedPerfrence.getInstance(context);
        String str = perfrence.getOtherInConFile("avater");
        if (str.length()>0) {
            Uri uri = Uri.parse(str);
            showImage2(uri, imageDirectionalLayout);
        }
    }
    private void showImage2(Uri inUri,Image imageDirectionalLayout) {
        DataAbilityHelper helper = DataAbilityHelper.creator(context);
        try {
            // columns为null，查询记录所有字段，当前例子表示查询id字段
            System.out.println("------------");
            ResultSet resultSet = helper.query(AVStorage.Images.Media.EXTERNAL_DATA_ABILITY_URI, new String[]{AVStorage.Images.Media.ID,AVStorage.Images.Media.DATA}, null);
            while (resultSet != null && resultSet.goToNextRow()) {
                System.out.println("====================");
                PixelMap pixelMap = null;
                ImageSource imageSource = null;

                int id = resultSet.getInt(resultSet.getColumnIndexForName(AVStorage.Images.Media.ID));
                String data = resultSet.getString(resultSet.getColumnIndexForName(AVStorage.Images.Media.DATA));
//                dataability:///media/external/images/media/30
                Uri uri = Uri.appendEncodedPathToUri(AVStorage.Images.Media.EXTERNAL_DATA_ABILITY_URI, String.valueOf(id));
                if (uri.toString().split(":///")[1].equals(inUri.toString().split("://")[1])){
                    FileDescriptor fd = helper.openFile(uri, "r");
                    try {
                        imageSource = ImageSource.create(fd, null);
                        pixelMap = imageSource.createPixelmap(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (imageSource != null) {
                            imageSource.release();
                        }
                    }

                    Glide.with(imageDirectionalLayout.getContext())
                            .asDrawable()
                            .load(pixelMap)
                            .circleCrop()
                            .into(imageDirectionalLayout);

                    Text txShowName_ = (Text) ((ComponentContainer)imageDirectionalLayout.getComponentParent()).findComponentById(ResourceTable.Id_tx_show_name_);
                    txShowName_.setVisibility(Component.HIDE);

                }else {
//                    string+="false";
                }
                System.out.println(uri.getDecodedHost()+"===="+inUri.getDecodedHost());

            }

        } catch (DataAbilityRemoteException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void startAnim() {
        doAnim1();
        doAnim2();
    }
    public void doAnim1(){
        AnimatorProperty animatorProperty = mLayoutWelcomePage1SplashImage.createAnimatorProperty();
        animatorProperty.moveFromY(mLayoutWelcomePage1SplashImage.getLocationOnScreen()[1]+50).moveToY(mLayoutWelcomePage1SplashImage.getLocationOnScreen()[1]).setDuration(1000);
        System.out.println("动画执行了1");
        animatorProperty.start();
    }
    public void doAnim2(){
//        AnimatorProperty animatorProperty = layout_welcome_page_1_details.createAnimatorProperty();
        AnimatorProperty animatorProperty = mLayoutWelcomePage1Details.createAnimatorProperty();
        System.out.println("动画执行了2");
        animatorProperty.moveFromY(mLayoutWelcomePage1Details.getLocationOnScreen()[1]+650).moveToY(mLayoutWelcomePage1Details.getLocationOnScreen()[1]).setDuration(1000);
        animatorProperty.start();
    }
    public void doQx(){
        if (context.verifySelfPermission("ohos.permission.READ_USER_STORAGE") != IBundleManager.PERMISSION_GRANTED) {
            // 应用未被授予权限
            if (context.canRequestPermission("ohos.permission.READ_USER_STORAGE")) {
                // 是否可以申请弹框授权(首次申请或者用户未选择禁止且不再提示)
                context.requestPermissionsFromUser(
                        new String[] { "ohos.permission.READ_USER_STORAGE" } , 4);
            } else {
                // 显示应用需要权限的理由，提示用户进入设置授权
                Toast.show(context,"请开放权限设置头像");
            }
        } else {
            // 权限已被授予
            // new ToastDialog(context).setText("存储读写权限申请成功").setDuration(2000).show();
        }

    }
}
