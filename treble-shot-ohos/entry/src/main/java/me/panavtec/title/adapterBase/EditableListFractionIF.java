package me.panavtec.title.adapterBase;

import com.ohos.trebleshot.object.Editable;
import ohos.agp.components.ListContainer;
import ohos.utils.net.Uri;

/**
 * created by: veli
 * date: 14/04/18 10:35
 */
public interface EditableListFractionIF<T extends Editable> extends ListFractionIF<T>
{
    boolean applyViewingChanges(int gridSize);

    void changeGridViewSize(int gridSize);

    void changeOrderingCriteria(int id);

    void changeSortingCriteria(int id);

    EditableListProviderIF<T> getEditableListProvider();

    EditableListFraction.FilteringDelegate<T> getFilteringDelegate();

    void setFilteringDelegate(EditableListFraction.FilteringDelegate<T> delegate);

    ListContainer getListContainer();

    int getOrderingCriteria();

//    PowerfulActionMode.SelectorConnection<T> getSelectionConnection();
//    void setSelectorConnection(PowerfulActionMode.SelectorConnection<T> selectionConnection);

    EditableListFraction.SelectionCallback<T> getSelectionCallback();

    void setSelectionCallback(EditableListFraction.SelectionCallback<T> selectionCallback);

    int getSortingCriteria();

    String getUniqueSettingKey(String setting);

    boolean isRefreshLocked();

    boolean isRefreshRequested();

    boolean isSortingSupported();

    boolean loadIfRequested();

    boolean openUri(Uri uri);

}
