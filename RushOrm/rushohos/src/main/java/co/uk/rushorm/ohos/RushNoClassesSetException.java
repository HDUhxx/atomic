package co.uk.rushorm.ohos;

/**
 * Created by stuart on 26/06/2016.
 */

public class RushNoClassesSetException extends RuntimeException {

    public RushNoClassesSetException() {
        super("No classes set on OhosInitializeConfig, add all classes you require to the OhosInitializeConfig otherwise they will not be saved. See www.rushorm.com setup for more details.");
    }

}
