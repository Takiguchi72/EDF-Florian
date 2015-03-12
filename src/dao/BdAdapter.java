package dao;

import classes.Client;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

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
		values.put(COL_ID, 					unClient.getIdentifiant());
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
	 * Converti un "Cursor" en un Client
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
										c.getDouble(NUM_COL_ANCIENRELEVE));
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
											COL_ANCIENRELEVE},
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
		values.put(COL_ID, 					unClient.getIdentifiant());
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
	 * Retourne un objet de type "Cursor" correspondant au contenu de la table "Clients"
	 * @return Le contenu de la table "Clients" [Cursor]
	 */
	public Cursor getData()
	{
		return db.rawQuery("SELECT * FROM " + TABLE_CLIENTS, null);
	}//fin getData
	
	
	
	
	
	
	
}//fin classe