package co.uk.rushorm.ohos;


import co.uk.rushorm.core.RushStringSanitizer;
import ohos.data.rdb.RdbUtils;

/**
 * Created by Stuart on 15/12/14.
 */
public class OhosRushStringSanitizer implements RushStringSanitizer {
    @Override
    public String sanitize(String string) {
        if(string != null) {
            return RdbUtils.escapeQuote(string);
        } else {
            return "'" + string + "'";
        }
    }
}
