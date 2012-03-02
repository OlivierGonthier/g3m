package com.roly.GiveMeMyMoney2;

import java.util.ArrayList;


import com.roly.GiveMeMyMoney2.R;
import com.roly.GiveMeMyMoney2.persistence.Database;
import com.roly.GiveMeMyMoney2.pret.AbstractPretable;
import com.roly.GiveMeMyMoney2.pret.Argent;
import com.roly.GiveMeMyMoney2.pret.Objet;
import com.roly.GiveMeMyMoney2.pret.Pret;
import com.roly.GiveMeMyMoney2.pret.Pretable;
import com.roly.GiveMeMyMoney2.utils.PretItem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import greendroid.app.GDListActivity;
import greendroid.widget.ItemAdapter;
import greendroid.widget.item.Item;
import greendroid.widget.item.TextItem;

public class PretsListActivity extends GDListActivity{

	private final ArrayList<Item> items = new ArrayList<Item>();
	private ItemAdapter adapter;
	private Database db;
	private int selected=-1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getListView().setCacheColorHint(Color.rgb(204, 204, 204));
		getListView().setBackgroundColor(Color.rgb(204, 204, 204));

		db = new Database(this);

		boolean sens = getIntent().getBooleanExtra("SENS",true);
		populate(sens);
		if(!sens)
			getActionBar().setTitle(getString(R.string.MenuToOther));
		else
			getActionBar().setTitle(getString(R.string.MenuToMe));

		adapter = new ItemAdapter(this, items);
		setListAdapter(adapter);

		getListView().setOnItemClickListener(clickOnList);
	}

	@Override
	protected void onResume() {
		if(db==null)
			db = new Database(this);
		super.onResume();
	}

	public void populate(boolean toWho){
		Cursor c;
		c = db.getPretbySens(toWho);
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
				Pret pret = new Pret(c.getString(Database.COLUMN_PRET_NOM), toWho);
				pret.setTimestamp(c.getLong(Database.COLUMN_PRET_DATE));

				long date = c.getLong(Database.COLUMN_PRET_DATE_LIMITE);
				pret.setTimestampDateLimite(date);
				pretable.setPret(pret);
				items.add(new PretItem(this, pretable));
			} while (c.moveToNext());
		}else{
			TextItem item = new TextItem(getString(R.string.NothingToShow));
			item.enabled = false;
			items.add(item);
		}
		c.close();
	}

	@Override
	protected void onPause() {
		db.close();
		super.onPause();
	}

	/*****Analyse les résultats du dialog*****/

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
		if(requestCode==0){
			if(resultCode==RESULT_OK){
				if(data==null)
					rendu();
				else if(data.hasExtra("part"))
					part(data.getFloatExtra("part", 0));	
			}
		}
	}

	/*****Listener de la liste*****/

	OnItemClickListener clickOnList = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long id) {		
			selected = position;
			Pretable pretable = ((PretItem)items.get(selected)).getPretable();
			switch (pretable.getNature()) {
			case AbstractPretable.NATURE_ARGENT:
				showMoneyDialog();	
				break;
			case AbstractPretable.NATURE_OBJET:
				showObjectDialog();
			}
		}
	};

	public void showObjectDialog() {
		new AlertDialog.Builder(this)
		.setMessage(R.string.ToQuestion)
		.setCancelable(true)
		.setPositiveButton(R.string.ToYes, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				rendu();
			}
		})
		.setNegativeButton(R.string.ToNo, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {

			}
		}).show();

	}

	/*****Affiche le dialog*****/

	public void showMoneyDialog(){
		Intent i= new Intent(this,MoneyDialog.class);
		startActivityForResult(i, 0);			
	}

	/*****Enlever un prêt*****/

	public void rendu(){
		db.deletePretable(((PretItem)items.get(selected)).getPretable());
		adapter.remove((Item) adapter.getItem(selected));
		adapter.notifyDataSetChanged();
	}

	/*****Si la liste est vide, mettre un element indicateur*****/	
	/*****Probleme ici******/
	private void checkIfEmpty() {
		if(adapter.getCount()<=0){
			TextItem item = new TextItem(getString(R.string.NothingToShow));
			item.enabled = false;
			adapter.add(item);
			adapter.notifyDataSetChanged();
		}
	}

	/*****Change la valeur d'un prêt*****/

	public void part(float less){
		Pretable pretable = ((PretItem)items.get(selected)).getPretable();

		if(pretable.getNature() != AbstractPretable.NATURE_ARGENT || less == 0)
			return;

		Argent argent = (Argent)pretable;
		float new_value = argent.getValue() -less;

		if(new_value==0){

			rendu();

		}else if(new_value<0){

			argent.getPret().setSens(!argent.getPret().getSens());
			argent.setValue(-new_value);

			db.updatePret(argent);
			adapter.remove((Item) adapter.getItem(selected));
			adapter.notifyDataSetChanged();
			
		}else{

			argent.setValue(new_value);
			db.updatePret(argent);
			adapter.remove((Item) adapter.getItem(selected));
			adapter.insert(new PretItem(this, argent), selected);
			adapter.notifyDataSetChanged();

		}


	} 


}
