package com.scaninfotech.android.msmartlite.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.PortUnreachableException;
import java.security.PublicKey;
import java.util.ArrayList;

public class TasksSQLiteOpenHelper extends SQLiteOpenHelper
{
	public SQLiteDatabase database;
	static Context mcontext;

	public static final String DATABASE_NAME = "tasks_db.sqllite";
	public static final String DATABASE_NAME1 = "AssetMaster.sqllite";
	public static final int VERSION = 1;
	private static final String DB_PATH_SUFFIX = "/databases/";
	
	public static final String TASKS_TABLE = "tasks";
	public static final String ASSETS_TABLE = "Assets";
	public static final String LOCATION_TABLE = "Location";
	public static final String USERS_TABLE = "Users";


  /* // This is for assets table only
	public static final String ASSETS_LOCALID="_id";
	public static final String ASSETS_ID="AssetsID";
	public static final String ASSETS_CODE="AssetsCode";
	public static final String ASSETS_DISCRIPTION="Description";
	public static final String ASSETS_LOCATIONID="LocationID";
	public static final String ASSETS_CAPITALISATIONDATE="CapitalisationDate";
	public static final String ASSETS_ISDISPOSED="IsDisposed";

	// This is for location table
	public static final String LOCATION_LOCALID="_id";
	public static final String LOC_LACATION_ID="LocationId";
	public static final String LOC_PARENT_ID="ParentID";
	public static final String LOC_DISCRIPTION="Discription";
	public static final String LOC_STATUS="Status";

	//this is for Users table
	public static final String USER_LOCALID="_id";
	public static final String USER_USERID="UserID";
	public static final String USER_USER_NAME="UserName";
	public static final String USER_USER_PASSWORD="Password";
*/


    // This is for task table only
	public static final String TASK_ID = "id";
	public static final String TASK_NAME = "BARCODE";
	public static final String TASK_DESG = "LOCATION";
	public static final String TASK_ORG = "IS_USED";
	public static final String TASK_TO = "turnover";
	public static final String TASK_YEARS = "years";
	public static final String TASK_COMPLETE = "complete";
	public static final String TASK_INT1 = "interest1";
	public static final String TASK_INT2 = "interest2";
	public static final String TASK_INT3 = "interest3";
	public static final String TASK_INT4 = "interest4";




 /* private static String CREATE_TABLE_ASSETS= "CREATE TABLE " +ASSETS_TABLE
		  +"(" +ASSETS_LOCALID+ "INTEGER PRIMARY KEY AUTOINCREMENT, "
		  +ASSETS_ID+
		  " TEXT NOT NULL , " +ASSETS_CODE+
		  " TEXT NOT NULL , " +ASSETS_DISCRIPTION+
		  " TEXT NOT NULL , " +ASSETS_LOCATIONID+
		  " TEXT NOT NULL , " +ASSETS_CAPITALISATIONDATE+
		  " TEXT NOT NULL , " +ASSETS_ISDISPOSED+
		  " TEXT NOT NULL);";

	private static String CREATE_TABLE_LOCATIONID= " CREATE TABLE " +LOCATION_TABLE
			+"(" +LOCATION_LOCALID+ "INTEGER PRIMARY KEY AUTOINCREMENT, "
			+LOC_LACATION_ID+
			" TEXT NOT NULL , " +LOC_PARENT_ID+
			" TEXT NOT NULL , " +LOC_DISCRIPTION+
			" TEXT NOT NULL , " +LOC_STATUS+
			" TEXT NOT NULL);";

	private static String CREATE_TABLE_USERID= " CREATE TABLE " +USERS_TABLE
			+"("  +USER_LOCALID+ "INTEGER PRIMARY KEY AUTOINCREMENT , "
			+USER_USERID+
			" TEXT NOT NULL , " +USER_USER_NAME+
			" TEXT NOT NULL , " +USER_USER_PASSWORD+
			" TEXT NOT NULL);";*/


	public void open()
	{
		database = this.getWritableDatabase();
	}

	public void close()
	{
		database.close();
	}
	
	public TasksSQLiteOpenHelper(Context context)
	{
		super(context, DATABASE_NAME, null, VERSION);
		mcontext=context;
	}


	@Override
	public void onCreate(SQLiteDatabase db)
	{
		createTable(db);
	}







	private void createTable(SQLiteDatabase db)
	{
		db.execSQL("create table " + TASKS_TABLE + " ( " +
				TASK_ID + " integer primary key autoincrement not null, " +
				TASK_NAME + " text, " +
				TASK_DESG + " text, " +
				TASK_ORG + " text " +
				");"
		);
		/*db.execSQL(CREATE_TABLE_ASSETS);
		db.execSQL(CREATE_TABLE_LOCATIONID);
		db.execSQL(CREATE_TABLE_USERID);*/


		/*db.execSQL("create table " + TASKS_TABLE + " ( " +
		TASK_ID + " integer primary key autoincrement not null, " +
		TASK_NAME + " text, " +
		TASK_DESG + " text, " +
		TASK_ORG + " text, " +
		TASK_TO + " text, " +
		TASK_YEARS + " text, " +
		TASK_INT1 + " text, " +
		TASK_INT2 + " text, " +
		TASK_INT3 + " text, " +
		TASK_INT4 + " text, " +
		TASK_COMPLETE + " text " +
		");"
		);*/
	}



	public SQLiteDatabase openDataBase() throws SQLException
	{
		File dbFile = mcontext.getDatabasePath(DATABASE_NAME1);

		if (dbFile.exists())
		{
			try
			{
				CopyDataBaseFromAsset();
				Toast.makeText(mcontext,"Data Successfully pulled",Toast.LENGTH_SHORT).show();
				System.out.println("Copying sucess from Extenal folder");
			} catch (IOException e)
			{
				Toast.makeText(mcontext,"Database file not exists in external storage please insert",Toast.LENGTH_LONG).show();
				//throw new RuntimeException("Error creating source database", e);
			}
		}
		else
		{
			Toast.makeText(mcontext,"Data already Exists",Toast.LENGTH_SHORT).show();
		}

		return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
	}


	public void CopyDataBaseFromAsset() throws IOException
	{

		InputStream myInput = null;
		File sd = Environment.getExternalStorageDirectory();
		File backupDB = null;

			if (sd.canWrite())
			{
				Log.e("Database read", "Database is ready");
				//String backupDBPath  = "/database/quotes.db";
				String backupDBPath = "/database/AssetMaster.db";
				Log.e("Database1", backupDBPath);
				backupDB = new File(sd, backupDBPath);
				myInput = new FileInputStream(new File(backupDB.toString()));
			}

		    if (backupDB.exists())
			{


				// Path to the just created empty db
				String outFileName = getDatabasePath();

				// if the path doesn't exist first, create it
				File f = new File(mcontext.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
				if (!f.exists())
					f.mkdir();

				// Open the empty db as the output stream
				OutputStream myOutput = new FileOutputStream(outFileName);

				// transfer bytes from the inputfile to the outputfile
				byte[] buffer = new byte[1024];
				int length;
				while ((length = myInput.read(buffer)) > 0) {
					myOutput.write(buffer, 0, length);
				}

// Close the streams
				myOutput.flush();
				myOutput.close();
				myInput.close();
			}else
			{
				Toast.makeText(mcontext,"Database file not exists in external storage please insert first",Toast.LENGTH_LONG).show();
			}
		}



	private static String getDatabasePath()
	{
		return mcontext.getApplicationInfo().dataDir + DB_PATH_SUFFIX
				+ DATABASE_NAME;
	}

	public ArrayList<String> getdataList()
	{
		SQLiteDatabase db = this.getReadableDatabase();
		//open();
		Cursor cursor = db.rawQuery("SELECT * FROM Assets WHERE AssetCode", null);
		ArrayList<ArrayList<String>> getname = new ArrayList<>();
		ArrayList<String> getcityname = new ArrayList<String>();
		if (cursor != null && cursor.moveToFirst())
		{
			cursor.moveToFirst();
			while (!cursor.isAfterLast())
			{
				getcityname.add( cursor.getString(1));
				cursor.moveToNext();
			}
			cursor.close();
		}
		return getcityname;
	}


	/*public void GetIntotable()
	{
		Log.e("Aa rah hai","Yha par tho");

		open();

		Cursor cursor =database.rawQuery("SELECT * FROM  Assets",null);
		ContentValues cv = new ContentValues();
		if (cursor != null && cursor.moveToFirst())
		{
			cursor.moveToFirst();
			do
			{
				int ColumnIDAssets = Integer.parseInt(cursor.getString(0));
				String ColumnAssetscode= cursor.getString(1);
				String ColumnAssetsDescription= cursor.getString(2);
				int ColumnAssetsLoactionId= Integer.parseInt(cursor.getString(3));
				String ColumnAssetCapitilization= cursor.getString(4);
				boolean ColumnIsdesposed=Boolean.parseBoolean(cursor.getString(5));

				Log.e("ColumnIDAssets",""+ColumnIDAssets);
				Log.e("ColumnAssetscode",""+ColumnAssetscode);
				Log.e("ColumnAssetsDescription",""+ColumnAssetsDescription);
				Log.e("ColumnAssetsCap",""+ColumnAssetsLoactionId);
				Log.e("ColumnAssetsIsused",""+ColumnAssetCapitilization);
				Log.e("ColumnAssetIsDespoed",""+ColumnIsdesposed );


				*//*cv.put(ASSETS_ID,ColumnIDAssets);
				cv.put(ASSETS_CODE,ColumnAssetscode);
				cv.put(ASSETS_DISCRIPTION,ColumnAssetsDescription);
				cv.put(ASSETS_LOCATIONID,ColumnAssetsLoactionId);
				cv.put(ASSETS_CAPITALISATIONDATE,ColumnAssetCapitilization);
				cv.put(ASSETS_ISDISPOSED,ColumnIsdesposed);*//*

				long long_insertdata= database.insert(ASSETS_TABLE,null,cv);
				Log.e("Isvalue_database",""+long_insertdata);
			}while (cursor.moveToNext());
		}

		Cursor cursor1 = database.rawQuery("SELECT * FROM Location", null);
		ContentValues cv1 = new ContentValues();
		if ( cursor1 != null && cursor1.moveToFirst())
		{
			cursor1.moveToFirst();
			do {
				int locationId = Integer.parseInt(cursor1.getString(0));
				int parentId = Integer.parseInt(cursor1.getString(1));
				String Description = cursor1.getString(2);
				boolean status = Boolean.parseBoolean(cursor1.getString(3));

				Log.e("locationId", "" + locationId);
				Log.e("parentId", "" + parentId);
				Log.e("Description", "" + Description);
				Log.e("status", "" + status);

				*//*cv1.put(LOC_LACATION_ID, locationId);
				cv1.put(LOC_PARENT_ID, parentId);
				cv1.put(LOC_DISCRIPTION, Description);
				cv1.put(LOC_STATUS, status);*//*

				long logn_Locationid = database.insert(LOCATION_TABLE, null, cv1);
				Log.e("LongLocationId", "" + logn_Locationid);
			}while (cursor1.moveToNext());

		}

		Cursor cursor2 = database.rawQuery("SELECT * FROM Users",null);
		ContentValues cv3 = new ContentValues();
		if (cursor2  != null && cursor2.moveToFirst())
		{
			cursor2.moveToFirst();
			do {
				int UserId = Integer.parseInt(cursor2.getString(0));
				String UserName = cursor2.getString(1);
				String Password = cursor2.getString(2);

				Log.e("UserId", "" + UserId);
				Log.e("UserName", "" + UserName);
				Log.e("Password", "" + Password);


				*//*cv3.put(USER_USERID, UserId);
				cv3.put(USER_USER_NAME, UserName);
				cv3.put(USER_USER_PASSWORD, Password);*//*

				long Long_userTbale = database.insert(USERS_TABLE, null, cv3);
				Log.e("Long_userTbale",""+Long_userTbale);

			}while (cursor2.moveToNext());
		}
	}*/




	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		if (newVersion > oldVersion)
		{
			String TASKS_TABLE ="DROP TABLE IF EXISTS";
			db.execSQL(TASKS_TABLE);

			String CREATE_TABLE_ASSETS ="DROP TABLE IF EXISTS";
			db.execSQL(CREATE_TABLE_ASSETS);

			String CREATE_TABLE_LOCATIONID="DROP TABLE IF EXISTS";
			db.execSQL(CREATE_TABLE_LOCATIONID);

			String CREATE_TABLE_USERID="DROP TABLE IF EXISTS";
			db.execSQL(CREATE_TABLE_USERID);

			onCreate(db);
		}

	}
}