package dao;

public class CreateBdClient {
	private static final String TABLE_CLIENTS = "table_clients";
	static final String COL_ID = "_id";
	private static final String COL_NOM = "NOM";
	private static final String COL_PRENOM = "PRENOM";
	private static final String COL_ADRESSE = "ADRESSE";
	private static final String COL_CP = "CP";
	private static final String COL_VILLE = "VILLE";
	private static final String COL_TEL = "TEL";
	private static final String COL_IDCOMPTEUR = "IDCOMPTEUR";
	private static final String COL_DATEANCIENRELEVE = "DATEANCIENRELEVE";
	private static final String COL_ANCIENRELEVE = "ANCIENRELEVE";
	private static final String COL_DATEDERNIERRELEVE = "DATEDERNIERRELEVE";
	private static final String COL_SIGNATUREBASE64 = "SIGNATUREBASE64";
	private static final String COL_DERNIERRELEVE = "DERNIERRELEVE";
	private static final String COL_SITUATION = "SITUATION";
	private static final String CREATE_BDD =
			"CREATE TABLE " + TABLE_CLIENTS + " ("
				+ COL_ID 				+ " TEXT PRIMARY KEY,"
				+ COL_NOM 				+ " TEXT,"
				+ COL_PRENOM			+ " TEXT,"
				+ COL_ADRESSE			+ " TEXT,"
				+ COL_CP 				+ " TEXT,"
				+ COL_VILLE				+ " TEXT,"
				+ COL_TEL 				+ " TEXT,"
				+ COL_IDCOMPTEUR		+ " TEXT,"
				+ COL_DATEANCIENRELEVE	+ " TEXT,"
				+ COL_ANCIENRELEVE		+ " REAL,"
				+ COL_DATEDERNIERRELEVE + " TEXT,"
				+ COL_SIGNATUREBASE64 	+ " TEXT,"
				+ COL_DERNIERRELEVE		+ " REAL,"
				+ COL_SITUATION 		+ " INTEGER,"
			+");";
				
				
}
