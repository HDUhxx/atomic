package co.uk.rushorm.ohos;

import co.uk.rushorm.core.Rush;
import co.uk.rushorm.core.RushClassFinder;
import co.uk.rushorm.core.RushConfig;

import java.util.List;

/**
 * Created by stuartc on 11/12/14.
 */
public class OhosRushClassFinder implements RushClassFinder {

    private final List<Class<? extends Rush>> classes;

    public OhosRushClassFinder(List<Class<? extends Rush>> classes) {
        this.classes = classes;
    }

    @Override
    public List<Class<? extends Rush>> findClasses(RushConfig rushConfig) {
        return classes;
    }

}
