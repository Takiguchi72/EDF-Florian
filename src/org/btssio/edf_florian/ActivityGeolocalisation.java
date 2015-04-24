package org.btssio.edf_florian;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import android.app.Activity;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import classes.Client;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import dao.BdAdapter;

public class ActivityGeolocalisation extends Activity implements LocationListener {
	private GoogleMap googleMap;
	private LocationManager locationManager;
	private String provider, adresseClient;
	private LatLng positionClient, positionAgent;
	private boolean reussiGeolocalisationAgent = false,
					reussiGeolocalisationClient = false;
	private LatLngBounds.Builder builder = new LatLngBounds.Builder();
	
	/**
	 * Initialise l'activité
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_geolocalisation);
		
		//Récupération de l'adresse du client
		BdAdapter bdd = new BdAdapter(this);
		Client leClient = bdd.open().getClientWithIdentifiant(this.getIntent().getExtras().getString("identifiant"));
		bdd.close();
		adresseClient = leClient.getAdresse() + "," + leClient.getCodePostal() + " " + leClient.getVille() + " France";

		//On va récupérer la position de l'utilisateur via la géolocalisation
		recupPositionAgent();
		
		//On récupère la position du client et on va
		recupPositionClient();
		
		//On va afficher la map avec la position de l'agent et le pin correspondant à l'adresse du client
		afficheCarte();
	}//fin onCreate

	/**
	 * Initialise le menu de l'activité
	 * @param Le menu permettant d'initialiser celui de l'activité [Menu]
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_geolocalisation, menu);
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
	 * Récupère la position géolocalisée de l'utilisateur via le système de géolocalisation et met à jour "reussiGeolocalisationAgent"
	 */
	public void recupPositionAgent()
	{
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		/*
		* Vous pouvez modifiez les critères exemple
		* criteria.setPowerRequirement(Criteria.POWER_LOW);
		* criteria.setAccuracy(Criteria.ACCURACY_FINE);
		* criteria.setCostAllowed(true);
		* criteria.setSpeedRequired(false);
		* Puis trouver le meilleur provider en fonction des critères
		*/
		
		//On récupère le meilleur fournisseur de géolocalisation
		provider = locationManager.getBestProvider(criteria, false);
		if (provider == null || provider.equals(""))
		{
			Log.d("Étape", "~ Echec de l'utilisation du service de géolocalisation (Est-il activé ?)");
			Toast.makeText(this, "Veuillez activer le service de géolocalisation svp !",Toast.LENGTH_LONG).show();
		}//fin if
	}//fin recupPositionAgent
	
	/**
	 * Détermine la position du client à partir de son adresse et met à jour "reussiGeolocalisationClient"
	 */
	public void recupPositionClient()
	{
		//Récupération de la position de l'agent à partir du service de géolocalisation
		locationManager.requestLocationUpdates(provider, 20000, 0, this);
		Location location = locationManager.getLastKnownLocation(provider);
		if (location != null)
		{
			positionAgent = new LatLng(location.getLatitude(),location.getLongitude());
			reussiGeolocalisationAgent = true;
		}//fin if
		else
		{
			Log.d("Étape", "~ Impossible de récupérer la position de l'agent !");
			Toast.makeText(this, "Impossible de récupérer votre position !",Toast.LENGTH_LONG).show();
		}//fin else
		
		//On génère la localisation du client à partir de son adresse
		Geocoder fwdGeocoder = new Geocoder(this, Locale.FRANCE);
		List<Address> locations = null;
		try {
			locations = fwdGeocoder.getFromLocationName(adresseClient, 10);
		} catch (IOException e) {
			Log.d("Étape", "~ Impossible de générer la localisation du Client !");
			Toast.makeText(this, "Impossible de générer la localisation du Client !",Toast.LENGTH_LONG).show();
		}//fin catch
		
		//Si on a pas réussi à récupérer la localisation du client, on affiche qu'il est inconnu sur la carte
		if ((locations == null) || (locations.isEmpty()))
		{
			//"Adresse client inconnu !"
			Log.d("Étape", "~ L'adresse du client est inconnue !");
			Toast.makeText(this, "L'adresse du client est inconnue !",Toast.LENGTH_LONG).show();
		}//fin if
		//Sinon, on va enregistrer sa localisation
		else
		{
			Log.d("Étape", "~ Localisation du client !");
			positionClient = new LatLng(locations.get(0).getLatitude(),locations.get(0).getLongitude());
			reussiGeolocalisationClient = true;
		}//fin else
	}//fin recupPositionClient
	
	/**
	 * Affiche la carte en positionnant les marqueurs de l'utilisateur et du client
	 */
	public void afficheCarte()
	{
		//On récupère la carte
		googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		
		//On y affiche le marqueur du client
		if (reussiGeolocalisationClient)
		{
			googleMap.addMarker(new MarkerOptions().position(positionClient).title("Client").snippet("Point de rendez vous prochain client")
									.icon(BitmapDescriptorFactory.fromResource(R.drawable.grnpushpin)));
			builder.include(positionClient);
		}//fin if
		
		//On y affiche le marqueur de l'utilisateur
		if (reussiGeolocalisationAgent) {
			googleMap.addMarker(new MarkerOptions().position(positionAgent).title("Ma position"));
			builder.include(positionAgent);
		}//fin if
		
		//On zoom sur la position des deux clients
		if (reussiGeolocalisationClient && reussiGeolocalisationAgent) {
			googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),
																	 this.getResources().getDisplayMetrics().widthPixels,
																	 this.getResources().getDisplayMetrics().heightPixels
																	 ,210));
		}//fin if
	}//fin afficherCarte

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
	}
}//fin classe