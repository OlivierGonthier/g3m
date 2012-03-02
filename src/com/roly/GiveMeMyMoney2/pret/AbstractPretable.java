package com.roly.GiveMeMyMoney2.pret;

import android.content.Context;
import android.text.format.DateFormat;

import com.roly.GiveMeMyMoney2.R;

public abstract class AbstractPretable implements Pretable{

	public static final int NATURE_ARGENT = 0;
	public static final int NATURE_OBJET = 1;
	
	private int mNature;
	private int id;
	
	protected AbstractPretable(int nature){
		this.mNature = nature;
	}
	
	protected AbstractPretable(int nature, int id){
		this.mNature = nature;
		this.id=id;
	}
	
	@Override
	public int getNature() {
		return mNature;
	}

	@Override
	public int getId() {
		return id;
	}
	
	@Override
	public String getDescription(Context context) {	
		java.text.DateFormat formatter = DateFormat.getDateFormat(context);
		StringBuilder builder = new StringBuilder();
		builder.append("Date: ");
		builder.append(formatter.format(getPret().getTimestamp()));
		if(getPret().getTimestampDateLimite()>0){
			builder.append(", "+context.getString(R.string.AddLimit)+"  ");
			builder.append(formatter.format(getPret().getTimestampDateLimite()));
		}
		return builder.toString();
	}
}
