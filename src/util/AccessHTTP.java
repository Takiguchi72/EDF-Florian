package util;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import android.os.AsyncTask;
import android.util.Log;

public abstract class AccessHTTP extends AsyncTask<String, Integer, Long>{
	protected String ret = "";
	private ArrayList<NameValuePair> parametres;
	
	/**
	 * Initialise l'objet
	 */
	public AccessHTTP() {
		parametres = new ArrayList<NameValuePair>();
	}//fin constructeur
	
	/**
	 * Ajoute des paramètres de connexion
	 * @param Le nom du paramètre [String]
	 * @param La valeur du paramètre [String]
	 */
	public void addParam(String nom, String valeur)
	{
		parametres.add(new BasicNameValuePair(nom, valeur));
	}//fin addParam
	
	/**
	 * Établit la connexion au webService correspondant au paramètre
	 */
	@Override
	protected Long doInBackground(String... params) {
		//On initialise une connexion
		Log.d("Étape", "~ Initialisation de la connexion au webService");
		HttpClient connexionHttp = new DefaultHttpClient();
		
		//On initialise les paramètres de connexion
		Log.d("Étape", "~ Ajout de paramètres à la connexion");
		HttpPost parametresConnexion = new HttpPost(params[0]);
		
		try {
			//On renseigne un paramètre de connexion
			parametresConnexion.setEntity(new UrlEncodedFormEntity(parametres));
			
			//On exécute la connexion et on récupère la réponse
			Log.d("Étape", "~ Récupération des données");
			HttpResponse reponse = connexionHttp.execute(parametresConnexion);
			Log.d("Étape", "~ Les données ont été récupérées !");
			
			//On transforme la réponse en chaîne de caractères
			Log.d("Étape", "~ Transformation des valeurs récupérées en une chaîne");
			ret = EntityUtils.toString(reponse.getEntity());
			Log.d("Étape", "~ Transformation réussie !");
		} catch (ClientProtocolException e) {
			Log.d("Catch", "Erreur ClientProtocolException : " + e.getMessage());
		} catch (IOException e) {
			Log.d("Catch", "Erreur IOException : " + e.getMessage());
		}//fin catch
		return null;
	}//fin doInBackground
}//fin classe