package me.panavtec.title.adapterBase;

import com.ohos.trebleshot.object.Editable;
import com.ohos.trebleshot.service.preference.SharedPreferences;
import com.ohos.trebleshot.utils.AppUtils;
import com.ohos.trebleshot.utils.FileUtils;
import me.panavtec.title.ResourceTable;
import ohos.aafwk.content.Intent;
import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.app.Context;
import ohos.utils.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.panavtec.title.hmutils.Toast.LENGTH_LONG;

/**
 * created by: Veli
 * date: 21.11.2017 10:12
 */

abstract public class EditableListFraction<T extends Editable, V extends EditableListProvider.EditableViewHolder, E extends EditableListProvider<T, V>>
        extends ListFraction implements EditableListFractionIF<T>, EditableListFractionModelIF<V> {
    private SelectionCallback<T> mSelectionCallback;
    private SelectionCallback<T> mDefaultSelectionCallback;
//    private PowerfulActionMode.SelectorConnection<T> mSelectionConnection;
//    private PowerfulActionMode.SelectorConnection<T> mDefaultSelectionConnection;
    private EditableListProvider mEditableListProviderIF;
    private FilteringDelegate<T> mFilteringDelegate;
    private Snackbar mRefreshDelayedSnackbar;
    private boolean mRefreshRequested = false;
    private boolean mSortingSupported = true;
    private boolean mFilteringSupported = false;
    private boolean mUseDefaultPaddingDecoration = false;
    private boolean mUseDefaultPaddingDecorationSpaceForEdges = true;
    private float mDefaultPaddingDecorationSize = -1;
    private int mDefaultOrderingCriteria = EditableListProvider.MODE_SORT_ORDER_ASCENDING;
    private int mDefaultSortingCriteria = EditableListProvider.MODE_SORT_BY_NAME;
    private int mDefaultViewingGridSize = 1;
    private int mDefaultViewingGridSizeLandscape = 1;
    private int mDividerResId = 0;//ResourceTable.Id_abstract_layout_fast_scroll_recyclerview_bottom_divider;
    private Map<String, Integer> mSortingOptions = new HashMap<>();
    private Map<String, Integer> mOrderingOptions = new HashMap<>();
//    private ContentObserver mObserver;
    private LayoutClickListener<V> mLayoutClickListener;
    private String mSearchText;
    private FilteringDelegate<T> mDefaultFilteringDelegate = new FilteringDelegate<T>() {
        @Override
        public boolean changeFilteringKeyword(String keyword) {
            mSearchText = keyword;
            return true;
        }


        @Override
        public String[] getFilteringKeyword(EditableListFractionIF<T> listFragment) {
            if (mSearchText != null && mSearchText.length() > 0)
                return mSearchText.split(" ");

            return null;
        }
    };

    public EditableListFraction() {
        super();
    }

    abstract public boolean onDefaultClickAction(V holder);

    public boolean onDefaultLongClickAction(V holder) {
        return false;
    }

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        getListProvider().setFragment(this);
    }


    public void setEditableListProvider(EditableListProvider editableListProviderIF) {
        mEditableListProviderIF = editableListProviderIF;
    }

    public EditableListProvider getListProvider() {
        return mEditableListProviderIF;
    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        if (getPowerfulActionMode() != null && getSelectionCallback() != null)
//            setDefaultSelectionConnection(new PowerfulActionMode.SelectorConnection<>(getPowerfulActionMode(), getSelectionCallback()));
//
//        //setHasOptionsMenu(true);
//
//        if (mUseDefaultPaddingDecoration) {
//            float padding = mDefaultPaddingDecorationSize > -1
//                    ? mDefaultPaddingDecorationSize
//                    : getResourceManager().getDimension(ResourceTable.dimen.padding_list_content_parent_layout);
//
//            getListContainer().addItemDecoration(new PaddingItemDecoration((int) padding,
//                    mUseDefaultPaddingDecorationSpaceForEdges, isHorizontalOrientation()));
//        }
//    }
//
//    @Override
//    public void onViewCreated(Component component, Bundle savedInstanceState) {
//        super.onViewCreated(component, savedInstanceState);
//
//        getListProvider().notifyGridSizeUpdate(getViewingGridSize(), isScreenLarge());
//        getListProvider().setSortingCriteria(getSortingCriteria(), getOrderingCriteria());
//
//        // We have to recreate the provider class because old one loses its ground
//        getFastScroller().setViewProvider(new LongTextBubbleFastScrollViewProvider());
//        setDividerVisible(true);
//        getListContainer().addOnItemTouchListener(new SwipeTouchSelectionListener<>(this));
//    }
//
//    @Override
//    protected ListContainer onListContainer(Component mainContainer, Component listViewContainer) {
//        super.onListContainer(mainContainer, listViewContainer);
//        Component component =
//        LayoutScatter.getInstance(getContext()).parse(0, null, false);
//
//        Component recyclerViewContainer = component.findComponentById(0);
//        ListContainer listContainer = onListContainer(recyclerViewContainer);
//        mFastScroller = component.findComponentById(0);
//
//        // TODO: 1/18/19 Something like onSetListView method would be more safe to set the layout manager etc.
//        listContainer.setLayoutManager(onLayoutManager());
//
//        listViewContainer.add(component);
//
//        return listContainer;
//    }

    @Override
    protected Component onListContainer(ComponentContainer componentContainer, ComponentContainer listViewContainer) {
        Component component = LayoutScatter.getInstance(getContext())
                .parse(0, null, false);

        ((ComponentContainer)componentContainer).addComponent(component);

        return component;
    }

    @Override
    public boolean onSetListProvider(ListProviderIF adapter) {
//        if (super.onSetListProvider(adapter)) {
//            mFastScroller.setRecyclerView(getListContainer());
            return true;
//        }
    }

    @Override
    public void onActive() {
        super.onActive();
        refreshList();
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.parse(0, menu);
//
//        MenuItem filterItem = menu.findItem(0);
//
//        if (filterItem != null) {
//            filterItem.setVisible(mFilteringSupported);
//
//            if (mFilteringSupported) {
//                Component component = filterItem.getActionView();
//
//                if (component instanceof SearchView) {
//                    ((SearchView) component).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                        @Override
//                        public boolean onQueryTextSubmit(String query) {
//                            refreshList();
//                            return true;
//                        }
//
//                        @Override
//                        public boolean onQueryTextChange(String newText) {
//                            mSearchText = newText;
//                            refreshList();
//                            return true;
//                        }
//                    });
//                }
//            }
//        }
//
//        MenuItem gridSizeItem = menu.findItem(0);
//
//        if (gridSizeItem != null) {
//            Menu gridSizeMenu = gridSizeItem.getSubMenu();
//
//            for (int i = 1; i < (isScreenLandscape() ? 7 : 5); i++)
//                gridSizeMenu.add(0, 0, i,
//                        getContext().getResourceManager().getQuantityString(ResourceTable.plurals.text_gridRow, i, i));
//
//            gridSizeMenu.setGroupCheckable(0, true, true);
//        }
//
//        Map<String, Integer> sortingOptions = new HashMap<>();
//        onSortingOptions(sortingOptions);
//
//        if (sortingOptions.size() > 0) {
//            mSortingOptions.clear();
//            mSortingOptions.putAll(sortingOptions);
//
//            applyDynamicMenuItems(menu.findItem(0),
//                    0, mSortingOptions);
//
//            Map<String, Integer> orderingOptions = new HashMap<>();
//            onOrderingOptions(orderingOptions);
//
//            if (orderingOptions.size() > 0) {
//                mOrderingOptions.clear();
//                mOrderingOptions.putAll(orderingOptions);
//
//                applyDynamicMenuItems(menu.findItem(0),
//                        0, mOrderingOptions);
//            }
//        }
//    }
//
////    @Override
//    public void onPrepareOptionsMenu(Menu menu) {
////        super.onPrepareOptionsMenu(menu);
//
//        menu.findItem(0)
//                .setEnabled(isSortingSupported());
//
//        MenuItem multiSelect = menu.findItem(0);
//
//        if (multiSelect != null
//                && (getSelectionConnection() == null
//                || !getSelectionConnection().getMode().getEngineToolbar().isFinishAllowed()))
//            multiSelect.setVisible(false);
//
//        if (!getListProvider().isGridSupported())
//            menu.findItem(0)
//                    .setVisible(false);
//
//        MenuItem sortingItem = menu.findItem(0);
//
//        if (sortingItem != null) {
//            sortingItem.setVisible(mSortingSupported);
//
//            if (sortingItem.isVisible()) {
//                checkPreferredDynamicItem(sortingItem, getSortingCriteria(), mSortingOptions);
//
//                MenuItem orderingItem = menu.findItem(0);
//
//                if (orderingItem != null)
//                    checkPreferredDynamicItem(orderingItem, getOrderingCriteria(),
//                            mOrderingOptions);
//            }
//        }
//
//        MenuItem gridSizeItem = menu.findItem(0);
//
//        if (gridSizeItem != null) {
//            Menu gridRowMenu = gridSizeItem.getSubMenu();
//            int currentRow = getViewingGridSize() - 1;
//
//            if (currentRow < gridRowMenu.size())
//                gridRowMenu.getItem(currentRow).setChecked(true);
//        }
//    }
//
//    //@Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        int groupId = item.getGroupId();
//
//        if (id == ResourceTable.Id_actions_abs_editable_multi_select && getSelectionCallback() != null)
//            getSelectionConnection().getMode().start(getSelectionCallback());
//        else if (groupId == 0)
//            changeSortingCriteria(item.getOrder());
//        else if (groupId == 0)
//            changeOrderingCriteria(item.getOrder());
//        else if (groupId == 0)
//            changeGridViewSize(item.getOrder());
//
//        return super.onOptionsItemSelected(item);
//    }

    public void onSortingOptions(Map<String, Integer> options) {
        options.put(getString(ResourceTable.String_text_sortByName), EditableListProvider.MODE_SORT_BY_NAME);
        options.put(getString(ResourceTable.String_text_sortByDate), EditableListProvider.MODE_SORT_BY_DATE);
        options.put(getString(ResourceTable.String_text_sortBySize), EditableListProvider.MODE_SORT_BY_SIZE);
    }

    public void onOrderingOptions(Map<String, Integer> options) {
        options.put(getString(ResourceTable.String_text_sortOrderAscending),
                EditableListProvider.MODE_SORT_ORDER_ASCENDING);
        options.put(getString(ResourceTable.String_text_sortOrderDescending),
                EditableListProvider.MODE_SORT_ORDER_DESCENDING);
    }

//    @Override
//    public void onPrepareDetach() {
//        if (getPowerfulActionMode() != null && getSelectionCallback() != null)
//            getPowerfulActionMode().finish(getSelectionCallback());
//    }

    public int onGridSpanSize(int viewType, int currentSpanSize) {
        return 1;
    }

//    @Override
//    public LayoutManager onLayoutManager() {
//        final LayoutManager defaultLayoutManager = super.onLayoutManager();
//        final int preferredGridSize = getViewingGridSize();
//        final int optimalGridSize = preferredGridSize > 1 ? preferredGridSize
//                : !getListProvider().isGridSupported() && isScreenLarge()
//                && !isHorizontalOrientation() ? 2 : 1;
//
//        final TableLayoutManager layoutManager;
//
//        if (defaultLayoutManager instanceof TableLayoutManager) {
//            layoutManager = (TableLayoutManager) defaultLayoutManager;
//            layoutManager.setSpanCount(optimalGridSize);
//        } else
//            layoutManager = new TableLayoutManager(getContext(), optimalGridSize);
//
//        layoutManager.setSpanSizeLookup(new TableLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                // should be reserved so it can occupy all the available space of a line
//                int viewType = getListProvider().getItemViewType(position);
//
//                return viewType == EditableListProvider.VIEW_TYPE_DEFAULT
//                        ? 1
//                        : onGridSpanSize(viewType, optimalGridSize);
//            }
//        });
//
//        return layoutManager;
//    }

//    protected void applyDynamicMenuItems(MenuItem mainItem, int groupId,
//                                         Map<String, Integer> options) {
//        if (mainItem != null) {
//            mainItem.setVisible(true);
//
//            Menu dynamicMenu = mainItem.getSubMenu();
//
//            for (String currentKey : options.keySet()) {
//                int modeId = options.get(currentKey);
//                dynamicMenu.add(groupId, 0, modeId, currentKey);
//            }
//
//            dynamicMenu.setGroupCheckable(groupId, true, true);
//        }
//    }

    public boolean applyViewingChanges(int gridSize) {
        if (!getListProvider().isGridSupported())
            return false;

        getListProvider().notifyGridSizeUpdate(gridSize, true);

//        getListContainer().setLayoutManager(onLayoutManager());
        getListContainer().setItemProvider((BaseItemProvider)getListProvider());

        refreshList();

        return true;
    }

    private boolean isScreenLandscape() { return  false; }

    public void changeGridViewSize(int gridSize) {
        getViewPreferences().edit()
                .putInt(getUniqueSettingKey("GridSize" + (isScreenLandscape() ? "Landscape" : "")),
                        gridSize)
                .apply();

        applyViewingChanges(gridSize);
    }

    public void changeOrderingCriteria(int id) {
        getViewPreferences().edit()
                .putInt(getUniqueSettingKey("SortOrder"), id)
                .apply();

        getListProvider().setSortingCriteria(getSortingCriteria(), id);

        refreshList();
    }

    public void changeSortingCriteria(int id) {
        getViewPreferences().edit()
                .putInt(getUniqueSettingKey("SortBy"), id)
                .apply();

        getListProvider().setSortingCriteria(id, getOrderingCriteria());

        refreshList();
    }

//    public void checkPreferredDynamicItem(MenuItem dynamicItem, int preferredItemId, Map<String,
//            Integer> options) {
//        if (dynamicItem != null) {
//            Menu gridSizeMenu = dynamicItem.getSubMenu();
//
//            for (String title : options.keySet()) {
//                if (options.get(title) == preferredItemId) {
//                    MenuItem menuItem;
//                    int iterator = 0;
//
//                    while ((menuItem = gridSizeMenu.getItem(iterator)) != null) {
//                        if (title.equals(String.valueOf(menuItem.getTitle()))) {
//                            menuItem.setChecked(true);
//                            return;
//                        }
//
//                        iterator++;
//                    }
//
//                    // normally we should not be here
//                    return;
//                }
//            }
//        }
//    }

    public EditableListProviderIF<T> getEditableListProvider() {
        return getListProvider();
    }

//
//    public ContentObserver getDefaultContentObserver() {
//        if (mObserver == null)
//            mObserver = new ContentObserver(new EventHandler(EventRunner.getMainEventRunner())) {
//                @Override
//                public boolean deliverSelfNotifications() {
//                    return true;
//                }
//
//                @Override
//                public void onChange(boolean selfChange) {
//                    refreshList();
//                }
//            };
//
//        return mObserver;
//    }

    @Override
    public FilteringDelegate<T> getFilteringDelegate() {
        return mFilteringDelegate == null
                ? mDefaultFilteringDelegate
                : mFilteringDelegate;
    }

    @Override
    public void setFilteringDelegate(FilteringDelegate<T> delegate) {
        mFilteringDelegate = delegate;
    }

//    public FastScroller getFastScroller() {
//        return mFastScroller;
//    }

    public int getOrderingCriteria() {
        return getViewPreferences().getInt(getUniqueSettingKey("SortOrder"),
                mDefaultOrderingCriteria);
    }

    public String getUniqueSettingKey(String setting) {
        return getClass().getSimpleName() + "_" + setting;
    }

//    public PowerfulActionMode.SelectorConnection<T> getSelectionConnection() {
//        return mSelectionConnection == null
//                ? mDefaultSelectionConnection
//                : mSelectionConnection;
//    }

    public SelectionCallback<T> getSelectionCallback() {
        return mSelectionCallback == null
                ? mDefaultSelectionCallback
                : mSelectionCallback;
    }

    public void setSelectionCallback(SelectionCallback<T> selectionCallback) {
        mSelectionCallback = selectionCallback;
    }

    public int getSortingCriteria() {
        return getViewPreferences().getInt(getUniqueSettingKey("SortBy"), mDefaultSortingCriteria);
    }

//    public PowerfulActionMode getPowerfulActionMode() {
//        return getActivity() != null && getActivity() instanceof PowerfulActionModeSupport
//                ? ((PowerfulActionModeSupport) getActivity()).getPowerfulActionMode()
//                : null;
//    }

    public SharedPreferences getViewPreferences() {
        return AppUtils.getViewingPreferences(getContext());
    }

    public int getViewingGridSize() {
        if (getViewPreferences() == null)
            return 1;

        return isScreenLandscape()
                ? getViewPreferences().getInt(getUniqueSettingKey("GridSizeLandscape"),
                mDefaultViewingGridSizeLandscape)
                : getViewPreferences().getInt(getUniqueSettingKey("GridSize"),
                mDefaultViewingGridSize);
    }

//    public int getActiveViewingGridSize() {
//        return getListContainer().getLayoutManager() instanceof TableLayoutManager
//                ? ((TableLayoutManager) getListContainer().getLayoutManager()).getSpanCount()
//                : 1;
//    }

    public boolean isRefreshLocked() {
        return false;
    }

    public boolean isRefreshRequested() {
        return mRefreshRequested;
    }

    public void setRefreshRequested(boolean requested) {
        mRefreshRequested = requested;
    }

    public boolean isSortingSupported() {
        return mSortingSupported;
    }

    public void setSortingSupported(boolean sortingSupported) {
        mSortingSupported = sortingSupported;
    }

    public boolean loadIfRequested() {
        boolean refreshed = isRefreshRequested();

        setRefreshRequested(false);

        if (refreshed)
            refreshList();

        return refreshed;
    }

    public boolean openUri(Uri uri) {
        return FileUtils.openUri(getContext(), uri);
    }

    public boolean performLayoutClick(V holder) {
        return setItemSelected(holder)
                || (mLayoutClickListener != null && mLayoutClickListener.onLayoutClick(
                this, holder, false))
                || onDefaultClickAction(holder);
    }

    public boolean performLayoutLongClick(V holder) {
        return (mLayoutClickListener != null && mLayoutClickListener.onLayoutClick(
                this, holder, true))
                || onDefaultLongClickAction(holder);
//                || getSelectionConnection() != null && getSelectionConnection().setSelected(holder);
    }

//    public boolean performLayoutClickOpen(V holder) {
//        try {
//            T object = getListProvider().getItem(holder);
//
//            if (object instanceof Shareable)
//                return openUri(((Shareable) object).uri);
//        } catch (NotReadyException e) {
//            e.printStackTrace();
//        }
//
//        return false;
//    }

    public void registerLayoutViewClicks(final V holder) {
        holder.getClickableView().setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                performLayoutClick(holder);
            }
        });

        holder.getClickableView().setLongClickedListener(new Component.LongClickedListener() {
            @Override
            public void onLongClicked(Component component) {
                performLayoutLongClick(holder);
            }
        });
    }

    @Override
    public void refreshList() {
        if (isRefreshLocked()) {
            setRefreshRequested(true);

            if (mRefreshDelayedSnackbar == null) {
                mRefreshDelayedSnackbar = createSnackbar(0);
                mRefreshDelayedSnackbar.setDuration(LENGTH_LONG);
            }

            mRefreshDelayedSnackbar.show();
        } else {
//            super.refreshList();

            if (mRefreshDelayedSnackbar != null) {
                mRefreshDelayedSnackbar.dismiss();
                mRefreshDelayedSnackbar = null;
            }
        }
    }

    public void setDefaultPaddingDecorationSize(float defaultPadding) {
        mDefaultPaddingDecorationSize = defaultPadding;
    }

    public void setDefaultOrderingCriteria(int criteria) {
        mDefaultOrderingCriteria = criteria;
    }

    public void setDefaultSelectionCallback(SelectionCallback<T> selectionCallback) {
        mDefaultSelectionCallback = selectionCallback;
    }
//
//    public void setDefaultSelectionConnection(PowerfulActionMode.SelectorConnection<T> selectionConnection) {
//        mDefaultSelectionConnection = selectionConnection;
//    }

    public void setDefaultSortingCriteria(int criteria) {
        mDefaultSortingCriteria = criteria;
    }

    public void setDefaultViewingGridSize(int gridSize, int gridSizeLandscape) {
        mDefaultViewingGridSize = gridSize;
        mDefaultViewingGridSizeLandscape = gridSizeLandscape;
    }

    public void setDividerVisible(boolean visible) {
        if (getComponent() != null) {
            Component divider = getComponent().findComponentById(mDividerResId);
            divider.setVisibility(visible ? Component.VISIBLE : Component.INVISIBLE);
        }
    }

    public void setDividerView(int resId) {
        mDividerResId = resId;
    }

    public boolean setItemSelected(V holder) {
        return false;//getSelectionCallback() != null && getSelectionCallback().setItemSelected(holder.getAdapterPosition());
    }

    @Override
    public void setLayoutClickListener(LayoutClickListener<V> clickListener) {
        mLayoutClickListener = clickListener;
    }

    public void setFilteringSupported(boolean supported) {
        mFilteringSupported = supported;
    }

//    @Override
//    public void setSelectorConnection(PowerfulActionMode.SelectorConnection<T> selectionConnection) {
//        mSelectionConnection = selectionConnection;
//    }

    public void setUseDefaultPaddingDecoration(boolean use) {
        mUseDefaultPaddingDecoration = use;
    }

    public void setUseDefaultPaddingDecorationSpaceForEdges(boolean use) {
        mUseDefaultPaddingDecorationSpaceForEdges = use;
    }

    public interface LayoutClickListener<V extends EditableListProvider.EditableViewHolder> {
        boolean onLayoutClick(EditableListFraction listFragment, V holder, boolean longClick);
    }

    public interface FilteringDelegate<T extends Editable> {
        boolean changeFilteringKeyword(String keyword);
        String[] getFilteringKeyword(EditableListFractionIF<T> listFragment);
    }

    public static abstract class SelectionCallback<T extends Editable> { //implements PowerfulActionMode.Callback<T> {
        private EditableListFractionIF<T> mFraction;

        public SelectionCallback(EditableListFractionIF<T> fragment) {
            updateProvider(fragment);
        }

        public EditableListProviderIF<T> getAdapter() {
            return mFraction.getEditableListProvider();
        }

        public EditableListFractionIF<T> getFragment() {
            return mFraction;
        }

        public boolean isSelectionActivated() {
//            return mFraction.getSelectionConnection() != null
//                    && mFraction.getSelectionConnection().selectionActive();
            return false;
        }

        //        @Override
        public List<T> getSelectableList() {
            return getAdapter().getList();
        }

//        public boolean setItemSelected(int position) {
//            return isSelectionActivated() && mFraction.getSelectionConnection().setSelected(position);
//        }

//        public void setSelection(boolean selection, List<T> selectableList) {
//            for (T selectable : selectableList)
//                mFraction.getSelectionConnection().setSelected(selectable, selection);
//        }
//
//        public boolean setSelection() {
//            boolean allSelected = mFraction.getSelectionConnection().getSelectedItemList().size() != getSelectableList().size();
//
//            setSelection(allSelected);
//
//            return allSelected;
//        }

//        public void setSelection(boolean selection) {
//            setSelection(selection, getSelectableList());
//
//            // One-by-one calling caused an ANR
//            getAdapter().syncSelectionList();
//            getAdapter().notifyItemRangeChanged(0, getSelectableList().size());
//        }

        public void updateProvider(EditableListFractionIF<T> fragment) {
            mFraction = fragment;
        }

//        private void updateSelectionTitle(PowerfulActionMode actionMode) {
//            int selectedSize = mFraction.getSelectionConnection()
//                    .getSelectedItemList()
//                    .size();
//
//            actionMode.setTitle(String.valueOf(selectedSize));
//        }

//        @Override
//        public boolean onPrepareActionMenu(Context context, PowerfulActionMode actionMode) {
//            updateSelectionTitle(actionMode);
//            return true;
//        }
//
//        //@Override
//        public boolean onCreateActionMenu(Context context, PowerfulActionMode actionMode, Menu menu) {
//            actionMode.getMenuInflater().parse(0, menu);
//            return false;
//        }
//
//        @Override
//        public void onItemChecked(Context context, PowerfulActionMode actionMode, T selectable, int position) {
//            updateSelectionTitle(actionMode);
//
//            if (position != -1) {
//                getAdapter().syncSelectionList();
//                getAdapter().notifyItemChanged(position);
//            }
//        }

        //@Override
//
    }
}
