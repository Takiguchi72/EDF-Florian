package classes;

public class Client {
	/* *********************
	 * 	A T T R I B U T S  *
	 ***********************/
	//Données ne pouvant être modifiées
	private String 	identifiant, 
					nom, 
					prenom, 
					adresse, 
					codePostal, 
					ville, 
					telephone, 
					idCompteur, 
					dateAncienReleve;
	private Double 	ancienReleve;
	//Données pouvant être modifiées
	private String 	dateDernierReleve, 
					signatureBase64;
	private Double 	dernierReleve;
	private int 	situation;
	
	/* *********************************
	 * 	G E T T E R S / S E T T E R S  *
	 ***********************************/
	public String getIdentifiant() {
		return identifiant;
	}
	public void setIdentifiant(String identifiant) {
		this.identifiant = identifiant;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getAdresse() {
		return adresse;
	}
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	public String getCodePostal() {
		return codePostal;
	}
	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}
	public String getVille() {
		return ville;
	}
	public void setVille(String ville) {
		this.ville = ville;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getIdCompteur() {
		return idCompteur;
	}
	public void setIdCompteur(String idCompteur) {
		this.idCompteur = idCompteur;
	}
	public String getDateAncienReleve() {
		return dateAncienReleve;
	}
	public void setDateAncienReleve(String dateAncienReleve) {
		this.dateAncienReleve = dateAncienReleve;
	}
	public String getDateDernierReleve() {
		return dateDernierReleve;
	}
	public void setDateDernierReleve(String dateDernierReleve) {
		this.dateDernierReleve = dateDernierReleve;
	}
	public String getSignatureBase64() {
		return signatureBase64;
	}
	public void setSignatureBase64(String signatureBase64) {
		this.signatureBase64 = signatureBase64;
	}
	public Double getAncienReleve() {
		return ancienReleve;
	}
	public void setAncienReleve(Double ancienReleve) {
		this.ancienReleve = ancienReleve;
	}
	public Double getDernierReleve() {
		return dernierReleve;
	}
	public void setDernierReleve(Double dernierReleve) {
		this.dernierReleve = dernierReleve;
	}
	public int getSituation() {
		return situation;
	}
	public void setSituation(int situation) {
		this.situation = situation;
	}
	
	/* *****************************
	 * 	C O N S T R U C T E U R S  *
	 *******************************/
	/**
	 * Constructeur par défaut
	 */
	public Client()
	{
		//Affectation des Strings
		identifiant 		= 
		nom 				=
		prenom 				= 
		adresse				= 
		codePostal			=
		ville 				=
		telephone			=
		idCompteur			=
		dateAncienReleve	=
		dateDernierReleve	=
		signatureBase64		= "";
		//Affectation des Doubles
		ancienReleve		= 
		dernierReleve		= 0.0;
		//Affectation des Integers
		situation			= 0;
	}//fin Client
	
	/**
	 * Constructeur où il faut saisir les attributs non modifiables. Les attributs modifiables seront initialisés à { 0 ; 0.0 ; "" }.
	 * @param identifiant [String]
	 * @param nom [String]
	 * @param prenom [String]
	 * @param adresse [String]
	 * @param codePostal [String]
	 * @param ville [String]
	 * @param telephone [String]
	 * @param idCompteur [String]
	 * @param dateAncienReleve [String]
	 * @param ancienReleve [Double]
	 */
	public Client(String identifiant, String nom, String prenom,
			String adresse, String codePostal, String ville, String telephone,
			String idCompteur, String dateAncienReleve, Double ancienReleve) 
	{
		this.identifiant = identifiant;
		this.nom = nom;
		this.prenom = prenom;
		this.adresse = adresse;
		this.codePostal = codePostal;
		this.ville = ville;
		this.telephone = telephone;
		this.idCompteur = idCompteur;
		this.dateAncienReleve = dateAncienReleve;
		this.ancienReleve = ancienReleve;
		dernierReleve = 0.0;
		dateDernierReleve = getDateDuJour();
		signatureBase64="";
		situation = 0;
	}//fin Client(...)
	
	/* *******************
	 *  M E T H O D E S  *
	 *********************/
	/**
	 * Retourne la date du jour
	 * @return Date au format "dd/MM/yy" [String]
	 */
	private String getDateDuJour()
	{
		//Récupération de la date du jour
		java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat( "dd/MM/yy" );	//Récupération de la date au format "dd/MM/yy"
		java.util.Date date = new java.util.Date();
		return formater.format(date);
	}//fin getDateDuJour
	
}//fin classe