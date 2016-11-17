package com.scaninfotech.android.msmartlite;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.NumberPicker;

//import android.view.Menu;
//import android.view.MenuItem;

public class NumberPickerActivity extends Activity
{

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.add_tasks);
    NumberPicker np = (NumberPicker)findViewById(R.id.visitor_years);
    np.setMinValue(1);// restricted number to minimum value i.e 1
    np.setMaxValue(31);// restricted number to maximum value i.e. 31
    np.setWrapSelectorWheel(true); 

    np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener()
    {

      @Override
      public void onValueChange(NumberPicker picker, int oldVal, int newVal)
      {

       // TODO Auto-generated method stub

       //String Old = "Old Value : ";

       //String New = "New Value : ";

      }
    });

     Log.d("NumberPicker", "NumberPicker");

   }

}/* NumberPickerActivity */