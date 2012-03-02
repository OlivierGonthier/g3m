package com.roly.GiveMeMyMoney2;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.roly.GiveMeMyMoney2.R;
import com.roly.GiveMeMyMoney2.pret.Pret;

import greendroid.app.GDActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class G3MActivity extends GDActivity{

	public Context context;
	public Intent intent;
	
	
	OnClickListener button_listener = new OnClickListener() {
	    @Override
		public void onClick(View v) {
	        Button button = (Button) v;
	        if (button.getId()== (R.id.resume)){
	        	intent=new Intent(context,ResumeActivity.class);
	        }
	        else if (button.getId()== (R.id.tome)){
	        	intent=new Intent(context,PretsListActivity.class);
	        	intent.putExtra("SENS", Pret.SENS_FOR_ME);
	        }	
	        else if (button.getId()== (R.id.toanother)){
	        	intent=new Intent(context,PretsListActivity.class);
	        	intent.putExtra("SENS", Pret.SENS_FROM_ME);
	        }
	        else if (button.getId()== (R.id.add)){
	        	intent=new Intent(context,AddActivity.class);
	        }
	        context.startActivity(intent);
	    };
	};
	
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       
	       setActionBarContentView(R.layout.dashboard_layout);
	       
	       context=this;	       
	  
	       final Button button_resume = (Button) findViewById(R.id.resume);
	       final Button button_tome = (Button) findViewById(R.id.tome);
	       final Button button_toanother = (Button) findViewById(R.id.toanother);
	       final Button button_add = (Button) findViewById(R.id.add);
	       
	       button_resume.setOnClickListener(button_listener);
	       button_tome.setOnClickListener(button_listener);
	       button_toanother.setOnClickListener(button_listener);
	       button_add.setOnClickListener(button_listener);
	      
	       AdView adView = (AdView)this.findViewById(R.id.adView);
	       adView.loadAd(new AdRequest());


	    }


		
}
