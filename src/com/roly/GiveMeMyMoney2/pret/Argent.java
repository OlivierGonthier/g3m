package com.roly.GiveMeMyMoney2.pret;

import java.util.Currency;
import java.util.Locale;

public class Argent extends AbstractPretable{
	
	private float value;
	private Pret pret;

	public Argent(){
		super(AbstractPretable.NATURE_ARGENT);
	}
	
	public Argent(int id){
		super(AbstractPretable.NATURE_ARGENT,id);
	}
	
	public float getValue() {
		return value;
	}
	
	public void setValue(float value) {
		this.value =  value;
	}

	public Pret getPret() {
		return pret;
	}

	public void setPret(Pret pret) {
		this.pret = pret;
	}

	@Override
	public Object getValeur() {
		return value;
	}
	
	@Override
	public String toString() {
		Locale locale = Locale.getDefault();
		
		StringBuilder builder = new StringBuilder();
		builder.append(getPret().getNom());
		builder.append(" : ");
		builder.append(getValue());
		builder.append(Currency.getInstance(locale).getSymbol());
		return builder.toString();
	}

}
