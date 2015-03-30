package dao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import classes.Client;

import java.util.List;

import org.btssio.edf_florian.R;

public class ClientAdapter extends BaseAdapter {
	private List<Client> listeClients;
	private LayoutInflater layoutInflater;
	private class ViewHolder {
		TextView textViewIdentifiant;
		TextView textViewNom;
		TextView textViewPrenom;
		TextView textViewTelephone;
		TextView textViewAdresse;
		TextView textViewCodePostal;
		TextView textViewVille;
	}
	
	
	/**
	 * Instancie un objet de la classe ClientAdapter
	 * @param ctx
	 * @param listeClients
	 */
	public ClientAdapter(Context ctx, List<Client> listeClients) {
		layoutInflater  	= LayoutInflater.from(ctx);
		this.listeClients	= listeClients;
	}

	/**
	 * Retourne le nombre de clients dans la liste
	 * @return Taille de la liste [Integer]
	 */
	@Override
	public int getCount() {
		return listeClients.size();
	}

	/**
	 * Retourne un client depuis la liste, en fonction de son index dans la liste
	 * @return Le client [Object]
	 */
	@Override
	public Object getItem(int arg0) {
		return listeClients.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null)
		{
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.vue_clients, null);
			holder.textViewIdentifiant = (TextView) convertView.findViewById(R.id.vueIdentifiant);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.textViewIdentifiant.setText(listeClients.get(position).getIdentifiant());
		
		return convertView;
	}

}
