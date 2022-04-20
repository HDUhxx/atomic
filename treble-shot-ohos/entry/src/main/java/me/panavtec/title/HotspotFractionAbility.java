package me.panavtec.title;

import me.panavtec.title.fraction.HotspotMgrFraction;
import ohos.aafwk.ability.fraction.FractionAbility;
import ohos.aafwk.ability.fraction.FractionScheduler;
import ohos.aafwk.content.Intent;

public class HotspotFractionAbility extends FractionAbility {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_hotspot_fraction_ability);

        FractionScheduler fractionScheduler = getFractionManager().startFractionScheduler();
        fractionScheduler
                .add(ResourceTable.Id_hotspot_container, new HotspotMgrFraction())
                .submit();
    }

}
