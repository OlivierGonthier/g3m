package com.roly.GiveMeMyMoney2;


import greendroid.app.GDActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import com.roly.GiveMeMyMoney2.R;
import com.roly.GiveMeMyMoney2.persistence.Database;
import com.roly.GiveMeMyMoney2.pret.AbstractPretable;
import com.roly.GiveMeMyMoney2.pret.Argent;
import com.roly.GiveMeMyMoney2.pret.Objet;
import com.roly.GiveMeMyMoney2.pret.Pret;
import com.roly.GiveMeMyMoney2.pret.Pretable;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;


public class ResumeActivity extends GDActivity{

	private final List<Pretable> prets = new ArrayList<Pretable>();
	private TextView all;
	private TextView toMe;
	private TextView toOthers;
	private TextView dateDepassee;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.resume);
		getActionBar().setTitle(getString(R.string.MenuResume));

		all = (TextView)findViewById(R.id.resume_total);
		toMe = (TextView)findViewById(R.id.resume_tome);
		toOthers = (TextView)findViewById(R.id.resume_toothers);
		dateDepassee = (TextView)findViewById(R.id.resume_limite_depassee);

		new PopulatorTask().execute();
	}

	private void notifyListPopulated() {
		String loans = getString(R.string.Loans);
		all.setText(prets.size()+" "+loans);
		toMe.setText(getSublistForSens(Pret.SENS_FOR_ME).size()+" "+loans);
		toOthers.setText(getSublistForSens(Pret.SENS_FROM_ME).size()+" "+loans);
		dateDepassee.setText(getItemsWithDateExpired().size()+" "+loans);

	}

	private ArrayList<Pretable> getSublistForSens(boolean sens) {
		ArrayList<Pretable> sublist = new ArrayList<Pretable>();
		for(Pretable pretable:prets){
			if(pretable.getPret().getSens()==sens)
				sublist.add(pretable);
		}
		return sublist;
	}

	private ArrayList<Pretable> getItemsWithDateExpired() {
		ArrayList<Pretable> sublist = new ArrayList<Pretable>();
		long current = Calendar.getInstance().getTimeInMillis();
		for(Pretable pretable:prets)
			if(pretable.getPret().getTimestampDateLimite() != 0 && pretable.getPret().getTimestampDateLimite() < current)
				sublist.add(pretable);
		return sublist;
	}

	private class PopulatorTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			final Database db= new Database(ResumeActivity.this);		
			Cursor c= db.getAllPret();
			if (c != null && c.isFirst()) {
				Pretable pretable = null;
				do{
					switch (c.getInt(Database.COLUMN_PRET_NATURE)) {
					case AbstractPretable.NATURE_ARGENT:
						pretable = new Argent(c.getInt(Database.COLUMN_PRET_ID));
						((Argent)pretable).setValue(c.getFloat(Database.COLUMN_PRET_VALEUR));
						break;
					case AbstractPretable.NATURE_OBJET:
						pretable = new Objet(c.getInt(Database.COLUMN_PRET_ID));
						((Objet)pretable).setName(c.getString(Database.COLUMN_PRET_NOM_OBJET));
					}
					Pret pret = new Pret(c.getString(Database.COLUMN_PRET_NOM), (c.getInt(Database.COLUMN_PRET_SENS) == 1));
					pret.setTimestamp(c.getLong(Database.COLUMN_PRET_DATE));

					long date = c.getLong(Database.COLUMN_PRET_DATE_LIMITE);
					pret.setTimestampDateLimite(date);
					pretable.setPret(pret);
					prets.add(pretable);
				} while (c.moveToNext());
			}
			c.close();
			db.close();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			notifyListPopulated();
			super.onPostExecute(result);
		}
	}	
}
