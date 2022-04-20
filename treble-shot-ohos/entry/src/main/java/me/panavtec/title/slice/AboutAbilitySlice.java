package me.panavtec.title.slice;

import me.panavtec.config.AppConfig;
import me.panavtec.network.getter.GitHubContributorsListGetter;
import me.panavtec.title.ResourceTable;
import me.panavtec.title.hmadapter.about.AboutContributorListProvider;
import me.panavtec.title.hmutils.HMUtils;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ListContainer;
import ohos.agp.window.dialog.ToastDialog;
import ohos.utils.net.Uri;

import static me.panavtec.title.hmutils.HMUtils.isNetWorkAvailable;

public class AboutAbilitySlice extends AbilitySlice {
    private ListContainer mContributorList;
    private AboutContributorListProvider mContributorListProvider;

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_about_layout);

        findComponentById(ResourceTable.Id_about_close_button).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                onBackPressed();
            }
        });

        findComponentById(ResourceTable.Id_about_email_icon).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                createFeedbackIntent();
            }
        });

        findComponentById(ResourceTable.Id_orgIcon).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                startAbility(new Intent().setUri(Uri.parse(AppConfig.URI_REPO_ORG)));
            }
        });

        findComponentById(ResourceTable.Id_slice_about_see_source_layout).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                startAbility(new Intent().setUri(Uri.parse(AppConfig.URI_REPO_APP)));
            }
        });

        findComponentById(ResourceTable.Id_slice_about_translate_layout).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                startAbility(new Intent().setUri(Uri.parse(AppConfig.URI_TRANSLATE)));
            }
        });

        findComponentById(ResourceTable.Id_slice_about_changelog_layout).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                present(new ChangelogAbilitySlice(), new Intent());
            }
        });

        findComponentById(ResourceTable.Id_slice_about_telegram_layout).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                startAbility(new Intent().setUri(Uri.parse(AppConfig.URI_TELEGRAM_CHANNEL)));
            }
        });

        findComponentById(ResourceTable.Id_slice_about_option_fourth_layout).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                new ToastDialog(component.getContext()).setText("已经是最新版本！").show();
            }
        });

        findComponentById(ResourceTable.Id_slice_about_third_party_libraries_layout).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                present(new ThirdPartyLibrariesAbilitySlice(), new Intent());
            }
        });

        mContributorList = (ListContainer) findComponentById(ResourceTable.Id_slice_about_contributors_container);

        mContributorListProvider = new AboutContributorListProvider(this);

        mContributorList.setItemProvider(mContributorListProvider);

        if(isNetWorkAvailable(this)) {
            HMUtils.asyncRun(
                    new Runnable() {
                        @Override
                        public void run() {
                            mContributorListProvider.setList(GitHubContributorsListGetter.get());
                        }
                    },
                    new Runnable() {
                        @Override
                        public void run() {
                            mContributorListProvider.notifyDataChanged();
                        }
                    }
            );
        }else {
            mContributorList.setVisibility(Component.HIDE);
            findComponentById(ResourceTable.Id_slice_about_contributors_empty).setVisibility(Component.VISIBLE);
        }
    }

    public void createFeedbackIntent()
    {
//      intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.setParam("android.intent.extra.SUBJECT", getString(ResourceTable.String_app_name));
        intent.setParam("android.intent.extra.EMAIL", new String[]{AppConfig.EMAIL_DEVELOPER});
        intent.addFlags(Intent.FLAG_NOT_OHOS_COMPONENT);
        startAbility(intent);
    }
}