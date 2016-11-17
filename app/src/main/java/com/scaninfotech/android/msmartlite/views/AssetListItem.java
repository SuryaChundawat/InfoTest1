package com.scaninfotech.android.msmartlite.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scaninfotech.android.msmartlite.R;
import com.scaninfotech.android.msmartlite.tasks.Task;

public class AssetListItem extends LinearLayout
{

	private Task task;
	private CheckedTextView checkbox;
	private TextView setLocation,setIsUsed;

	public AssetListItem(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	@Override
	protected void onFinishInflate()
	{
		super.onFinishInflate();
		checkbox = (CheckedTextView) findViewById(android.R.id.text1);
		setLocation=(TextView)findViewById(R.id.textLocation);
		setIsUsed=(TextView)findViewById(R.id.textIsused);
	}

	public void setTask(Task task)
	{
		this.task = task;
		checkbox.setText(task.getName());
		setLocation.setText(task.getDesg());
		setIsUsed.setText(task.getTurnover());
		checkbox.setChecked(task.isComplete());
	}

	public Task getTask()
	{
		return task;
	}
}
