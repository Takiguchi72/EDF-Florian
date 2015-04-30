package org.btssio.edf_florian;

import java.util.ArrayList;
import java.util.List;

import classes.Client;
import dao.BdAdapter;
import dao.ClientAdapter;
import daoPostgreSQL.ClientDAO;
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
	
	public ListView getListView() {
		return listView;
	}

	public void setListView(ListView listView) {
		this.listView = listView;
	}

	public List<Client> getListeClient() {
		return listeClient;
	}

	public void setListeClient(List<Client> listeClient) {
		this.listeClient = listeClient;
	}

	/**
	 * Initialise l'activité
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_affiche_liste_client);
		
		//On initialise l'attribut listeClient car on va y ajouter après des éléments
		listeClient = new ArrayList<Client>();
		
		//On va récupérer les clients à partir de la bdd distante, et initialiser les composants graphiques
		initialiserListeClients();
	}//fin onCreate
	
	/**
	 * Récupère les Clients depuis la base de données distante en faisant appel au webService mit en place, et initialise les composants graphiques
	 */
	public void initialiserListeClients()
	{
		ClientDAO clientAccess = new ClientDAO() {
			/**
			 * Initialise la liste des clients à partir de la bdd après avoir reçu les données
			 */
			@Override
			public void onTacheTerminee(ArrayList<Client> resultat) {
				Log.d("Étape", "~ Parcours de la liste récupérée");
				//Pour chaque client retourné, on initialise la liste de clients
				for(int i = 0 ; i < resultat.size() ; i++)
				{
					Log.d("Test", "# Client : " + resultat.get(i).toString());
					listeClient.add(resultat.get(i));
				}//fin for
				
				Log.d("Étape", "~ Initialisation du ClientAdapter");
				ClientAdapter clientAdapter = new ClientAdapter(AfficheListeClient.this, listeClient);
				
				Log.d("Étape", "~ Initialisation de la listView");
				listView = (ListView)findViewById(R.id.lvListe);
				listView.setOnItemClickListener(AfficheListeClient.this);
				
				Log.d("Étape", "~ Ajout du ClientAdapter à la listView");
				listView.setAdapter(clientAdapter);
			}//fin onTacheTerminee
			
			@Override
			public void onTacheTerminee(Client resultat) {
			}
			
			@Override
			public void onTacheTerminee(String resultat) {
			}
		};
		
		clientAccess.getClients();	//Récupère les clients et initialise les attributs => Ligne 68
	}//fin initialiserListeClients
	
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
		theIntent.putExtra("indexClient", position);
		//Lancement de l'activité
		this.startActivityForResult(theIntent,0);
	}//fin onItemClick
}//fin classe