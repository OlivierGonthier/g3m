package com.roly.GiveMeMyMoney2.persistence;

import java.util.Calendar;

import com.roly.GiveMeMyMoney2.pret.AbstractPretable;
import com.roly.GiveMeMyMoney2.pret.Pret;
import com.roly.GiveMeMyMoney2.pret.Pretable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.util.Log;

public class Database extends SQLiteOpenHelper{

	private static final String DATABASE_NAME="GMMMdb";
	private static final int DATABASE_VERSION=6;

	//========PRET=========//
	private static final String TABLE_PRET="pret";

	public static final String KEY_PRET_ID = "_id";
	public static final String KEY_PRET_NOM = "nom";
	public static final String KEY_PRET_SENS = "sens";
	public static final String KEY_PRET_DATE = "date";
	public static final String KEY_PRET_DATE_LIMITE = "date_limite";
	public static final String KEY_PRET_NATURE = "nature";
	public static final String KEY_PRET_VALEUR = "valeur";
	public static final String KEY_PRET_NOM_OBJET = "nom_objet";

	public static final int COLUMN_PRET_ID = 0;
	public static final int COLUMN_PRET_NOM = 1;
	public static final int COLUMN_PRET_SENS = 2;
	public static final int COLUMN_PRET_DATE = 3;
	public static final int COLUMN_PRET_DATE_LIMITE = 4;
	public static final int COLUMN_PRET_NATURE = 5;
	public static final int COLUMN_PRET_VALEUR = 6;
	public static final int COLUMN_PRET_NOM_OBJET = 7;

	private static final String CREATE_TABLE_PRET="create table if not exists "+TABLE_PRET+"("
			+KEY_PRET_ID+" integer primary key autoincrement, "
			+KEY_PRET_NOM+" text not null, "
			+KEY_PRET_SENS+" boolean not null, "
			+KEY_PRET_DATE+" long not null," 
			+KEY_PRET_DATE_LIMITE+" dateLimite long,"
			+KEY_PRET_NATURE+ " int not null," 
			+KEY_PRET_VALEUR+ " float," 
			+KEY_PRET_NOM_OBJET+ " text"
			+");"
			;
	//--------------------------------------------------------------//
	//					Gestion de la table							//
	//--------------------------------------------------------------//

	/*****Appel de la base(constructeur)*****/

	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/*****Création de la table*****/

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_PRET);
		Log.d("DB:table create"," Table Created! ");
	}

	/*****Mise à jour de la table*****/

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w("[DB]", "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_PRET);
		onCreate(db);
	}

	//--------------------------------------------------------------//
	//					Gestion des prêts							//
	//--------------------------------------------------------------//

	/*****Insertion*****/

	public boolean insertPret(Pretable pretable) 
	{
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = getContentValues(pretable);
	
		try{
			db.insert(TABLE_PRET,null,values);
		}catch(SQLException e){
			Log.e("Erreur d'écriture dans la table",e.toString());
			return false;
		}finally{
			db.close();
		}
		return true;
	}

	/**
	 * Remplit un ContentValues avec les valeurs d'un pretable
	 * @param Pretable
	 * @return ContentValues
	 */
	private ContentValues getContentValues(Pretable pretable){

		ContentValues values = new ContentValues();
		
		Pret pret = pretable.getPret();
		
		values.put(KEY_PRET_NOM, pret.getNom());
		
		values.put(KEY_PRET_SENS, pret.getSens()?0:1);
		values.put(KEY_PRET_DATE, Calendar.getInstance().getTimeInMillis());
		if(pret.getTimestampDateLimite() > 0)
			values.put(KEY_PRET_DATE_LIMITE, pret.getTimestampDateLimite());
		values.put(KEY_PRET_NATURE, pretable.getNature());
		
		switch (pretable.getNature()) {
		case AbstractPretable.NATURE_ARGENT:
			values.put(KEY_PRET_VALEUR, (Float) pretable.getValeur());
			break;
		case AbstractPretable.NATURE_OBJET:
			values.put(KEY_PRET_NOM_OBJET, (String) pretable.getValeur());
		default:
			break;
		}
		return values;
	}

	/*****Suppression*****/

	public boolean deletePretable(Pretable pretable) 
	{
		SQLiteDatabase db = getWritableDatabase();
		boolean result = (db.delete(TABLE_PRET, KEY_PRET_ID + "=" + pretable.getId(), null) > 0);
		return result;
	}

	/*****Selection de tous les prêts*****/

	public Cursor getAllPret() throws SQLException
	{
		SQLiteDatabase db = getReadableDatabase();
		Cursor mCursor=db.query(TABLE_PRET, 
				new String[] {KEY_PRET_ID, KEY_PRET_NOM, KEY_PRET_SENS, KEY_PRET_DATE, KEY_PRET_DATE_LIMITE, KEY_PRET_NATURE, KEY_PRET_VALEUR, KEY_PRET_NOM_OBJET}, 
				null, null, null, null, null);
		if (mCursor != null) 
			mCursor.moveToFirst();
		Log.d("getAllPret","Curseur renvoyé!");
		return mCursor;
	}

	/*****Selection de tous les prêts par nature*****/

	public Cursor getPretbySens(boolean toWho) throws SQLException 
	{
		SQLiteDatabase db = getReadableDatabase();
		Cursor mCursor=db.query(true,TABLE_PRET, 
				new String[] {KEY_PRET_ID, KEY_PRET_NOM, KEY_PRET_SENS, KEY_PRET_DATE, KEY_PRET_DATE_LIMITE, KEY_PRET_NATURE, KEY_PRET_VALEUR, KEY_PRET_NOM_OBJET}, 
				KEY_PRET_SENS+"="+((toWho==true)?0:1),null,null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	/*****Selection d'un seul prêt selon son id*****/

	public Cursor getPret(int rowId) throws SQLException 
	{
		SQLiteDatabase db = getReadableDatabase();
		Cursor mCursor=db.query(TABLE_PRET, 
				new String[] {KEY_PRET_ID, KEY_PRET_NOM, KEY_PRET_SENS, KEY_PRET_DATE, KEY_PRET_DATE_LIMITE, KEY_PRET_NATURE, KEY_PRET_VALEUR, KEY_PRET_NOM_OBJET}, 
				KEY_PRET_ID+"="+rowId, null, null, null, null, null); 
		if (mCursor != null)
			mCursor.moveToFirst();
		return mCursor;
	}

	/*****Mise à jour d'un prêt*****/

	public boolean updatePret(Pretable pretable)
	{
		ContentValues values = getContentValues(pretable);
		SQLiteDatabase db = getWritableDatabase();
		boolean result;
		try{
			result = db.update(TABLE_PRET, values, KEY_PRET_ID+ "=" + pretable.getId(), null) > 0;
		}catch(SQLException e){
			Log.e("Erreur d'écriture dans la table",e.toString());
			return false;
		}finally{
			db.close();
		}
		return result;
	}
}