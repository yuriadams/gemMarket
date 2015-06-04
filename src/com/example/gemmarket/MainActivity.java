package com.example.gemmarket;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MainActivity extends Activity {
	Button purchaseButton;
	Button spendButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ParseUser.logInInBackground("yuriadams", "12345678", new LogInCallback() {
		  public void done(ParseUser user, ParseException e) {
		    if (user != null) {
				setButtonsBehaviors();	
		    } else {
		    	Toast.makeText(getBaseContext(), "You don't have a user.", Toast.LENGTH_LONG).show();
		    }
		  }
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void setButtonsBehaviors() {
		purchaseButton = (Button) findViewById(R.id.purchaseButton);
		spendButton = (Button) findViewById(R.id.spendButton);

		purchaseButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getBaseContext(), "You purchased 100 gems", Toast.LENGTH_LONG).show();
				
			}
		});

		spendButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getBaseContext(), "You spent 55 gems", Toast.LENGTH_LONG).show();
			}
		});
	}

}
