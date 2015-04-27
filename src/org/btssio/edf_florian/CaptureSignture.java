package org.btssio.edf_florian;

import java.io.ByteArrayOutputStream;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import classes.Client;
import dao.BdAdapter;

public class CaptureSignture extends Activity implements OnClickListener{
	/* **********************************
	 * A T T R I B U T S
	 * ******************************* */
	private Client leClient;
	private LinearLayout linearLayout;
	private Button btnAnnuler;
	private Button btnEffacer;
	private Button btnSauvegarder;
	private Signature signature;
	
	public class Signature extends View {
		/* ********* *
		 * Attributs *
		 * ********* */
		private Paint paint = new Paint();
		private Path path = new Path();// collection de l'ensemble des points sauvegardés lors des mouvements du doigt
		private Canvas canvas;
		private Bitmap bitmap;
		private String lig1, lig2;
		
		
		/* ************ *
		 * Constructeur *
		 * ************ */
		/**
		 * Constructeur par défaut
		 * @param context
		 * @param attrs
		 * @param Le récapitulatif du client. Ex: "Client : cli1 Nom Prénom" [String]
		 * @param Le récapitulatif du relevé du client. Ex: "Compteur n°0123456789 - Relevé : 1234.56 - Date : 01/02/03" [String]
		 */
		public Signature(Context context, AttributeSet attrs, String lig1, String lig2) {
			super(context, attrs);
			this.setBackgroundColor(Color.WHITE);
			paint.setAntiAlias(true); // empêche le scintillement gourmand en cpu et mémoire 
			paint.setColor(Color.BLACK);
			paint.setStrokeWidth(5f); //taille de la grosseur du trait en pixel
			paint.setTextSize(20);// taille du texte pur afficher les lignes
			this.lig1 = lig1;
			this.lig2 = lig2;
		}//fin constructeur

		/* ******** *
		 * Méthodes *
		 * ******** */
		/**
		 * Affiche le dessin en fonction du tracé de l'utilisateur
		 * @param Le canvas sur lequel l'utilisateur dessine [Canvas]
		 */
		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			
			//On affiche le dessin en fonction du tracé de l'utilisateur
			paint.setStyle(Paint.Style.FILL);
			canvas.drawText(lig1, 20, 30, paint);
			canvas.drawText(lig2, 20, 60, paint);
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawPath(path, paint);
		}//fin onDraw
		
		/**
		 * Gère les évènements générés par le doigt sur la zone de dessin
		 * @param L'évènement capté
		 */
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			//On rend accessible le bouton Sauvegarder
			btnSauvegarder.setEnabled(true);
			
			//On récupère les coordonnées du doigt sur le dessin
			float eventX = event.getX();
			float eventY = event.getY();
			
			//En fonction de l'action (touche l'écran ; bouge le doigt ; relève le doigt ; etc...)
			switch (event.getAction()) {
				/* ~~~~~~~~~~~~~~~~ *
				 *  Touche l'écran  *
				 * ~~~~~~~~~~~~~~~~ */
				case MotionEvent.ACTION_DOWN:
					path.moveTo(eventX, eventY);
					return true;
				/* ~~~~~~~~~~~~~~~~ *
				 *  Bouge le doigt  *
				 * ~~~~~~~~~~~~~~~~ */
				case MotionEvent.ACTION_MOVE:
					path.lineTo(eventX, eventY);
					break;
				/* ~~~~~~~~~~~~~~~~~ *
				 *  Relève le doigt  *
				 * ~~~~~~~~~~~~~~~~~ */	
				case MotionEvent.ACTION_UP:
					//On ne fait rien
					break;
				default:
					return false;
			}//fin switch
			invalidate();
			return true;
		}//fin onTouchEvent
		
		/**
		 * Réinitialise la zone de dessin et désactive le bouton "Sauvegarder"
		 */
		public void reinitialiser()
		{
			path.reset();
			invalidate();
			btnSauvegarder.setEnabled(false);
		}//fin reinitialiser
		
		/**
		 * Retourne la signature sous la forme d'une chaîne de caractère correspondant à l'encodage en JPEG de la signature
		 * @return La signature encodé en JPEG sous forme de chaîne de caractères [String]
		 */
		public String save()
		{
			String vretour;
			
			//Si le bitmap est vide, on va le créer en spécifiant ses dimensions
			if (bitmap == null)
			{
				bitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(),
							Bitmap.Config.RGB_565);
			}//fin if
			
			try {
				//On récupère la signature dessinée
				canvas = new Canvas(bitmap);
				this.draw(canvas);
				
				//On spécifie l'image en JPEG, et on l'encore en base 64 
				ByteArrayOutputStream ByteStream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ByteStream);
				byte[] b = ByteStream.toByteArray();
				vretour = Base64.encodeToString(b, Base64.DEFAULT);
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "Erreur Sauvegarde", Toast.LENGTH_LONG).show();
				vretour = null;
			}//fin catch
			
			Log.d("Signature", vretour);
			return vretour;
		}//fin save
	}//fin Signature
	
	/* **********************************
	 * M É T H O D E S
	 * ******************************* */
	/**
	 * Initialise l'activité
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture_signture);
		
		Log.d("Étape", "~ Récupération du client depuis la bdd");
		//On récupère le client
		BdAdapter bdd = new BdAdapter(this);
		bdd.open();
		leClient = bdd.getClientWithIdentifiant(this.getIntent().getExtras().getString("identifiant"));
		bdd.close();
		
		Log.d("Étape", "~ Client récupéré !");
		//On va initialiser les textview à partir des données du client
		String lig1 = "Client : " + leClient.getIdentifiant() + " - " + leClient.getNom() + " " + leClient.getPrenom();
		String lig2 = "Compteur : " + leClient.getIdCompteur() + " - Relevé : " + leClient.getDernierReleve() + " - Date : " + leClient.getDateDernierReleve();
		
		Log.d("Étape", "~ Création de la signature");
		signature = new Signature(this, null,lig1,lig2);
		
	 	linearLayout = (LinearLayout) this.findViewById(R.id.lytDessin);
		
		//On va ajouter un OnClickListener aux boutons
	 	btnAnnuler = (Button) this.findViewById(R.id.capt_sign_btnAnnuler);
		btnAnnuler.setOnClickListener(this);
		
		btnEffacer = (Button) this.findViewById(R.id.capt_sign_btnEffacer);
		btnEffacer.setOnClickListener(this);
		
		btnSauvegarder = (Button) this.findViewById(R.id.capt_sign_btnSauvegarder);
		btnSauvegarder.setOnClickListener(this);
		
		//On rend le bouton Sauvegarder innaccessible
		btnSauvegarder.setEnabled(false);
		
		linearLayout.addView(signature, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	}//fin onCreate
	
	/**
	 * Initialise le menu de l'activité
	 * @param Le menu permettant d'initialiser celui de l'activité [Menu]
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.capture_signture, menu);
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
	 * Gère les clics sur les différents boutons de l'application
	 * @param L'élément sur lequel l'utilisateur a cliqué [View]
	 */
	@Override
	public void onClick(View v)
	{
		Log.d("Étape", "~ Click sur un bouton");
		switch(v.getId())
		{
			case R.id.capt_sign_btnAnnuler:
				Log.d("Étape", "~ Click sur Annuler détecté");
				finish();
				break;
			case R.id.capt_sign_btnEffacer:
				signature.reinitialiser();
				break;
			case R.id.capt_sign_btnSauvegarder:
				//Sur le clic du bouton save, appelez la méthode de Signature permettant de sauvegarder le dessin en String, 
				//puis modifiez la variable signature_Base64 de l'objet client, 
				//sauvegardez l'objet client et appelez finish()
				
				
				Intent returnIntent = new Intent();
				
				returnIntent.putExtra("signatureBase64", signature.save());
				//On indique à l'activité appelante qu'on va lui retourner des données, et donc que l'activité s'est bien passée
				setResult(RESULT_OK,returnIntent);
				
				//On termine l'activité ModificationClient
				finish();
				break;
		}//fin switch
	}//fin onClick
}//fin classe