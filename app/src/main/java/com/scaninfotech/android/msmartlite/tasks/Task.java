package com.scaninfotech.android.msmartlite.tasks;

import java.io.Serializable;

public class Task implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private boolean complete;
	private String designation;
	private String organisation;
	private String turnover;
	private String years;
	private String visitorInterest1;
	private String visitorInterest2;
	private String visitorInterest3;
	private String visitorInterest4;
	

	private long id;

	public Task(String taskName, String visitor_desg,  String visitor_to)
	{
		name = taskName;
		designation = visitor_desg;
		/*organisation = visitor_org;*/
		turnover = visitor_to;
		/*years = visitor_years;
		visitorInterest1 = visitor_Interest1;
		visitorInterest2 = visitor_Interest2;
		visitorInterest3 = visitor_Interest3;
		visitorInterest4 = visitor_Interest4;*/

	}
	
	/*public Task(String taskName, String visitor_desg, String visitor_org, String visitor_to, String visitor_years, String visitor_Interest1, String visitor_Interest2,String visitor_Interest3,String visitor_Interest4) {
		name = taskName;
		designation = visitor_desg;
		organisation = visitor_org;
		turnover = visitor_to;
		years = visitor_years;
		visitorInterest1 = visitor_Interest1;
		visitorInterest2 = visitor_Interest2;
		visitorInterest3 = visitor_Interest3;
		visitorInterest4 = visitor_Interest4;

	}*/

	public String getName()
	{
		return name;
	}
	
	public String getDesg()
	{
		return designation;
	}
	
	public String getOrg()
	{
		return organisation;
	}

	public String getTurnover()
	{
		return turnover;
	}
	public String getYears()
	{
		return years;
	}
	public String getInt1()
	{
		return visitorInterest1;
	}
	public String getInt2()
	{
		return visitorInterest2;
	}
	public String getInt3()
	{
		return visitorInterest3;
	}
	public String getInt4()
	{
		return visitorInterest4;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String toString()

	{
		return name;
	}

	public boolean isComplete()
	{
		return complete;
	}

	public void setComplete(boolean complete)
	{
		this.complete = complete;
	}
	
	public void toggleComplete()
	{
		complete = !complete;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public long getId()
	{
		return id;
	}	
};