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
		
		public Signature(Context context, AttributeSet attrs, String lig1, String lig2) {
			super(context, attrs);
			this.setBackgroundColor(Color.WHITE);
			paint.setAntiAlias(true); // empêche le scintillement gourmand en cpu et mémoire 
			paint.setColor(Color.BLACK);
			paint.setStrokeWidth(5f); //taille de la grosseur du trait en pixel
			paint.setTextSize(20);// taille du texte pur afficher les lignes
			this.lig1 = lig1;
			this.lig2 = lig2;
		}

		//gestion du dessin
		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			paint.setStyle(Paint.Style.FILL);
			canvas.drawText(lig1, 20, 30, paint);
			canvas.drawText(lig2, 20, 60, paint);
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawPath(path, paint);
		}
		
		public void reinitialiser()
		{
			path.reset();invalidate();
		}//fin reinitialiser
		
		
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_affiche_signature);
		
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
		
		linearLayout = (LinearLayout) this.findViewById(R.id.aff_sign_lytDessin);
		
		btnRetour = (Button) this.findViewById(R.id.aff_sign_btnRetour);
		btnRetour.setOnClickListener(this);
		
		linearLayout.addView(signature, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		
		signature.dessine(leClient.getSignatureBase64());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.affiche_signature, menu);
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
