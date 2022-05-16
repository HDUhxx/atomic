package com.lxj.xpopup.util;

import com.lxj.xpopup.core.AttachPopupView;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.core.BubbleAttachPopupView;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.core.PositionPopupView;
import com.lxj.xpopup.enums.ImageType;
import com.lxj.xpopup.impl.FullScreenPopupView;
import com.lxj.xpopup.impl.PartShadowPopupView;
import com.lxj.xpopup.interfaces.XPopupImageLoader;
import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorValue;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.TextField;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.PixelMapElement;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.components.element.StateElement;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.render.Texture;
import ohos.agp.utils.Color;
import ohos.agp.utils.Rect;
import ohos.agp.utils.RectFloat;
import ohos.agp.window.service.Display;
import ohos.agp.window.service.DisplayManager;
import ohos.app.Context;
import ohos.app.Environment;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Size;
import ohos.media.photokit.common.AVLogCompletedListener;
import ohos.media.photokit.metadata.AVLoggerConnection;
import ohos.utils.net.Uri;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description:
 * Create by lxj, at 2018/12/7
 */
public class XPopupUtils {

    private static final String TAG = XPopupUtils.class.getName();

    /**
     * 获取屏幕宽度
     *
     * @param context 上下文
     * @return 屏幕宽度
     */
    public static int getWindowWidth(Context context) {
        return context.getResourceManager().getDeviceCapability().width * context.getResourceManager().getDeviceCapability().screenDensity / 160;
    }

    /**
     * 获取屏幕的高度，包含状态栏，导航栏
     *
     * @param context 上下文
     * @return 高度，单位px
     */
    public static int getScreenHeight(Context context) {
        Display display = DisplayManager.getInstance().getDefaultDisplay(context).get();
        return display.getRealAttributes().height;
    }

    /**
     * 获取应用界面可见高度，不包状态栏、导航栏
     *
     * @param context 上下文
     * @return 高度，单位px
     */
    public static int getAppHeight(Context context) {
        Display display = DisplayManager.getInstance().getDefaultDisplay(context).get();
        return display.getAttributes().height;
    }

    /**
     * vp转px
     *
     * @param context 上下文
     * @param vpValue vp
     * @return px
     */
    public static int vp2px(Context context, float vpValue) {
        return (int) (vpValue * context.getResourceManager().getDeviceCapability().screenDensity / 160);
    }

    /**
     * 获取状态栏高度
     *
     * @param component 任意一个处于布局中的控件
     * @return 状态栏高度，单位px
     */
    public static int getStatusBarHeight(Component component) {
        Rect rect = new Rect();
        component.getWindowVisibleRect(rect);
        return rect.top;
    }

    /**
     * Return the navigation bar's height.
     *
     * @return the navigation bar's height
     */
    public static int getNavBarHeight(Component component) {
        int screenHeight = getScreenHeight(component.getContext());
        int appHeight = getAppHeight(component.getContext());
        int statusBarHeight = getStatusBarHeight(component);
        return screenHeight - appHeight - statusBarHeight;
    }

    /**
     * 给控件设置宽高
     *
     * @param target 控件
     * @param width  宽
     * @param height 高
     */
    public static void setWidthHeight(Component target, int width, int height) {
        if (width <= 0 && height <= 0) {
            return;
        }
        ComponentContainer.LayoutConfig params = target.getLayoutConfig();
        if (width > 0) {
            params.width = width;
        }
        if (height > 0) {
            params.height = height;
        }
        target.setLayoutConfig(params);
    }

    public static void applyPopupSize(final ComponentContainer content, final int maxWidth, final int maxHeight, final int popupWidth, final int popupHeight, final Runnable afterApplySize) {
        content.addDrawTask(new Component.DrawTask() {
            @Override
            public void onDraw(Component component, Canvas canvas) {
                ComponentContainer.LayoutConfig params = content.getLayoutConfig();
                Component implView = content.getComponentAt(0);
                ComponentContainer.LayoutConfig implParams = implView.getLayoutConfig();
                // 假设默认Content宽是match，高是wrap
                int contentWidth = content.getWidth();
                // response impl view wrap_content params
                if (maxWidth > 0) {
                    // 指定了最大宽度，就限制最大宽度
                    params.width = Math.min(contentWidth, maxWidth);
                    if (popupWidth > 0) {
                        params.width = Math.min(popupWidth, maxWidth);
                        implParams.width = Math.min(popupWidth, maxWidth);
                    }
                } else if (popupWidth > 0) {
                    params.width = popupWidth;
                    implParams.width = popupWidth;
                }

                int contentHeight = content.getHeight();
                if (maxHeight > 0) {
                    params.height = Math.min(contentHeight, maxHeight);
                    if (popupHeight > 0) {
                        params.height = Math.min(popupHeight, maxHeight);
                        implParams.height = Math.min(popupHeight, maxHeight);
                    }
                } else if (popupHeight > 0) {
                    params.height = popupHeight;
                    implParams.height = popupHeight;
                }
                implView.setLayoutConfig(implParams);
                content.setLayoutConfig(params);

                if (afterApplySize != null) {
                    afterApplySize.run();
                }
            }
        });
    }

    public static void setCursorDrawableColor(TextField et, int color) {
        // 暂时没有找到有效的方法来动态设置cursor的颜色
    }

    public static PixelMapElement createBitmapDrawable(int width, int color) {
        PixelMap.InitializationOptions initializationOptions = new PixelMap.InitializationOptions();
        initializationOptions.size = new Size(width, 20);
        initializationOptions.pixelFormat = PixelFormat.ARGB_8888;
        PixelMap bitmap = PixelMap.create(initializationOptions);
        Texture texture = new Texture(bitmap);
        Canvas canvas = new Canvas(texture);
        Paint paint = new Paint();
        paint.setColor(new Color(color));
        RectFloat rectFloat1 = new RectFloat();
        rectFloat1.left = 0;
        rectFloat1.top = 0;
        rectFloat1.right = bitmap.getImageInfo().size.width;
        rectFloat1.bottom = 4;
        canvas.drawRect(rectFloat1, paint);
        paint.setColor(Color.TRANSPARENT);
        RectFloat rectFloat2 = new RectFloat();
        rectFloat2.left = 0;
        rectFloat2.top = 4;
        rectFloat2.right = bitmap.getImageInfo().size.width;
        rectFloat2.bottom = 20;
        canvas.drawRect(rectFloat2, paint);
        PixelMapElement bitmapDrawable = new PixelMapElement(bitmap);
        return bitmapDrawable;
    }

    public static StateElement createSelector(Element defaultDrawable, Element focusDrawable) {
        StateElement stateListDrawable = new StateElement();
        stateListDrawable.addState(new int[]{}, focusDrawable);
        stateListDrawable.addState(new int[]{}, defaultDrawable);
        return stateListDrawable;
    }

    public static boolean isInRect(float x, float y, Rect rect) {
        return x >= rect.left && x <= rect.right && y >= rect.top && y <= rect.bottom;
    }

    public static void findAllEditText(ArrayList<TextField> list, ComponentContainer group) {
        for (int i = 0; i < group.getChildCount(); i++) {
            Component component = group.getComponentAt(i);
            if (component instanceof TextField) {
                list.add((TextField) component);
            } else if (component instanceof ComponentContainer) {
                findAllEditText(list, (ComponentContainer) component);
            }
        }
    }

    private static int correctKeyboardHeight = 0;

    public static void moveUpToKeyboard(final int keyboardHeight, final BasePopupView pv) {
        correctKeyboardHeight = keyboardHeight;
        moveUpToKeyboardInternal(correctKeyboardHeight, pv);
    }

    private static void moveUpToKeyboardInternal(int keyboardHeight, BasePopupView pv) {
        if (pv.popupInfo == null || !pv.popupInfo.isMoveUpToKeyboard) {
            return;
        }
        // 暂时忽略PartShadow弹窗和AttachPopupView
        if (pv instanceof PositionPopupView || pv instanceof AttachPopupView || pv instanceof BubbleAttachPopupView) {
            return;
        }
        // 判断是否盖住输入框
        ArrayList<TextField> allEts = new ArrayList<>();
        findAllEditText(allEts, pv);
        TextField focusEt = null;
        for (TextField et : allEts) {
            if (et.isFocused()) {
                focusEt = et;
                break;
            }
        }

        int dy = 0;
        int popupHeight = pv.getPopupContentView().getHeight();
        int popupWidth = pv.getPopupContentView().getWidth();
        if (pv.getPopupImplView() != null) {
            popupHeight = Math.min(popupHeight, pv.getPopupImplView().getEstimatedHeight());
            popupWidth = Math.min(popupWidth, pv.getPopupImplView().getEstimatedWidth());
        }

        int screenHeight = pv.getEstimatedHeight();
        int focusEtTop = 0;
        int focusBottom = 0;
        if (focusEt != null) {
            int[] locations = focusEt.getLocationOnScreen();
            focusEtTop = locations[1];
            focusBottom = focusEtTop + focusEt.getEstimatedHeight();
        }
        // 执行上移
        if (pv instanceof FullScreenPopupView || (popupWidth == XPopupUtils.getWindowWidth(pv.getContext()) && popupHeight == screenHeight)) {
            // 如果是全屏弹窗，特殊处理，只要输入框没被盖住，就不移动
            if (focusBottom + keyboardHeight < screenHeight) {
                return;
            }
        }
        if (pv instanceof FullScreenPopupView) {
            int overflowHeight = focusBottom + keyboardHeight - screenHeight;
            if (focusEt != null && overflowHeight > 0) {
                dy = overflowHeight;
            }
        } else if (pv instanceof CenterPopupView) {
            int popupBottom = (screenHeight + popupHeight) / 2;
            int targetY = popupBottom + keyboardHeight - screenHeight;
            if (focusEt != null && focusEtTop - targetY < 0) {
                targetY += focusEtTop - targetY - getStatusBarHeight(pv); // 限制不能被状态栏遮住
            }
            dy = Math.max(0, targetY);
        } else if (pv instanceof BottomPopupView) {
            dy = keyboardHeight;
            if (focusEt != null && focusEtTop - dy < 0) {
                dy += focusEtTop - dy - getStatusBarHeight(pv); // 限制不能被状态栏遮住
            }
        }

        // dy=0说明没有触发移动，有些弹窗有translationY，不能影响它们
        if (dy == 0 && pv.getPopupContentView().getTranslationY() != 0) {
            return;
        }

        int finalDy = dy;
        AnimatorValue animatorValue = new AnimatorValue();
        animatorValue.setDuration(200);
        animatorValue.setCurveType(Animator.CurveType.OVERSHOOT);
        animatorValue.setValueUpdateListener(new AnimatorValue.ValueUpdateListener() {
            @Override
            public void onUpdate(AnimatorValue animatorValue, float v) {
                pv.getPopupContentView().setTranslationY(-finalDy * v);
            }
        });
        animatorValue.start();
    }

    public static void moveDown(BasePopupView pv) {
        // 暂时忽略PartShadow弹窗和AttachPopupView
        if (pv instanceof PositionPopupView || pv instanceof AttachPopupView || pv instanceof BubbleAttachPopupView) {
            return;
        }
        Component animComponent;
        if (pv instanceof PartShadowPopupView) {
            animComponent = pv.getPopupImplView();
        } else {
            animComponent = pv.getPopupContentView();
        }
        float translationY = animComponent.getTranslationY();
        AnimatorValue animatorValue = new AnimatorValue();
        animatorValue.setDuration(100);
        animatorValue.setCurveType(Animator.CurveType.OVERSHOOT);
        animatorValue.setValueUpdateListener(new AnimatorValue.ValueUpdateListener() {
            @Override
            public void onUpdate(AnimatorValue animatorValue, float v) {
                pv.getPopupContentView().setTranslationY((1 - v) * translationY);
            }
        });
        animatorValue.start();
    }

    private static Context mContext;

    public static void saveBmpToAlbum(final Context context, final XPopupImageLoader imageLoader, final String url) {
        final EventHandler mainHandler = new EventHandler(EventRunner.getMainEventRunner());
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        mContext = context;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                File source = imageLoader.getImageFile(mContext, url);
                if (source == null) {
                    mainHandler.postTask(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showToast(mContext, "图片不存在！");
                            mContext = null;
                        }
                    });
                    return;
                }
                // 1. create path
                // 应该保存到SD卡的PICTURES目录，暂时保存到应用的缓存目录
                String dirPath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath();
                File dirFile = new File(dirPath);
                if (!dirFile.exists()) {
                    dirFile.mkdirs();
                }
                try {
                    ImageType type = ImageHeaderParser.getImageType(new FileInputStream(source));
                    String ext = getFileExt(type);
                    final File target = new File(dirPath, System.currentTimeMillis() + "." + ext);
                    if (target.exists()) {
                        target.delete();
                    }
                    target.createNewFile();
                    // 2. save
                    boolean writeFileFromIS = writeFileFromIS(target, new FileInputStream(source));
                    if (writeFileFromIS) {
                        mainHandler.postTask(new Runnable() {
                            @Override
                            public void run() {
                                if (mContext != null) {
                                    ToastUtil.showToast(mContext, "已保存到应用的缓存目录！");
                                    mContext = null;
                                }
                            }
                        });
                    }
                    // 3. notify
                    AVLoggerConnection.performLoggerFile(mContext, new String[]{target.getCanonicalPath()}, new String[]{"image/" + ext}, new AVLogCompletedListener() {
                        @Override
                        public void onLogCompleted(final String path, Uri uri) {
                            mainHandler.postTask(new Runnable() {
                                @Override
                                public void run() {
                                    if (mContext != null) {
                                        ToastUtil.showToast(mContext, "已保存到相册！");
                                        mContext = null;
                                    }
                                }
                            });
                        }
                    });
                } catch (IOException e) {
                    LogUtil.error(TAG, e.getMessage());
                    mainHandler.postTask(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showToast(mContext, "没有保存权限，保存功能无法使用！！");
                            mContext = null;
                        }
                    });
                }
            }
        });
    }

    private static String getFileExt(ImageType type) {
        switch (type) {
            case GIF:
                return "gif";
            case PNG:
            case PNG_A:
                return "png";
            case WEBP:
            case WEBP_A:
                return "webp";
            case JPEG:
                return "jpeg";
            default:
                break;
        }
        return "jpeg";
    }

    private static boolean writeFileFromIS(final File file, final InputStream is) {
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            byte[] data = new byte[8192];
            int len;
            while ((len = is.read(data, 0, 8192)) != -1) {
                os.write(data, 0, len);
            }
            return true;
        } catch (IOException e) {
            LogUtil.error(TAG, e.getMessage());
            return false;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                LogUtil.error(TAG, e.getMessage());
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                LogUtil.error(TAG, e.getMessage());
            }
        }
    }

    /**
     * 获取布局是否是从右到左的布局
     *
     * @param context 上下文
     * @return 布局是否是从右到左的布局
     */
    public static boolean isLayoutRtl(Context context) {
        return false;
    }

    /**
     * 获取圆角背景
     *
     * @param color  背景颜色
     * @param radius 四个角的圆角度数
     * @return 圆角背景
     */
    public static ShapeElement createDrawable(int color, float radius) {
        ShapeElement drawable = new ShapeElement();
        drawable.setShape(ShapeElement.RECTANGLE);
        drawable.setRgbColor(new RgbColor(color));
        drawable.setCornerRadius(radius);
        return drawable;
    }

    /**
     * 获取圆角背景
     *
     * @param color    背景颜色
     * @param tlRadius 左上角的圆角度数
     * @param trRadius 右上角的圆角度数
     * @param brRadius 左下角的圆角度数
     * @param blRadius 右下角的圆角度数
     * @return 圆角背景
     */
    public static ShapeElement createDrawable(int color, float tlRadius, float trRadius, float brRadius, float blRadius) {
        ShapeElement drawable = new ShapeElement();
        drawable.setShape(ShapeElement.RECTANGLE);
        drawable.setRgbColor(new RgbColor(color));
        drawable.setCornerRadiiArray(new float[]{
                tlRadius, tlRadius,
                trRadius, trRadius,
                brRadius, brRadius,
                blRadius, blRadius});
        return drawable;
    }

}
