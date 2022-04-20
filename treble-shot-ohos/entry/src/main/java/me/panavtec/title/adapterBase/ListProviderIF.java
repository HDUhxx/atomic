package me.panavtec.title.adapterBase;

import ohos.agp.components.LayoutScatter;
import ohos.app.Context;

import java.util.List;

public interface ListProviderIF<T>
{
//    void onDataSetChanged();

    List<T> onLoad();

    void onUpdate(List<T> passedItem);

//    Context getContext();

    int getCount();

//    LayoutScatter getScatter();

    List<T> getList();

    int getItemViewType(int position);
}