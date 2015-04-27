package org.btssio.edf_florian;

import java.util.List;
import classes.Client;
import dao.BdAdapter;
import dao.ClientAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class AfficheListeClient extends Activity implements OnItemClickListener {
	private ListView listView;
	private List<Client> listeClient;
	private BdAdapter bdd;
	private Intent theIntent;
	
	/**
	 * Initialise l'activité
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_affiche_liste_client);
		//On Initialise la bdd
		Log.d("Étape", "~ Initialisation de la base de données");
		bdd = new BdAdapter(this);
		//On ouvre la bdd
		Log.d("Étape", "~ Ouverture de la bdd");
		bdd.open();
		//On supprime tous les tuples de la table pour supprimer les tuples insérrés lors de tests
		//bdd.viderLaTable();
		//On insère des clients dans la base
		//bdd.insererDesClients();
		//On récupère les clients de la base de données
		Log.d("Étape", "~ Récupération du contenu de la table Client");
		listeClient = bdd.getListeDesClients();
		
		Log.d("Étape", "~ Fermeture de la base");
		bdd.close();
		
		Log.d("Étape", "~ Attribution de la vue à listView à partir de R.id.lvListe");
		listView = (ListView)findViewById(R.id.lvListe);
		
		Log.d("Étape", "~ Génération du ClientAdapter");
		ClientAdapter clientAdapter = new ClientAdapter(this, listeClient);
		
		listView.setOnItemClickListener(this) ;
		listView.setAdapter(clientAdapter);
	}//fin onCreate

	/**
	 * Initialise le menu de l'activité
	 * @param Le menu permettant d'initialiser celui de l'activité [Menu]
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.affiche_liste_client, menu);
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
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}//fin onOptionsItemSelected
	
	/**
	 * Vérifie que l'activité "ModificationClient" a bien retourné des valeurs, si c'est la cas, la fonction va mettre à jour la liste des clients de l'activité
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//On rafraichit la liste des clients
		Log.d("Étape", "~ L'activité \"ModificationClient\" a bien retourné un résultat");

		//On vide la liste de clients
		for(int i = 0 ; i < listeClient.size() ; i++)
		{
			listeClient.remove(i);
		}//fin for

		//On récupère tous les clients à partir de la bdd
		bdd = new BdAdapter(this);
		bdd.open();
		listeClient = bdd.getListeDesClients();
		bdd.close();

		//On met à jour la liste des clients
		ClientAdapter clientAdapter = new ClientAdapter(this, listeClient);
		listView.setOnItemClickListener(this) ;
		listView.setAdapter(clientAdapter);
	}//fin onActivityResult
	
	/**
	 * Gère le clic sur les clients
	 * @param AdapterView<?> a
	 * @param L'élément sur lequel l'utilisateur a cliqué [View]
	 * @param L'index de l'élément cliqué dans la liste [int]
	 * @param long id
	 */
	@Override
	public void onItemClick(AdapterView<?> a, View v, int position, long id)
	{
		Log.d("Étape", "~ Clic sur le " + position + "° item de la ListView");
		theIntent = new Intent(this, ModificationClient.class);
		
		//On va passer des paramètres à l'activité que l'on va lancer
		theIntent.putExtra("identifiant",			listeClient.get(position).getIdentifiant());
		theIntent.putExtra("nom",					listeClient.get(position).getNom());
		theIntent.putExtra("prenom",				listeClient.get(position).getPrenom());
		theIntent.putExtra("adresse",				listeClient.get(position).getAdresse());
		theIntent.putExtra("codePostal",			listeClient.get(position).getCodePostal());
		theIntent.putExtra("ville",					listeClient.get(position).getVille());
		theIntent.putExtra("telephone",				listeClient.get(position).getTelephone());
		theIntent.putExtra("idCompteur",			listeClient.get(position).getIdCompteur());
		theIntent.putExtra("dateAncienReleve",		listeClient.get(position).getDateAncienReleve());
		theIntent.putExtra("ancienReleve",			listeClient.get(position).getAncienReleve());
		theIntent.putExtra("dateDernnierReleve", 	listeClient.get(position).getDateDernierReleve());
		theIntent.putExtra("signatureBase64", 		listeClient.get(position).getSignatureBase64());
		theIntent.putExtra("dernierReleve", 		listeClient.get(position).getDernierReleve());
		theIntent.putExtra("situation", 			listeClient.get(position).getSituation());
		
		//On retourne la position du client dans la liste pour le retour de l'activité
		theIntent.putExtra("indexClient", 			position);
		//Lancement de l'activité
		this.startActivityForResult(theIntent,0);
	}//fin onItemClick
}//fin classe