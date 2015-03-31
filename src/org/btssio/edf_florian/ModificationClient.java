package org.btssio.edf_florian;

import classes.Client;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class ModificationClient extends Activity {
	private Client leClient;
	private TextView textViewIdentifiant;
	private TextView textViewIdentite;
	private TextView textViewTelephone;
	private TextView textViewAdresse;
	private TextView textViewCp;
	private TextView textViewVille;
	private TextView textViewCompteur;
	private TextView textViewAncienReleve;
	private TextView textViewDateAncienReleve;
	private EditText editTextReleve;
	private EditText editTextDateReleve;
	private EditText editTextSituation;
	
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
		
		initialiserActivite();
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
	}//fin onOptionsItemSelected
	
	public void initialiserActivite()
	{	
		textViewIdentifiant			= (TextView) this.findViewById(R.id.txvIdentifiantValue);
		textViewIdentite			= (TextView) this.findViewById(R.id.txvIdentiteValue);
		textViewTelephone			= (TextView) this.findViewById(R.id.txvTelephone);
		textViewAdresse				= (TextView) this.findViewById(R.id.txvAdresse);
		textViewCp					= (TextView) this.findViewById(R.id.txvCp);
		textViewVille				= (TextView) this.findViewById(R.id.txvVille);
		textViewCompteur			= (TextView) this.findViewById(R.id.txvCompteurValue);
		textViewAncienReleve		= (TextView) this.findViewById(R.id.txvAncienReleveValue);
		textViewDateAncienReleve	= (TextView) this.findViewById(R.id.txvDateAncienReleveValue);
		editTextReleve				= (EditText) this.findViewById(R.id.edtReleve);
		editTextDateReleve			= (EditText) this.findViewById(R.id.edtDateReleve);
		editTextSituation			= (EditText) this.findViewById(R.id.edtSituation);
		
		
		textViewIdentifiant			.setText(leClient.getIdentifiant());
		textViewIdentite			.setText(leClient.getNom() + " " + leClient.getPrenom());
		textViewTelephone			.setText(leClient.getTelephone());
		textViewAdresse				.setText(leClient.getAdresse());
		textViewCp					.setText(leClient.getCodePostal());
		textViewVille				.setText(leClient.getVille());
		textViewCompteur			.setText(leClient.getIdCompteur());
		textViewAncienReleve		.setText(leClient.getAncienReleve().toString());
		textViewDateAncienReleve	.setText(leClient.getDateAncienReleve());
	}//fin initialiserActivite
}
