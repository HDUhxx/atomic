package co.uk.rushorm.ohos;


import java.util.List;

import co.uk.rushorm.core.Rush;
import co.uk.rushorm.core.RushColumn;
import co.uk.rushorm.core.RushCore;
import co.uk.rushorm.core.RushInitializeConfig;
import ohos.app.Context;

/**
 * Created by stuartc on 11/12/14.
 */
public class RushOhos {

    public static void initialize(Context context, List<Class<? extends Rush>> clazzes) {
        initialize(new OhosInitializeConfig(context, clazzes));
    }

    public static void initialize(Context context, List<Class<? extends Rush>> clazzes, List<RushColumn> columns) {
        OhosInitializeConfig ohosInitializeConfig = new OhosInitializeConfig(context, clazzes);
        for(RushColumn rushColumn : columns) {
            ohosInitializeConfig.addRushColumn(rushColumn);
        }
        initialize(ohosInitializeConfig);
    }

    public static void initialize(RushInitializeConfig rushInitializeConfig){
        RushCore.initialize(rushInitializeConfig);
    }

}
