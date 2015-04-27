package org.btssio.edf_florian;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import classes.Client;
import dao.BdAdapter;

public class AfficheSignature extends Activity implements OnClickListener{
	private Client leClient;
	private LinearLayout linearLayout;
	private Button btnRetour;
	private Signature signature;
	
	
	public class Signature extends View {
		// variables nécessaire au dessin

		private Paint paint = new Paint();
		private Path path = new Path();// collection de l'ensemble des points sauvegardés lors des mouvements du doigt
		private Canvas canvas;
		private Bitmap bitmap;
		private String lig1, lig2;
		
		public Signature(Context context, AttributeSet attrs) {
			super(context, attrs);
			this.setBackgroundColor(Color.WHITE);
		}

		//gestion du dessin
		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			canvas.drawBitmap(bitmap, 0, 0, null);
		}
		
		/**
		 * Décode la signature qui est sous forme de chaîne codée en base 64 JPEG, en image pour pouvoir l'afficher à l'écran
		 * @param La signature encodée [String]
		 */
		public void dessine(String encodedString) {
			try {
				byte[] encodeByte = Base64
							.decode(encodedString, Base64.DEFAULT);
				bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
						encodeByte.length);
				bitmap = bitmap.copy(bitmap.getConfig(), true);
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "error dessine",
							Toast.LENGTH_LONG).show();
			}
			canvas = new Canvas(bitmap);
			this.draw(canvas);
		}
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
		setContentView(R.layout.activity_affiche_signature);
		
		Log.d("Étape", "~ Récupération du client depuis la bdd");
		//On récupère le client à partir de l'identifiant passé en paramètres via "ModificationClient"
		BdAdapter bdd = new BdAdapter(this).open();
		leClient = bdd.getClientWithIdentifiant(this.getIntent().getExtras().getString("identifiant"));
		bdd.close();
		Log.d("Étape", "~ Client récupéré !");
		
		//On va initialiser la signature pour pouvoir l'afficher
		Log.d("Étape", "~ Création de la signature");
		signature = new Signature(this, null);
		
		//On initialise les composants de l'activité
		linearLayout = (LinearLayout) this.findViewById(R.id.aff_sign_lytDessin);
		btnRetour = (Button) this.findViewById(R.id.aff_sign_btnRetour);
		btnRetour.setOnClickListener(this);
		
		//On affecte la signature au layout
		linearLayout.addView(signature, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		//Puis on affiche la signature
		signature.dessine(leClient.getSignatureBase64());
	}//fin onCreate

	/**
	 * Initialise le menu de l'activité
	 * @param Le menu permettant d'initialiser celui de l'activité [Menu]
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.affiche_signature, menu);
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
	public void onClick(View v) {
		//Si l'utilisateur a cliqué sur le bouton "Retour"
		if(v.getId() == R.id.aff_sign_btnRetour)
		{
			//On termine l'activité
			finish();
		}//fin if
	}//fin onClick
}//fin classe