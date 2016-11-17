package com.scaninfotech.android.msmartlite;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;
import android.text.TextUtils;

import com.scaninfotech.android.msmartlite.tasks.Task;
import com.scaninfotech.android.msmartlite.tasks.TasksSQLiteOpenHelper;

public class AddAssetActivity extends AssetRegistrationActivity {

	private EditText taskNameEditText;
	private EditText visitorDesgEditText;
	private EditText visitorOrgEditText;
	public ListView Spinner1;
	//private EditText visitorTOEditText;
	//private EditText visitorYearsEditText;
	private NumberPicker np;
	private Spinner visitorTOSpinner;
	private Button addButton;
	private Button cancelButton;
	protected boolean changesPending;
	private AlertDialog unsavedChangesDialog;
	private boolean interest1;
	private boolean interest2;
	private boolean interest3;
	private boolean interest4;
	public TasksSQLiteOpenHelper dbhelper;
	private TasksSQLiteOpenHelper tasksSQLiteOpenHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_tasks);
		setUpViews();
		tasksSQLiteOpenHelper=new TasksSQLiteOpenHelper(this);

		//tasksSQLiteOpenHelper.openDataBase();

		tasksSQLiteOpenHelper.getdataList();

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tasksSQLiteOpenHelper.getdataList());
		Spinner1.setAdapter(adapter);

	}

	protected void addTask()
	{
        //private AutoCompleteTextView mEmailView;
        View focusView = null;
        boolean cancel = false;

		String taskName = taskNameEditText.getText().toString();
		String visitorDesg = visitorDesgEditText.getText().toString();
		String visitorOrg = visitorOrgEditText.getText().toString();
		//String visitorTO = visitorTOEditText.getText().toString();
		String visitorTO = visitorTOSpinner.getSelectedItem().toString();
		//String visitorYears = visitorYearsEditText.getText().toString();
		String visitorYears = String.valueOf(np.getValue());
		String visitorinterest1 = String.valueOf(interest1);
		String visitorinterest2 = String.valueOf(interest2);
		String visitorinterest3 = String.valueOf(interest3);
		String visitorinterest4 = String.valueOf(interest4);
		Log.e("Addtaskname",taskName);
		Log.e("AddVisitorDesg",visitorDesg);
		Log.e("AddvisitorOrgEdit",visitorOrg);
		Log.e("AddvisitorOrgSpin",visitorTO);

        if (TextUtils.isEmpty(taskName)) {
            taskNameEditText.setError(getString(R.string.error_field_required));
            focusView = taskNameEditText;
            cancel = true;
        }

        if (cancel)
		{
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        else {
			Task t = new Task(taskName, visitorDesg, visitorTO);
            /*Task t = new Task(taskName, visitorDesg, visitorOrg, visitorTO, visitorYears, visitorinterest1, visitorinterest2, visitorinterest3, visitorinterest4);*/
            getStuffApplication().addTask(t);
            finish();
        }
	}

	protected void cancel() {
		if (changesPending) {
			unsavedChangesDialog = new AlertDialog.Builder(this)
				.setTitle(R.string.unsaved_changes_title)
				.setMessage(R.string.unsaved_changes_message)
				.setPositiveButton(R.string.add_task, new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						addTask();
					}
				})
				.setNeutralButton(R.string.discard, new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				})
				.setNegativeButton(android.R.string.cancel, new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						unsavedChangesDialog.cancel();
					}
				})
				.create();
			unsavedChangesDialog.show();
		} else {
			finish();
		}
	}
	
	private void setUpViews() {
		taskNameEditText = (EditText)findViewById(R.id.task_name);
		visitorDesgEditText = (EditText)findViewById(R.id.visitor_desgn);
		visitorOrgEditText = (EditText)findViewById(R.id.visitor_org);
		//visitorTOEditText = (EditText)findViewById(R.id.visitor_to);
		visitorTOSpinner = (Spinner) findViewById(R.id.turnover_spinner);
		Spinner1=(ListView) findViewById(R.id.Spinner1 );
		//visitorYearsEditText = (EditText)findViewById(R.id.visitor_years);
		
		np = (NumberPicker)findViewById(R.id.visitor_years);
	    np.setMinValue(1);// restricted number to minimum value i.e 1
	    np.setMaxValue(31);// restricted number to maximum value i.e. 31
	    np.setWrapSelectorWheel(true);
		
	 // Create an ArrayAdapter using the string array and a default spinner layout
	 ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.turnover_array, android.R.layout.simple_spinner_item);
	 // Specify the layout to use when the list of choices appears
	 adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	 // Apply the adapter to the spinner
	 visitorTOSpinner.setAdapter(adapter);
	    
	    
		addButton = (Button)findViewById(R.id.add_button);
		cancelButton = (Button)findViewById(R.id.cancel_button);
		
		addButton.setOnClickListener(new View.OnClickListener() {
		
		public void onClick(View v) {
				addTask();
			}
		});
		cancelButton.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
			cancel();
		}
		});
		taskNameEditText.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				changesPending = true;
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
			public void afterTextChanged(Editable s) { }
		});
	}

	public void onCheckboxClicked(View view) {
	    // Is the view now checked?
	    boolean checked = ((CheckBox) view).isChecked();
	    
	    // Check which check box was clicked
	    switch(view.getId()) {
	        case R.id.checkbox_1:
	            if (checked)
	                // Put some meat on the sandwich
	            	interest1 = true;
	            else
	            	interest1 = false;
	            break;
	        case R.id.checkbox_2:
	            if (checked)
	            	interest2 = true;
	            else
	            	interest2 = false;
	            break;
	        case R.id.checkbox_3:
	            if (checked)
	            	interest3 = true;
	            else
	            	interest3 = false;
	            break;
	        case R.id.checkbox_4:
	            if (checked)
	            	interest4 = true;
	            else
	            	interest4 = false;
	            break;
	        // TODO: Veggie sandwich
	    }
	}

    public void launchSimpleFragmentActivity(View v) {
        Intent intent = new Intent(this, SimpleScannerActivity.class);

        //IntentIntegrator integrator = new IntentIntegrator(SimpleScannerActivity.class);
        //IntentIntegrator integrator = new IntentIntegrator(this);
        //integrator.initiateScan();
        //intent.putExtra("scanner_result","value");
        startActivityForResult(intent,1);
    }

    /*public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
             // handle scan result
        }
        // else continue with any other code you need in the method
        // ...
        //}
    }*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // A contact was picked.  Here we will just display it
                // to the user.
                //startActivity(new Intent(Intent.ACTION_VIEW, data));
                //Toast.makeText(this, "Contents = " + rawResult.getText() +
                //        ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(this, "Result has come", Toast.LENGTH_LONG).show();

                String newString = data.getExtras().getString("string_key");
                //set your EditText
                final EditText assetName = (EditText) findViewById(R.id.task_name);
                assetName.setText(newString);


                //assetName.setText(((AssetRegistration) this.getApplication()).getBarcodeValue());

            }
        }
    }

    public void setAssetNo(String barcodeString) {
        final EditText assetName = (EditText) findViewById(R.id.task_name);
        assetName.setText(barcodeString);
    }
}

