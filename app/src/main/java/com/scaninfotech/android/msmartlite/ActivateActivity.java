package com.scaninfotech.android.msmartlite;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A login screen that offers login via email/password.
 */
public class ActivateActivity extends Activity implements LoaderCallbacks<Cursor>
{

    TextView userNameView;
    TextView serialNoView;
    TextView activateEmailView;
//    TextView street;
//    TextView place;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";
    public static final String Phone = "phoneKey";
    public static final String Email = "emailKey";
    public static final String Street = "streetKey";
    public static final String Place = "placeKey";
    public static final String ActKey = "ActKey";

    public static String seedValue = "I AM UNBREAKABLE";
    public static String MESSAGE = "No one can read this message without decrypting me.";

    public final static String strikeIronUserName = "stikeironusername@yourdomain.com";
    public final static String strikeIronPassword = "strikeironpassword";
    public final static String apiURL = "http://ws.strikeiron.com/StrikeIron/EMV6Hygiene/VerifyEmail?";
    public final static String apiRCAURL = "http://182.72.41.229/rca_oneservice/RestServiceImpl.svc/UserAuthenticate/Administrator/De1234!/SSLC/";
    emailVerificationResult result = new emailVerificationResult();

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
    private AutoCompleteTextView mUserNameView;
    private EditText mEmailView;
    private EditText mSerialNo;
    private Button activateButton;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activate);
        FindViewByIdRef();

        // Set up the login form.
       // mUserNameView = (AutoCompleteTextView) findViewById(R.id.userName);
        populateAutoComplete();

        //mEmailView = (EditText) findViewById(R.id.activateEmail);
//        mEmailView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
//                if (id == R.id.login || id == EditorInfo.IME_NULL) {
//                    attemptLogin();
//                    return true;
//                }
//                return false;
//            }
//        });
       // mSerialNo = (EditText) findViewById(R.id.activateSerialNo);

       /* Button mActivateButton = (Button) findViewById(R.id.activateButton);
        mActivateButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                attemptActivation();
            }
        });*/

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    public void FindViewByIdRef()
    {
        //EditText
        mSerialNo = (EditText) findViewById(R.id.activateSerialNo);
        mEmailView = (EditText) findViewById(R.id.activateEmail);

        //AutoCompletextview
        mUserNameView = (AutoCompleteTextView) findViewById(R.id.userName);

        //Button
        activateButton=(Button)findViewById(R.id.activateButton);



    }

    public void activateButton(View view)
    {

        Toast.makeText(this,"Button is pressed", Toast.LENGTH_SHORT).show();

        attemptActivation();

    }


    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptActivation() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUserNameView.setError(null);
        mEmailView.setError(null);
        mSerialNo.setError(null);

        // Store values at the time of the login attempt.
        String userName = mUserNameView.getText().toString();
        String emailID = mEmailView.getText().toString();
        String serialNo = mSerialNo.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
//        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
//            mPasswordView.setError(getString(R.string.error_invalid_password));
//            focusView = mPasswordView;
//            cancel = true;
//        }
        // Check for a valid username.
        if (TextUtils.isEmpty(userName)) {
            mUserNameView.setError(getString(R.string.error_field_required));
            focusView = mUserNameView;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(emailID)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }
        // Check for a valid serial no.
        if (TextUtils.isEmpty(serialNo)) {
            mSerialNo.setError(getString(R.string.error_field_required));
            focusView = mSerialNo;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

//            showProgress(true);
//
//            mAuthTask = new UserLoginTask(userName,emailID, serialNo);
//            mAuthTask.execute((Void) null);
//            Intent intent = new Intent(ActivateActivity.this, ViewAssetsActivity.class);
//            startActivity(intent);

            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            //System.out.println("Emi::" + telephonyManager.getDeviceId());
            Log.v("NSLOG", "Emi: " + telephonyManager.getDeviceId());

            //Add key to preferences

            userNameView = (TextView) findViewById(R.id.userName);
            activateEmailView = (TextView) findViewById(R.id.activateEmail);
            serialNoView = (TextView) findViewById(R.id.activateSerialNo);
            //phone = (TextView) findViewById(R.id.editTextPhone);
            //street = (TextView) findViewById(R.id.editTextEmail);
            //place = (TextView) findViewById(R.id.editTextCity);

            //String n  = name.getText().toString();
            String un  = userNameView.getText().toString();
            String e  = activateEmailView.getText().toString();
            String sr  = serialNoView.getText().toString();
            String actKey = un + "|" + e +"|" + sr + "|" + telephonyManager.getDeviceId();
            Log.v("NSLOG", "ActKey: " + actKey);

//            String urlString = apiURL + "LicenseInfo.RegisteredUser.UserID=" + strikeIronUserName + "&LicenseInfo.RegisteredUser.Password=" + strikeIronPassword + "&VerifyEmail.Email=" + un + "&VerifyEmail.Timeout=30";
            String urlString = apiRCAURL;

            new CallAPI().execute(urlString);
            startActivity(new Intent(this,ViewAssetsActivity.class)
            );

            if (!TextUtils.isEmpty(result.statusNbr))
            {
              /*  startActivity(new Intent(this,ViewAssetsActivity.class)
                );*/
               /* Intent i = new Intent(this, DownloadFile.class);
               //Create the bundle
                Bundle bundle = new Bundle();
                //Add your data to bundle
                bundle.putString("fileURL", result.statusNbr);
               //Add the bundle to the intent
                i.putExtras(bundle);
               //Fire that second activity
                startActivity(i);*/
            }
            //String p  = place.getText().toString();

            //editor.putString(Name, n);
            //editor.putString(Phone, ph);

/*            try {
//                String encryptedData = AESHelper.encrypt(seedValue, (e+"|"+telephonyManager.getDeviceId()));
//                Log.v("EncryptDecrypt", "Encoded String " + encryptedData);
                Editor editor = sharedpreferences.edit();

                String encryptedData1 = AESEncryptDecrypt.encryptNew((actKey+"|"+telephonyManager.getDeviceId()), seedValue);
                Log.v("EncryptDecrypt1", "Encoded String " + encryptedData1);

                String decryptedData1 = AESEncryptDecrypt.decryptNew(encryptedData1, seedValue);
                Log.v("EncryptDecrypt1", "Decrypted String " + decryptedData1);

                editor.putString(ActKey, encryptedData1);
                //String decryptedData = AESHelper.decrypt(seedValue, first);
                //Log.v("EncryptDecrypt", "Decoded String " + decryptedData);

                editor.commit();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
*/
            //editor.putString(ActKey, encryptedData);
            //editor.putString(Street, s);
            //editor.putString(Place, p);

        }
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

    @Override
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

    }

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
                new ArrayAdapter<String>(ActivateActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        //mEmailView.setAdapter(adapter);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUserName;
        private final String mEmail;
        private final String mSerialNo;

        UserLoginTask(String userName, String email, String serialNo) {

            mUserName = userName;
            mEmail = email;
            mSerialNo = serialNo;
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
                if (pieces[0].equals(mUserNameView)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mEmail);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mUserNameView.setError(getString(R.string.error_incorrect_password));
                mUserNameView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

    }

    private class CallAPI extends AsyncTask<String, String, String>
    {

        @Override
        protected String doInBackground(String... params)
        {

            String urlString=params[0]; // URL to call
            String resultToDisplay = "";
            InputStream in = null;
            emailVerificationResult result = null;
            String returnString = null;

            // HTTP Get
            try
            {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                in = new BufferedInputStream(urlConnection.getInputStream());
            } catch (Exception e )
            {
                System.out.println(e.getMessage());
                return e.getMessage();
            }

            Log.v("NSLOG", "Raw URl Return Value: " + in.toString());

            // Parse XML
            XmlPullParserFactory pullParserFactory;

            try
            {
                pullParserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = pullParserFactory.newPullParser();

                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in, null);
                result = parseXML(parser);
            } catch (XmlPullParserException e)
            {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            }

            Log.v("NSLOG", "URl Return Value: " + result);
            resultToDisplay = result.toString();
            return resultToDisplay;
        }

        protected void onPostExecute(String result)
        {
            Toast.makeText(getApplicationContext(),"return resut is :"+result, Toast.LENGTH_SHORT).show();
        }

        private emailVerificationResult parseXML( XmlPullParser parser ) throws XmlPullParserException, IOException
        {

            int eventType = parser.getEventType();
//            emailVerificationResult result = new emailVerificationResult();

            while( eventType!= XmlPullParser.END_DOCUMENT)
            {
                String name = null;

                switch(eventType)
                {
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        Log.v("NSLOG", "URl Return Value: " + name);

                        if( name.equals("UserAuthenticateResult")) {
                            result.statusNbr = "http://"+ parser.nextText();
                            Log.v("NSLOG", "URl: " + result.statusNbr);
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        break;
                } // end switch

                eventType = parser.next();
            } // end while

            return result;
        }
    } // end CallAPI

    private class emailVerificationResult
    {
        public String statusNbr;
        public String hygieneResult;
    }

}



