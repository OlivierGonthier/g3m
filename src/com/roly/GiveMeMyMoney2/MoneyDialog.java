package com.roly.GiveMeMyMoney2;

import com.roly.GiveMeMyMoney2.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MoneyDialog extends Activity{
	public RadioButton radio_Oui;
	public RadioButton radio_EnPartie;
	public RadioButton radio_Non;
	public RadioGroup radio_group;
	public EditText mPart;
	public Button mDialogOk;
	
	OnClickListener radio_listener = new OnClickListener() {
	    @Override
		public void onClick(View v) {
	        RadioButton rb = (RadioButton) v;
	        rb.setChecked(true);	   
	        if(!rb.equals(radio_Oui))
	        	radio_Oui.setChecked(false);
	        if(!rb.equals(radio_EnPartie))
	        	radio_EnPartie.setChecked(false);
	        if(!rb.equals(radio_Non))
	        	radio_Non.setChecked(false);
	    }
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);
        setTitle(getString(R.string.ToQuestion));
        mPart=(EditText)findViewById(R.id.money_partie);
		radio_Oui = (RadioButton) findViewById(R.id.Oui);
		radio_EnPartie = (RadioButton) findViewById(R.id.en_partie);
		radio_Non = (RadioButton) findViewById(R.id.non);
		radio_group=(RadioGroup)findViewById(R.id.dialogGroup);
		mDialogOk=(Button)findViewById(R.id.dialogOk);
		
        radio_Oui.setOnClickListener(radio_listener);
    	radio_EnPartie.setOnClickListener(radio_listener);
    	radio_Non.setOnClickListener(radio_listener);
    	radio_Oui.setChecked(true); 
        
        mPart.setOnKeyListener(new OnKeyListener() {

		    @Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {		    	
		        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) 		           
		           return true;
		        return false;
		    }
		});
       
        mDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
			public void onClick(View v) {
            	
            	if(radio_Oui.isChecked()){
            				Log.d("radio","oui");
            				 setResult(RESULT_OK);
            	}
            				
            	else if(radio_EnPartie.isChecked()){
            				Log.d("radio","en partie");
            				Intent intent=new Intent();
    						Editable value = mPart.getText();
    						float f= 0;
    						try{
        						f = Float.parseFloat(value.toString());
    						}catch (NumberFormatException e) {
								
							}
            				intent.putExtra("part",f);
            				setResult(RESULT_OK,intent);
            	}
            	else			
            				setResult(RESULT_CANCELED);            		            		
            	finish();
            }
        });
    }
}