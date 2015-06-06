package com.adams.gemmarket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MainActivity extends Activity implements BillingProcessor.IBillingHandler{
	private Button purchaseButton;
	private Button spendButton;
	private BillingProcessor bp;
	private static final String LICENSE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjRX6qKpcQpRELgXBiJuoKi3VvtyWZUgjQpaXjuhPAVxMEP/LsABRjaRWjToqbROfCeBBp6SwfFPjJOE9NCEWL7jswFx2Gl7BsULclAVQ9KC8wMcpowmwKZXUjeflJi3G4WjxoSRK/4BxdumuNlPKYMyK7gVzv/gvMBGXC82z5rWoovecIDAMaUSCKvOr8ZleYSrelUWfCvo9vqDHCa7SW7vX4m0Af2c0cAStOPDI/aHwDURVwy1CDb2FmcKwCiXrBfYkWMgox7FYQJcuuoM8VVYTNY0FTd964P8Lk6sH23vyOcweKMRacrqOelcXgLAowM4JX3LM8adb+KTmqmBfmQIDAQAB";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ParseUser.logInInBackground("yuriadams", "12345678",
				new LogInCallback() {
					public void done(ParseUser user, ParseException e) {
						if (user != null) {
							initApplication();
						} else {
							Toast.makeText(getBaseContext(),
									"You don't have a user.", Toast.LENGTH_LONG)
									.show();
						}
					}

				});
	}

	private void initApplication() {
		setButtonsBehaviors();
		bp = new BillingProcessor(this, LICENSE_KEY, this);
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

	@Override
    public void onDestroy() {
        if (bp != null) 
            bp.release();

        super.onDestroy();
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

	@Override
	public void onBillingError(int errorCode, Throwable error) {
		Log.e("BILLING ERROR:", "HAPPENING HERE" + errorCode);
	}

	@Override
	public void onBillingInitialized() {
		Log.i("BILLING INITIALIZED:", "HAPPENING HERE");
		
	}

	@Override
	public void onProductPurchased(String productId, TransactionDetails details) {
		// TODO Auto-generated method stub
		Log.i("PURCHASE PRODUCT:", productId);
	}

	@Override
	public void onPurchaseHistoryRestored() {
		// TODO Auto-generated method stub
		Log.i("PURCHASE HISTORY:", "HAPPENING HERE!");
		
	}

}
