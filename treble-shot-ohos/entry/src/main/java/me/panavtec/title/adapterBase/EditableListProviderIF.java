package me.panavtec.title.adapterBase;

import com.ohos.trebleshot.exception.NotReadyException;
import com.ohos.trebleshot.object.Editable;

import java.util.List;

/**
 * created by: veli
 * date: 14/04/18 00:51
 */
public interface EditableListProviderIF<T extends Editable> extends ListProviderIF<T>
{
    boolean filterItem(T item);

    T getItem(int position);

    void notifyAllSelectionChanges();

    void notifyItemChanged(int position);

    void notifyItemRangeChanged(int positionStart, int itemCount);

    void syncSelectionList();

    void syncSelectionList(List<T> itemList);

    void setFragment(EditableListFractionIF<T> editableListFraction);
}
