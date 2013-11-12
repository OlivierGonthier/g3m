package com.roly.g3m.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database extends SQLiteOpenHelper{

	private static final String DATABASE_NAME="GMMMdb";
	private static final int DATABASE_VERSION=6;

	public static final String TABLE_LOAN ="pret";

	public static final String KEY_PRET_ID = "_id";
	public static final String KEY_PRET_NOM = "nom";
	public static final String KEY_PRET_SENS = "sens";
	public static final String KEY_PRET_DATE = "date";
	public static final String KEY_PRET_DATE_LIMITE = "date_limite";
	public static final String KEY_PRET_NATURE = "nature";
	public static final String KEY_PRET_VALEUR = "valeur";
	public static final String KEY_PRET_NOM_OBJET = "nom_objet";

    public static final int COLUMN_ID = 0;
    public static final int COLUMN_NOM = 1;
    public static final int COLUMN_SENS = 2;
    public static final int COLUMN_DATE = 3;
    public static final int COLUMN_DATE_LIMITE = 4;
    public static final int COLUMN_NATURE = 5;
    public static final int COLUMN_VALEUR = 6;
    public static final int COLUMN_NOM_OBJET = 7;

    private static final String CREATE_TABLE_PRET="create table if not exists "+ TABLE_LOAN +"("
			+KEY_PRET_ID+" integer primary key autoincrement, "
			+KEY_PRET_NOM+" text not null, "
			+KEY_PRET_SENS+" boolean not null, "
			+KEY_PRET_DATE+" long not null ,"
			+KEY_PRET_DATE_LIMITE+" long,"
			+KEY_PRET_NATURE+ " int not null," 
			+KEY_PRET_VALEUR+ " float," 
			+KEY_PRET_NOM_OBJET+ " text"
			+");"
			;

	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_PRET);
		Log.d("DB:table create"," Table Created! ");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w("[DB]", "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		//db.execSQL("DROP TABLE IF EXISTS "+ TABLE_LOAN);
		onCreate(db);
	}

}