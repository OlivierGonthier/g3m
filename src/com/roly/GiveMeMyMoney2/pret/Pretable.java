package com.roly.GiveMeMyMoney2.pret;

import android.content.Context;

public interface Pretable {
	
	public Pret getPret();

	public void setPret(Pret pret);
	
	public int getNature();
	
	public Object getValeur();
	
	public int getId();

	public String getDescription(Context context);
}
