package com.scaninfotech.android.msmartlite;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.scaninfotech.android.msmartlite.tasks.Task;
import com.scaninfotech.android.msmartlite.tasks.TasksSQLiteOpenHelper;

import java.util.ArrayList;

import static com.scaninfotech.android.msmartlite.tasks.TasksSQLiteOpenHelper.TASKS_TABLE;
import static com.scaninfotech.android.msmartlite.tasks.TasksSQLiteOpenHelper.TASK_COMPLETE;
import static com.scaninfotech.android.msmartlite.tasks.TasksSQLiteOpenHelper.TASK_DESG;
import static com.scaninfotech.android.msmartlite.tasks.TasksSQLiteOpenHelper.TASK_ID;
import static com.scaninfotech.android.msmartlite.tasks.TasksSQLiteOpenHelper.TASK_INT1;
import static com.scaninfotech.android.msmartlite.tasks.TasksSQLiteOpenHelper.TASK_INT2;
import static com.scaninfotech.android.msmartlite.tasks.TasksSQLiteOpenHelper.TASK_INT3;
import static com.scaninfotech.android.msmartlite.tasks.TasksSQLiteOpenHelper.TASK_INT4;
import static com.scaninfotech.android.msmartlite.tasks.TasksSQLiteOpenHelper.TASK_NAME;
import static com.scaninfotech.android.msmartlite.tasks.TasksSQLiteOpenHelper.TASK_ORG;
import static com.scaninfotech.android.msmartlite.tasks.TasksSQLiteOpenHelper.TASK_TO;
import static com.scaninfotech.android.msmartlite.tasks.TasksSQLiteOpenHelper.TASK_YEARS;

public class AssetRegistration extends Application {

	private static final String SHARED_PREFS_FILE = "shared_prefs_file";
	private static final String TASKS = "tasks";
	private ArrayList<Task> currentTasks;
	private SQLiteDatabase database;
    private String barcodeValue;
	private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;



	@Override
	public void onCreate()
	{
		super.onCreate();
		TasksSQLiteOpenHelper helper = new TasksSQLiteOpenHelper(this);
		database = helper.getWritableDatabase();
		
		if (null == currentTasks)
		{
			loadTasks();
		}

		//	load tasks from preference		
//		SharedPreferences prefs = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
//
//		try {
//			currentTasks = (ArrayList<Task>) ObjectSerializer.deserialize(prefs.getString(TASKS, ObjectSerializer.serialize(new ArrayList<Task>())));
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}		
	}
	
	private void loadTasks()
	{
		currentTasks = new ArrayList<Task>();
		//query for get data from table to the listView
		Cursor tasksCursor = database.query(TASKS_TABLE,
				new String[] {TASK_ID, TASK_NAME, TASK_DESG,TASK_ORG},
				null, null, null, null, String.format("%s", TASK_NAME));



		/*Cursor tasksCursor = database.query(TASKS_TABLE,
				new String[] {TASK_ID, TASK_NAME, TASK_DESG,TASK_ORG,TASK_TO,TASK_YEARS,TASK_INT1,TASK_INT2,TASK_INT3,TASK_INT4,TASK_COMPLETE},
				null, null, null, null, String.format("%s,%s", TASK_COMPLETE, TASK_NAME));
		*/
		tasksCursor.moveToFirst();
		Task t;
		if (! tasksCursor.isAfterLast()) {
			do
			{
				int id = tasksCursor.getInt(0);
				String name = tasksCursor.getString(1);
				String boolValue = tasksCursor.getString(2);
				String desg = tasksCursor.getString(3);
				/*String org = tasksCursor.getString(4);
				String turnover = tasksCursor.getString(5);
				String years = tasksCursor.getString(6);
				String interest1 = tasksCursor.getString(7);
				String interest2 = tasksCursor.getString(8);
				String interest3 = tasksCursor.getString(9);
				String interest4 = tasksCursor.getString(10);*/
				boolean complete = Boolean.parseBoolean(boolValue);
				Log.e("String idM",""+id);
				Log.e("String nameM",""+name);
				Log.e("String boolValueM",""+boolValue);


				t = new Task(name,boolValue,desg);
				/*t = new Task(name,desg,org,turnover,years,interest1,interest2,interest3,interest4);*/
				t.setId(id);
				t.setComplete(complete);
				currentTasks.add(t);
			} while(tasksCursor.moveToNext());
		}
	}

	public void setCurrentTasks(ArrayList<Task> currentTasks)
	{
		this.currentTasks  = currentTasks;
	}
	
	public ArrayList<Task> getCurrentTasks()
	{
		return currentTasks;
	}
	
	public void addTask(Task t)
	{
		assert(null != t);
		
		ContentValues values = new ContentValues();
		values.put(TASK_NAME, t.getName());
		values.put(TASK_DESG, t.getDesg());
		values.put(TASK_ORG,t.getTurnover());
		/*values.put(TASK_ORG, t.getOrg());
		values.put(TASK_TO, t.getTurnover());
		values.put(TASK_YEARS, t.getYears());
		values.put(TASK_INT1,t.getInt1());
		values.put(TASK_INT2,t.getInt2());
		values.put(TASK_INT3,t.getInt3());
		values.put(TASK_INT4,t.getInt4());*/

		Log.e("String id",""+t.getName());
		Log.e("String name",""+t.getDesg());
		Log.e("String boolValue",""+t.getTurnover());
		
			
		t.setId(database.insert(TASKS_TABLE, null, values));
		currentTasks.add(t);

		
		//save the task list to preference
//		SharedPreferences prefs = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);		
//		Editor editor = prefs.edit();
//		try {
//			editor.putString(TASKS, ObjectSerializer.serialize(currentTasks));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		editor.commit();
	}

	public void saveTask(Task t)
	{
		assert (null != t);
		
		ContentValues values = new ContentValues();
		values.put(TASK_NAME, t.getName());
		//values.put(TASK_COMPLETE, Boolean.toString(t.isComplete()));
		
		long id = t.getId();
		String where = String.format("%s = %d", TASK_ID, id);
		database.update(TASKS_TABLE, values, where, null);
	}
	
	public void deleteTasks(Long[] ids)
	{
		StringBuffer idList = new StringBuffer();
		for (int i =0; i< ids.length; i++)
		{
			idList.append(ids[i]);
			if (i < ids.length -1 )
			{
				idList.append(",");
			}
		}
		
		String where = String.format("%s in (%s)", TASK_ID, idList);
		database.delete(TASKS_TABLE, where, null);
	}

    public String getBarcodeValue()
	{
        return barcodeValue;
    }

    public void setBarcodeValue(String barcodeString) {
        this.barcodeValue = barcodeString;
    }
}