package com.example.doodle;

import com.example.doodle.core.IDoodle;

import ohos.aafwk.ability.Ability;
import ohos.agp.utils.Color;
import ohos.utils.PacMap;
import ohos.utils.Parcel;

/**
 * DoodleParams
 *
 * @since 2021-04-29
 */
public class DoodleParams extends PacMap {
    /**
     * 图片路径
     */
    public String mImagePath;
    /**
     * 　保存路径，如果为null，则图片保存在根目录下/DCIM/Doodle/
     */
    public String mSavePath;
    /**
     * 　保存路径是否为目录，如果为目录，则在该目录生成由时间戳组成的图片名称
     */
    public boolean mSavePathIsDir;

    /**
     * 触摸时，图片区域外是否绘制涂鸦轨迹
     */
    public boolean mIsDrawableOutside;

    /**
     * 涂鸦时（手指按下）隐藏设置面板的延长时间(ms)，当小于等于0时则为不尝试隐藏面板（即保持面板当前状态不变）;当大于0时表示需要触摸屏幕超过一定时间后才隐藏
     * 或者手指抬起时展示面板的延长时间(ms)，或者表示需要离开屏幕超过一定时间后才展示
     * 默认为200ms
     */
    public long mChangePanelVisibilityDelay = 200; //ms

    /**
     * 设置放大镜的倍数，当小于等于0时表示不使用放大器功能
     * 放大器只有在设置面板被隐藏的时候才会出现
     * 默认为2.5倍
     */
    public float mZoomerScale = 2.5f;

    /**
     * 是否全屏显示，即是否隐藏状态栏
     * 默认为false，表示状态栏继承应用样式
     */
    public boolean mIsFullScreen = false;

    /**
     * 初始化的画笔大小,单位为像素
     */
    public float mPaintPixelSize = -1;

    /**
     * 初始化的画笔大小,单位为涂鸦坐标系中的单位大小，该单位参考dp，独立于图片
     * mPaintUnitSize值优先于mPaintPixelSize
     */
    public float mPaintUnitSize = -1;

    /**
     * 画布的最小缩放倍数
     */
    public float mMinScale = DoodleView.MIN_SCALE;
    /**
     * 画布的最大缩放倍数
     */
    public float mMaxScale = DoodleView.MAX_SCALE;

    /**
     * 初始的画笔颜色
     */
    public int mPaintColor = Color.RED.getValue();

    /**
     * 是否支持缩放item
     */
    public boolean mSupportScaleItem = true;

    /**
     * 是否优化绘制，开启后涂鸦会及时绘制在图片上，以此优化绘制速度和性能.
     */
    public boolean mOptimizeDrawing = true;

    @Override
    public boolean marshalling(Parcel out) {

        out.writeString(mImagePath);
        out.writeString(mSavePath);
        out.writeInt(mSavePathIsDir ? 1 : 0);
        out.writeInt(mIsDrawableOutside ? 1 : 0);
        out.writeLong(mChangePanelVisibilityDelay);
        out.writeFloat(mZoomerScale);
        out.writeInt(mIsFullScreen ? 1 : 0);
        out.writeFloat(mPaintPixelSize);
        out.writeFloat(mPaintUnitSize);
        out.writeFloat(mMinScale);
        out.writeFloat(mMaxScale);
        out.writeInt(mPaintColor);
        out.writeInt(mSupportScaleItem ? 1 : 0);
        out.writeInt(mOptimizeDrawing ? 1 : 0);

        return super.marshalling(out);
    }

    @Override
    public boolean unmarshalling(Parcel in) {
        DoodleParams params = new DoodleParams();
        params.mImagePath = in.readString();
        params.mSavePath = in.readString();
        params.mSavePathIsDir = in.readInt() == 1;
        params.mIsDrawableOutside = in.readInt() == 1;
        params.mChangePanelVisibilityDelay = in.readLong();
        params.mZoomerScale = in.readFloat();
        params.mIsFullScreen = in.readInt() == 1;
        params.mPaintPixelSize = in.readFloat();
        params.mPaintUnitSize = in.readFloat();
        params.mMinScale = in.readFloat();
        params.mMaxScale = in.readFloat();
        params.mPaintColor = in.readInt();
        params.mSupportScaleItem = in.readInt() == 1;
        params.mOptimizeDrawing = in.readInt() == 1;

        return super.unmarshalling(in);
    }

    private static DialogInterceptor sDialogInterceptor;

    /**
     * 设置涂鸦中对话框的拦截器，如点击返回按钮（或返回键）弹出保存对话框，可以进行拦截，弹出自定义的对话框
     * 切记：需要自行处理内存泄漏的问题！！！
     *
     * @param interceptor
     */
    public static void setDialogInterceptor(DialogInterceptor interceptor) {
        sDialogInterceptor = interceptor;
    }

    /**
     * getDialogInterceptor
     *
     * @return DialogInterceptor
     */
    public static DialogInterceptor getDialogInterceptor() {
        return sDialogInterceptor;
    }

    /**
     * DialogType
     *
     * @since 2021-04-29
     */
    public enum DialogType {
        /**
         * SAVE
         */
        SAVE,
        /**
         * CLEAR_ALL
         */
        CLEAR_ALL,
        /**
         * COLOR_PICKER
         */
        COLOR_PICKER;
    }

    /**
     * DialogInterceptor
     *
     * @since 2021-04-29
     */
    public interface DialogInterceptor {
        /**
         * onShow
         *
         * @param ability
         * @param doodle
         * @param dialogType 对话框类型
         * @return 返回true表示拦截
         */
        boolean onShow(Ability ability, IDoodle doodle, DialogType dialogType);
    }
}
