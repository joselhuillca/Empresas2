package com.example.joselhm.safepath_droid;

import android.app.Fragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MapaGeneral extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    public List<Zona> list_zonas;
    private LatLng sydney;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstancestate)
    {
        return  inflater.inflate(R.layout.activity_mapa_general,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MapFragment fragment = (MapFragment)getChildFragmentManager().findFragmentById(R.id.mapB);
        fragment.getMapAsync(this);

        list_zonas = new ArrayList<Zona>();
        sydney = new LatLng(-16.411141,-71.540515);
        //load_zones();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        try{
            int zoom_ = 10;
            mMap = googleMap;

            // Add a marker in Sydney and move the camera

            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoom_));

            // Zoom out to zoom level 10, animating with a duration of 2 seconds.
            mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom_ + 5), 2000, null);

            /*for(int i=0;i<list_zonas.size();i++){
                googleMap.addCircle(new CircleOptions()
                        .center(new LatLng(list_zonas.get(i).getLat(), list_zonas.get(i).getLng()))
                        .radius(list_zonas.get(i).getRadio())
                        .strokeColor(Color.parseColor(Constantes.list_colores.get(list_zonas.get(i).nivel)))
                        .fillColor(Color.parseColor(Constantes.list_colores_center.get(list_zonas.get(i).nivel))));
            }*/
            //notificZonaP();
            load_zones(googleMap);
            //drawZonas(mMap);
            /*mMap.addCircle(new CircleOptions()
                    .center(new LatLng(-16.411141, -71.540515))
                    .radius(54)
                    .strokeColor(Color.parseColor(Constantes.list_colores.get(1)))
                    .fillColor(Color.parseColor(Constantes.list_colores_center.get(1))));*/
        }catch (Exception e){
            //Problemas al cargar el mapa
        }
    }

    //MIS FUNCIONES

    //Cargar las Zonas de la Base de Datos
    public  void load_zones(GoogleMap googleMap)
    {
        try{
            list_zonas.clear();
            AsyncHttpClient client = new AsyncHttpClient();

            client.get(Constantes.URL_BASE + Constantes.LINK_BD_ZONA, null,getzonas(googleMap));
            /*list_zonas = new ArrayList<Zona>();
            list_zonas.add(new Zona(-16.4548539,-71.5405225,70,"Zona1",2));
            list_zonas.add(new Zona(-16.4513143,-71.5269613,90,"Zona2",1));
            list_zonas.add(new Zona(-16.4331215, -71.5361452, 110, "Zona3", 1));
            list_zonas.add(new Zona(-16.4290052, -71.5222406, 70, "Zona4", 2));
            list_zonas.add(new Zona(-16.4164912, -71.5395784, 160, "Zona5", 1));
            list_zonas.add(new Zona(-16.4008476, -71.5428400, 84, "Zona6", 2));*/
        }catch (Exception e) {
            //
        }

    }

    //Dibujar las zonas en el mapa
    public void drawZonas(final GoogleMap googleMap){
        Toast.makeText(getActivity(), String.valueOf(list_zonas.size()), Toast.LENGTH_LONG).show();
        Thread t=new Thread()
        {
            public void run()
            {
                try {
                    sleep(1000);
                    for(int i=0;i<list_zonas.size();i++){
                        int nivel = list_zonas.get(i).getNivel()-1;
                        if(nivel>=5){nivel=4;}
                        googleMap.addCircle(new CircleOptions()
                        .center(new LatLng(list_zonas.get(i).getLat(), list_zonas.get(i).getLng()))
                        .radius(list_zonas.get(i).getRadio())
                        .strokeColor(Color.parseColor(Constantes.list_colores.get(nivel)))
                        .fillColor(Color.parseColor(Constantes.list_colores_center.get(nivel))));
                        /*Log.d("Lat:",String.valueOf(list_zonas.get(i).getLat()));
                        Log.d("Lng:",String.valueOf(list_zonas.get(i).getLng()));
                        Log.d("Radio:",String.valueOf(list_zonas.get(i).getRadio()));
                        Log.d("Color1:",String.valueOf(Constantes.list_colores.get(nivel)));
                        Log.d("Color2:",String.valueOf(Constantes.list_colores_center.get(nivel)));*/
                    }
                }
                catch (InterruptedException e) {
                    //e.printStackTrace();
                }
            }
        };
        t.start();

    }

    //Mostrar mensajes al añadir una zona
    private AsyncHttpResponseHandler getzonas(final GoogleMap googleMap) {
        return new AsyncHttpResponseHandler() {
            ProgressDialog pDialog;

            @Override
            public void onStart() {
                super.onStart();
                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Download Data ...");
                pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pDialog.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] response,
                                  Throwable arg3) {
                // TODO Auto-generated method stub
                pDialog.dismiss();
                Toast.makeText(getActivity(), "Error al descargar zonas!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // TODO Auto-generated method stub
                pDialog.dismiss();
                String resultadoJson = new String(response);
                JsonParser parser = new JsonParser();
                JsonElement tradeElement = parser.parse(resultadoJson);
                JsonArray arrayZonas = tradeElement.getAsJsonArray();
                int numZonas = arrayZonas.size();
                for(int i=0;i<numZonas;i++){
                    JsonElement obj = arrayZonas.get(i);
                    JsonObject json = obj.getAsJsonObject();
                    //JsonElement ele = json.get("_id");
                    Zona z = new Zona();
                    //z.set_id(json.get("_id").getAsString());
                    z.setIdFacebook(json.get("idFacebook").getAsInt());
                    z.setIdGooglePlus(json.get("idGooglePlus").getAsString());
                    z.setIdExtra(json.get("idExtra").getAsString());
                    z.setLat(json.get("lat").getAsDouble());
                    z.setLng(json.get("lng").getAsDouble());
                    z.setRadio(json.get("radio").getAsInt());
                    z.setDescripcion(json.get("descripcion").getAsString());
                    z.setNivel(json.get("nivel").getAsInt());
                    //z.set_v(_v);
                    list_zonas.add(z);
                    /*mMap.addCircle(new CircleOptions()
                            .center(new LatLng(z.getLat(), z.getLng()))
                            .radius(z.getRadio())
                            .strokeColor(Color.parseColor(Constantes.list_colores.get(z.getNivel())))
                            .fillColor(Color.parseColor(Constantes.list_colores_center.get(z.getNivel()))));*/

                }
                drawZonas(googleMap);
                /*mMap.addCircle(new CircleOptions()
                        .center(new LatLng(-16.411141, -71.540515))
                        .radius(54)
                        .strokeColor(Color.parseColor(Constantes.list_colores.get(1)))
                        .fillColor(Color.parseColor(Constantes.list_colores_center.get(1))));*/
                Toast.makeText(getActivity(), "Zonas Cargadas...!", Toast.LENGTH_LONG).show();

            }
        };
    }

    //Verificar si estas en una zona peligrosa
    public void notificZonaP()
    {
        double dist = 100;
        boolean a= false;
        for(int i=0;i<list_zonas.size();i++){
            double distancia = Math.sqrt(Math.pow(sydney.latitude-list_zonas.get(i).lat,2)+Math.pow(sydney.longitude-list_zonas.get(i).lng,2));
            if(distancia<dist){
                showNotification();
            }
        }
    }

    // Ejemplo de Notificacion
    public void showNotification(){
        //Builder es el que proporciona una manera conveniente diferentes campos de una notificación y generar vistas de contenido
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity());
        builder.setSmallIcon(R.drawable.icono);
        builder.setContentTitle("Safe Paths V1");
        builder.setContentText("Te estás aproximando a una zona peligrosa");
        //Intent sirve para invocar componentes, ahora solo llamara al mismo layout
        Intent intent= new Intent(getActivity(),NavigatorMapas.class);
        //Es para que funcione en versiones anteriores
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        //PendingIntent será el que contenga la configuración que necesitamos para que cuando pulsemos la notificación en este caso abrirá el mainActivity
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        //NotificationManager es capaz de lanzar el Intent que nosotros hemos configurado
        NotificationManager NM= (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
        NM.notify(0,builder.build());
    }
}
