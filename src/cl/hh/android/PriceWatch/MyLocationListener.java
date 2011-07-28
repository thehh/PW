package cl.hh.android.PriceWatch;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.maps.OverlayItem;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.content.Context;
import android.graphics.drawable.Drawable;

public class MyLocationListener implements LocationListener {
	String TAG = "[LOG - LocationListener]";
	DialogBuilder dialog;
	Context context;
	String direccion = "Buscando Direccion";
	OverlayItem overlayitemMe;
	Drawable myIcon;
	double latitude;
	double longitude;
	String ciudad;
	String pais;
	String shortPais;
	
	public MyLocationListener(){
		
	}
	
	public MyLocationListener(String S, Context c){
		this.TAG = S;
		this.context = c;
	}
	
	public void setLatitude(double d){
		this.latitude = d;
	}
	
	public double getLatitude(){
		return this.latitude;
	}
	
	public void setLongitude(double d){
		this.longitude = d;
	}
	
	public double getLongitude(){
		return this.longitude;
	}
	
	public void setMyIcon(Drawable d){
		this.myIcon = d;
	}
	
	public Drawable getMyIcon(){
		return this.myIcon;
	}
	
	public void setOverlayItem(OverlayItem o){
		this.overlayitemMe = o;
	}
	
	public OverlayItem getOverlayItem(){
		return this.overlayitemMe;
	}
	
	public void setDireccion(String S){
		Log.i(TAG, "[setDireccion] Direccion es:\n " + S);
		this.direccion = S;
	}
	
	public String getDireccion(){
		return this.direccion;
	}
	
	public void setTAG(String S){
		this.TAG = S;
	}
	
	public String getTAG(){
		return this.TAG;
	}
	
	public void setContext(Context c){
			this.context = c;
	}
	
	public Context getContext(){
		return this.context;
	}
	
	public void setDialog(DialogBuilder d){
		this.dialog = d;
	}
	
	public DialogBuilder getDialog(){
		return this.dialog;
	}
	
	public void addDialog(){
		DialogBuilder dB = new DialogBuilder(this.context, "[LOG - LocationListener - addDialog]");
		this.setDialog(dB);
	}
	
	private double truncate(double value, int places) {
	    double multiplier = Math.pow(10, places);
	    return Math.floor(multiplier * value) / multiplier;
	}
	
	public void setCiudad(String S){
		this.ciudad = S;
	}
	
	public String getCiudad(){
		return this.ciudad;
	}
	
	public void setPais(String S){
		this.pais = S;
	}
	
	public String getPais(){
		return this.pais;
	}
	
	public void setShortPais(String S){
		this.shortPais = S;
	}
	
	public String getShortPais(){
		return this.shortPais;
	}


	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	public void onLocationChanged(Location loc) {
		if (loc != null) {
			String strLoc = " Ubicación: \n(" + truncate(loc.getLatitude(),6) + ";" + truncate(loc.getLongitude(),6) + ")";
			Log.i(TAG, "Lat: " + truncate(loc.getLatitude(),6) );
			Log.i(TAG, "Long: " + truncate(loc.getLongitude(),6));
			String strDireccion = null;
			Geocoder geoDireccion = new Geocoder(this.context, Locale.getDefault());
			try{
				List<Address> listaDirecciones = geoDireccion.getFromLocation(truncate(loc.getLatitude(),6), truncate(loc.getLongitude(),6), 1);
				if(listaDirecciones != null && listaDirecciones.size() > 0){
					Address address = listaDirecciones.get(0);
					strDireccion = " Dirección:\n" + address.getAddressLine(0) + ", " + address.getLocality();
					this.setCiudad(address.getLocality());
					this.setPais(address.getCountryName());
					this.setShortPais(address.getCountryCode());
				}
			}
			catch (IOException e){
				Log.i(TAG, e.toString());
				//showDialog(R.string.msg, e.toString());
			}
			//TextView ubicacion = (TextView) findViewById(R.id.locationLabel);
			if(strDireccion != null){
				this.setDireccion(strDireccion);
			}
			else
				this.setDireccion(strLoc);
		}
		else
			dialog.showDialog(R.string.error, "No se ha detectado la posicion");
	
	}
	

}
