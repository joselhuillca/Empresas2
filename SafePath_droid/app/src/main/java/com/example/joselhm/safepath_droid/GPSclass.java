package com.example.joselhm.safepath_droid;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by JoseLHM on 09/06/2016.
 */
public class GPSclass {
    public Context context;
    private int distancia;
    private boolean updateGPS;
    public Marker my_marker;
    //Es posible sacar la presicion del gps, entre otros ...

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }

    public double getDistancia() {
        return distancia;
    }

    private LocationManager locationManager;
    private Location currentLocation;
    private LocationListener locationListener;

    public GPSclass(Context ctx, boolean update_gps)
    {
        distancia = 0;//por defecto
        this.context = ctx;
        this.updateGPS = update_gps;
    }

    //Ver si es GPS esta activado o no, si lo esta actualizamos  'location'
    public boolean IniGPS()
    {
        locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            //Toast.makeText(context, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();

            setLocation();
            return true;
        }else{
            showGPSDisabledAlertToUser();
            return false;
        }
    }

    public Location getLocation(){
        return  currentLocation;
    }
    //Para setear la localizacion, primero debe de iniciarse el GPS- locationManager - IniGPS()
    public void setLocation()
    {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        try{//Intentamos obtener las posicion actual del GPS
            this.currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //Log.d("Entro:", currentLocation.toString());

            if(updateGPS){
                locationListener = new LocationListener() {
                    //Entra aui solo si cumplen las condiciones de tiempo y metros
                    public void onLocationChanged(Location location) {
                        //my_marker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
                        //PROBLEMA con los toast si pasas de un activity a otro
                        //Toast.makeText(getActivity(), location.toString(), Toast.LENGTH_LONG).show();
                        try{//Seteamos la posicion del marcador
                            my_marker.setPosition(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()));
                            Log.d("Intentando:", currentLocation.toString());
                        }catch (Exception e){
                            Log.d("Error:","actualizando currentlocation, my_marker puede que no este inicializado");
                        }
                    }
                    public void onProviderDisabled(String provider){
                        //Toast.makeText(getActivity(), "Provider OFF" + " " + provider, Toast.LENGTH_LONG).show();
                    }
                    public void onProviderEnabled(String provider){
                        // Toast.makeText(getActivity(), "Provider ON" + " " + provider, Toast.LENGTH_LONG).show();
                    }
                    public void onStatusChanged(String provider, int status, Bundle extras){
                        // Toast.makeText(getActivity(),"Provider Status: " + status + " " + provider, Toast.LENGTH_LONG).show();
                    }
                };
                //PARAMETRES->requestLocationUpdates(PROVEEDOR,Tiempo,metros,listener)
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, distancia, locationListener);
            }

        }catch (Exception e)
        {
            Log.d("Location:", "Error al setear la localizacion");//Error al setear la localizacion
        }
    }

    //Hacemos que ya no se actualice el GPS
    public void terminarLocalizacion() {
        if(updateGPS){
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.removeUpdates(locationListener);
        }
    }

    //Mostramos una alerta: GPS desactivado, y si el usuario quiere activar el GPS lo redirecciona al activity de settings de android
    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(Constantes.GPS_DISABLED)
                .setCancelable(false)
                .setPositiveButton(Constantes.GPS_SETTINGS,
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                context.startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
}
