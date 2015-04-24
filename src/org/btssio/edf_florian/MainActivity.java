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
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private Intent theIntent;
	private ImageButton imgbtnIdentification;
	private ImageButton imgbtnClients;
	private ImageButton imgbtnImport;
	private ImageButton imgbtnSave;
	private Button btnInitBdd;
	private BdAdapter bdd;
	
	/**
	 * Initialise l'activité
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//Affectation de la vue xml aux attributs "ImageButton", et affectation d'un onClickListener
	 	imgbtnIdentification = (ImageButton) findViewById(R.id.imgVIdentification);
	 	imgbtnIdentification.setOnClickListener(this);
		imgbtnClients = (ImageButton) findViewById(R.id.imgVClients);
		imgbtnClients.setOnClickListener(this);
		imgbtnImport = (ImageButton) findViewById(R.id.imgVImport);
		imgbtnImport.setOnClickListener(this);
		imgbtnSave = (ImageButton) findViewById(R.id.imgVSave);
		imgbtnSave.setOnClickListener(this);
		btnInitBdd = (Button) findViewById(R.id.btnInitBdd);
		btnInitBdd.setOnClickListener(this);
		//testBd(); //==> TEST OK ! :)
	}//fin onCreate

	/**
	 * Initialise le menu de l'activité
	 * @param Le menu permettant d'initialiser celui de l'activité [Menu]
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}//fin onCreateOptionsMenu

	/**
	 * Gère les clics sur les différents éléments du menu
	 * @param L'élément du menu sur lequel l'utilisateur a cliqué [MenuItem]
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		/* as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}*/
		return super.onOptionsItemSelected(item);
	}//fin onOptionsItemSelected
	
	/**
	 * Gère les clics sur les différents boutons-image de l'application
	 * @param L'élément sur lequel l'utilisateur a cliqué [View]
	 */
	@Override
	public void onClick(View v)
	{
		Log.d("Étape", "~ Click sur une image !");
		switch (v.getId())
		{
			/* ~~~~~~~~~~~~~~~~~~~~~~~ *
			 *  Bouton Identification  *
			 * ~~~~~~~~~~~~~~~~~~~~~~~ */
		 	case R.id.imgVIdentification:
		 		theIntent = new Intent(this, LayoutIdentification.class);
		 		break;
		 	/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ *
			 *  Bouton Clients - Relevé Compteur  *
			 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
		 	case R.id.imgVClients:
		 		theIntent = new Intent(this, AfficheListeClient.class);
		 		break;
		 	/* ~~~~~~~~~~~~~~~~~~~~~~~~~~ *
			 *  Bouton Import de données  *
			 * ~~~~~~~~~~~~~~~~~~~~~~~~~~ */
		 	case R.id.imgVImport:
		 		theIntent = new Intent(this, LayoutImport.class);
		 		break;
		 	/* ~~~~~~~~~~~~~~~~~~~ *
			 *  Bouton Sauvegarde  *
			 * ~~~~~~~~~~~~~~~~~~~ */
		 	case R.id.imgVSave:
		 		theIntent = new Intent(this, LayoutSauvegarde.class);
		 		break;
		 	/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~ *
			 *  Bouton Initialiser la BDD  *
			 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
		 	case R.id.btnInitBdd:
		 		bdd = new BdAdapter(this);
		 		bdd.open();
		 		try{
		 			bdd.viderLaTable();
		 			Toast.makeText(this, "La table Client a été vidée", Toast.LENGTH_SHORT).show();
		 		} catch (Exception ex) {
		 			Log.d("Étape", "~ Impossible de vider la table Client !");
		 			Toast.makeText(this, "Impossible de vider la table Client !", Toast.LENGTH_SHORT).show();
		 		}
		 		try {
		 			bdd.insererDesClients();
			 		Toast.makeText(this, "Réinitialisation de la bdd terminée !", Toast.LENGTH_SHORT).show();
		 		} catch (Exception ex) {
		 			Log.d("Étape", "~ Impossible d'insérer les nouveaux clients !");
		 			Toast.makeText(this, "Impossible d'insérer les nouveaux clients !", Toast.LENGTH_SHORT).show();
		 		}//fin catch
		 		break;
		 	/* ~~~~~~~~~ *
			 *  Default  *
			 * ~~~~~~~~~ */
		 	default :
		 		Log.d("", "Erreur dans le switch du onClick de imageclick");
		}//fin switch.
		
		//Si l'utilisateur a cliqué sur un bouton-image et non le bouton "Initialiser la BDD", on va lancer l'activité
		if(v.getId() != R.id.btnInitBdd)
			this.startActivity(theIntent); //lancement de l'activité
	}//fin onClick
	
	/**
	 * Réalise une suite de tests pour vérifier le bon fonctionnement de la gestion de la bdd
	 */
	public void testBd()
	{
		//Création d'une instance de la classe BdAdapter
		BdAdapter clientsBdd = new BdAdapter(this);
		Log.d("Étape #1", "Création d'un Client");
		//Création d'un client
		Client unClient = new Client("cli01",
									"NOM",
									"Prénom",
									"une adresse quelque part",
									"12345",
									"une ville",
									"1234567890",
									"compteur01",
									"01/02/03",
									12.3);
		Log.d("Étape #2", unClient.toString());
		Log.d("Étape #3", "Ouverture de la bdd...");
		//On ouvre la bdd pourduration pouvoir ouvrir dedans
		clientsBdd.open();
		Log.d("Étape #4", "Insertion du client dans la base");
		//On insère le client que l'on vient de créer
		clientsBdd.insererClient(unClient);
		Log.d("Étape #5", "Récupération du client depuis la base");
		//Pour vérifier que l'on a bien créé un Article dans la bdd,
		//on l'extrait de a bdd grâce à l'identifiant de Client que l'on a créé précédament
		Client unClientFromBdd = clientsBdd.getClientWithIdentifiant("cli01");
		Log.d("Étape #6", unClientFromBdd.toString());
		//Si on a bien récupéré un client grâce à l'id donné,
		if(unClientFromBdd != null)
		{
			//on affiche des données du client récupéré
			Toast.makeText(this, unClientFromBdd.getIdentifiant() + ", " + unClientFromBdd.getNom() + " " + unClientFromBdd.getPrenom(), Toast.LENGTH_SHORT).show();
		}//fin if
		else
		{
			Toast.makeText(this, "Echec de la récupération du client :(", Toast.LENGTH_SHORT).show();
		}//fin else
		Log.d("Étape #7", "Modification du client qu'on vient de récupérer");
		//on modifie son nom,
		unClientFromBdd.setNom("unAutreNom");
		Log.d("Étape #8", unClientFromBdd.toString());
		Log.d("Étape #9", "Modification du client dans la base");
		//puis on met à jour la bdd
		clientsBdd.updateClient(unClientFromBdd.getIdentifiant(), unClientFromBdd);
		Log.d("Étape #10", "Récupération du client modifié");
		//On extrait le client de la bdd grâce à son nouvel identifiant
		Client unAutreClientFromBdd = clientsBdd.getClientWithIdentifiant("cli01");
		Log.d("Étape #11", unAutreClientFromBdd.toString());
		//S'il existe un client possédant cet identifiant dans la bdd
		if(unAutreClientFromBdd != null)
		{
			//on affiche des données du client récupéré
			Toast.makeText(this, unAutreClientFromBdd.getIdentifiant() + ", " + unAutreClientFromBdd.getNom() + " " + unAutreClientFromBdd.getPrenom(), Toast.LENGTH_SHORT).show();
			Log.d("Étape #12", "Suppression du client dans la base");
			clientsBdd.removeClientWithId(unClientFromBdd.getIdentifiant());
		}//fin if
		else
		{
			Toast.makeText(this, "Echec de la récupération du 2ème client :(", Toast.LENGTH_SHORT).show();
		}//fin else
		Log.d("Étape #13", "Récupération du client n'existant plus");
		//On essait d'extraire de nouveau le client de la bdd toujours grâce à son nouvel identifiant
		unClientFromBdd = clientsBdd.getClientWithIdentifiant("cli02");
		//Si aucun client n'a été retourné
		if(unClientFromBdd == null)
		{
			Toast.makeText(this, "Le client a bien été supprimé de la bdd :)", Toast.LENGTH_SHORT).show();
		}//fin if
		else
		{
			Toast.makeText(this, "Le client N'a PAS été supprimé de la bdd :(", Toast.LENGTH_SHORT).show();
		}//fin else
		clientsBdd.close();
	}//fin testBd
}//fin classe