package com.roly.GiveMeMyMoney2.pret;

public class Pret {


	public final static boolean SENS_FOR_ME = true;
	public final static boolean SENS_FROM_ME = false;
	
	private boolean sens;
	private String nom;
	private long timestamp;
	private long timestampDateLimite=0;
	
	public Pret(String nom, boolean sens){
		this.nom = nom;
		this.sens=sens;
	}
	
	public String getNom() {
		return nom;
	}
	public void setNom(String text) {
		this.nom = text;
	}

	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public long getTimestampDateLimite() {
		return timestampDateLimite;
	}
	public void setTimestampDateLimite(long timestampDateLimite) {
		this.timestampDateLimite = timestampDateLimite;
	}

	public boolean getSens() {
		return sens;
	}
	
	public void setSens(boolean sens){
		this.sens = sens;
	}
	
}
