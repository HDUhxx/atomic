package me.panavtec.title.slice;

import me.panavtec.network.getter.ThirdPartyLibraryListGetter;
import me.panavtec.title.ResourceTable;
import me.panavtec.title.hmadapter.about.ThirdPartyLIBListProvider;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ListContainer;
import ohos.agp.components.Text;

/**
 * created by: veli
 * date: 7/20/18 10:19 PM
 */
public class ThirdPartyLibrariesAbilitySlice extends AbilitySlice {
    private ListContainer mListContainer;
    private ThirdPartyLIBListProvider mThirdPartLIBListProvider;

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_main_content);

        findComponentById(ResourceTable.Id_main_simple_header_action_close).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                onBackPressed();
            }
        });

        Text titleText = (Text) findComponentById(ResourceTable.Id_main_simple_header_action_text);
        titleText.setText(getString(ResourceTable.String_text_thirdPartyLibraries));

        initListContainer();

    }

    private void initListContainer() {
        mListContainer = (ListContainer) findComponentById(ResourceTable.Id_main_content_list);
        mThirdPartLIBListProvider = new ThirdPartyLIBListProvider( this);
        try {
            mThirdPartLIBListProvider.setList(ThirdPartyLibraryListGetter.get(this));
            mListContainer.setItemProvider(mThirdPartLIBListProvider);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    @Override
//    public LicencesAdapter onAdapter()
//    {
//        final AppUtils.QuickActions<RecyclerViewAdapter.ViewHolder> quickActions = new AppUtils.QuickActions<RecyclerViewAdapter.ViewHolder>()
//        {
//            @Override
//            public void onQuickActions(final RecyclerViewAdapter.ViewHolder clazz)
//            {
//                clazz.getView().findComponentById(0).setClickedListener(new Component.ClickedListener()
//                {
//                    @Override
//                    public void onClick(Component component)
//                    {
//                        final ModuleItem moduleItem = getAdapter().getList().get(clazz.getAdapterPosition());
//
//                        PopupMenu popupMenu = new PopupMenu(getContext(), v);
//                        popupMenu.getMenuInflater().parse(0, popupMenu.getMenu());
//
//                        popupMenu.getMenu()
//                                .findItem(0)
//                                .setEnabled(moduleItem.moduleUrl != null);
//
//                        popupMenu.getMenu()
//                                .findItem(0)
//                                .setEnabled(moduleItem.licenceUrl != null);
//
//                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
//                        {
//                            @Override
//                            public boolean onMenuItemClick(MenuItem item)
//                            {
//                                int id = item.getItemId();
//
//                                if (id == 0)
//                                    AppUtils.startAbility(null,new ExtIntent().setAction(ExtIntent.ACTION_VIEW)
//                                            .setUri(Uri.parse(moduleItem.licenceUrl)));
//                                else if (id == 0)
//                                    AppUtils.startAbility(null, new ExtIntent().setAction(ExtIntent.ACTION_VIEW)
//                                            .setUri(Uri.parse(moduleItem.moduleUrl)));
//                                else
//                                    return false;
//
//                                return true;
//                            }
//                        });
//
//                        popupMenu.show();
//                    }
//                });
//            }
//        };
//
//        return new LicencesAdapter(getContext())
//        {
//
//            @Override
//            public ViewHolder onCreateViewHolder(Component parent, int viewType)
//            {
//                return AppUtils.quickAction(super.onCreateViewHolder(parent, viewType), quickActions);
//            }
//        };
//    }

}
