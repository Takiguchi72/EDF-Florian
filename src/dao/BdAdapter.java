package dao;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import classes.Client;

public class BdAdapter {
	/* *********************
	 * 	A T T R I B U T S  *
	 ***********************/
	static final int VERSION_BDD = 1;
	private static final String NOM_BDD = "edf.florian.db";
	static final String TABLE_CLIENTS = "table_clients";
	static final String COL_ID = "_id";
	static final int NUM_COL_ID = 0;
	static final String COL_NOM = "NOM";
	static final int NUM_COL_NOM = 1;
	static final String COL_PRENOM = "PRENOM";
	static final int NUM_COL_PRENOM = 2;
	static final String COL_ADRESSE = "ADRESSE";
	static final int NUM_COL_ADRESSE = 3;
	static final String COL_CP = "CP";
	static final int NUM_COL_CP = 4;
	static final String COL_VILLE = "VILLE";
	static final int NUM_COL_VILLE = 5;
	static final String COL_TEL = "TEL";
	static final int NUM_COL_TEL = 6;
	static final String COL_IDCOMPTEUR = "IDCOMPTEUR";
	static final int NUM_COL_IDCOMPTEUR = 7;
	static final String COL_DATEANCIENRELEVE = "DATEANCIENRELEVE";
	static final int NUM_COL_DATEANCIENRELEVE = 8;
	static final String COL_ANCIENRELEVE = "ANCIENRELEVE";
	static final int NUM_COL_ANCIENRELEVE = 9;
	static final String COL_DATEDERNIERRELEVE = "DATEDERNIERRELEVE";
	static final int NUM_COL_DATEDERNIERRELEVE = 10;
	static final String COL_SIGNATUREBASE64 = "SIGNATUREBASE64";
	static final int NUM_COL_SIGNATUREBASE64 = 11;
	static final String COL_DERNIERRELEVE = "DERNIERRELEVE";
	static final int NUM_COL_DERNIERRELEVE = 12;
	static final String COL_SITUATION = "SITUATION";
	static final int NUM_COL_SITUATION = 13;
	
	private CreateBdClient bdClients;
	private Context context;
	private SQLiteDatabase db;
	
	/* *****************************
	 * 	C O N S T R U C T E U R S  *
	 *******************************/
	/**
	 * Constructeur
	 * @param context [Context]
	 */
	public BdAdapter(Context context)
	{
		this.context = context;
		bdClients = new CreateBdClient(context, NOM_BDD, null, VERSION_BDD);
	}//fin BdAdapter
	
	/**
	 * Si la base n'existe pas, l'objet SQLiteOpenHelper exécute la méthode "onCreate()".
	 * Si la version de la base a changé, la méthode "onUpgrade" sera lancée.
	 * Dans les deux cas l'appel à "getWritableDatabase()" ou "getReadableDatabase"
	 * renverra la base de données en cache, nouvellement ouverte, nouvellement créee ou mise à jour.
	 * @return la base [BdAdapter]
	 */
	public BdAdapter open()
	{
		db = bdClients.getWritableDatabase();
		return this;
	}//fin BdAdapter open()
	
	/**
	 * Ferme la base de donnée et retourne la valeur "null"
	 * @return null
	 */
	public BdAdapter close()
	{
		db.close();
		return null;
	}//fin BdAdapter close()
	
	/**
	 * Insert un nouveau client dans la base de données
	 * @param unClient [Client]
	 * @return Le code retour de la fonction "db.insert(...)" [long]
	 */
	public long insererClient(Client unClient)
	{
		//Création d'un ContentValues (fonctionne comme une HashMap)
		ContentValues values = new ContentValues();
		//On lui ajoute une valeur associé à une clé (pour chaque nom de colonne où on veut mettre la valeur)
		values.put(COL_ID,					unClient.getIdentifiant());
		values.put(COL_NOM, 				unClient.getNom());
		values.put(COL_PRENOM, 				unClient.getPrenom());
		values.put(COL_ADRESSE, 			unClient.getAdresse());
		values.put(COL_CP, 					unClient.getCodePostal());
		values.put(COL_VILLE, 				unClient.getVille());
		values.put(COL_TEL, 				unClient.getTelephone());
		values.put(COL_IDCOMPTEUR, 			unClient.getIdCompteur());
		values.put(COL_DATEANCIENRELEVE,	unClient.getDateAncienReleve());
		values.put(COL_ANCIENRELEVE, 		unClient.getAncienReleve());
		values.put(COL_DATEDERNIERRELEVE,	unClient.getDateDernierReleve());
		values.put(COL_SIGNATUREBASE64, 	unClient.getSignatureBase64());
		values.put(COL_DERNIERRELEVE, 		unClient.getDernierReleve());
		values.put(COL_SITUATION, 			unClient.getSituation());
		//exécution de l'insertion dans la base
		return db.insert(TABLE_CLIENTS, null, values);
	}//fin insererClient
	
	/**
	 * Convertit un "Cursor" en un Client
	 * @param c
	 * @return
	 */
	private Client cursorToClient(Cursor c)
	{
		//Si aucun élément n'a été retourné dans la requête, on renvoie null
		if(c.getCount() == 0)
			return null;
		//Sinon
		//On se place sur le premier élément
		c.moveToFirst();
		//On créer un client en lui addectant toutes les infos grâce aux infos contenues dans le Cursor
		Client unClient = new Client(c.getString(NUM_COL_ID),
										c.getString(NUM_COL_NOM),
										c.getString(NUM_COL_PRENOM),
										c.getString(NUM_COL_ADRESSE),
										c.getString(NUM_COL_CP),
										c.getString(NUM_COL_VILLE),
										c.getString(NUM_COL_TEL),
										c.getString(NUM_COL_IDCOMPTEUR),
										c.getString(NUM_COL_DATEANCIENRELEVE),
										c.getDouble(NUM_COL_ANCIENRELEVE),
										c.getString(NUM_COL_SIGNATUREBASE64));
		//On ferme le cursor
		c.close();
		//On retourne le client
		return unClient;
	}//fin cursorToClient
	
	
	public Client getClientWithIdentifiant(String identifiant)
	{
		//Récupère dans un Cursor les valeurs correspondant à un article
		//grâce à sa désignation
		Cursor c = db.query(TABLE_CLIENTS,
							new String[] {	COL_ID,
											COL_NOM,
											COL_PRENOM,
											COL_ADRESSE,
											COL_CP,
											COL_VILLE,
											COL_TEL,
											COL_IDCOMPTEUR,
											COL_DATEANCIENRELEVE,
											COL_ANCIENRELEVE,
											COL_DATEDERNIERRELEVE,
											COL_SIGNATUREBASE64,
											COL_DERNIERRELEVE,
											COL_SITUATION},
							COL_ID + " LIKE \"" + identifiant + "\"",
							null, null, null, null);
		return cursorToClient(c);
	}//fin getClientWithIdentifiant
	
	/**
	 * Met à jour un client à partir d'un objet de classe Client
	 * @param L'identifiant du client à mettre à jour [String]
	 * @param L'objet "Client" où sont les valeurs à mettre à jour [Client]
	 * @return Le code retour de la fonction "db.update(...)" [int]
	 */
	public int updateClient(String identifiant, Client unClient)
	{
		ContentValues values = new ContentValues();
		//values.put(COL_ID, 					unClient.getIdentifiant());
		values.put(COL_NOM, 				unClient.getNom());
		values.put(COL_PRENOM, 				unClient.getPrenom());
		values.put(COL_ADRESSE, 			unClient.getAdresse());
		values.put(COL_CP, 					unClient.getCodePostal());
		values.put(COL_VILLE, 				unClient.getVille());
		values.put(COL_TEL, 				unClient.getTelephone());
		values.put(COL_IDCOMPTEUR, 			unClient.getIdCompteur());
		values.put(COL_DATEANCIENRELEVE,	unClient.getDateAncienReleve());
		values.put(COL_ANCIENRELEVE, 		unClient.getAncienReleve());
		values.put(COL_DATEDERNIERRELEVE,	unClient.getDateDernierReleve());
		values.put(COL_SIGNATUREBASE64, 	unClient.getSignatureBase64());
		values.put(COL_DERNIERRELEVE, 		unClient.getDernierReleve());
		values.put(COL_SITUATION, 			unClient.getSituation());
		
		return db.update(TABLE_CLIENTS, values, COL_ID + "=\"" + identifiant + "\"", null);
	}//fin updateClient
	
	/**
	 * Supprime un Client dans la base de données
	 * @param L'identifiant du client à supprimer [String]
	 * @return Le code retour de la fonction "db.delete(...)" [int]
	 */
	public int removeClientWithId(String identifiant)
	{
		//Suppression d'un article de la BDD grâce à son identifiant
		return db.delete(TABLE_CLIENTS, COL_ID + "=\"" + identifiant + "\"", null);
	}//fin removeClientWithId
	
	/**
	 * Retourne une liste d'objets de la classe Client, correspondant au contenu de la table "Clients"
	 * @return Le contenu de la table "Clients" [List<Client>]
	 */
	public List<Client> getListeDesClients()
	{
		//On créer une liste (de Clients) vide
		List<Client> listeDesClients = new ArrayList<Client>();
		//On récupère tous les Clients de la table Clients
		Cursor c = db.rawQuery("SELECT * FROM " + TABLE_CLIENTS, null);
		//Si la requête n'a rien retourné, on retourne la liste qui est actuellement vide
		if(c.getCount() == 0)
			return listeDesClients;
		//Sinon, on sélectionne la première ligne du resultat retourné par SQLite
		c.moveToFirst();
		//On va ajouter un Client à la liste pour chaque tuple du résultat
		do {
		  	listeDesClients.add(new Client( c.getString(NUM_COL_ID),
											c.getString(NUM_COL_NOM),
											c.getString(NUM_COL_PRENOM),
											c.getString(NUM_COL_ADRESSE),
											c.getString(NUM_COL_CP),
											c.getString(NUM_COL_VILLE),
											c.getString(NUM_COL_TEL),
											c.getString(NUM_COL_IDCOMPTEUR),
											c.getString(NUM_COL_DATEANCIENRELEVE),
											c.getDouble(NUM_COL_ANCIENRELEVE),
											c.getString(NUM_COL_SIGNATUREBASE64)));
		} while (c.moveToNext());
		//On ferme le curseur pour éviter les soucis
		c.close();
		return listeDesClients;
	}//fin getData
	
	/**
	 * Supprime tous les tuples de la table Clients
	 * 
	 * À utiliser UNIQUEMENT pour nettoyer la base des insertions inutiles (lors de tests par exemple)
	 */
	public void viderLaTable()
	{
		Log.d("Étape", "~ Suppression des données de la table Clients");
		db.delete(TABLE_CLIENTS, null, null);
	}//fin viderLaTable
	
	/**
	 * Insère des clients dans la base
	 */
	public void insererDesClients()
	{
		//Insertion de clients
		//(Les données ont été récupérées sur internet, sur un site générant des identités aléatoires)
		// => http://fr.fakenamegenerator.com/
		Log.d("Étape", "~ Insertion des clients dans la base");
		Log.d("Étape", "~ 		1er client");
		insererClient(new Client(	"cli1", 			"FOUCAULT", 
									"Royce",  			"49 rue Isambard", 
									"97234",  			"FORT-DE-FRANCE", 
									"0123456789", 		"19950055123", 
									"01/02/14",  		3461.35));
		Log.d("Étape", "~ 		2eme client");
		insererClient(new Client( 	"cli2",				"ÉTOILE", 
									"Nouel",  			"45 rue Marie De Médicis", 
									"59400",  			"CAMBRAI", 
									"0123456798",  		"19950055124", 
									"30/04/14",  		2147.84));
		Log.d("Étape", "~ 		3eme client");
		insererClient(new Client( 	"cli3", 			"SAUVÉ", 
									"Clarimunda",  		"23 Avenue Marhum", 
									"64100",  			"BAYONNE", 
									"0123456789",  		"19950055130", 
									"21/07/14",  		1264.92));
		Log.d("Étape", "~ 		4eme client");
		insererClient(new Client( 	"cli4", 			"COLLIN", 
									"Stéphanie",  		"La Plaine Saint-Denis", 
									"93210",  			"SAINT-DENIS", 
									"0126448784",  		"19950055125", 
									"16/08/14",  		2463.5));
		Log.d("Étape", "~ 		5eme client");
		insererClient(new Client( 	"cli5", 			"BOILEAU", 
									"Olivier",  		"13 Avenue François Cevert", 
									"72700",  			"ALLONNES", 
									"0174821550",  		"19950055129", 
									"13/07/14",  		1801.43));
		Log.d("Étape", "~ 		6eme client");
		insererClient(new Client( 	"cli6", 			"MENARD", 
									"Émmelyne",  		"7 rue Goya", 
									"72000",  			"LE MANS", 
									"0147614572",  		"19950055126", 
									"07/10/14",  		2973.81));
		Log.d("Étape", "~ 		7eme client");
		insererClient(new Client( 	"cli7", 			"NEUFVILLE", 
									"Gabriel",  		"3 Avenue de Verdun", 
									"20090",  			"AJACCIO", 
									"0163458701",  		"19950055127", 
									"20/07/14",  		945.6));
		Log.d("Étape", "~ 		8eme client");
		insererClient(new Client( 	"cli8", 			"DESJARDINS", 
									"Aurélie",  		"7 rue Polignais", 
									"42000",  			"SAINT-ÉTIENNE", 
									"0142087569",  		"19950055128", 
									"06/02/14",  		1851.47));
		Log.d("Étape", "~ Insertion des clients terminée !");
	}//fin insererLes5Clients
}//fin classe