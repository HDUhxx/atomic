package com.lxj.xpopupdemo.stackLayout;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.enums.PopupPosition;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopup.interfaces.SimpleCallback;
import com.lxj.xpopup.util.ToastUtil;
import com.lxj.xpopupdemo.DemoAbility;
import com.lxj.xpopupdemo.ResourceTable;
import com.lxj.xpopupdemo.custom.CustomAttachPopup;
import com.lxj.xpopupdemo.custom.CustomAttachPopup2;
import com.lxj.xpopupdemo.custom.CustomBubbleAttachPopup;
import com.lxj.xpopupdemo.custom.CustomEditTextBottomPopup;
import com.lxj.xpopupdemo.custom.CustomFullScreenPopup;
import com.lxj.xpopupdemo.custom.CustomHorizontalBubbleAttachPopup;
import com.lxj.xpopupdemo.custom.ListDrawerPopupView;
import com.lxj.xpopupdemo.custom.LoginPopup;
import com.lxj.xpopupdemo.custom.PagerBottomPopup;
import com.lxj.xpopupdemo.custom.PagerDrawerPopup;
import com.lxj.xpopupdemo.custom.QQMsgPopup;
import com.lxj.xpopupdemo.custom.ZhihuCommentPopup;
import ohos.agp.components.Component;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

/**
 * Description:
 * Create by lxj, at 2018/12/11
 */
public class QuickStartDemo extends BaseStackLayout implements Component.ClickedListener {

    private static final String TAG = QuickStartDemo.class.getName();

    public QuickStartDemo(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return ResourceTable.Layout_stacklayout_quickstart;
    }

    @Override
    public void init(final Component component) {
        component.findComponentById(ResourceTable.Id_btnShowConfirm).setClickedListener(this);
        component.findComponentById(ResourceTable.Id_btnLogin).setClickedListener(this);
        component.findComponentById(ResourceTable.Id_btnBindLayout).setClickedListener(this);
        component.findComponentById(ResourceTable.Id_btnShowInputConfirm).setClickedListener(this);
        component.findComponentById(ResourceTable.Id_btnShowCenterList).setClickedListener(this);
        component.findComponentById(ResourceTable.Id_btnShowCenterListWithCheck).setClickedListener(this);
        component.findComponentById(ResourceTable.Id_btnShowLoading).setClickedListener(this);
        component.findComponentById(ResourceTable.Id_btnShowBottomList).setClickedListener(this);
        component.findComponentById(ResourceTable.Id_btnShowBottomListWithCheck).setClickedListener(this);
        component.findComponentById(ResourceTable.Id_btnCustomBottomPopup).setClickedListener(this);
        component.findComponentById(ResourceTable.Id_btnPagerBottomPopup).setClickedListener(this);
        component.findComponentById(ResourceTable.Id_tv1).setClickedListener(this);
        component.findComponentById(ResourceTable.Id_tv2).setClickedListener(this);
        component.findComponentById(ResourceTable.Id_tv3).setClickedListener(this);
        component.findComponentById(ResourceTable.Id_btnAttachPopup1).setClickedListener(this);
        component.findComponentById(ResourceTable.Id_btnAttachPopup2).setClickedListener(this);
        component.findComponentById(ResourceTable.Id_btnShowDrawerLeft).setClickedListener(this);
        component.findComponentById(ResourceTable.Id_btnShowDrawerRight).setClickedListener(this);
        component.findComponentById(ResourceTable.Id_btnFullScreenPopup).setClickedListener(this);
        component.findComponentById(ResourceTable.Id_btnCustomEditPopup).setClickedListener(this);
        component.findComponentById(ResourceTable.Id_btnShowPosition1).setClickedListener(this);
        component.findComponentById(ResourceTable.Id_btnShowPosition2).setClickedListener(this);
        component.findComponentById(ResourceTable.Id_btnMultiPopup).setClickedListener(this);
        component.findComponentById(ResourceTable.Id_btnBubbleAttachPopup1).setClickedListener(this);
        component.findComponentById(ResourceTable.Id_btnBubbleAttachPopup2).setClickedListener(this);

        // 必须在事件发生前，调用这个方法来监视Commonent的触摸
        final XPopup.Builder builder = new XPopup.Builder(getContext())
                .watchView(component.findComponentById(ResourceTable.Id_btnShowAttachPoint));
        component.findComponentById(ResourceTable.Id_btnShowAttachPoint).setLongClickedListener(new LongClickedListener() {
            @Override
            public void onLongClicked(Component component) {
                XPopup.fixLongClick(component); // 能保证弹窗弹出后，下层的Commonent无法滑动
                builder.asAttachList(new String[]{"置顶11", "复制", "删除", "编辑编辑编辑编辑"
                        }, null,
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                toast("click " + text);
                            }
                        })
                        .show();
            }
        });
    }

    // 复用弹窗
    BasePopupView popupView1;
    BasePopupView popupView2;

    @Override
    public void onClick(Component component) {
        switch (component.getId()) {
            case ResourceTable.Id_btnShowConfirm: // 带确认和取消按钮的弹窗
                if (popupView1 == null) { // 复用弹窗
                    popupView1 = new XPopup.Builder(getContext())
                            .dismissOnBackPressed(false) // 点击返回键是否消失
                            .dismissOnTouchOutside(true) // 点击外部是否消失
                            .setPopupCallback(new DemoXPopupListener())
                            .isComponentMode(true, component) // Component实现模式
                            .keepScreenOn(true) //保持屏幕常亮
                            .asConfirm("哈哈", "床前明月光，疑是地上霜；举头望明月，低头思故乡。",
                                    "取消", "确定",
                                    new OnConfirmListener() {
                                        @Override
                                        public void onConfirm() {
                                            toast("click confirm");
                                        }
                                    }, null, false);
                }
                popupView1.show();
                break;
            case ResourceTable.Id_btnLogin: // Login弹窗
                new XPopup.Builder(getContext())
                        .dismissOnBackPressed(false) // 点击返回键是否消失
                        .dismissOnTouchOutside(true) // 点击外部是否消失
                        .asCustom(new LoginPopup(getContext()))
                        .show();
                break;
            case ResourceTable.Id_btnBindLayout: // 复用项目中已有布局，使用XPopup已有的交互能力
                if (popupView2 == null) { // 复用弹窗
                    popupView2 = new XPopup.Builder(getContext())
                            .setPopupCallback(new DemoXPopupListener())
                            .asConfirm("复用项目已有布局", "您可以复用项目已有布局，来使用XPopup强大的交互能力和逻辑封装，弹窗的布局完全由你自己控制。\n" +
                                            "注意：你自己的布局必须提供一些控件Id，否则XPopup找不到控件。",
                                    "关闭", "XPopup牛逼",
                                    new OnConfirmListener() {
                                        @Override
                                        public void onConfirm() {
                                            toast("click confirm");
                                        }
                                    }, null, false, ResourceTable.Layout_my_confim_popup); // 最后一个参数绑定已有布局
                }
                popupView2.show();
                break;
            case ResourceTable.Id_btnShowInputConfirm: // 带确认和取消按钮，输入框的弹窗
                new XPopup.Builder(getContext())
                        .hasStatusBarShadow(false) // 暂无实现
                        .autoOpenSoftInput(true)
                        .isDarkTheme(true)
                        .setComponent(component) // 用于获取页面根容器，监听页面高度变化，解决输入法盖住弹窗的问题
                        .asInputConfirm("我是标题", null, null, "我是默认Hint文字",
                                new OnInputConfirmListener() {
                                    @Override
                                    public void onConfirm(String text) {
                                        toast("input text: " + text);
                                    }
                                })
                        .show();
                break;
            case ResourceTable.Id_btnShowCenterList: // 在中间弹出的List列表弹窗
                new XPopup.Builder(getContext())
                        .isDarkTheme(true)
                        .asCenterList("请选择一项", new String[]{"条目1", "条目2", "条目3", "条目4"},
                                new OnSelectListener() {
                                    @Override
                                    public void onSelect(int position, String text) {
                                        toast("click " + text);
                                    }
                                })
                        .show();
                break;
            case ResourceTable.Id_btnShowCenterListWithCheck: // 在中间弹出的List列表弹窗，带选中效果
                new XPopup.Builder(getContext())
                        .maxHeight(800)
                        .asCenterList("请选择一项", new String[]{"条目1", "条目2", "条目3", "条目4", "条目5", "条目6", "条目7", "条目8"},
                                null, 1,
                                new OnSelectListener() {
                                    @Override
                                    public void onSelect(int position, String text) {
                                        toast("click " + text);
                                    }
                                })
                        .show();
                break;
            case ResourceTable.Id_btnShowLoading:
                final LoadingPopupView loadingPopup = (LoadingPopupView) new XPopup.Builder(getContext())
                        .dismissOnBackPressed(false)
                        .asLoading("加载中")
                        .show();
                getContext().getUITaskDispatcher().delayDispatch(new Runnable() {
                    @Override
                    public void run() {
                        loadingPopup.setTitle("加载中长度变化啊");
                        getContext().getUITaskDispatcher().delayDispatch(new Runnable() {
                            @Override
                            public void run() {
                                loadingPopup.setTitle("");
                            }
                        }, 2000);
                    }
                }, 2000);
                loadingPopup.delayDismissWith(6000, new Runnable() {
                    @Override
                    public void run() {
                        toast("我消失了！！！");
                    }
                });
                break;
            case ResourceTable.Id_btnShowBottomList: // 从底部弹出，带手势拖拽的列表弹窗
                new XPopup.Builder(getContext())
                        .isDarkTheme(true)
                        .enableDrag(true)
                        .asBottomList("请选择一项", new String[]{"条目1", "条目2", "条目3", "条目4", "条目5", "条目6", "条目7"},
                                new OnSelectListener() {
                                    @Override
                                    public void onSelect(int position, String text) {
                                        toast("click " + text);
                                    }
                                })
                        .show();
                break;
            case ResourceTable.Id_btnShowBottomListWithCheck: // 从底部弹出，带选中效果
                new XPopup.Builder(getContext())
                        .isDestroyOnDismiss(true) // 对于只使用一次的弹窗，推荐设置这个
                        .asBottomList("标题可以没有", new String[]{"条目1", "条目2", "条目3", "条目4", "条目5"},
                                null, 2,
                                new OnSelectListener() {
                                    @Override
                                    public void onSelect(int position, String text) {
                                        toast("click " + text);
                                    }
                                })
                        .show();
                break;
            case ResourceTable.Id_btnCustomBottomPopup: // 自定义的底部弹窗
                new XPopup.Builder(getContext())
                        .moveUpToKeyboard(false) // 如果不加这个，评论弹窗会移动到软键盘上面
                        .enableDrag(true)
                        .asCustom(new ZhihuCommentPopup(getContext()))
                        .show();
                break;
            case ResourceTable.Id_btnPagerBottomPopup: // 自定义的底部弹窗
                new XPopup.Builder(getContext())
                        .moveUpToKeyboard(false) // 如果不加这个，评论弹窗会移动到软键盘上面
                        .asCustom(new PagerBottomPopup(getContext()))
                        .show();
                break;
            case ResourceTable.Id_tv1: // 依附于某个Commonent的Attach类型弹窗
            case ResourceTable.Id_tv2:
            case ResourceTable.Id_tv3:
                new XPopup.Builder(getContext())
                        .hasShadowBg(false)
                        .isDestroyOnDismiss(true) // 对于只使用一次的弹窗，推荐设置这个
                        .atView(component)  // 依附于所点击的Commonent，内部会自动判断在上方或者下方显示
                        .isComponentMode(true, component) // Component实现模式
                        .asAttachList(new String[]{"分享", "编辑", "不带icon不带icon", "分享分享分享"},
                                new int[]{ResourceTable.Media_icon, ResourceTable.Media_icon},
                                new OnSelectListener() {
                                    @Override
                                    public void onSelect(int position, String text) {
                                        toast("click " + text);
                                    }
                                }, 0, 0).show();
                break;
            case ResourceTable.Id_btnAttachPopup1: // 水平方向的Attach弹窗，就像微信朋友圈的点赞弹窗那样
                new XPopup.Builder(getContext())
                        .hasShadowBg(false) // 去掉半透明背景
                        .atView(component)
                        .isComponentMode(true, component) // Component实现模式
                        .asCustom(new CustomAttachPopup(getContext()))
                        .show();
                break;
            case ResourceTable.Id_btnAttachPopup2:
                new XPopup.Builder(getContext())
                        .isDestroyOnDismiss(true) // 对于只使用一次的弹窗，推荐设置这个
                        .atView(component)
                        .hasShadowBg(false) // 去掉半透明背景
                        .isComponentMode(true, component) // Component实现模式
                        .asCustom(new CustomAttachPopup2(getContext())).show();
                break;
            case ResourceTable.Id_btnBubbleAttachPopup1: // 水平方向带气泡弹窗
                new XPopup.Builder(getContext())
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .atView(component)
                        .hasShadowBg(false) // 去掉半透明背景
                        .isComponentMode(true, component) // Component实现模式
                        .asCustom(new CustomHorizontalBubbleAttachPopup(getContext()))
                        .show();
                break;
            case ResourceTable.Id_btnBubbleAttachPopup2: // 垂直方向带气泡弹窗
                new XPopup.Builder(getContext())
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .atView(component)
                        .hasShadowBg(false) // 去掉半透明背景
                        .isComponentMode(true, component) // Component实现模式
                        .asCustom(new CustomBubbleAttachPopup(getContext()))
                        .show();
                break;
            case ResourceTable.Id_btnShowDrawerLeft: // 像DrawerLayout一样的Drawer弹窗
                new XPopup.Builder(getContext())
                        .enableDrag(true)
                        .asCustom(new PagerDrawerPopup(getContext()))
                        .show();
                break;
            case ResourceTable.Id_btnShowDrawerRight:
                new XPopup.Builder(getContext())
                        .dismissOnTouchOutside(false)
                        .popupPosition(PopupPosition.Right) // 右边
                        .hasStatusBarShadow(true) // 启用状态栏阴影
                        .asCustom(new ListDrawerPopupView(getContext()))
                        .show();
                break;
            case ResourceTable.Id_btnFullScreenPopup: // 全屏弹窗，看起来像Ability
                new XPopup.Builder(getContext())
                        .hasStatusBarShadow(true)
                        .autoOpenSoftInput(true)
                        .setComponent(component) // 用于获取页面根容器，监听页面高度变化，解决输入法盖住弹窗的问题
                        .asCustom(new CustomFullScreenPopup(getContext()))
                        .show();
                break;
            case ResourceTable.Id_btnCustomEditPopup: // 自定义依附在输入法之上的Bottom弹窗
                new XPopup.Builder(getContext())
                        .autoOpenSoftInput(true)
                        .isDestroyOnDismiss(true) // 对于只使用一次的弹窗，推荐设置这个
                        .setComponent(component) // 用于获取页面根容器，监听页面高度变化，解决输入法盖住弹窗的问题
                        .asCustom(new CustomEditTextBottomPopup(getContext()))
                        .show();
                break;
            case ResourceTable.Id_btnShowPosition1:
                new XPopup.Builder(getContext())
                        .offsetY(300)
                        .offsetX(-100)
                        .popupAnimation(PopupAnimation.TranslateFromLeft)
                        .asCustom(new QQMsgPopup(getContext()))
                        .show();
                break;
            case ResourceTable.Id_btnShowPosition2:
                new XPopup.Builder(getContext())
                        .isCenterHorizontal(true)
                        .offsetY(200)
                        .asCustom(new QQMsgPopup(getContext()))
                        .show();
                break;
            case ResourceTable.Id_btnMultiPopup:
                startAbility(getContext(), DemoAbility.class);
                break;
            default:
                break;
        }
    }

    class DemoXPopupListener extends SimpleCallback {
        @Override
        public void onCreated(BasePopupView pv) {
            loge("tag", "onCreater");
        }

        @Override
        public void onShow(BasePopupView popupView) {
            loge("tag", "onShow");
        }

        @Override
        public void onDismiss(BasePopupView popupView) {
            loge("tag", "onDismiss");
        }

        @Override
        public void beforeDismiss(BasePopupView popupView) {
            loge("tag", "beforeDismiss");
        }

        // 如果你自己想拦截返回按键事件，则重写这个方法，返回true即可
        @Override
        public boolean onBackPressed(BasePopupView popupView) {
            loge("tag", "拦截的返回按键，按返回键XPopup不会关闭了");
            ToastUtil.showToast(getContext(), "onBackPressed返回true，拦截了返回按键，按返回键XPopup不会关闭了");
            return true;
        }

        @Override
        public void onKeyBoardStateChanged(BasePopupView popupView, int height) {
            super.onKeyBoardStateChanged(popupView, height);
            loge("tag", "onKeyBoardStateChanged height: " + height);
        }
    }

    private void loge(String tag, String msg) {
        HiLog.error(new HiLogLabel(3, 0, "systembartint"), "%{public}s: %{public}s", tag, msg);
    }

}
