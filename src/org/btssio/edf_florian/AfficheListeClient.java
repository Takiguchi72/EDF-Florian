package org.btssio.edf_florian;

import java.util.List;

import classes.Client;
import dao.BdAdapter;
import dao.ClientAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class AfficheListeClient extends Activity {
	private ListView listView;
	private List<Client> listeClient;
	private BdAdapter bdAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_affiche_liste_client);
		Log.d("Étape", "### - New BdAdapter");
		bdAdapter = new BdAdapter(this);
		bdAdapter.open();
		//Insertion de 3 clients
		Log.d("Étape", "### - Inserion de clients");
		bdAdapter.insererClient(new Client( "cli10",
											"LEROUX", 
											"Brandon", 
											"5 place Georges Bouttié", 
											"72000", 
											"Le Mans", 
											"0123456789", 
											"1", 
											"01/02/03", 
											12.1));
		
		Log.d("Étape", "### - Récupération de la table Clients");
		listeClient = bdAdapter.getListeDesClients();
		
		listView = (ListView)findViewById(R.id.lvListe);
		ClientAdapter clientAdapter = new ClientAdapter(this, listeClient);
		listView.setAdapter(clientAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.affiche_liste_client, menu);
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
