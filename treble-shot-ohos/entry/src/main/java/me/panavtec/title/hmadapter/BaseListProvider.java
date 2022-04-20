package me.panavtec.title.hmadapter;

import me.panavtec.title.Entity.BaseSelectEntity;
import me.panavtec.title.hmutils.TextUtils;
import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.app.Context;

import java.util.ArrayList;
import java.util.List;

/** @noinspection unchecked*/
public abstract class BaseListProvider<T extends BaseSelectEntity, V extends BaseListProvider.BaseViewHolder> extends BaseItemProvider {
    private List<T> listData;
    protected final Context context;
    private final List<T> copyList = new ArrayList<>();

    public static final int VIEW_TYPE_DEFAULT = 0;

    public static final int MODE_SORT_BY_NAME = 100;
    public static final int MODE_SORT_BY_DATE = 110;
    public static final int MODE_SORT_BY_SIZE = 120;

    public static final int MODE_SORT_ORDER_ASCENDING = 100;
    public static final int MODE_SORT_ORDER_DESCENDING = 110;

    public BaseListProvider(List<T> listData, Context context) {
        this.listData = listData;
        this.context = context;
        copyList.addAll(listData);
    }


    public void setList(List<T> list) { listData = list;}

    @Override
    public int getCount() {
        return listData == null ? 0 : listData.size();
    }

    @Override
    public Object getItem(int position) {
        if (listData != null && position >= 0 && position < listData.size()) {
            return listData.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Component getComponent(int position, Component convertComponent, ComponentContainer componentContainer) {
        Component cpt;
        V viewHolder;
        if (convertComponent == null) {
            cpt = LayoutScatter.getInstance(context).parse(getItemLayout(), null, false);
            viewHolder = getViewHolder(cpt);
            cpt.setTag(viewHolder);
        } else {
            cpt = convertComponent;
            viewHolder = (V) cpt.getTag();
        }
        convertItem(position, listData, viewHolder);
        return cpt;
    }

    abstract int getItemLayout();

    abstract void convertItem(int position, List<T> data, V viewHolder);

    abstract V getViewHolder(Component component);

    public List<T> getListData() {
        return listData;
    }

    public abstract static class BaseViewHolder {
        public BaseViewHolder(Component component) {
        }
    }

    public String nullCheck(String s) {
        if (s == null) return "-";
        else return s;
    }

    public String nullCheck(String s, String defaultStr) {
        boolean b = s == null;
        if (s == null) {
            if (defaultStr != null) return defaultStr;
            else return "-";
        } else return s;
    }


    public void longClickItem(int position) {
        BaseSelectEntity transferEntity = listData.get(position);
        if (transferEntity == null) return;
        transferEntity.setSelected(!transferEntity.isSelected());
        notifyDataSetItemChanged(position);
    }

    public void clearSelected() {
        if (this.listData == null || this.listData.size() == 0) return;
        for (BaseSelectEntity preloadedGroup : this.listData) {
            preloadedGroup.setSelected(false);
        }
        notifyDataChanged();
    }


    private List<T> getFilterData(String s) {
        if (TextUtils.isEmpty(s)) return listData;
        ArrayList<T> needList = new ArrayList<>();
        if (copyList.size() == 0) copyList.addAll(listData);
        for (T baseSelectEntity : copyList) {
            String filterStr = baseSelectEntity.getFilterStr();
            if (TextUtils.isEmpty(filterStr)) continue;
            String s1 = s.toLowerCase();
            String s2 = filterStr.toLowerCase();
            if (s2.contains(s1)) needList.add(baseSelectEntity);
        }
        return needList;
    }


    public void setFilterData(String s) {
        if (!TextUtils.isEmpty(s)) {
            List<T> filterData = getFilterData(s);
            listData.clear();
            listData.addAll(filterData);
        } else {
            listData.clear();
            listData.addAll(copyList);
        }
        notifyDataChanged();
    }

    public List<T> getSelectedList() {
        if (listData == null || listData.size() == 0) return listData;
        ArrayList<T> objects = new ArrayList<>();
        for (int i = 0; i < listData.size(); i++) {
            T t = listData.get(i);
            if (t.isSelected()) objects.add(t);
        }
        return objects;
    }
}

