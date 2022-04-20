package me.panavtec.title.slice;

import com.bumptech.glide.Glide;
import com.ohos.trebleshot.utils.FileIntents;
import me.panavtec.title.MainAbility;
import me.panavtec.title.ResourceTable;
import me.panavtec.title.hmadapter.WelcomeProvider;
import me.panavtec.title.hminterface.AvatarInterface;
import me.panavtec.title.hmutils.HmAddConstant;
import me.panavtec.title.hmutils.HmSharedPerfrence;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.components.element.VectorElement;
import ohos.data.resultset.ResultSet;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.photokit.metadata.AVStorage;
import ohos.utils.net.Uri;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;

public class WelcomeAbilitySlice extends AbilitySlice implements Component.ClickedListener, AvatarInterface {

    Image abilityWelcomeViewNext;
    PageSlider abilityWelcomeViewFlipper;
    Image abilityWelcomeViewPrevious;
    ProgressBar progressBar;
    WelcomeProvider welcomeProvider;
    static int isLeftOrRight = -1;

    Image imageDL;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_activity_welcome);
        initView();
    }

    @Override
    public void onActive() {
        super.onActive();
        //直接调用startAnim()第一次不执行
        getUITaskDispatcher().delayDispatch(new Runnable() {
            @Override
            public void run() {
                welcomeProvider.startAnim();
            }
        },1);
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }


    public void initView() {

        abilityWelcomeViewNext = (Image) findComponentById(ResourceTable.Id_ability_welcome_view_next);
        abilityWelcomeViewNext.setImageElement(new VectorElement(this,ResourceTable.Layout_ic_navigate_next_white_24dp_os));
        abilityWelcomeViewFlipper = (PageSlider) findComponentById(ResourceTable.Id_ability_welcome_view_flipper);
        abilityWelcomeViewPrevious = (Image) findComponentById(ResourceTable.Id_ability_welcome_view_previous);
        progressBar = (ProgressBar) findComponentById(ResourceTable.Id_ability_welcome_progress_bar);
        welcomeProvider = new WelcomeProvider(this);
        ((MainAbility) getAbility()).setAnInterface(welcomeProvider);
        abilityWelcomeViewFlipper.setProvider(welcomeProvider);
        welcomeProvider.setAvatarInterface(this);
        progressBar.setMaxValue((welcomeProvider.getCount() - 1) * 100);

        abilityWelcomeViewNext.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {

                if (abilityWelcomeViewFlipper.getCurrentPage() + 1 < welcomeProvider.getCount()) {
                    isLeftOrRight = 1;
                    abilityWelcomeViewFlipper.setCurrentPage(abilityWelcomeViewFlipper.getCurrentPage() + 1);
                } else {
//                    HmSharedPerfrence perfrence = new HmSharedPerfrence(WelcomeAbilitySlice.this);
                    HmSharedPerfrence perfrence = HmSharedPerfrence.getInstance(WelcomeAbilitySlice.this);
                    perfrence.saveFrist(HmAddConstant.HM_SHAREPERENCE_NAME);
                    System.out.println("========可以进入了"+perfrence.isFrist(HmAddConstant.HM_SHAREPERENCE_NAME));
                    present(new MainFractionAbilitySlice(), new Intent());
                }
            }
        });
        abilityWelcomeViewPrevious.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {

                if (abilityWelcomeViewFlipper.getCurrentPage() - 1 >= 0)
                    isLeftOrRight = -1;
                abilityWelcomeViewFlipper.setCurrentPage(abilityWelcomeViewFlipper.getCurrentPage() - 1, true);
            }
        });
        abilityWelcomeViewFlipper.addPageChangedListener(new PageSlider.PageChangedListener() {
            @Override
            public void onPageSliding(int i, float v, int i1) {
                progressBar.setProgressValue((i * 100) + (int) (v * 100 * isLeftOrRight));
            }

            @Override
            public void onPageSlideStateChanged(int i) {
            }

            @Override
            public void onPageChosen(int i) {
                System.out.println("================================" + i);
                nowChangePageState(i);
            }
        });

    }


    @Override
    protected void onBackPressed() {
        super.onBackPressed();
//        this.stopAbility(new Intent());
    }

    @Override
    public void onClick(Component component) {
    }

    public void nowChangePageState(int num) {
        if (num==0){
            abilityWelcomeViewPrevious.setVisibility(Component.INVISIBLE);
            progressBar.setVisibility(Component.INVISIBLE);
        }else{
            abilityWelcomeViewPrevious.setVisibility(Component.VISIBLE);
            progressBar.setVisibility(Component.VISIBLE);
        }
        if (num==4){
            abilityWelcomeViewNext.setImageElement(new VectorElement(this,ResourceTable.Layout_ic_check_white_24dp_os_white));
        }else{
//            ability_welcome_view_next.setPixelMap(null);
            abilityWelcomeViewNext.setImageElement(new VectorElement(this,ResourceTable.Layout_ic_navigate_next_white_24dp_os));
        }
    }


    @Override
    public void updateImage(Image imageDL_) {
        this.imageDL = imageDL_;
        Intent intent = FileIntents.from(this).pickImageFile().build();
        startAbilityForResult(intent,2001);
    }

    @Override
    protected void onAbilityResult(int requestCode, int resultCode, Intent resultData) {
        super.onAbilityResult(requestCode, resultCode, resultData);
        System.out.println(requestCode+"============"+resultCode);
        if (requestCode==2001&&resultCode==-1){//成功
            System.out.println(resultData.getUri());
//            HmSharedPerfrence sharedPerfrence = new HmSharedPerfrence(WelcomeAbilitySlice.this);
            HmSharedPerfrence sharedPerfrence = HmSharedPerfrence.getInstance(WelcomeAbilitySlice.this);
            showImage2(resultData.getUri(), imageDL);
            sharedPerfrence.saveOtherInConFile("avater",resultData.getUriString());
        }
    }
    private void showImage2(Uri inUri,Image imageDL_) {
        DataAbilityHelper helper = DataAbilityHelper.creator(this);
        try {
            // columns为null，查询记录所有字段，当前例子表示查询id字段
            System.out.println("------------");
            ResultSet resultSet = helper.query(AVStorage.Images.Media.EXTERNAL_DATA_ABILITY_URI, new String[]{AVStorage.Images.Media.ID,AVStorage.Images.Media.DATA}, null);
            while (resultSet != null && resultSet.goToNextRow()) {
                System.out.println("====================");
                PixelMap pixelMap = null;
                ImageSource imageSource = null;
                // 获取id字段的值
                int id = resultSet.getInt(resultSet.getColumnIndexForName(AVStorage.Images.Media.ID));
                String data = resultSet.getString(resultSet.getColumnIndexForName(AVStorage.Images.Media.DATA));
//                dataability:///media/external/images/media/30
                Uri uri = Uri.appendEncodedPathToUri(AVStorage.Images.Media.EXTERNAL_DATA_ABILITY_URI, String.valueOf(id));
//                string +=uri.toString();
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
                    Glide.with(this)
                            .asDrawable()
                            .load(pixelMap)
                            .circleCrop()
                            .into(imageDL_);
                    Text txShowName_ = (Text) ((ComponentContainer)imageDL_.getComponentParent()).findComponentById(ResourceTable.Id_tx_show_name_);
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
}
