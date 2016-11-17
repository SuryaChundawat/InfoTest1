package com.scaninfotech.android.msmartlite;

import android.Manifest;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.scaninfotech.android.msmartlite.adapters.AssetListAdapter;
import com.scaninfotech.android.msmartlite.tasks.Task;
import com.scaninfotech.android.msmartlite.tasks.TasksSQLiteOpenHelper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVWriter;

public class ViewAssetsActivity extends ListActivity {
    private Button addButton;
	private AssetRegistration app;
	private AssetListAdapter adapter;
	private Button removeButton;
	private Context ctx;
    private Button viewListButton;
    static final int REQUEST_WRITE_EXTERNAL_STORAGE=1;
    static final int REQUEST_READ_EXTERNAL_STORAGE=2;
    static final int REQUEST_READ_CONTACT=3;
    static final int REQUEST_WRITE_CONTACT=4;

    private int REQUEST_READ_PHONE_STATE;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        
        app = (AssetRegistration) getApplication();
        adapter = new AssetListAdapter(this, app.getCurrentTasks());
        setListAdapter(adapter);

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheckREAD = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheckREADCon = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        int permissionCheckWRITECon = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS);

        if (permissionCheckREADCon != PackageManager.PERMISSION_GRANTED)
        {
            Log.e("Came to Access","Yes it Access");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_READ_CONTACT);
        } else
        {
            Log.e("Came to denied","Yes it is");
            setUpViews();
        }

        if (permissionCheckWRITECon != PackageManager.PERMISSION_GRANTED)
        {
            Log.e("Came to Access","Yes it Access");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_CONTACT);
        } else
        {
            Log.e("Came to denied","Yes it is");
            setUpViews();
        }

        if (permissionCheck != PackageManager.PERMISSION_GRANTED)
        {
            Log.e("Came to Access","Yes it Access");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
        } else
        {
            Log.e("Came to denied","Yes it is");
            setUpViews();
        }

        if (permissionCheckREAD != PackageManager.PERMISSION_GRANTED)
        {
            Log.e("Came to Access","Yes it Access");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
        } else
        {
            Log.e("Came to denied","Yes it is");
            setUpViews();
        }

      /* if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)== PackageManager.PERMISSION_GRANTED)
       {
           Log.e("Came to Access","Yes it Access");
           setUpViews();
       }else
       {
           Log.e("Came to denied","Yes it is");
           Toast.makeText(this,"Permission denied",Toast.LENGTH_SHORT).show();
       }*/
    }

	@Override
	protected void onResume() {
		super.onResume();
		adapter.forceReload();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		adapter.toggleTaskCompleteAtPosition(position);
		Task t = adapter.getItem(position);
		app.saveTask(t);
	}


   /* @Override
    public void onRe*/
	
	protected void removeCompletedTasks() {
		Long[] ids = adapter.removeCompletedTasks();
		app.deleteTasks(ids);
	}

	
	private void setUpViews() {
		addButton = (Button)findViewById(R.id.add_button);
		removeButton = (Button)findViewById(R.id.remove_button);
        viewListButton = (Button)findViewById(R.id.export_button);
		
		addButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ViewAssetsActivity.this, AddAssetActivity.class);
				startActivity(intent);
			}
		});
		removeButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				removeCompletedTasks();
			}
		});

        viewListButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ViewAssetsActivity.this, AssetListItemImage.class);
                startActivity(intent);
            }
        });

		ctx = ViewAssetsActivity.this;
		
		Button impCSVBtn = (Button) findViewById(R.id.export_button);
		impCSVBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
		  try
		  {
		      new exportDataCSVTask().execute("");
		  }
		  catch(Exception ex)
		  {
		      Log.e("Error in MainActivity", ex.toString());
		  }
		}

		});
	}

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode) {
            case REQUEST_WRITE_EXTERNAL_STORAGE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED))
                {
                    //TODO
                    setUpViews();
                }
                break;
            case REQUEST_READ_EXTERNAL_STORAGE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED))
                {
                    //TODO
                    setUpViews();
                }
            case REQUEST_READ_CONTACT:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED))
                {
                    //TODO
                    setUpViews();
                }
            case REQUEST_WRITE_CONTACT:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED))
                {
                    //TODO
                    setUpViews();
                }


            default:
                break;
        }
    }

	
	class exportDataCSVTask extends AsyncTask<String, Void, Boolean> {
		private final ProgressDialog dialog = new ProgressDialog(ctx);
		
		@Override
		
		protected void onPreExecute() 
        {
            this.dialog.setMessage("Exporting database...");
            this.dialog.show();
        }
		
		protected Boolean doInBackground(String... args) {
			// TODO Auto-generated method stub
			//File dbFile=getDatabasePath(DB_NAME);
			TasksSQLiteOpenHelper helper = new TasksSQLiteOpenHelper(ViewAssetsActivity.this);
            //DbClass DBob = new DbClass(ViewTasksActivity.this);

            File exportDir = new File(Environment.getExternalStorageDirectory(), "");

            if (!exportDir.exists()) 

            {

                exportDir.mkdirs();

            }

            File file = new File(exportDir, "excerDB.csv");

            try 

            {

                file.createNewFile();                

                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));

                SQLiteDatabase db = helper.getReadableDatabase();

                Cursor curCSV = db.rawQuery("SELECT * FROM tasks",null);

                csvWrite.writeNext(curCSV.getColumnNames());

                while(curCSV.moveToNext())

                {
/*
                    String arrStr[] ={curCSV.getString(0),curCSV.getString(1),

                            curCSV.getString(2)};*/

                    String arrStr[] ={curCSV.getString(0),curCSV.getString(1),

                            curCSV.getString(2),curCSV.getString(3)};
                    csvWrite.writeNext(arrStr);


                   /* String arrStr[] ={curCSV.getString(0),curCSV.getString(1),

                        curCSV.getString(2),curCSV.getString(3),curCSV.getString(4),curCSV.getString(5),curCSV.getString(6),curCSV.getString(7),curCSV.getString(8),curCSV.getString(9),curCSV.getString(10)};
                    csvWrite.writeNext(arrStr);*/

                }

                csvWrite.close();

                curCSV.close();

                return true;

            }

            catch(SQLException sqlEx)

            {

                Log.e("MainActivity", sqlEx.getMessage(), sqlEx);

                return false;                

            }

            catch (IOException e)

            {

                Log.e("MainActivity", e.getMessage(), e);

                return false;

            }
		}
		@Override
		protected void onPostExecute(final Boolean success) {
			if (this.dialog.isShowing()) 
            {
                this.dialog.dismiss();
            }
            if (success) 
            {
                Toast.makeText(ctx, "Export successful!", Toast.LENGTH_SHORT).show();
            } 
            else 
            {
                Toast.makeText(ctx, "Export failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}