package com.adams.gemmarket;

import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.adams.gemmarket.util.IabHelper;
import com.adams.gemmarket.util.IabResult;
import com.adams.gemmarket.util.Inventory;
import com.adams.gemmarket.util.Purchase;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MainActivity extends Activity {
	private static final String PASSWORD = "12345678";
	private static final String USERNAME = "yuriadams";
	private Button purchaseButton;
	private Button spendButton;
	private String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjRX6qKpcQpRELgXBiJuoKi3VvtyWZUgjQpaXjuhPAVxMEP/LsABRjaRWjToqbROfCeBBp6SwfFPjJOE9NCEWL7jswFx2Gl7BsULclAVQ9KC8wMcpowmwKZXUjeflJi3G4WjxoSRK/4BxdumuNlPKYMyK7gVzv/gvMBGXC82z5rWoovecIDAMaUSCKvOr8ZleYSrelUWfCvo9vqDHCa7SW7vX4m0Af2c0cAStOPDI/aHwDURVwy1CDb2FmcKwCiXrBfYkWMgox7FYQJcuuoM8VVYTNY0FTd964P8Lk6sH23vyOcweKMRacrqOelcXgLAowM4JX3LM8adb+KTmqmBfmQIDAQAB";
	private static final String ITEM_SKU = "com.adams.gemmarket.100gems";

	IabHelper mHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ParseUser.logInInBackground(USERNAME, PASSWORD, new LogInCallback() {
			public void done(ParseUser user, ParseException e) {
				if (user != null) {
					initApplication();
				} else {
					Toast.makeText(getBaseContext(), "You don't have a user.", Toast.LENGTH_LONG).show();
				}
			}

		});
	}

	private void initApplication() {
		setUpInAppBilling();
		setButtonsBehaviors();
	}

	private void setUpInAppBilling() {
		mHelper = new IabHelper(this, base64EncodedPublicKey);

		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				if (!result.isSuccess()) {
					Log.e("BILLING FAILED", "In-app Billing setup failed: " + result);
				} else {
					Log.i("BILLING OK", "In-app Billing is set up OK");
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	private void setButtonsBehaviors() {
		purchaseButton = (Button) findViewById(R.id.purchaseButton);
		spendButton = (Button) findViewById(R.id.spendButton);

		purchaseButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mHelper.launchPurchaseFlow(MainActivity.this, ITEM_SKU, 10001, mPurchaseFinishedListener, UUID.randomUUID().toString());
			}
		});

		spendButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getBaseContext(), "You spent 55 gems", Toast.LENGTH_LONG).show();
			}
		});
	}

	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			if (result.isFailure()) {
				return;
			} else if (purchase.getSku().equals(ITEM_SKU)) {
				consumeItem();
				purchaseButton.setEnabled(false);
			}
		}
	};

	public void consumeItem() {
		mHelper.queryInventoryAsync(mReceivedInventoryListener);
	}

	IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result,
				Inventory inventory) {
			if (result.isFailure()) {
				Log.e("QUERY INVENTORY", "In-app Billing Query Invetory Failed: " + result);
			} else {
				mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU), mConsumeFinishedListener);
			}
		}
	};

	IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
		public void onConsumeFinished(Purchase purchase, IabResult result) {

			if (result.isSuccess()) {
				purchaseButton.setEnabled(true);
			} else {
				Log.e("CONSUME INVENTORY", "In-app Billing Consume Invetory Failed: " + result);
			}
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mHelper != null)
			mHelper.dispose();
		mHelper = null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
