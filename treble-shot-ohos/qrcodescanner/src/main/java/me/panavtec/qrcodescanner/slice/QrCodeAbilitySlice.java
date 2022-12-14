/*
 * Copyright (C) 2021 The Chinese Software International Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain an copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package me.panavtec.qrcodescanner.slice;

import com.google.zxing.Result;
import me.panavtec.qrcodescanner.ResourceTable;
import me.panavtec.qrcodescanner.decode.QrManager;
import me.panavtec.qrcodescanner.utils.QrUtils;
import me.panavtec.qrcodescanner.utils.ScreenUtils;
import me.panavtec.qrcodescanner.view.QrCodeFinderView;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.content.Intent;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.DependentLayout;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Text;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.components.surfaceprovider.SurfaceProvider;
import ohos.agp.graphics.Surface;
import ohos.agp.graphics.SurfaceOps;
import ohos.agp.utils.Color;
import ohos.data.resultset.ResultSet;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.global.resource.RawFileDescriptor;
import ohos.media.camera.CameraKit;
import ohos.media.camera.device.Camera;
import ohos.media.camera.device.CameraConfig;
import ohos.media.camera.device.CameraStateCallback;
import ohos.media.camera.device.FrameConfig;
import ohos.media.camera.params.Metadata;
import ohos.media.common.Source;
import ohos.media.image.Image;
import ohos.media.image.ImageReceiver;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.ImageFormat;
import ohos.media.photokit.metadata.AVStorage;
import ohos.media.player.Player;
import ohos.utils.net.Uri;

import static ohos.media.camera.device.Camera.FrameConfigType.FRAME_CONFIG_PREVIEW;

/**
 * QrCodeAbilitySlice
 *
 * @author ?????????
 * @since 2021-04-14
 */
public class QrCodeAbilitySlice extends AbilitySlice {
    private ImageReceiver imageReceiver;
    private CameraKit cameraKit;
    private SurfaceProvider surfaceProvider;
    private Surface previewSurface;
    private final int VIDEO_WIDTH = 256;
    private final int VIDEO_HEIGHT = 256;
    private QrCodeFinderView mLaserView;
    private Camera myCamera;
    /**
     * ????????????
     */
    private CameraConfig.Builder cameraConfigBuilder;
    /**
     * ????????????
     */
    private static FrameConfig.Builder frameConfigBuilder;
    private Player mPlayer;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_qr_code);
        initPlayer();
        initLayout();
        initImageReceiver();
        capture();
    }

    private void initPlayer() {
        try {
            RawFileDescriptor filDescriptor = getResourceManager()
                    .getRawFileEntry("resources/rawfile/qrcode.mp3").openRawFileDescriptor();
            Source source = new Source(filDescriptor.getFileDescriptor(),
                    filDescriptor.getStartPosition(), filDescriptor.getFileSize());
            mPlayer = new Player(getContext());
            mPlayer.setSource(source);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initLayout() {
        //????????????
        DependentLayout myLayout = (DependentLayout) findComponentById(ResourceTable.Id_root_view);
        DirectionalLayout openLight = (DirectionalLayout) findComponentById(ResourceTable.Id_open_light);
        Text picture = (Text) findComponentById(ResourceTable.Id_picture);
        Text des = (Text) findComponentById(ResourceTable.Id_sdt_des);
        picture.setClickedListener(v -> {
                    //picture()
                }
        );
        openLight.setClickedListener(v -> setFlash(des));
        ShapeElement viewShape = new ShapeElement();
        viewShape.setRgbColor(RgbColor.fromArgbInt(Color.BLACK.getValue()));
        myLayout.setBackground(viewShape);
        DependentLayout.LayoutConfig config = new DependentLayout.LayoutConfig(DependentLayout.LayoutConfig.MATCH_PARENT,
                DependentLayout.LayoutConfig.MATCH_PARENT);
        myLayout.setLayoutConfig(config);
        config.width = ScreenUtils.getDisplayWidthInPx(this);
        config.height = ScreenUtils.getDisplayWidthInPx(this);
        int px = ScreenUtils.getDisplayHeightInPX(this) - ScreenUtils.getDisplayWidthInPx(this);
        config.setMargins(0, px / 3, 0, 0);
        surfaceProvider = new SurfaceProvider(this);
        surfaceProvider.setLayoutConfig(config);
        getWindow().setTransparent(true);
        // ??????SurfaceOps??????
        SurfaceOps surfaceOps = surfaceProvider.getSurfaceOps().get();
        // ????????????????????????
        surfaceOps.setKeepScreenOn(true);
        // ????????????
        surfaceOps.addCallback(callback);
        myLayout.addComponent(surfaceProvider);
        mLaserView = new QrCodeFinderView(this);
        config.width = ScreenUtils.getDisplayWidthInPx(this);
        config.height = ScreenUtils.getDisplayHeightInPX(this);
        config.setMargins(0, 0, 0, 0);
        mLaserView.setLayoutConfig(config);
        myLayout.addComponent(mLaserView);
    }

    /**
     * ??????ImageReceiver??????????????????
     */
    private void initImageReceiver() {
        imageReceiver = ImageReceiver.create(VIDEO_WIDTH, VIDEO_HEIGHT, ImageFormat.JPEG, 50);
        IImageArrivalListenerImpl listener = new IImageArrivalListenerImpl();
        imageReceiver.setImageArrivalListener(listener);
    }

    /**
     * ?????????
     *
     * @param des
     */
    public void setFlash(Text des) {
        // ????????????????????????
        FrameConfig.Builder frameConfigBuilder = myCamera.getFrameConfigBuilder(FRAME_CONFIG_PREVIEW);
        // ????????????Surface
        frameConfigBuilder.addSurface(previewSurface);
        int flashMode = frameConfigBuilder.getFlashMode();
        if (flashMode == Metadata.FlashMode.FLASH_CLOSE) {
            frameConfigBuilder.setFlashMode(Metadata.FlashMode.FLASH_ALWAYS_OPEN);
            if (des != null) {
                des.setText("Close Flash Light");
            }
        } else {
            frameConfigBuilder.setFlashMode(Metadata.FlashMode.FLASH_CLOSE);
            if (des != null) {
                des.setText("Open flash light");
            }
        }
        // ????????????????????????
        FrameConfig previewFrameConfig = frameConfigBuilder.build();
        try {
            // ?????????????????????
            myCamera.triggerLoopingCapture(previewFrameConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ???????????????
     */
    public void closeFlash() {
        // ????????????????????????
        FrameConfig.Builder frameConfigBuilder = myCamera.getFrameConfigBuilder(FRAME_CONFIG_PREVIEW);
        // ????????????Surface
        frameConfigBuilder.addSurface(previewSurface);
        int flashMode = frameConfigBuilder.getFlashMode();
        if (flashMode == Metadata.FlashMode.FLASH_ALWAYS_OPEN) {
            frameConfigBuilder.setFlashMode(Metadata.FlashMode.FLASH_CLOSE);
            // ????????????????????????
            FrameConfig previewFrameConfig = frameConfigBuilder.build();
            try {
                // ?????????????????????
                myCamera.triggerLoopingCapture(previewFrameConfig);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void picture() {
        DataAbilityHelper helper = DataAbilityHelper.creator(this);
        try {
            // columns???null??????????????????????????????????????????????????????id??????
            ResultSet resultSet = helper.query(AVStorage.Images.Media.EXTERNAL_DATA_ABILITY_URI,
                    new String[]{AVStorage.Images.Media.ID}, null);
            if (resultSet != null || resultSet.goToNextRow()) {
                return;
            }
            while (resultSet != null && resultSet.goToNextRow()) {
                PixelMap pixelMap = null;
                ImageSource imageSource = null;
                ohos.agp.components.Image image = new ohos.agp.components.Image(this);
                image.setWidth(250);
                image.setHeight(250);
                image.setMarginsLeftAndRight(10, 10);
                image.setMarginsTopAndBottom(10, 10);
                image.setScaleMode(ohos.agp.components.Image.ScaleMode.CLIP_CENTER);
                // ??????id????????????
                int id = resultSet.getInt(resultSet.getColumnIndexForName(AVStorage.Images.Media.ID));
                Uri uri = Uri.appendEncodedPathToUri(AVStorage.Images.Media.EXTERNAL_DATA_ABILITY_URI, String.valueOf(id));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void capture() {
        getUITaskDispatcher().delayDispatch(() -> {
            if (myCamera==null) return;
            // ????????????????????????
            frameConfigBuilder = myCamera.getFrameConfigBuilder(Camera.FrameConfigType.FRAME_CONFIG_PICTURE);
            // ????????????Surface
            frameConfigBuilder.addSurface(imageReceiver.getRecevingSurface());
            try {
                // ??????????????????(??????)
                myCamera.triggerSingleCapture(frameConfigBuilder.build());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 2000);
    }


    private final SurfaceOps.Callback callback = new SurfaceOps.Callback() {

        @Override
        public void surfaceCreated(SurfaceOps surfaceOps) {
            //???SurfaceProvider??????Surface???previewsurface????????????
            previewSurface = surfaceOps.getSurface();
            //???????????????
            openCamera();
        }

        @Override
        public void surfaceChanged(SurfaceOps surfaceOps, int i, int i1, int i2) {
        }

        @Override
        public void surfaceDestroyed(SurfaceOps surfaceOps) {
        }
    };

    private void openCamera() {
        // ?????? CameraKit ??????
        cameraKit = CameraKit.getInstance(this);
        if (cameraKit == null) {
            return;
        }
        try {
            // ???????????????????????????????????????cameraIds
            String[] cameraIds = cameraKit.getCameraIds();
            if (cameraIds.length <= 0) {
                System.out.println("cameraIds size is 0");
                return;
            }
            // ??????????????????????????????cameraStateCallback??????????????????
            cameraKit.createCamera(cameraIds[0],
                    new CameraStateCallbackImpl(),
                    new EventHandler(EventRunner.create("CameraCb")));
        } catch (Exception e) {
            System.out.println("getCameraIds fail");
        }
    }

    private final class CameraStateCallbackImpl extends CameraStateCallback {

        @Override
        public void onCreated(Camera camera) {
            myCamera = camera;
            // ????????????
            cameraConfigBuilder = camera.getCameraConfigBuilder();
            // ????????????surface
            cameraConfigBuilder.addSurface(previewSurface);
            // ????????????surface
            cameraConfigBuilder.addSurface(imageReceiver.getRecevingSurface());
            try {
                // ??????????????????, ??????onConfigured??????
                camera.configure(cameraConfigBuilder.build());
            } catch (Exception e) {
                // HiLog.error(LOG, "Argument Exception");
            }
        }

        @Override
        public void onConfigured(Camera camera) {
            // ??????????????????
            frameConfigBuilder = camera.getFrameConfigBuilder(FRAME_CONFIG_PREVIEW);
            // ????????????surface
            frameConfigBuilder.addSurface(previewSurface);
            // ??????????????????
            //frameConfigBuilder.setZoom(1.2f);
            FrameConfig previewFrameConfig = frameConfigBuilder.build();
            try {
                // ?????????????????????,???????????????id
                camera.triggerLoopingCapture(previewFrameConfig);
            } catch (Exception e) {
                // HiLog.error(LOG, "Argument Exception");
            }
        }

        @Override
        public void onReleased(Camera camera) {
            // ??????????????????
            try {
                if (camera != null) {
                    // ??????????????????
                    camera.release();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //??????????????????????????????ImageReceiver?????????
    private class IImageArrivalListenerImpl implements ImageReceiver.IImageArrivalListener {

        @Override
        public void onImageArrival(ImageReceiver imageReceiver) {
            try {
                // ????????????
                Image image = imageReceiver.readNextImage();
                if (image == null) {
                    capture();
                } else {
                    Image.Component component = image.getComponent(ImageFormat.ComponentType.JPEG);
                    byte[] bytes = new byte[component.remaining()];
                    component.read(bytes);
                    ImageSource imageSource = ImageSource.create(bytes, null);
                    PixelMap bitmap = imageSource.createPixelmap(null);
                    int width = bitmap.getImageInfo().size.width;
                    int height = bitmap.getImageInfo().size.height;

                    byte[] yuv420sp = QrUtils.getYUV420sp(width, height, bitmap);
                    Result result = QrUtils.decodeImage(yuv420sp, width, height);
                    if (null == result) {
                        capture();
                    } else {
                        mPlayer.prepare();
                        mPlayer.play();
                        QrManager.getInstance().getResultCallback().onScanSuccess(result.getText());
                        closeFlash();
                        terminate();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                capture();
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

    @Override
    protected void onBackPressed() {
        if (myCamera != null) myCamera.release();
        cameraConfigBuilder = null;
        frameConfigBuilder = null;
        myCamera = null;
        super.onBackPressed();
    }
}
