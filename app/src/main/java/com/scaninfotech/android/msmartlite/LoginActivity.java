package com.scaninfotech.android.msmartlite;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.telephony.TelephonyManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.scaninfotech.android.msmartlite.tasks.TasksSQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity /*implements LoaderCallbacks<Cursor> */{

    TextView name ;
    TextView phone;
    TextView emailView;
    TextView street;
    TextView place;
    TasksSQLiteOpenHelper tasksSQLiteOpenHelper;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";
    public static final String Phone = "phoneKey";
    public static final String Email = "emailKey";
    public static final String Street = "streetKey";
    public static final String Place = "placeKey";
    public static final String ActKey = "ActKey";

    public static String seedValue = "I AM UNBREAKABLE";
    public static String MESSAGE = "No one can read this message without decrypting me.";
    private static final int REQUEST_READ_PHONE_STATE = 0;

    SharedPreferences sharedpreferences;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tasksSQLiteOpenHelper= new TasksSQLiteOpenHelper(this);


        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();


        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void populateAutoComplete()
    {
       // getLoaderManager().initLoader(0, null, this);
    }



    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } /*else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }*/

        //Check activation status
        if (checkActivation() == 0)
        {
            Toast.makeText(LoginActivity.this, "Application not activated", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, ActivateActivity.class);
            startActivity(intent);
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
            Intent intent = new Intent(LoginActivity.this, ViewAssetsActivity.class);
            startActivity(intent);

            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            //System.out.println("Emi::" + telephonyManager.getDeviceId());
            Log.v("NSLOG", "Emi: " + telephonyManager.getDeviceId());

            //Add key to preferences

            //name = (TextView) findViewById(R.id.editTextName);
            //phone = (TextView) findViewById(R.id.editTextPhone);
            emailView = (TextView) findViewById(R.id.email);
            //street = (TextView) findViewById(R.id.editTextEmail);
            //place = (TextView) findViewById(R.id.editTextCity);

            //String n  = name.getText().toString();
            //String ph  = phone.getText().toString();
            String e  = emailView.getText().toString();
            //String s  = street.getText().toString();
            //String p  = place.getText().toString();
            Editor editor = sharedpreferences.edit();
            //editor.putString(Name, n);
            //editor.putString(Phone, ph);

            try {
//                String encryptedData = AESHelper.encrypt(seedValue, (e+"|"+telephonyManager.getDeviceId()));
//                Log.v("EncryptDecrypt", "Encoded String " + encryptedData);

                String encryptedData1 = AESEncryptDecrypt.encryptNew((e+"|"+telephonyManager.getDeviceId()), seedValue);
                Log.v("EncryptDecrypt1", "Encoded String " + encryptedData1);

                String decryptedData1 = AESEncryptDecrypt.decryptNew(encryptedData1, seedValue);
                Log.v("EncryptDecrypt1", "Decrypted String " + decryptedData1);

                editor.putString(ActKey, encryptedData1);
                //String decryptedData = AESHelper.decrypt(seedValue, first);
                //Log.v("EncryptDecrypt", "Decoded String " + decryptedData);

            } catch (Exception ex)
            {
                ex.printStackTrace();
            }

            //editor.putString(ActKey, encryptedData);
            //editor.putString(Street, s);
            //editor.putString(Place, p);
            editor.commit();
        }
    }

    public void Browstable(View view)
    {

        tasksSQLiteOpenHelper.openDataBase();



    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

   /* @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }*/

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(20);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success)
        {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
    private int checkActivation()
    {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {


            String activationKey = "";
            String loginName = "";
            //Check Device authorisation status
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            //System.out.println("Emi::" + telephonyManager.getDeviceId());
            Log.v("NSLOG", "Emi: " + telephonyManager.getDeviceId());

            name = (TextView) findViewById(R.id.email);
            //Phone.setText(telephonyManager.getDeviceId());
            //phone = (TextView) findViewById(R.id.editTextPhone);
            //email = (TextView) findViewById(R.id.editTextStreet);
            //street = (TextView) findViewById(R.id.editTextEmail);
            //place = (TextView) findViewById(R.id.editTextCity);

            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

            if (sharedpreferences.contains(ActKey)) {
                activationKey = (sharedpreferences.getString(ActKey, ""));

                try {
                    String decryptedData = AESEncryptDecrypt.decryptNew(activationKey, seedValue);
                    Log.v("EncryptDecrypt", "Decoded String " + decryptedData);

                    StringTokenizer tokens = new StringTokenizer(decryptedData, "|");
                    String first = "";
                    String second = "";

                    first = tokens.nextToken();
                    while (tokens.hasMoreTokens()) {
                        second = tokens.nextToken();
                    }
                    Log.v("CheckActivation", "Tokens:" + first + "," + second);

                    if ((first.equals(name.getText().toString())) && (second.equals(telephonyManager.getDeviceId()))) {
                        Toast.makeText(LoginActivity.this, "Contents Match = " + first, Toast.LENGTH_SHORT).show();
                        Log.v("Key Matches", "Tokens:" + first + "," + second);
                        return 1;
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                return 0;
            } else return 1;
        }return 1;

//        String[] actParts = activationKey.split("|");

        //for (String retval: actParts){

        //    Log.v("NSLOG", "Retval1: " + actParts[1]);
        //}

        //Log.v("NSLOG", "Retval: " + retval[0]);

//        Toast.makeText(LoginActivity.this, "Contents = " + activationKey, Toast.LENGTH_SHORT).show();
//        loginName = name.getText().toString();


        //Toast.makeText(this, "Contents = " + rawResult.getText() + ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();
            /*if (sharedpreferences.contains(Phone))
            {
                phone.setText(sharedpreferences.getString(Phone, ""));

            }
            if (sharedpreferences.contains(Email))
            {
                email.setText(sharedpreferences.getString(Email, ""));

            }
            if (sharedpreferences.contains(Street))
            {
                street.setText(sharedpreferences.getString(Street, ""));

            }
            if (sharedpreferences.contains(Place))
            {
                place.setText(sharedpreferences.getString(Place,""));

            }*/

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                    checkActivation();
                }
                break;

            default:
                break;
        }
    }
}



