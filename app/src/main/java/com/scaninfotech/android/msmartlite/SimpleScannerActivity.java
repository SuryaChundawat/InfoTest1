package com.scaninfotech.android.msmartlite;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;
import android.media.MediaPlayer;

import com.google.zxing.Result;

//import com.scaninfotech.android.msmartlite.ZXingScannerView;

public class SimpleScannerActivity extends ActionBarActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult)
    {
        //Toast.makeText(this, "Contents = " + rawResult.getText() +
        //        ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();
        //mScannerView.startCamera();
        //SimpleScannerActivity.putExtra("result", 1);
        //setResult(RESULT_OK,SimpleScannerActivity);
        //this.barcodeValue = rawResult.getBarcodeFormat().toString();
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.laserscanner);
        mp.start();
        ((AssetRegistration) this.getApplication()).setBarcodeValue(rawResult.getText());

        Intent resultIntent = new Intent();

        //add your string from the clicked item
        resultIntent.putExtra("string_key", rawResult.getText());

        //return data back to parent, which is Activity A
        setResult(RESULT_OK, resultIntent);

        finish();
    }
}
