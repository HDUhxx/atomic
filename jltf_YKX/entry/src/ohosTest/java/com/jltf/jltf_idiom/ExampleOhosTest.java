package com.jltf.jltf_idiom;

import ohos.aafwk.ability.delegation.AbilityDelegatorRegistry;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExampleOhosTest {
    @Test
    public void testBundleName() {
        final String actualBundleName = AbilityDelegatorRegistry.getArguments().getTestBundleName();
        assertEquals("com.jltf.jltf_idiom.hmservice", actualBundleName);

//        ServiceAbility serviceAbility = new ServiceAbility();
//        serviceAbility.
    }
}