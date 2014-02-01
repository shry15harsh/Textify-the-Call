package com.example.xpoject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class email extends Activity {
	TextView email;
	EditText emailAdd;
	Button send;
	static String data;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.email);
		email=(TextView)findViewById(R.id.tvEmail);
		emailAdd=(EditText)findViewById(R.id.etEmailAdd);
		send=(Button)findViewById(R.id.btSend);
		Bundle b=getIntent().getExtras();
		if(b!=null){
			data=b.getString("Data");
		}
		Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
		if(data==""){
			Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
		}
		
		send.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent email=new Intent(android.content.Intent.ACTION_SENDTO);
				email.setData(Uri.parse("mailto:"+emailAdd.getText().toString()));
				email.putExtra(android.content.Intent.EXTRA_EMAIL, emailAdd.getText().toString());
				email.putExtra(android.content.Intent.EXTRA_SUBJECT,"conversation");
				email.putExtra(android.content.Intent.EXTRA_TEXT, data);
				try {
				    startActivity(Intent.createChooser(email, "Send mail..."));
				    checkInternerConnection();
				} catch (android.content.ActivityNotFoundException ex) {
				    Toast.makeText(email.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
				}
				Toast.makeText(getApplicationContext(), "Sending email", Toast.LENGTH_LONG).show();
			}
			@SuppressLint("ShowToast")
			private boolean checkInternerConnection() {
				// TODO Auto-generated method stub
				final ConnectivityManager conMgr = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
		           if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() &&    conMgr.getActiveNetworkInfo().isConnected()) {
		                 return true;
		           } else {
		                 Toast.makeText(getApplicationContext(),"Internet Connection Not Present!Try later",Toast.LENGTH_LONG);
		                 return false;
		           }
			}
		});
	}
}
