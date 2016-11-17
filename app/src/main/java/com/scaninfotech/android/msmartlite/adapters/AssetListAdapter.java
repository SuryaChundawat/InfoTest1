package com.scaninfotech.android.msmartlite.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.google.android.gms.wearable.Asset;
import com.scaninfotech.android.msmartlite.R;
import com.scaninfotech.android.msmartlite.tasks.Task;
import com.scaninfotech.android.msmartlite.views.AssetListItem;

import java.util.ArrayList;

public class AssetListAdapter extends BaseAdapter
{

	private ArrayList<Task> tasks;
	private Context context;

	public AssetListAdapter(Context context, ArrayList<Task> tasks)
	{
		super();
		this.tasks = tasks;
		this.context = context;
	}

	@Override
	public int getCount() {
		return tasks.size();
	}

	@Override
	public Task getItem(int position) {
		return (null == tasks) ? null : tasks.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AssetListItem tli;
		if (null == convertView) {
			tli = (AssetListItem) View.inflate(context, R.layout.task_list_item, null);
		} else {
			tli = (AssetListItem) convertView;
		}
		tli.setTask(tasks.get(position));
		return tli;
	}
	
	public void forceReload()
	{
		notifyDataSetChanged();
	}

	public void toggleTaskCompleteAtPosition(int position)
	{
		Task task = getItem(position);
		task.toggleComplete();
		notifyDataSetChanged();
	}

	public Long[] removeCompletedTasks()
	{
		ArrayList<Task> completedTasks = new ArrayList<Task>();
		ArrayList<Long> completedIds = new ArrayList<Long>();
		for (Task task : tasks)
		{
			if (task.isComplete())
			{
				completedTasks.add(task);
				completedIds.add(task.getId());
			}
		}
		tasks.removeAll(completedTasks);
		notifyDataSetChanged();
		return completedIds.toArray(new Long[]{});
	}	
}