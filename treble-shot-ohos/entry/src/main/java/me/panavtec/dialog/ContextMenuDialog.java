package me.panavtec.dialog;

import me.panavtec.title.ResourceTable;
import me.panavtec.title.hmadapter.about.ThirdPartyLIBListProvider.ModuleItem;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.LayoutScatter;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.CommonDialog;
import ohos.app.Context;
import ohos.utils.net.Uri;

/**
 * created by: veli
 * date: 4/8/19 9:16 AM
 */
public class ContextMenuDialog extends CommonDialog
{
    public ContextMenuDialog(Context context, ModuleItem moduleItem)
    {
        super(context);
        Component componentDlg = LayoutScatter.getInstance(context)
                .parse(ResourceTable.Layout_main_action_simple_menu,null, false);
        setContentCustomComponent(componentDlg);
        setAutoClosable(true);
        setTransparent(true);

        componentDlg.findComponentById(ResourceTable.Id_main_simple_menuaction_text1).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                context.startAbility(new Intent().setUri(Uri.parse(moduleItem.moduleUrl)),0);
            }
        });

        componentDlg.findComponentById(ResourceTable.Id_main_simple_menuaction_text2).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                context.startAbility(new Intent().setUri(Uri.parse(moduleItem.licenceUrl)),0);
            }
        });

    }
}
