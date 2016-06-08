package com.example.joselhm.safepath_droid;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class AddZona extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    public Constantes configuration;

    public Button btn_bajo;
    public Button btn_medio;
    public Button btn_alto;
    private Button btn_confirmar;

    private Location location;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private int calificacion;
    private TextView descripcion;
    private SeekBar seekBar_radio;
    public EditText descripcion_text;

    private Marker my_marker;
    private Circle my_zone;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstancestate)
    {
        iniciarConfig();
        return  inflater.inflate(R.layout.activity_add_zona,container,false);
    }

    public void iniciarConfig(){
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        int w = displayMetrics.widthPixels;
        int h = displayMetrics.heightPixels;
        configuration = new Constantes(w,h, getActivity());
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Iniciar Mapa
        MapFragment fragment = (MapFragment)getChildFragmentManager().findFragmentById(R.id.mapCalification);
        fragment.getMapAsync(this);
        //Inicializamos Componentes
        calificacion = 0;
        Ini_seekBar_radio();
        Ini_botones_calificar();
        Ini_addDescription_pop();
        descripcion_text = new EditText(getActivity());
        descripcion_text.setText("");

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
            mMap = googleMap;

            //seteamos la latitud y longitud que se mostrara en el mapa
            LatLng lat_lng = new LatLng(-16.4548539,-71.5405225);
            //LatLng lat_lng = new LatLng(location.getLatitude(),location.getLongitude());
            //El marcador y mi zona estaran juntos
            my_marker = googleMap.addMarker(new MarkerOptions()
                    .position(lat_lng)
                    .title("Zona a añadir")
                    .draggable(true));
            my_zone = googleMap.addCircle(new CircleOptions()
                    .center(lat_lng)
                    .radius(10)
                    .strokeColor(Color.parseColor("#9CCC65"))
                    .fillColor(Color.parseColor("#33691E")));

            int zoom_ = 13;
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lat_lng, zoom_));

            // Zoom out to zoom level 13, animating with a duration of 2 seconds.
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(zoom_ + 3), 2000, null);


            //AL  Arrastrar el marcador se ejecuta esta funcion
            googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

                @Override
                public void onMarkerDragStart(Marker marker) {
                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    marker.setSnippet(marker.getPosition().toString());
                    my_zone.setCenter(marker.getPosition());
                }

                @Override
                public void onMarkerDrag(Marker marker) {
                }

            });
        }catch (Exception e){
            //error al cargar el mapa
        }
    }

    //-----------------------------------  FUNCIONES DE CALIFICACION ---------------------------------------------
    //Inicializamos los botones para calificar
    public void Ini_botones_calificar()
    {
        btn_bajo = (Button)getView().findViewById(R.id.btn_cal_bajo);
        btn_medio = (Button)getView().findViewById(R.id.btn_cal_medio);
        btn_alto = (Button)getView().findViewById(R.id.btn_cal_alto);

        btn_bajo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_calif_Bajo();
            }
        });
        btn_medio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_calif_Medio();
            }
        });
        btn_alto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_calif_Alto();
            }
        });

        btn_confirmar = (Button)getView().findViewById(R.id.btn_confirmarAddZona);
        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_qualification();
            }
        });
    }
    //Boton calificacion tipo bajo
    public void btn_calif_Bajo()
    {
        calificacion = 1;

        btn_bajo.setBackgroundColor(Color.parseColor(getString(R.string.color_bajo_sel)));
        btn_medio.setBackgroundColor(Color.parseColor(getString(R.string.color_medio)));
        btn_alto.setBackgroundColor(Color.parseColor(getString(R.string.color_alto)));

        btn_bajo.setTextColor(Color.parseColor(getString(R.string.color_bajo)));
        btn_medio.setTextColor(Color.parseColor(getString(R.string.color_medio_sel)));
        btn_alto.setTextColor(Color.parseColor(getString(R.string.color_alto_sel)));
        //Cambiamos el color de mi zona
        my_zone.setFillColor(Color.parseColor(configuration.list_colores_center.get(0)));
        my_zone.setStrokeColor(Color.parseColor(configuration.list_colores.get(0)));
    }
    //Boton calificacion tipo Medio
    public void btn_calif_Medio()
    {
        calificacion = 3;

        btn_bajo.setBackgroundColor(Color.parseColor(getString(R.string.color_bajo)));
        btn_medio.setBackgroundColor(Color.parseColor(getString(R.string.color_medio_sel)));
        btn_alto.setBackgroundColor(Color.parseColor(getString(R.string.color_alto)));

        btn_bajo.setTextColor(Color.parseColor(getString(R.string.color_bajo_sel)));
        btn_medio.setTextColor(Color.parseColor(getString(R.string.color_medio)));
        btn_alto.setTextColor(Color.parseColor(getString(R.string.color_alto_sel)));
        //Cambiamos el color de mi zona
        my_zone.setFillColor(Color.parseColor(configuration.list_colores_center.get(1)));
        my_zone.setStrokeColor(Color.parseColor(configuration.list_colores.get(1)));
    }
    //Boton calificacion tipo Alto
    public void btn_calif_Alto()
    {
        calificacion = 5;

        btn_bajo.setBackgroundColor(Color.parseColor(getString(R.string.color_bajo)));
        btn_medio.setBackgroundColor(Color.parseColor(getString(R.string.color_medio)));
        btn_alto.setBackgroundColor(Color.parseColor(getString(R.string.color_alto_sel)));

        btn_bajo.setTextColor(Color.parseColor(getString(R.string.color_bajo_sel)));
        btn_medio.setTextColor(Color.parseColor(getString(R.string.color_medio_sel)));
        btn_alto.setTextColor(Color.parseColor(getString(R.string.color_alto)));
        //Cambiamos el color de mi zona
        my_zone.setFillColor(Color.parseColor(configuration.list_colores_center.get(2)));
        my_zone.setStrokeColor(Color.parseColor(configuration.list_colores.get(2)));
    }

    public void add_qualification()
    {
        RequestParams params = new RequestParams();
        String idface = "1526487456";
        String idGoogle = "";
        String idExtra = "";
        String descript = descripcion_text.getText().toString();
        double lat = my_marker.getPosition().latitude;
        double lng = my_marker.getPosition().longitude;
        double radio = my_zone.getRadius();
        //calificacion->nivel
        if(calificacion==0){
            Toast.makeText(getActivity(), "Califique la zona!!", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            params.put("idFacebook", idface);
            params.put("idGooglePlus",idGoogle);
            params.put("idExtra", idExtra);
            params.put("descripcion", descript);
            params.put("lat", lat);
            params.put("lng",lng);
            params.put("radio",radio);
            params.put("nivel",calificacion);

        } catch (Exception  e) {
            //Error al colocar los parametros
        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Constantes.URL_BASE + Constantes.LINK_BD_ZONA, params, registerZone());

    }
    //Mostrar mensajes al añadir una zona
    private AsyncHttpResponseHandler registerZone() {
        return new AsyncHttpResponseHandler() {
            ProgressDialog pDialog;

            @Override
            public void onStart() {
                super.onStart();
                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Añadiendo Zona ...");
                pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pDialog.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] response,
                                  Throwable arg3) {
                // TODO Auto-generated method stub
                pDialog.dismiss();
                Toast.makeText(getActivity(), "Error al añadir zona!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // TODO Auto-generated method stub
                pDialog.dismiss();
                btn_confirmar.setText("AÑADIR OTRA ZONA");
                Toast.makeText(getActivity(), "Listo!", Toast.LENGTH_LONG).show();
            }
        };
    }

    //Inicializamos la funcion de añadir descripcion - pop up
    public void Ini_addDescription_pop()
    {
        descripcion = (TextView)getView().findViewById(R.id.pop_addDescription);
        descripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getActivity(), PopAddQualification.class));
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());


                descripcion_text.setHint("Escribe una descripción ...");
                descripcion_text.setHeight(configuration.getHeight(200));
                descripcion_text.setGravity(Gravity.TOP);

                alertDialogBuilder.setTitle("Añadir una descripción:");
                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(descripcion_text);

                // set dialog message
                alertDialogBuilder.setCancelable(false)
                        .setNegativeButton("CANCELAR",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        descripcion_text.setText("");
                                        dialog.cancel();
                                    }
                                })
                        .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                descripcion.setTextColor(Color.parseColor("#1B5E20"));
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
        });
    }



    //Iniializamos el seekbar para el radio de la zona
    public void Ini_seekBar_radio()
    {
        seekBar_radio = (SeekBar)getView().findViewById(R.id.seekBar_radio);
        seekBar_radio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                my_zone.setRadius((double) progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
