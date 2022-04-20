package me.panavtec.title.fraction;

import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.app.Context;

public abstract class BaseFraction extends Fraction {
    public Component viewComponent;
    private Context context;

    @Override
    protected Component onComponentAttached(LayoutScatter scatter, ComponentContainer container, Intent intent) {
        viewComponent = scatter.parse(setXMLId(), container, false);
        context = viewComponent.getContext();
        return viewComponent;
    }

    public abstract int setXMLId();

    public Context getMyContext() {
        return context;
    }

    public void jumpAbility(Class<?> tclass) {
        if (viewComponent == null) return;
        Intent intent = new Intent();
        Operation build = new Intent.OperationBuilder()
                .withDeviceId("")
                .withAbilityName(tclass)
                .withBundleName(getMyContext().getBundleName())
                .build();
        intent.setOperation(build);
        viewComponent.getContext().startAbility(intent, 1);
    }


    public String printInfo(String... s) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s1 : s) {
            stringBuilder.append(s1 == null ? "@" : s1).append("**");
        }

        return stringBuilder.toString();
    }
}
