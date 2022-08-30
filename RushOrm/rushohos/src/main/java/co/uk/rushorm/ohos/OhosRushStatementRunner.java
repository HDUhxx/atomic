package co.uk.rushorm.ohos;

import co.uk.rushorm.core.LogUtil;
import co.uk.rushorm.core.RushConfig;
import co.uk.rushorm.core.RushQue;
import co.uk.rushorm.core.RushStatementRunner;
import co.uk.rushorm.core.exceptions.RushSqlException;
import ohos.app.Context;
import ohos.data.DatabaseHelper;
import ohos.data.preferences.Preferences;
import ohos.data.rdb.RdbOpenCallback;
import ohos.data.rdb.RdbStore;
import ohos.data.rdb.StoreConfig;
import ohos.data.resultset.ResultSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stuartc on 11/12/14.
 */
public class OhosRushStatementRunner extends DatabaseHelper implements RushStatementRunner {

    private int lastRunVersion = 1;
    private RushConfig rushConfig;
    private final Context context;
    private String dbName;
    private RdbStore getRdbStore = null;

    public OhosRushStatementRunner(Context context, String name, RushConfig rushConfig) {
        super(context);
        this.dbName = name;
        this.lastRunVersion = rushConfig.dbVersion();
        this.rushConfig = rushConfig;
        this.context = context;
    }

    public RdbStore getWritableDatabase() {
        if (getRdbStore == null) {
            LogUtil.error(this.getClass().getName(),"Rush---OhosRushStatementRunner---getWritableDatabase");
            StoreConfig storeConfig = StoreConfig.newDefaultConfig(dbName);
            RdbOpenCallback callback = new RdbOpenCallback() {
                @Override
                public void onCreate(RdbStore store) {
                }

                @Override
                public void onUpgrade(RdbStore store, int oldVersion, int newVersion) {
                    lastRunVersion = oldVersion;
                }
            };
            getRdbStore = getRdbStore(storeConfig, lastRunVersion, callback, null);
        }
        return getRdbStore;
    }

    @Override
    public void runRaw(String statement, RushQue que) {
        try {
            getWritableDatabase().executeSql(statement);
        } catch (Exception exception) {
            LogUtil.error(this.getClass().getName(), "runRaw执行报错" + exception.toString());
            if (rushConfig.inDebug()) {
                throw exception;
            } else {
                throw new RushSqlException();
            }
        }
    }

    @Override
    public ValuesCallback runGet(String sql, RushQue que) {
        final ResultSet cursor;
        try {
            cursor = getWritableDatabase().querySql(sql, null);
        } catch (Exception exception) {
            if (rushConfig.inDebug()) {
                throw exception;
            } else {
                throw new RushSqlException();
            }
        }
        cursor.goToFirstRow();
        return new ValuesCallback() {
            @Override
            public boolean hasNext() {
                return !cursor.isEnded();
            }

            @Override
            public List<String> next() {

                List<String> row = new ArrayList<>();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    row.add(cursor.getString(i));
                }
                cursor.goToNextRow();
                return row;
            }

            @Override
            public void close() {
                cursor.close();
            }
        };
    }

    @Override
    public void startTransition(RushQue que) {
        getWritableDatabase().beginTransaction();
    }

    @Override
    public void endTransition(RushQue que) {
        getWritableDatabase().markAsCommit();
        getWritableDatabase().endTransaction();
    }

    @Override
    public boolean isFirstRun() {
        return checkDbExits();
    }

    @Override
    public void initializeComplete(long version) {

    }

    @Override
    public boolean requiresUpgrade(long version, RushQue que) {
        return getLastRunVersion() != version;
    }

    private int getLastRunVersion() {
        getWritableDatabase().getVersion();
        return lastRunVersion;
    }

    private boolean checkDbExits(){
        DatabaseHelper databaseHelper=new DatabaseHelper(context);
        String fileName="dbrushorm_temp_cache";
        String dbrushorm="dbrushorm";
        Preferences preferences=databaseHelper.getPreferences(fileName);
        boolean result=preferences.getBoolean(dbrushorm,false);
        if(!result){
            preferences.putBoolean(dbrushorm,true);
            preferences.flush();
        }
        return !result;
    }
}
