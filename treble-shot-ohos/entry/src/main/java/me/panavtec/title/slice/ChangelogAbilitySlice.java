package me.panavtec.title.slice;

import me.panavtec.network.getter.GitHubChangelogListGetter;
import me.panavtec.title.ResourceTable;
import me.panavtec.title.hmadapter.about.AboutChangeLogListProvider;
import me.panavtec.title.hmutils.HMUtils;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ListContainer;
import ohos.agp.components.Text;

import static me.panavtec.title.hmutils.HMUtils.isNetWorkAvailable;

/**
 * created by: veli
 * date: 9/12/18 6:09 PM
 */
public class ChangelogAbilitySlice extends AbilitySlice {
    private ListContainer mListContainer;
    private AboutChangeLogListProvider mChangeLogListProvider;

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
        titleText.setText(getString(ResourceTable.String_butn_changelog));

        initListContainer();

    }

    private void initListContainer() {
        mListContainer = (ListContainer) findComponentById(ResourceTable.Id_main_content_list);
        mChangeLogListProvider = new AboutChangeLogListProvider( this);

        if(isNetWorkAvailable(this)) {
            try {
                mListContainer.setItemProvider(mChangeLogListProvider);
                HMUtils.asyncRun(
                        new Runnable() {
                            @Override
                            public void run() {
                                mChangeLogListProvider.setList(GitHubChangelogListGetter.get());
                            }
                        },
                        new Runnable() {
                            @Override
                            public void run() {
                                mChangeLogListProvider.notifyDataChanged();
                            }
                        }
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            mListContainer.setVisibility(Component.HIDE);
            findComponentById(ResourceTable.Id_network_not_available).setVisibility(Component.VISIBLE);
        }
    }
}
