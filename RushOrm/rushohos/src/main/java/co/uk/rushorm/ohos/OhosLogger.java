package co.uk.rushorm.ohos;

import co.uk.rushorm.core.LogUtil;
import co.uk.rushorm.core.Logger;
import co.uk.rushorm.core.RushConfig;

/**
 * Created by Stuart on 11/12/14.
 */
public class OhosLogger implements Logger {

    private static final String TAG = "RushOrm";

    private final RushConfig rushConfig;

    public OhosLogger(RushConfig rushConfig) {
        this.rushConfig = rushConfig;
    }

    @Override
    public void log(String message) {
        if(rushConfig.log() && message != null) {
            LogUtil.info(TAG, message);
        }
    }

    @Override
    public void logSql(String sql) {
        if(rushConfig.log() && sql != null) {
            LogUtil.debug(TAG, sql);
        }
    }

    @Override
    public void logError(String message) {
        if(message != null) {
            LogUtil.debug(TAG, message);
        }
    }
}
