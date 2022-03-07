package com.zxy.recovery.test;

import com.zxy.recovery.tools.SharedPreferencesCompat;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.delegation.AbilityDelegatorRegistry;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExampleOhosTest {
    @Test
    public void testBundleName() {
        final String actualBundleName = AbilityDelegatorRegistry.getArguments().getTestBundleName();
        Ability ability = AbilityDelegatorRegistry.getAbilityDelegator().getCurrentTopAbility();
        SharedPreferencesCompat.Builder builder = SharedPreferencesCompat.newBuilder(ability,"test");
        builder.put("test1","ohosTest");
        builder.commit();
        String info = SharedPreferencesCompat.get(ability,"test","test1","0");
        assertEquals("ohosTest", info);
    }
}