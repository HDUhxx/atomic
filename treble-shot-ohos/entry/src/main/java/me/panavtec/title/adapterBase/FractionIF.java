package me.panavtec.title.adapterBase;

import ohos.aafwk.ability.fraction.FractionAbility;
import ohos.app.Context;

import java.util.HashMap;

/**
 * created by: veli
 * date: 7/31/18 12:56 PM
 */
public interface FractionIF
{
    Snackbar createSnackbar(int resId, Object... objects);

    FractionAbility getFractionAbility();

    Context getContext();

    void setArguments(HashMap arguments);
}
