package org.btssio.edf_florian;

import classes.Client;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ModificationClient extends Activity {
	private Client leClient;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modification_client);
		//Initialisation du client
		leClient = new Client(	this.getIntent().getExtras().getString("identifiant"),
								this.getIntent().getExtras().getString("nom"),
								this.getIntent().getExtras().getString("prenom"),
								this.getIntent().getExtras().getString("adresse"),
								this.getIntent().getExtras().getString("codePostal"),
								this.getIntent().getExtras().getString("ville"),
								this.getIntent().getExtras().getString("telephone"),
								this.getIntent().getExtras().getString("idCompteur"),
								this.getIntent().getExtras().getString("dateAncienReleve"),
								this.getIntent().getExtras().getDouble("ancienReleve"));
		leClient.setDateDernierReleve(	this.getIntent().getExtras().getString("dateDernierReleve"));
		leClient.setSignatureBase64(	this.getIntent().getExtras().getString("signatureBase64"));
		leClient.setDernierReleve(		this.getIntent().getExtras().getDouble("dernierReleve"));
		leClient.setSituation(			this.getIntent().getExtras().getInt("situation"));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.modification_client, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
