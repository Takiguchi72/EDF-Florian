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
import android.widget.Toast;

public class AfficheListeClient extends Activity implements OnItemClickListener {
	private ListView listView;
	private List<Client> listeClient;
	private BdAdapter bdd;
	private Intent theIntent;
	private class ViewHolder {
		//TODO 01 - Générer un view holder qui correspond la la liste des widgets de l'activité, (cf ClientAdapter)
	}//fin ViewHolder
	
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
		
		listView = (ListView)findViewById(R.id.lvListe);
		ClientAdapter clientAdapter = new ClientAdapter(this, listeClient);
		listView.setOnItemClickListener(this) ;
		listView.setAdapter(clientAdapter);
	}//fin onCreate

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.affiche_liste_client, menu);
		return true;
	}//fin onCreateOptionsMenu

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
	public void onItemClick(AdapterView<?> a, View v, int position, long id)
	{
		Log.d("Étape", "~ Clic sur le " + position + "° item de la ListView");
		Toast.makeText(getApplicationContext(),"Choix : " + listeClient.get(position).getIdentifiant(), Toast.LENGTH_SHORT).show();
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
		this.startActivityForResult(theIntent,0);
	}//fin onItemClick
	
	public void initialiserActivite()
	{
		//TODO 02 - Créer un ViewHolder, s'inspirer de la classe ClientAdapter, 
		//			pour pouvoir remplir les widgets de l'activité avec leClient
	}//fin initialiserActivite
}//fin classe