package com.example.doodle;

import ohos.agp.animation.AnimatorValue;
import ohos.agp.render.Path;
import ohos.agp.utils.Point;
import ohos.agp.utils.RectFloat;
import ohos.multimodalinput.event.MmiPoint;
import ohos.multimodalinput.event.TouchEvent;

import com.example.doodle.core.IDoodle;
import com.example.doodle.core.IDoodleItem;
import com.example.doodle.core.IDoodlePen;
import com.example.doodle.core.IDoodleSelectableItem;
import com.example.doodle.util.DrawUtil;
import com.example.doodle.util.LogUtil;

import java.util.List;


/**
 * DoodleView的涂鸦手势监听
 *
 * @since 2021-04-29
 */
public class DoodleOnTouchGestureListener extends TouchGestureDetector.OnTouchGestureListener {
    /**
     * VALUE
     */
    private static final float VALUE = 1f;

    // 触摸的相关信息
    private float mTouchX, mTouchY;
    private float mLastTouchX, mLastTouchY;
    private float mTouchDownX, mTouchDownY;

    // 缩放相关
    private Float mLastFocusX;
    private Float mLastFocusY;
    private float mTouchCentreX, mTouchCentreY;


    private float mStartX, mStartY;
    private float mRotateDiff; // 开始旋转item时的差值（当前item的中心点与触摸点的角度）

    private Path mCurrPath; // 当前手写的路径
    private DoodlePath mCurrDoodlePath;
    private CopyLocation mCopyLocation;

    private DoodleView mDoodle;

    // 动画相关
    private ValueAnimator mScaleAnimator;
    private float mScaleAnimTransX, mScaleAnimTranY;
    private ValueAnimator mTranslateAnimator;
    private float mTransAnimOldY, mTransAnimY;

    private IDoodleSelectableItem mSelectedItem; // 当前选中的item
    private ISelectionListener mSelectionListener;

    private boolean mSupportScaleItem = true;

    /**
     * 初始化
     *
     * @param doodle
     * @param listener
     */
    public DoodleOnTouchGestureListener(DoodleView doodle, ISelectionListener listener) {
        mDoodle = doodle;
        mCopyLocation = DoodlePen.COPY.getCopyLocation();
        mCopyLocation.reset();
        mCopyLocation.updateLocation(doodle.getPixelMap().getImageInfo().size.width / 2, doodle.getPixelMap().getImageInfo().size.height / 2);
        mSelectionListener = listener;
    }

    /**
     * setSelectedItem
     *
     * @param selectedItem
     */
    public void setSelectedItem(IDoodleSelectableItem selectedItem) {
        IDoodleSelectableItem old = mSelectedItem;
        mSelectedItem = selectedItem;

        if (old != null) { // 取消选定
            old.setSelected(false);
            if (mSelectionListener != null) {
                mSelectionListener.onSelectedItem(mDoodle, old, false);
            }
            mDoodle.notifyItemFinishedDrawing(old);
        }
        if (mSelectedItem != null) {
            mSelectedItem.setSelected(true);
            if (mSelectionListener != null) {
                mSelectionListener.onSelectedItem(mDoodle, mSelectedItem, true);
            }
            mDoodle.markItemToOptimizeDrawing(mSelectedItem);
        }

    }

    /**
     * getSelectedItem
     *
     * @return IDoodleSelectableItem
     */
    public IDoodleSelectableItem getSelectedItem() {
        return mSelectedItem;
    }

    @Override
    public boolean onDown(TouchEvent e) {
        MmiPoint mp = e.getPointerPosition(0);
        mTouchX = mTouchDownX = mp.getX();
        mTouchY = mTouchDownY = mp.getY();
        return true;
    }

    /**
     * 开始滚动
     *
     * @param event
     */
    @Override
    public void onScrollBegin(TouchEvent event) {
        MmiPoint mp = event.getPointerPosition(0);
        mLastTouchX = mp.getX();
        mLastTouchY = mp.getY();
        mDoodle.setScrollingDoodle(true);

        if (mDoodle.isEditMode() || isPenEditable(mDoodle.getPen())) {
            if (mSelectedItem != null) {
                Point xy = mSelectedItem.getLocation();
                mStartX = xy.getPointX();
                mStartY = xy.getPointY();
                if (mSelectedItem instanceof DoodleRotatableItemBase
                    && (((DoodleRotatableItemBase) mSelectedItem).canRotate(mDoodle.toX(mTouchX), mDoodle.toY(mTouchY)))) {
                    ((DoodleRotatableItemBase) mSelectedItem).setIsRotating(true);
                    mRotateDiff = mSelectedItem.getItemRotate() -
                        DrawUtil.computeAngle(mSelectedItem.getPivotX(), mSelectedItem.getPivotY(), mDoodle.toX(mTouchX), mDoodle.toY(mTouchY));
                }
            } else {
                if (mDoodle.isEditMode()) {
                    mStartX = mDoodle.getDoodleTranslationX();
                    mStartY = mDoodle.getDoodleTranslationY();
                }
            }
        } else {
            // 点击copy
            if (mDoodle.getPen() == DoodlePen.COPY && mCopyLocation.contains(mDoodle.toX(mTouchX), mDoodle.toY(mTouchY), mDoodle.getSize())) {
                mCopyLocation.setRelocating(true);
                mCopyLocation.setCopying(false);
            } else {
                if (mDoodle.getPen() == DoodlePen.COPY) {
                    mCopyLocation.setRelocating(false);
                    if (!mCopyLocation.isCopying()) {
                        mCopyLocation.setCopying(true);
                        mCopyLocation.setStartPosition(mDoodle.toX(mTouchX), mDoodle.toY(mTouchY));
                    }
                }

                // 初始化绘制
                mCurrPath = new Path();
                mCurrPath.moveTo(mDoodle.toX(mTouchX), mDoodle.toY(mTouchY));
                if (mDoodle.getShape() == DoodleShape.HAND_WRITE) { // 手写
                    mCurrDoodlePath = DoodlePath.toPath(mDoodle, mCurrPath);
                } else {  // 画图形
                    mCurrDoodlePath = DoodlePath.toShape(mDoodle,
                        mDoodle.toX(mTouchDownX), mDoodle.toY(mTouchDownY), mDoodle.toX(mTouchX), mDoodle.toY(mTouchY));
                }
                if (mDoodle.isOptimizeDrawing()) {
                    mDoodle.markItemToOptimizeDrawing(mCurrDoodlePath);
                } else {
                    mDoodle.addItem(mCurrDoodlePath);
                }
            }
        }
        mDoodle.refresh();
    }

    @Override
    public boolean onFling(TouchEvent e1, TouchEvent e2, float velocityX, float velocityY) {
        LogUtil.d("onFling", "onFling");
        return super.onFling(e1, e2, velocityX, velocityY);
    }

    @Override
    public void onScrollEnd(TouchEvent e) {
        LogUtil.e("onScrollEnd", "TouchEvent is null： " + (e == null));
        if (e == null) {
            return;
        }
        mLastTouchX = mTouchX;
        mLastTouchY = mTouchY;
        mTouchX = e.getPointerPosition(0).getX();
        mTouchY = e.getPointerPosition(0).getY();
        mDoodle.setScrollingDoodle(false);
        if (mDoodle.isEditMode() || isPenEditable(mDoodle.getPen())) {
            if (mSelectedItem instanceof DoodleRotatableItemBase) {
                ((DoodleRotatableItemBase) mSelectedItem).setIsRotating(false);
            }
            if (mDoodle.isEditMode()) {
                limitBound(true);
            }
        }

        if (mCurrDoodlePath != null) {
            if (mDoodle.isOptimizeDrawing()) {
                mDoodle.notifyItemFinishedDrawing(mCurrDoodlePath);
            }
            mCurrDoodlePath = null;
        }

        mDoodle.refresh();
    }

    @Override
    public boolean onScroll(TouchEvent e1, TouchEvent e2, float distanceX, float distanceY) {
        mLastTouchX = mTouchX;
        mLastTouchY = mTouchY;
        mTouchX = e2.getPointerPosition(0).getX();
        mTouchY = e2.getPointerPosition(0).getY();

        if (mDoodle.isEditMode() || isPenEditable(mDoodle.getPen())) { //画笔是否是可选择的
            if (mSelectedItem != null) {
                if ((mSelectedItem instanceof DoodleRotatableItemBase) && (((DoodleRotatableItemBase) mSelectedItem).isRotating())) { // 旋转item
                    mSelectedItem.setItemRotate(mRotateDiff + DrawUtil.computeAngle(
                        mSelectedItem.getPivotX(), mSelectedItem.getPivotY(), mDoodle.toX(mTouchX), mDoodle.toY(mTouchY)
                    ));
                } else { // 移动item
                    mSelectedItem.setLocation(
                        mStartX + mDoodle.toX(mTouchX) - mDoodle.toX(mTouchDownX),
                        mStartY + mDoodle.toY(mTouchY) - mDoodle.toY(mTouchDownY));
                }
            } else {
                if (mDoodle.isEditMode()) {
                    mDoodle.setDoodleTranslation(mStartX + mTouchX - mTouchDownX,
                        mStartY + mTouchY - mTouchDownY);
                }
            }
        } else {
            if (mDoodle.getPen() == DoodlePen.COPY && mCopyLocation.isRelocating()) {
                // 正在定位location
                mCopyLocation.updateLocation(mDoodle.toX(mTouchX), mDoodle.toY(mTouchY));
            } else {
                if (mDoodle.getPen() == DoodlePen.COPY) {
                    mCopyLocation.updateLocation(mCopyLocation.getCopyStartX() + mDoodle.toX(mTouchX) - mCopyLocation.getTouchStartX(),
                        mCopyLocation.getCopyStartY() + mDoodle.toY(mTouchY) - mCopyLocation.getTouchStartY());
                }
                if (mDoodle.getShape() == DoodleShape.HAND_WRITE) { // 手写
                    mCurrPath.quadTo(
                        mDoodle.toX(mLastTouchX),
                        mDoodle.toY(mLastTouchY),
                        mDoodle.toX((mTouchX + mLastTouchX) / 2),
                        mDoodle.toY((mTouchY + mLastTouchY) / 2));
                    mCurrDoodlePath.updatePath(mCurrPath);
                } else {  // 画图形
                    mCurrDoodlePath.updateXY(mDoodle.toX(mTouchDownX), mDoodle.toY(mTouchDownY), mDoodle.toX(mTouchX), mDoodle.toY(mTouchY));
                }
            }
        }
        mDoodle.refresh();
        return true;
    }

    // 判断当前画笔是否可编辑
    private boolean isPenEditable(IDoodlePen pen) {
        return (mDoodle.getPen() == DoodlePen.TEXT && pen == DoodlePen.TEXT)
            || (mDoodle.getPen() == DoodlePen.BITMAP && pen == DoodlePen.BITMAP);
    }

    @Override
    public boolean onSingleTapUp(TouchEvent e) {
        mLastTouchX = mTouchX;
        mLastTouchY = mTouchY;
        mTouchX = e.getPointerPosition(0).getX();
        mTouchY = e.getPointerPosition(0).getY();

        if (mDoodle.isEditMode()) {
            boolean found = false;
            IDoodleSelectableItem item;
            List<IDoodleItem> items = mDoodle.getAllItem();
            for (int i = items.size() - 1; i >= 0; i--) {
                IDoodleItem elem = items.get(i);
                if (!elem.isDoodleEditable()) {
                    continue;
                }

                if (!(elem instanceof IDoodleSelectableItem)) {
                    continue;
                }

                item = (IDoodleSelectableItem) elem;

                if (item.contains(mDoodle.toX(mTouchX), mDoodle.toY(mTouchY))) {
                    found = true;
                    setSelectedItem(item);
                    Point xy = item.getLocation();
                    mStartX = xy.getPointX();
                    mStartY = xy.getPointY();
                    break;
                }
            }
            if (!found) { // not found
                if (mSelectedItem != null) { // 取消选定
                    IDoodleSelectableItem old = mSelectedItem;
                    setSelectedItem(null);
                    if (mSelectionListener != null) {
                        mSelectionListener.onSelectedItem(mDoodle, old, false);
                    }
                }
            }
        } else if (isPenEditable(mDoodle.getPen())) {
            if (mSelectionListener != null) {
                mSelectionListener.onCreateSelectableItem(mDoodle, mDoodle.toX(mTouchX), mDoodle.toY(mTouchY));
            }
        } else {
            // 模拟一次滑动
            onScrollBegin(e);
            e.setScreenOffset(VALUE, VALUE);
            onScroll(e, e, VALUE, VALUE);
            onScrollEnd(e);
        }
        mDoodle.refresh();
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetectorApi27 detector) {
        mLastFocusX = null;
        mLastFocusY = null;
        return true;
    }

    private float pendingX, pendingY, pendingScale = 1;

    @Override
    public boolean onScale(ScaleGestureDetectorApi27 detector) {
        // 屏幕上的焦点
        mTouchCentreX = detector.getFocusX();
        mTouchCentreY = detector.getFocusY();

        if (mLastFocusX != null && mLastFocusY != null) { // 焦点改变
            final float dx = mTouchCentreX - mLastFocusX;
            final float dy = mTouchCentreY - mLastFocusY;
            // 移动图片
            if (Math.abs(dx) > 1 || Math.abs(dy) > 1) {
                if (mSelectedItem == null || !mSupportScaleItem) {
                    mDoodle.setDoodleTranslationX(mDoodle.getDoodleTranslationX() + dx + pendingX);
                    mDoodle.setDoodleTranslationY(mDoodle.getDoodleTranslationY() + dy + pendingY);
                } else {
                    // nothing
                }
                pendingX = pendingY = 0;
            } else {
                pendingX += dx;
                pendingY += dy;
            }
        }

        if (Math.abs(1 - detector.getScaleFactor()) > 0.005f) {
            if (mSelectedItem == null || !mSupportScaleItem) {
                // 缩放图片
                float scale = mDoodle.getDoodleScale() * detector.getScaleFactor() * pendingScale;
                mDoodle.setDoodleScale(scale, mDoodle.toX(mTouchCentreX), mDoodle.toY(mTouchCentreY));
            } else {
                mSelectedItem.setScale(mSelectedItem.getScale() * detector.getScaleFactor() * pendingScale);
            }
            pendingScale = 1;
        } else {
            pendingScale *= detector.getScaleFactor();
        }

        mLastFocusX = mTouchCentreX;
        mLastFocusY = mTouchCentreY;

        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetectorApi27 detector) {
        if (mDoodle.isEditMode()) {
            limitBound(true);
            return;
        }
        center();
    }

    /**
     * center
     */
    public void center() {
        if (mDoodle.getDoodleScale() < 1) { //
            if (mScaleAnimator == null) {
                mScaleAnimator = new ValueAnimator();
                mScaleAnimator.setDuration(100);
                mScaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(AnimatorValue animatorValue, float fraction, Object animatedValue) {
                        float value = (float) animatedValue;
                        mDoodle.setDoodleScale(value, mDoodle.toX(mTouchCentreX), mDoodle.toY(mTouchCentreY));
                        mDoodle.setDoodleTranslation(mScaleAnimTransX * (1 - fraction), mScaleAnimTranY * (1 - fraction));
                    }
                });
            }
            mScaleAnimator.cancel();
            mScaleAnimTransX = mDoodle.getDoodleTranslationX();
            mScaleAnimTranY = mDoodle.getDoodleTranslationY();
            mScaleAnimator.setFloatValues(mDoodle.getDoodleScale(), 1);
            mScaleAnimator.start();
        } else { //
            limitBound(true);
        }
    }

    /**
     * 限定边界
     *
     * @param anim 动画效果
     */
    public void limitBound(boolean anim) {
        if (mDoodle.getDoodleRotation() % 90 != 0) { // 只处理0,90,180,270
            return;
        }

        final float oldX = mDoodle.getDoodleTranslationX(), oldY = mDoodle.getDoodleTranslationY();
        RectFloat bound = mDoodle.getDoodleBound();
        float x = mDoodle.getDoodleTranslationX(), y = mDoodle.getDoodleTranslationY();
        float width = mDoodle.getCenterWidth() * mDoodle.getRotateScale(), height = mDoodle.getCenterHeight() * mDoodle.getRotateScale();

        // 上下都在屏幕内
        if (bound.getHeight() <= mDoodle.getHeight()) {
            if (mDoodle.getDoodleRotation() == 0 || mDoodle.getDoodleRotation() == 180) {
                y = (height - height * mDoodle.getDoodleScale()) / 2;
            } else {
                x = (width - width * mDoodle.getDoodleScale()) / 2;
            }
        } else {
            float heightDiffTop = bound.top;
            // 只有上在屏幕内
            if (bound.top > 0 && bound.bottom >= mDoodle.getHeight()) {
                if (mDoodle.getDoodleRotation() == 0 || mDoodle.getDoodleRotation() == 180) {
                    if (mDoodle.getDoodleRotation() == 0) {
                        y = y - heightDiffTop;
                    } else {
                        y = y + heightDiffTop;
                    }
                } else {
                    if (mDoodle.getDoodleRotation() == 90) {
                        x = x - heightDiffTop;
                    } else {
                        x = x + heightDiffTop;
                    }
                }
            } else if (bound.bottom < mDoodle.getHeight() && bound.top <= 0) { // 只有下在屏幕内
                float heightDiffBottom = mDoodle.getHeight() - bound.bottom;
                if (mDoodle.getDoodleRotation() == 0 || mDoodle.getDoodleRotation() == 180) {
                    if (mDoodle.getDoodleRotation() == 0) {
                        y = y + heightDiffBottom;
                    } else {
                        y = y - heightDiffBottom;
                    }
                } else {
                    if (mDoodle.getDoodleRotation() == 90) {
                        x = x + heightDiffBottom;
                    } else {
                        x = x - heightDiffBottom;
                    }
                }
            }
        }

        // 左右都在屏幕内
        if (bound.getWidth() <= mDoodle.getWidth()) {
            if (mDoodle.getDoodleRotation() == 0 || mDoodle.getDoodleRotation() == 180) {
                x = (width - width * mDoodle.getDoodleScale()) / 2;
            } else {
                y = (height - height * mDoodle.getDoodleScale()) / 2;
            }
        } else {
            float widthDiffLeft = bound.left;
            // 只有左在屏幕内
            if (bound.left > 0 && bound.right >= mDoodle.getWidth()) {
                if (mDoodle.getDoodleRotation() == 0 || mDoodle.getDoodleRotation() == 180) {
                    if (mDoodle.getDoodleRotation() == 0) {
                        x = x - widthDiffLeft;
                    } else {
                        x = x + widthDiffLeft;
                    }
                } else {
                    if (mDoodle.getDoodleRotation() == 90) {
                        y = y + widthDiffLeft;
                    } else {
                        y = y - widthDiffLeft;
                    }
                }
            } else if (bound.right < mDoodle.getWidth() && bound.left <= 0) { // 只有右在屏幕内
                float widthDiffRight = mDoodle.getWidth() - bound.right;
                if (mDoodle.getDoodleRotation() == 0 || mDoodle.getDoodleRotation() == 180) {
                    if (mDoodle.getDoodleRotation() == 0) {
                        x = x + widthDiffRight;
                    } else {
                        x = x - widthDiffRight;
                    }
                } else {
                    if (mDoodle.getDoodleRotation() == 90) {
                        y = y - widthDiffRight;
                    } else {
                        y = y + widthDiffRight;
                    }
                }
            }
        }
        if (anim) {
            if (mTranslateAnimator == null) {
                mTranslateAnimator = new ValueAnimator();
                mTranslateAnimator.setDuration(100);
                mTranslateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(AnimatorValue animatorValue, float fraction, Object animatedValue) {
                        float value = (float) animatedValue;
                        mDoodle.setDoodleTranslation(value, mTransAnimOldY + (mTransAnimY - mTransAnimOldY) * fraction);
                    }
                });
            }
            mTranslateAnimator.setFloatValues(oldX, x);
            mTransAnimOldY = oldY;
            mTransAnimY = y;
            mTranslateAnimator.start();
        } else {
            mDoodle.setDoodleTranslation(x, y);
        }
    }

    public void setSelectionListener(ISelectionListener doodleListener) {
        mSelectionListener = doodleListener;
    }

    public ISelectionListener getSelectionListener() {
        return mSelectionListener;
    }

    public void setSupportScaleItem(boolean supportScaleItem) {
        mSupportScaleItem = supportScaleItem;
    }

    public boolean isSupportScaleItem() {
        return mSupportScaleItem;
    }

    /**
     * ISelectionListener
     *
     * @since 2021-04-29
     */
    public interface ISelectionListener {
        /**
         * called when the item(such as text, texture) is selected/unselected.
         * item（如文字，贴图）被选中或取消选中时回调
         *
         * @param selected 是否选中，false表示从选中变成不选中
         * @param doodle
         * @param selectableItem
         */
        void onSelectedItem(IDoodle doodle, IDoodleSelectableItem selectableItem, boolean selected);

        /**
         * called when you click the view to create a item(such as text, texture).
         * 点击View中的某个点创建可选择的item（如文字，贴图）时回调
         *
         * @param doodle
         * @param x
         * @param y
         */
        void onCreateSelectableItem(IDoodle doodle, float x, float y);
    }

}
