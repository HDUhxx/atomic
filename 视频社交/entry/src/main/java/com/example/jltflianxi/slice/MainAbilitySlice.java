package com.example.jltflianxi.slice;

import com.example.jltflianxi.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.*;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;



public class MainAbilitySlice extends AbilitySlice {

    static final HiLogLabel logLabel=new HiLogLabel(HiLog.LOG_APP,0x001010,"视频测试");

    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);


       Image image = (Image) findComponentById(ResourceTable.Id_jltf_image1);
       if (image !=null){
           HiLog.info(logLabel,"按键存在");
           image.setClickedListener(new Component.ClickedListener() {
               @Override
               public void onClick(Component component) {
                   HiLog.info(logLabel,"跳转开始");
                   Intent intent = new Intent();
                   Operation operation = (Operation) new Intent.OperationBuilder()
                                  .withAction("action.vedio_layout")
                                  .build();
                   intent.setOperation(operation);
                   startAbility(intent);
                   HiLog.info(logLabel,"跳转结束");
               }
           });
       }

        Image image1 = (Image) findComponentById(ResourceTable.Id_jltf_image2);
        if (image1 !=null){
            HiLog.info(logLabel,"按键存在");
            image1.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    HiLog.info(logLabel,"跳转开始");
                    Intent intent = new Intent();
                    Operation operation = (Operation) new Intent.OperationBuilder()
                            .withAction("action.vedio_layout1")
                            .build();
                    intent.setOperation(operation);
                    startAbility(intent);
                    HiLog.info(logLabel,"跳转结束");
                }
            });
        }

        Image image3 = (Image) findComponentById(ResourceTable.Id_jltf_image4);
        if (image3 !=null){
            HiLog.info(logLabel,"按键存在");
            image3.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    HiLog.info(logLabel,"跳转开始");
                    Intent intent = new Intent();
                    Operation operation = (Operation) new Intent.OperationBuilder()
                            .withAction("action.vedio_layout3")
                            .build();
                    intent.setOperation(operation);
                    startAbility(intent);
                    HiLog.info(logLabel,"跳转结束");
                }
            });
        }
        Image image4 = (Image) findComponentById(ResourceTable.Id_jltf_image5);
        if (image4 !=null){
            HiLog.info(logLabel,"按键存在");
            image4.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    HiLog.info(logLabel,"跳转开始");
                    Intent intent = new Intent();
                    Operation operation = (Operation) new Intent.OperationBuilder()
                            .withAction("action.vedio_layout4")
                            .build();
                    intent.setOperation(operation);
                    startAbility(intent);
                    HiLog.info(logLabel,"跳转结束");
                }
            });
        }
        Image image5 = (Image) findComponentById(ResourceTable.Id_jltf_image6);
        if (image5 !=null){
            HiLog.info(logLabel,"按键存在");
            image5.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    HiLog.info(logLabel,"跳转开始");
                    Intent intent = new Intent();
                    Operation operation = (Operation) new Intent.OperationBuilder()
                            .withAction("action.vedio_layout5")
                            .build();
                    intent.setOperation(operation);
                    startAbility(intent);
                    HiLog.info(logLabel,"跳转结束");
                }
            });
        }
        Button button = (Button) findComponentById(ResourceTable.Id_jltf_btn6);
        button.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                present(new jltfshipinwodeSlice(),new Intent());
            }
        });
        Button button1 = (Button) findComponentById(ResourceTable.Id_jltf_btn4);
        button1.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                present(new jltfshiliaoSlice(),new Intent());
            }
        });
        Button button11 = (Button) findComponentById(ResourceTable.Id_jltf_btn44);
        button11.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                present(new jltfshiliaoSlice(),new Intent());
            }
        });
        Button button2 = (Button) findComponentById(ResourceTable.Id_jltf_btn5);
        button2.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                present(new jltfhaoyouSlice(),new Intent());
            }
        });
        Button button22 = (Button) findComponentById(ResourceTable.Id_jltf_btn55);
        button22.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                present(new jltfhaoyouSlice(),new Intent());
            }
        });
        Button button3 = (Button) findComponentById(ResourceTable.Id_jltf_btn3);
        button3.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                present(new jltfshangchuanSlice(),new Intent());
            }
        });

    }
    //列表




    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
