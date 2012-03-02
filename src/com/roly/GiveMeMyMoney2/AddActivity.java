package com.roly.GiveMeMyMoney2;

import greendroid.app.GDActivity;

import java.util.Calendar;


import com.roly.GiveMeMyMoney2.R;
import com.roly.GiveMeMyMoney2.persistence.Database;
import com.roly.GiveMeMyMoney2.pret.AbstractPretable;
import com.roly.GiveMeMyMoney2.pret.Argent;
import com.roly.GiveMeMyMoney2.pret.Objet;
import com.roly.GiveMeMyMoney2.pret.Pret;
import com.roly.GiveMeMyMoney2.pret.Pretable;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class AddActivity extends GDActivity{

	private static final int DATE_DIALOG_ID = 0;
	
	private TextView text;
	public EditText editNom;
	public EditText editValue;

	private Button mDate;
	private Calendar date;
	private int mYear;
	private int mMonth;
	private int mDay;
	
	private boolean mToWho = Pret.SENS_FOR_ME;
	private int mType = AbstractPretable.NATURE_ARGENT;


	OnClickListener radio_listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			RadioButton rb = (RadioButton) v;
			if (rb.getId()== (R.id.newmoney)){
				text.setText(R.string.AddHowMany);
				editValue.setInputType(R.string.type_number);
				mType = AbstractPretable.NATURE_ARGENT;
			}
			else if (rb.getId()== (R.id.newobject)){
				text.setText(R.string.AddWhat);
				editValue.setInputType(MODE_APPEND);
				mType = AbstractPretable.NATURE_OBJET;
			}	
			else if (rb.getId()== (R.id.newtoanother))
				mToWho = Pret.SENS_FROM_ME;
			else if (rb.getId()== (R.id.newtome))
				mToWho = Pret.SENS_FOR_ME;
		}
	};

	OnKeyListener key_listener = new OnKeyListener() {
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {		          
				return true;
			}
			return false;
		}
	};

	private DatePickerDialog.OnDateSetListener mDateSetListener =
			new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, 
				int monthOfYear, int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDate();
		}
	};

	private void updateDate() {
		date.set(mYear, mMonth, mDay);
		mDate.setText(DateFormat.getDateFormat(getApplicationContext()).format(date.getTime()));
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this,
					mDateSetListener,
					mYear, mMonth, mDay);
		}
		return null;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
		
		setActionBarContentView(R.layout.add);
		getActionBar().setTitle(getString(R.string.MenuAdd));
		
		text=(TextView)findViewById(R.id.combien);
		final Database db=new Database(this);

		final RadioButton radio_tome = (RadioButton) findViewById(R.id.newtome);
		final RadioButton radio_toanother = (RadioButton) findViewById(R.id.newtoanother);
		final RadioButton radio_object = (RadioButton) findViewById(R.id.newobject);
		final RadioButton radio_money = (RadioButton) findViewById(R.id.newmoney);

		radio_tome.setOnClickListener(radio_listener);
		radio_toanother.setOnClickListener(radio_listener);
		radio_object.setOnClickListener(radio_listener);
		radio_money.setOnClickListener(radio_listener);
		radio_tome.setChecked(true);
		radio_money.setChecked(true);



		editNom = (EditText) findViewById(R.id.nom);
		editValue = (EditText) findViewById(R.id.value);

		editNom.setOnKeyListener(key_listener);
		editValue.setOnKeyListener(key_listener); 

		final CheckBox box= (CheckBox)findViewById(R.id.checkbox);
		final Button mAdd = (Button) findViewById(R.id.newAdd);
		mDate=(Button)findViewById(R.id.AddChangeDate);

		mDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});


		mAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!(editValue.getText().length()>0 && editNom.getText().length()>0)){
					Toast.makeText(getApplicationContext(), getString(R.string.FillFormToast), Toast.LENGTH_SHORT).show();
					return;
				}
				
				String nom = editNom.getText().toString();
				Pret pret = new Pret(nom,mToWho);
				if(box.isChecked())
					pret.setTimestampDateLimite(date.getTimeInMillis());
				Pretable pretable = null;
				switch (mType) {
				case AbstractPretable.NATURE_ARGENT:
					pretable = new Argent();
					float value;
					try{
						value = Float.parseFloat(editValue.getText().toString());
					}catch (NumberFormatException e) {
						e.printStackTrace();
						return;
					}
					((Argent)pretable).setValue(value);
					break;
				case AbstractPretable.NATURE_OBJET:
					pretable = new Objet();
					((Objet)pretable).setName(editValue.getText().toString());
				}
				if(pretable==null)	
					return;
				
				pretable.setPret(pret);	
				if(db.insertPret(pretable))
					Toast.makeText(getApplicationContext(), R.string.AddRegistered, Toast.LENGTH_LONG).show();				
				
				editValue.getText().clear();
				editNom.getText().clear();
			}
		});

	    date = Calendar.getInstance();
	    mYear = date.get(Calendar.YEAR);
	    mMonth = date.get(Calendar.MONTH);
	    mDay = date.get(Calendar.DAY_OF_MONTH);
	    
		updateDate();
	}
}

