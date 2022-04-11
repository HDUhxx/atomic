package com.zxy.recovery.callback;

import com.zxy.recovery.core.RecoveryStore;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.AbilityLifecycleCallbacks;
import ohos.aafwk.content.Intent;
import ohos.agp.window.service.Window;
import ohos.utils.PacMap;

/**
 * Callback
 *
 * @author:zhengxiaoyong
 * @since 2021-04-06
 */
public class RecoveryActivityLifecycleCallback implements AbilityLifecycleCallbacks {
    @Override
    public void onAbilityStart(Ability ability) {
        boolean isLegal = RecoveryStore.getInstance().verifyAbility(ability);
        if (!isLegal) {
            return;
        }
        if (RecoveryStore.getInstance().contains(ability)) {
            return;
        }

        Window window = ability.getWindow();
        if (window != null) {
            RecoveryStore.getInstance().putAbility(ability);
            Object ojb = ability.getIntent().clone();
            RecoveryStore.getInstance().setIntent((Intent) ojb);
        }
    }

    @Override
    public void onAbilityActive(Ability ability) {
    }

    @Override
    public void onAbilityInactive(Ability ability) {
    }

    @Override
    public void onAbilityForeground(Ability ability) {
    }

    @Override
    public void onAbilityBackground(Ability ability) {
    }

    @Override
    public void onAbilityStop(Ability ability) {
        RecoveryStore.getInstance().removeAbility(ability);
    }

    @Override
    public void onAbilitySaveState(PacMap pacMap) {
    }
}
