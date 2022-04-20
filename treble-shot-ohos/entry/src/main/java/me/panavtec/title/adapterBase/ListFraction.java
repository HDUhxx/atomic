package me.panavtec.title.adapterBase;


import me.panavtec.title.ResourceTable;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;


public abstract class ListFraction<Z extends Component, T, E extends ListProviderIF<T>> extends Fraction {
    public static final String TAG = "ListFragment";
    public static final int TASK_ID_REFRESH = 0;
    private E mAdapter;
    private Component mListViewContainer;
    private Component mCustomViewContainer;
    private Component mDefaultViewContainer;
    private Component mContainer;
    private Component mEmptyView;
    private Text mEmptyText;
    private Image mEmptyImage;
    private ProgressBar mProgressView;
    private Button mEmptyActionButton;

    public ListFraction() {
        super();
    }

    @Override
    public void onStart(Intent intent) {
        mAdapter = onAdapter();
        if (getListView() != null && getListView().getId() != ResourceTable.Id_genfw_customListFragment_listViewContainer)
            getListView().setId(ResourceTable.Id_genfw_customListFragment_listViewContainer);
        setListProvider(mAdapter);
    }

    @Override
    protected Component onComponentAttached(LayoutScatter scatter, ComponentContainer container, Intent intent) {
        Component component = LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_genfw_abstract_list_fragment, container, false);

        mCustomViewContainer = component.findComponentById(ResourceTable.Id_genfw_customListFragment_customViewContainer);
        mDefaultViewContainer = component.findComponentById(ResourceTable.Id_genfw_customListFragment_defaultViewContainer);
        mListViewContainer = component.findComponentById(ResourceTable.Id_genfw_customListFragment_listViewContainer);
        mContainer = component.findComponentById(ResourceTable.Id_genfw_customListFragment_container);
        mEmptyView = component.findComponentById(ResourceTable.Id_genfw_customListFragment_emptyView);
        mEmptyText = (Text) component.findComponentById(ResourceTable.Id_genfw_customListFragment_emptyTextView);
        mEmptyImage = (Image) component.findComponentById(ResourceTable.Id_genfw_customListFragment_emptyImageView);
        mProgressView = (ProgressBar) component.findComponentById(ResourceTable.Id_genfw_customListFragment_progressView);
        mEmptyActionButton = (Button) component.findComponentById(ResourceTable.Id_genfw_customListFragment_emptyActionButton);

        return super.onComponentAttached(scatter, container, intent);

    }


    public abstract E onAdapter();

    protected abstract void onEnsureList();

    protected abstract Z onListContainer(ComponentContainer mainContainer, ComponentContainer listViewContainer);

    public abstract boolean onSetListProvider(E adapter);

    public abstract Z getListView();

    protected void onPrepareRefreshingList() {

    }

    protected void onListRefreshed() {
    }


    public E getAdapter() {
        return mAdapter;
    }

    protected Component getContainer() {
        return mContainer;
    }

    public Component getCustomViewContainer() {
        return mCustomViewContainer;
    }

    public Component getDefaultViewContainer() {
        return mDefaultViewContainer;
    }

    public Image getEmptyImage() {
        onEnsureList();
        return mEmptyImage;
    }

    public Text getEmptyText() {
        onEnsureList();
        return mEmptyText;
    }

    protected Component getEmptyView() {
        return mEmptyView;
    }

    protected Component getListViewContainer() {
        return mListViewContainer;
    }


    public E getListAdapter() {
        return mAdapter;
    }

    public ProgressBar getProgressView() {
        onEnsureList();
        return mProgressView;
    }

    public Button getEmptyActionButton() {
        return mEmptyActionButton;
    }

    public void refreshList()
    {
//        getLoaderCallbackRefresh().requestRefresh();
    }

    public void setEmptyImage(int resId) {
        onEnsureList();
        mEmptyImage.setId(resId);
    }

    public void setEmptyText(CharSequence text) {
        onEnsureList();
        mEmptyText.setText(text.toString());
    }

    public void setListProvider(E adapter) {
        boolean hadAdapter = mAdapter != null;
        mAdapter = adapter;

        if (onSetListProvider(adapter)) {
            if (mContainer.getVisibility() != Component.VISIBLE && !hadAdapter) {
            }
//                setListShown(true, getComponent.getWindowToken() != null);
        }
    }

    public void setListShown(boolean shown) {
        setListShown(shown, true);
    }

    public void setListShown(boolean shown, boolean animate) {
        onEnsureList();

        if ((mContainer.getVisibility() == Component.VISIBLE) == shown)
            return;

//        if (animate) {
//            Animation fadeOut = AnimationUtils.loadAnimation(getContext(), android.ResourceTable.anim.fade_out);
//            Animation fadeIn = AnimationUtils.loadAnimation(getContext(), android.ResourceTable.anim.fade_in);
//
//            mProgressView.startAnimation(shown ? fadeOut : fadeIn);
//            mContainer.startAnimation(shown ? fadeIn : fadeOut);
//        } else {
//            mProgressView.clearAnimation();
//            mContainer.clearAnimation();
//        }

        mContainer.setVisibility(shown ? Component.VISIBLE : Component.INVISIBLE);
        mProgressView.setVisibility(shown ? Component.INVISIBLE : Component.VISIBLE);
    }

    public void showCustomView(boolean shown) {
        mCustomViewContainer.setVisibility(shown ? Component.VISIBLE : Component.INVISIBLE);
        mDefaultViewContainer.setVisibility(shown ? Component.INVISIBLE : Component.VISIBLE);
    }

    public boolean toggleCustomView() {
        boolean isVisible = getCustomViewContainer().getVisibility() == Component.VISIBLE;

        showCustomView(!isVisible);

        return !isVisible;
    }

    public void useEmptyActionButton(String buttonText, Component.ClickedListener clickListener) {
        mEmptyActionButton.setText(buttonText);
        mEmptyActionButton.setClickedListener(clickListener);

        useEmptyActionButton(true);
    }

    public void useEmptyActionButton(boolean use) {
        mEmptyActionButton.setVisibility(use ? Component.VISIBLE : Component.INVISIBLE);
    }

}
