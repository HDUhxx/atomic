package me.panavtec.title.adapterBase;

/**
 * created by: veli
 * date: 8/24/18 1:36 PM
 */
public interface EditableListFractionModelIF<V extends EditableListProvider.EditableViewHolder>
{
    void setLayoutClickListener(EditableListFraction.LayoutClickListener<V> clickListener);
}
