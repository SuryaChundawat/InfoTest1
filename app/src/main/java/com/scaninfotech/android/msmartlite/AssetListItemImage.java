package com.scaninfotech.android.msmartlite;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Activity;

/**
 * Created by kauser on 14/04/15.
 */
public class AssetListItemImage extends Activity
{
    ListView list;
    String[] web = {
            "Google Plus",
            "Twitter",
            "Windows",
            "Bing",
            "Itunes",
            "Wordpress",
            "Drupal"
    } ;
    Integer[] imageId = {
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3,
            R.drawable.image4,
            R.drawable.image5,
            R.drawable.image6,
            R.drawable.image7
    };
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasks_list_item_images);
        CustomList adapter = new
                CustomList(AssetListItemImage.this, web, imageId);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(AssetListItemImage.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();
            }
        });
    }
}