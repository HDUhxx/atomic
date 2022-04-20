package com.ohos.trebleshot.service.preference;

import com.ohos.trebleshot.database.*;
import ohos.app.Context;
import ohos.data.rdb.RdbStore;
import ohos.data.rdb.ValuesBucket;
import ohos.hiviewdfx.HiLog;

import java.util.*;

/**
 * created by: veli
 * date: 31.03.2018 19:29
 */
public class DbSharablePreferences extends SQLiteDatabase implements SharedPreferences
{
	public static final String DATABASE_NAME = DbSharablePreferences.class.getSimpleName() + ".db";
	public static final int DATABASE_VERSION = 1;

	public static final String FIELD_KEY = "__key";
	public static final String FIELD_VALUE = "__value";
	public static final String FIELD_TYPE = "__type";

	private String mCategory;
	private boolean mSyncReliant = false;

	private List<OnSharedPreferenceChangeListener> mChangeListener = new ArrayList<>();
	private Map<String, Object> mSyncedList = new HashMap<>();
	private AsynchronousUpdateListener mUpdateListener; // only called when mSyncReliant is true

	// Do not use DB vulnerable words like 'transaction'
	public DbSharablePreferences(Context context, String categoryName, boolean syncReliant)
	{
		super(context, DATABASE_NAME, DATABASE_VERSION);
		mCategory = categoryName;
		mSyncReliant = syncReliant;

		initialize();
	}

	public DbSharablePreferences(Context context, String categoryName)
	{
		this(context, categoryName, false);
	}

	@Override
	public void onCreate(RdbStore db)
	{

	}

	@Override
	public void onUpgrade(RdbStore db, int oldVersion, int newVersion)
	{

	}

	private void initialize()
	{
		SQLValues sqlValues = new SQLValues();

		sqlValues.defineTable(mCategory, true)
				.define(new SQLValues.Column(FIELD_KEY, SQLType.TEXT, false))
				.define(new SQLValues.Column(FIELD_TYPE, SQLType.TEXT, false))
				.define(new SQLValues.Column(FIELD_VALUE, SQLType.TEXT, true));

		SQLQuery.createTables(mStore, sqlValues);

		setSyncReliant(mSyncReliant);
	}

	@Override
	public Map<String, ?> getAll()
	{
		List<StoredData> data = castQuery(new SQLQuery.Select(mCategory), StoredData.class);
		Map<String, Object> output = new HashMap<>();

		for (StoredData object : data)
			output.put(object.getKey(), object.getTypedValue());

		return output;
	}

	public synchronized Map<String, ?> getSyncedList()
	{
		return mSyncedList;
	}

	public boolean isSyncReliant()
	{
		return mSyncReliant;
	}


	@Override
	public String getString(String key, String defValue)
	{
		return valueCast(key, String.class, defValue);
	}


	@Override
	public Set<String> getStringSet(String key, Set<String> defValues)
	{
		return null;
	}

	@Override
	public int getInt(String key, int defValue)
	{
		return valueCast(key, Integer.class, defValue);
	}

	@Override
	public long getLong(String key, long defValue)
	{
		return valueCast(key, Long.class, defValue);
	}

	@Override
	public float getFloat(String key, float defValue)
	{
		return valueCast(key, Float.class, defValue);
	}

	@Override
	public boolean getBoolean(String key, boolean defValue)
	{
		return valueCast(key, Boolean.class, defValue);
	}

	@Override
	public boolean contains(String key)
	{
		if (isSyncReliant())
			return mSyncedList.containsKey(key);

		try {
			reconstruct(new StoredData(mCategory, key));
			return true;
		} catch (Exception e) {
		}

		return false;
	}

	@Override
	public Editor edit()
	{
		return new DbEditor();
	}

	@Override
	public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener)
	{
		mChangeListener.add(listener);
	}

	@Override
	public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener)
	{
		mChangeListener.remove(listener);
	}

	public void setSyncReliant(boolean syncReliant)
	{
		mSyncReliant = syncReliant;

		if (syncReliant)
			sync();
	}

	public DbSharablePreferences setUpdateListener(AsynchronousUpdateListener updateListener)
	{
		mUpdateListener = updateListener;
		return this;
	}

	public void sync()
	{
		mSyncedList = new HashMap<>();
		mSyncedList.putAll(getAll());
	}

	public <T> T valueCast(String key, Class<T> clazz, T defaultValue)
	{
		if (isSyncReliant()) {
			if (getSyncedList().containsKey(key))
				return (T) getSyncedList().get(key);
		} else {
			StoredData data = new StoredData(mCategory, key);

			try {
				reconstruct(data);
				return (T) data.getTypedValue();
			} catch (Exception e) {
				HiLog.debug(null, DbSharablePreferences.class.getSimpleName() +
						"Failed to read and returning the default value: " + key +
								"; default: " + defaultValue +
								"; requested type: " + clazz.getSimpleName() +
								"; error msg: " + e.getMessage());
			}
		}

		return defaultValue;
	}

	public static class StoredData implements DatabaseObject<Object>
	{
		private String mCategory;
		private String mKey;
		private String mValue;
		private Type mType;

		public StoredData()
		{
		}

		public StoredData(String category, String key)
		{
			mCategory = category;
			mKey = key;
		}

		public StoredData(String category, String key, Object value, Type type)
		{
			this(category, key);
			mType = type;

			if (value != null)
				mValue = String.valueOf(value);
		}

		public StoredData(String category, String key, boolean value)
		{
			this(category, key, value, Type.BOOLEAN);
		}

		public StoredData(String category, String key, float value)
		{
			this(category, key, value, Type.FLOAT);
		}

		public StoredData(String category, String key, int value)
		{
			this(category, key, value, Type.INTEGER);
		}

		public StoredData(String category, String key, long value)
		{
			this(category, key, value, Type.LONG);
		}

		public StoredData(String category, String key, String value)
		{
			this(category, key, value, Type.STRING);
		}

		public String getKey()
		{
			return mKey;
		}

		public Type getType()
		{
			return mType;
		}

		public Object getTypedValue()
		{
			try {
				switch (mType) {
					case BOOLEAN:
						return Boolean.valueOf(mValue);
					case FLOAT:
						return Float.valueOf(mValue);
					case INTEGER:
						return Integer.valueOf(mValue);
					case LONG:
						return Long.valueOf(mValue);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return mValue;
		}

		@Override
		public SQLQuery.Select getWhere()
		{
			return new SQLQuery.Select(mCategory).setWhere(FIELD_KEY + "=?", mKey);
		}

		@Override
		public ValuesBucket getValues()
		{
			ValuesBucket values = new ValuesBucket();

			values.putString(FIELD_KEY, mKey);
			values.putString(FIELD_VALUE, String.valueOf(mValue));
			values.putString(FIELD_TYPE, mType.toString());

			return values;
		}

		@Override
		public void reconstruct(CursorItem item)
		{
			mKey = item.getString(FIELD_KEY);
			mValue = item.getString(FIELD_VALUE);
			mType = Type.valueOf(item.getString(FIELD_TYPE));
		}

		@Override
		public void onCreateObject(RdbStore dbInstance, SQLiteDatabase database, Object parent)
		{

		}

		@Override
		public void onUpdateObject(RdbStore dbInstance, SQLiteDatabase database, Object parent)
		{

		}

		@Override
		public void onRemoveObject(RdbStore dbInstance, SQLiteDatabase database, Object parent)
		{

		}
	}

	public enum Type
	{
		INTEGER,
		STRING,
		FLOAT,
		BOOLEAN,
		LONG,
	}

	public class DbEditor implements Editor
	{
		private List<StoredData> mPendingPublish = new ArrayList<>();
		private List<StoredData> mPendingRemoval = new ArrayList<>();

		protected void execute()
		{
			Map<String, Object> passiveSyncList = new HashMap<>();

			if (isSyncReliant())
				passiveSyncList.putAll(mSyncedList);

			List<StoredData> pendingRemoval = new ArrayList<>(mPendingRemoval);
			List<StoredData> pendingPublish = new ArrayList<>(mPendingPublish);

			for (StoredData removal : pendingRemoval) {
				DbSharablePreferences.this.remove(removal);

				if (isSyncReliant())
					passiveSyncList.remove(removal.getKey());

				for (OnSharedPreferenceChangeListener listener : mChangeListener)
					listener.onSharedPreferenceChanged(DbSharablePreferences.this, removal.getKey());
			}

			for (StoredData publish : pendingPublish) {
				DbSharablePreferences.this.publish(publish);

				if (isSyncReliant())
					passiveSyncList.put(publish.getKey(), publish.getTypedValue());

				for (OnSharedPreferenceChangeListener listener : mChangeListener)
					listener.onSharedPreferenceChanged(DbSharablePreferences.this, publish.getKey());
			}

			mPendingRemoval = new ArrayList<>();
			mPendingPublish = new ArrayList<>();

			if (isSyncReliant()) {
				mSyncedList = passiveSyncList;

				if (mUpdateListener != null)
					mUpdateListener.onCommitComplete();
			}
		}

		@Override
		public Editor putString(String key, String value)
		{
			mPendingPublish.add(new StoredData(mCategory, key, value));
			return this;
		}

		@Override
		public Editor putStringSet(String key, Set<String> values)
		{
			return this;
		}

		@Override
		public Editor putInt(String key, int value)
		{
			mPendingPublish.add(new StoredData(mCategory, key, value));
			return this;
		}

		@Override
		public Editor putLong(String key, long value)
		{
			mPendingPublish.add(new StoredData(mCategory, key, value));
			return this;
		}

		@Override
		public Editor putFloat(String key, float value)
		{
			mPendingPublish.add(new StoredData(mCategory, key, value));
			return this;
		}

		@Override
		public Editor putBoolean(String key, boolean value)
		{
			mPendingPublish.add(new StoredData(mCategory, key, value));
			return this;
		}

		@Override
		public Editor remove(String key)
		{
			mPendingRemoval.add(new StoredData(mCategory, key));
			return this;
		}

		@Override
		public Editor clear()
		{
			DbSharablePreferences.this.remove(new SQLQuery.Select(mCategory));
			return this;
		}

		@Override
		public boolean commit()
		{
			apply();
			return true;
		}

		@Override
		public void apply()
		{
			if (isSyncReliant())
				new Thread()
				{
					@Override
					public void run()
					{
						execute();
					}
				}.run();
			else
				execute();
		}
	}

	public interface AsynchronousUpdateListener
	{
		void onCommitComplete();
	}
}
