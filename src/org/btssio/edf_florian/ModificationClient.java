package org.btssio.edf_florian;

import classes.Client;
import dao.BdAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import util.RegexTests;

public class ModificationClient extends Activity implements OnClickListener{
	private Client leClient;
	private int indexClientDansListe;
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
	private BdAdapter bdd;
	private Button btnOk;
	private Button btnGeoloc;
	
	/**
	 * Initialise l'activité
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modification_client);
		
		indexClientDansListe = this.getIntent().getExtras().getInt("indexClient");
		
		Log.d("Étape", "~ Création du client à partir des extras"); 
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
		
		Log.d("Étape", "~ Initialisation de l'activité");
		//On va remplir les "TextView"s grâce au client
		initialiserActivite();
	}//fin onCreate

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
	
	@Override
	public void onClick(View v) {
		Log.d("Étape", "~ Click sur un bouton");
		switch (v.getId())
		{
			case R.id.btnOk:
				Log.d("Étape", "~ Click sur Ok détecté");
				try {
					enregistrerModifications();
					Toast.makeText(this, "Modifications enregistrées !",Toast.LENGTH_SHORT).show();
					//On prépare le retour de l'activité
					Intent returnIntent = new Intent();
					returnIntent.putExtra("indexClient", indexClientDansListe);
					setResult(RESULT_OK,returnIntent);
					finish();
				} catch (Exception ex) {
					Toast.makeText(this, ex.getMessage(),Toast.LENGTH_SHORT).show();
				}//fin catch
				break;
			case R.id.btnAnnuler:
				finish();
				break;
			case R.id.btnGeoloc:
				Log.d("Étape", "~ Click sur Géoloc. détecté");
				Intent theIntent = new Intent(this, ActivityGeolocalisation.class);
				theIntent.putExtra("identifiant", leClient.getIdentifiant());
				this.startActivityForResult(theIntent,0);
				break;
		}//fin switch
	}//fin onClick
	
	/**
	 * Effectue les contrôles de saisie avant de modifier le client dans la BDD
	 * @throws Lève une exception si une des saisies est incorrecte ou que la modification du client a échoué [Exception]
	 */
	public void enregistrerModifications() throws Exception
	{
		//On vérifie le bon format des données saisies
		checkEditText();
		//On va modifier le client dans la base de données
		Log.d("Étape", "~ Modification du client dans la base");
		try {
			bdd = new BdAdapter(this);
			bdd.open();
			bdd.updateClient(leClient.getIdentifiant(), leClient);
			bdd.close();
		} catch (Exception ex) {
			Log.d("Étape", "~ Echec de la modification du client dans la BDD");
			throw new Exception("Echec de la modification du client dans la BDD", new Throwable("echecModifError"));
		}//fin catch
		Log.d("Étape", "~ Modification réussie");
	}//fin save
	
	/**
	 * Effectue les contrôles de saisie nécessaires avant la modification du client
	 * @throws Lève une Exception si une des saisies est incorrecte [Exception]
	 */
	private void checkEditText() throws Exception
	{
		Log.d("Étape", "~ Vérification des champs vides");
		//On vérifie qu'il n'y ai pas de champ vide
		checkEditTextVide();
		
		//Création de variables qui contiendront les saisies "cast"ées si elles sont correctes
		Double doubleReleve = 0.0;
		int intSituation = 0;
		
		Log.d("Étape", "~ Vérification du champ \"Relevé\"");
		//On vérifie que la valeur saisie dans editTextReleve peut être cast en Double
		try {
			doubleReleve = Double.valueOf(editTextReleve.getText().toString());
		} catch (Exception ex) {
			Log.d("Étape", "~ Relevé incorrect");
			throw new Exception("Veuillez saisir un nombre décimal dans le champ \"Relevé\"", new Throwable("castError"));
		}//fin catch
		
		Log.d("Étape", "~ Vérification du champ \"Date\"");
		//On vérifie que la date saisie correspond au format jj/mm/aa
		if (RegexTests.executeMatching("^[0-9]{2}(/[0-9]{2}){2}$", editTextDateReleve.getText().toString()) == false)
		{
			Log.d("Étape", "~ Date incorrecte");
			throw new Exception("Date incorrecte !\nVeuillez saisir une date au format \"jj/mm/aa\"", new Throwable("dateFormatError"));
		}//fin if
		
		Log.d("Étape", "~ Vérification du champ \"Situation\"");
		//On vérifie que la saisie correspond à un nombre entier
		try {
			intSituation = Integer.valueOf(editTextSituation.getText().toString());
			//On vérifie que la situation saisie n'est pas 0
			if (intSituation == 0)
			{
				Log.d("Étape", "~ Situation incorrecte");
				throw new Exception("La situation ne doit pas être 0", new Throwable("situation0Error"));
			}//fin if
		} catch (Exception ex) {
			Log.d("Étape", "~ Situation incorrecte");
			throw new Exception("Veuillez saisir un nombre entier dans le champ \"Situation\"", new Throwable("castError"));
		}//fin catch
		
		Log.d("Étape", "~ Modification du client");
		//On modifie le client s'il n'y a pas eu d'exception de levée
		leClient.setAncienReleve(doubleReleve);
		leClient.setDateAncienReleve(editTextDateReleve.getText().toString());
		leClient.setSituation(intSituation);
	}//fin checkEditText
	
	/**
	 * Vérifie si les "EditText"s sont vide.
	 * 
	 * Dans le cas où l'une d'entre-elles serait vide, une exception est levée
	 * @throws Une exception "emptyFieldError" [Exception]
	 */
	private void checkEditTextVide() throws Exception
	{
		//Si le champ Relevé est vide
		if (editTextReleve.getText().equals(""))
		{
			Log.d("Étape", "~ Champ \"Relevé\" vide");
			throw new Exception("Veuillez remplir le champ \"Relevé\" !", new Throwable("emptyFieldError"));
		}//fin if
			
		//Si le champ Date est vide
		if (editTextDateReleve.getText().equals(""))
		{
			Log.d("Étape", "~ Champ \"Date\" vide");
			throw new Exception("Veuillez remplir le champ \"Date\" !", new Throwable("emptyFieldError"));
		}//fin if
			
		//Si le champ Situation est vide
		if (editTextSituation.getText().equals(""))
		{
			Log.d("Étape", "~ Champ \"Situation\" vide");
			throw new Exception("Veuillez remplir le champ \"Situation\" !", new Throwable("emptyFieldError"));
		}//fin if
	}//fin checkEditTextVide
	
	/**
	 * Initialise les "TextView"s et les "EditText"s en fonction des vues xml,
	 * et modifie leur valeur en fonction du client.
	 */
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
		
		btnOk = (Button) this.findViewById(R.id.btnOk);
		btnOk.setOnClickListener(this);
		
		btnGeoloc = (Button) this.findViewById(R.id.btnGeoloc);
		btnGeoloc.setOnClickListener(this);
	}//fin initialiserActivite
}//fin classe
