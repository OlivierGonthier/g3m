package com.roly.GiveMeMyMoney2.utils;


import android.content.Context;

import com.roly.GiveMeMyMoney2.pret.Pretable;


import greendroid.widget.item.SubtitleItem;

public class PretItem extends SubtitleItem{

	private final Pretable pretable;

	public PretItem(Context context, Pretable pretable){
		super(pretable.toString(), pretable.getDescription(context));
		this.pretable=pretable;
	}

	public Pretable getPretable() {
		return pretable;
	}


}
