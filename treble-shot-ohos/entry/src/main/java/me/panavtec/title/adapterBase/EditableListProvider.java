package me.panavtec.title.adapterBase;

import com.ohos.trebleshot.exception.NotReadyException;
import com.ohos.trebleshot.object.Editable;
import com.ohos.trebleshot.utils.AppUtils;
import me.panavtec.title.hmutils.DateUtils;
import me.panavtec.title.hmutils.FileUtils;
import me.panavtec.title.hmutils.MathUtils;
import me.panavtec.title.hmutils.TextUtils;
import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Component;
import ohos.app.Context;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * created by: Veli
 * date: 12.01.2018 16:55
 */

abstract public class EditableListProvider<T extends Editable, V extends EditableListProvider.EditableViewHolder>
        extends BaseItemProvider implements EditableListProviderIF<T> {
    public static final int VIEW_TYPE_DEFAULT = 0;

    public static final int MODE_SORT_BY_NAME = 100;
    public static final int MODE_SORT_BY_DATE = 110;
    public static final int MODE_SORT_BY_SIZE = 120;

    public static final int MODE_SORT_ORDER_ASCENDING = 100;
    public static final int MODE_SORT_ORDER_DESCENDING = 110;
    protected final Context mContext;

    private EditableListFractionIF<T> mFraction;
    private List<T> mItemList = new ArrayList<>();
    private int mSortingCriteria = MODE_SORT_BY_NAME;
    private int mSortingOrderAscending = MODE_SORT_ORDER_ASCENDING;
    private boolean mGridLayoutRequested = false;
    private Comparator<T> mGeneratedComparator;

    public EditableListProvider(Context context) {
        mContext = context;
    }

    @Override
    public void onUpdate(List<T> passedItem) {
        synchronized (getItemList()) {
            mItemList.clear();
            mItemList.addAll(passedItem);

            syncSelectionList(getItemList());
        }
    }

    public int compareItems(int sortingCriteria, int sortingOrder, T objectOne, T objectTwo) {
        return 1;
    }

    public boolean filterItem(T item) {
        String[] filteringKeywords = getFraction()
                .getFilteringDelegate()
                .getFilteringKeyword(getFraction());

        return filteringKeywords == null
                || filteringKeywords.length <= 0
                || item.applyFilter(filteringKeywords);
    }

    @Override
    public int getCount() {
        return getItemList().size();
    }

    public Comparator<T> getDefaultComparator() {
        if (mGeneratedComparator == null)
            mGeneratedComparator = new Comparator<T>() {
                Collator mCollator;

                public Collator getCollator() {
                    if (mCollator == null) {
                        mCollator = Collator.getInstance();
                        mCollator.setStrength(Collator.TERTIARY);
                    }

                    return mCollator;
                }

                @Override
                public int compare(T toCompare, T compareTo) {
                    boolean sortingAscending = getSortingOrder(toCompare, compareTo) == MODE_SORT_ORDER_ASCENDING;

                    T objectFirst = sortingAscending ? toCompare : compareTo;
                    T objectSecond = sortingAscending ? compareTo : toCompare;

                    if (objectFirst.comparisonSupported() == objectSecond.comparisonSupported()
                            && !objectFirst.comparisonSupported())
                        return 1;
                    else if (!toCompare.comparisonSupported())
                        return 1;
                    else if (!compareTo.comparisonSupported())
                        return -1;

                    // sorting direction is not used, so that the method doesn't have to guess which is used.
                    switch (getSortingCriteria(toCompare, compareTo)) {
                        case MODE_SORT_BY_DATE:
                            return MathUtils.compare(objectFirst.getComparableDate(), objectSecond.getComparableDate());
                        case MODE_SORT_BY_SIZE:
                            return MathUtils.compare(objectFirst.getComparableSize(), objectSecond.getComparableSize());
                        case MODE_SORT_BY_NAME:
                            return getCollator().compare(objectFirst.getComparableName(), objectSecond.getComparableName());
                        default:
                            return compareItems(getSortingCriteria(), getSortingOrder(), objectFirst, objectSecond);
                    }
                }
            };

        return mGeneratedComparator;
    }

    public EditableListFractionIF<T> getFraction() {
        return mFraction;
    }

    public void setFragment(EditableListFractionIF<T> fraction) {
        mFraction = fraction;
    }

    @Override
    public T getItem(int position) {
        if (position >= getCount() || position < 0) return null;
        return getList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<T> getItemList() {
        return mItemList;
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_DEFAULT;
    }

    @Override
    public List<T> getList() {
        return getItemList();
    }

    public String getSectionName(int position, T object) {
        switch (getSortingCriteria()) {
            case MODE_SORT_BY_NAME:
                return getSectionNameTrimmedText(object.getComparableName());
            case MODE_SORT_BY_DATE:
                return getSectionNameDate(object.getComparableDate());
            case MODE_SORT_BY_SIZE:
                return FileUtils.sizeExpression(object.getComparableSize(), false);
        }

        return String.valueOf(position);
    }

    public String getSectionNameDate(long date) {
        return String.valueOf(DateUtils.formatDateTime(mContext, date, DateUtils.FORMAT_SHOW_DATE));
    }

    public String getSectionNameTrimmedText(String text) {
        return TextUtils.trimText(text, 1).toUpperCase();
    }

    public int getSortingCriteria(T objectOne, T objectTwo) {
        return getSortingCriteria();
    }

    public int getSortingCriteria() {
        return mSortingCriteria;
    }

    public int getSortingOrder(T objectOne, T objectTwo) {
        return getSortingOrder();
    }

    public int getSortingOrder() {
        return mSortingOrderAscending;
    }

    public boolean isGridSupported() {
        return false;
    }

    public boolean isGridLayoutRequested() {
        return mGridLayoutRequested;
    }

    public void notifyAllSelectionChanges() {
        syncSelectionList();
        notifyDataChanged();
    }

    public boolean notifyGridSizeUpdate(int gridSize, boolean isScreenLarge) {
        return mGridLayoutRequested = (!isScreenLarge && gridSize > 1)
                || gridSize > 2;
    }

    public void setSortingCriteria(int sortingCriteria, int sortingOrder) {
        mSortingCriteria = sortingCriteria;
        mSortingOrderAscending = sortingOrder;
    }

    public synchronized void syncSelectionList() {
        synchronized (getItemList()) {
            syncSelectionList(getItemList());
        }
    }

    public synchronized void syncSelectionList(List<T> itemList) {
//        if (getFraction() == null || getFraction().getSelectionConnection() == null)
//            return;
//
//        for (T item : itemList)
//            item.setSelectableSelected(mFraction.getSelectionConnection().isSelected(item));
    }

    public static class EditableViewHolder {
        private Component mClickableView;

        public EditableViewHolder(Component itemView) {
        }

        public Component getClickableView() {
            return mClickableView == null ? null : mClickableView;
        }

        public EditableViewHolder setClickableView(int resId) {
            return null;//setClickableView(itemView.findComponentById(resId));
        }

        public EditableViewHolder setClickableView(Component clickableLayout) {
            mClickableView = clickableLayout;
            return this;
        }

        public int getAdapterPosition() {
            return 0;
        }

        public Component getComponent() {
            return null;
        }
    }
}
