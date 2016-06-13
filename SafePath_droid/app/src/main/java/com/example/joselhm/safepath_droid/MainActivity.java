package com.example.joselhm.safepath_droid;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private EditText edt_email;
    private EditText edt_passw;
    private Button btn_singIn;
    private InternetClass internetClass;
    private boolean isConnect; //Para saber si esta conectado a internet

    private LoginButton loginButton;
    private CallbackManager callbackManager;

    private Constantes configuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IniFacebookLogin();
        setContentView(R.layout.activity_main);

        verificarSiYaIniciasteSesion();

        IniConfiguration();
        IniComponents();
        //Aneste de VerConnectionInternet se necesita IniComponents
        VerConnectionInternet();
        IniButomFacebook();
        DarEstiloCompo();
    }

    public void IniComponents() {
        //REMOVE TITLE AND FULLSCREEN enable
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //InternetClass Inicializando...
        internetClass = new InternetClass(this);
        //Inicializamos los datos del Layout
        edt_email = (EditText)findViewById(R.id.edit_email);
        edt_passw = (EditText)findViewById(R.id.edit_pass);
        btn_singIn = (Button)findViewById(R.id.btn_iniSesion);
    }

    //Daremos estilo a los componentes del xml
    public void DarEstiloCompo()
    {
        //Colocamos una imagen de logo
        final ImageView imgLogo= (ImageView)findViewById(R.id.logo_);
        imgLogo.setImageBitmap(configuration.escalarImagen("icons/logo.png", configuration.getWidth(300), configuration.getHeight(82)));

        //Tipo de fuente usado
        final Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeueLight.ttf");
        final Typeface tf_bold = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");

        //Dar padding al mainlayout
        final LinearLayout mainLayout = (LinearLayout)findViewById(R.id.mainLayout);
        mainLayout.setPadding(configuration.getWidth(30),configuration.getHeight(135),configuration.getWidth(30),configuration.getHeight(30));
        //Dar padding al linear_feneral
        final LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linear_general);
        linearLayout.setPadding(configuration.getWidth(100),configuration.getHeight(100),configuration.getWidth(100),configuration.getHeight(50));

        //Dar estilo al texto Iniciar Sesion
        final TextView textIniSesion = (TextView)findViewById(R.id.text_inisesion);
        textIniSesion.setTextColor(Color.BLACK);
        textIniSesion.setGravity(Gravity.CENTER_HORIZONTAL);
        textIniSesion.setTypeface(tf_bold);
        textIniSesion.setPadding(0,0,0,configuration.getHeight(35));
        textIniSesion.setTextSize(configuration.getHeight(20));
        //Dar estilo al email
        edt_email.setSingleLine(true);
        edt_email.setTypeface(tf);
        edt_email.setTextSize(configuration.getHeight(20));
        edt_email.setTextColor(Color.BLACK);
        edt_email.setHintTextColor(Color.parseColor("#BDBDBD"));
        //Dar estilo al password
        edt_passw.setSingleLine(true);
        edt_passw.setTypeface(tf);
        edt_passw.setTransformationMethod(PasswordTransformationMethod.getInstance());
        edt_passw.setTextSize(configuration.getHeight(20));
        edt_passw.setTextColor(Color.BLACK);
        edt_passw.setHintTextColor(Color.parseColor("#BDBDBD"));
        //Dar estilo a"Tambien puedes iniciarcsesión usando:"
        final TextView text_infosesion = (TextView)findViewById(R.id.text_tamb_inisesion);
        text_infosesion.setTextColor(Color.parseColor("#616161"));
        text_infosesion.setTypeface(tf);
        text_infosesion.setTextSize(configuration.getHeight(18));
        text_infosesion.setPadding(0, configuration.getHeight(35), 0, configuration.getHeight(35));
        text_infosesion.setGravity(Gravity.CENTER_HORIZONTAL);
        //Dar estilo a "Si no estas registrado,"
        final TextView text_registro1 = (TextView)findViewById(R.id.text_sinoReg);
        text_registro1.setTextColor(Color.parseColor("#616161"));
        text_registro1.setTypeface(tf);
        text_registro1.setTextSize(configuration.getHeight(18));
        text_registro1.setPadding(0,configuration.getHeight(35),0,configuration.getHeight(35));
        text_registro1.setGravity(Gravity.RIGHT);
        //Dar estilo a "registrate aqui!"
        final TextView text_registro2 = (TextView)findViewById(R.id.text_registr);
        text_registro2.setTextColor(Color.parseColor("#F44336"));
        text_registro2.setTypeface(tf);
        text_registro2.setTextSize(configuration.getHeight(18));
        text_registro2.setGravity(Gravity.LEFT);
        text_registro2.setPadding(0, configuration.getHeight(35), 0, configuration.getHeight(35));
        text_registro2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funRegister();
            }
        });

        //Dar estilo a los BOTONES
        btn_singIn.setTextColor(Color.WHITE);
        btn_singIn.setTextSize(configuration.getHeight(20));
        btn_singIn.setBackgroundColor(Color.parseColor("#E53935"));
        btn_singIn.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        btn_singIn.setPadding(configuration.getWidth(65),0, configuration.getWidth(65), 0);
        btn_singIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funSingIn();
            }
        });
    }

    public void IniConfiguration(){
        DisplayMetrics displayMetrics = getBaseContext().getResources().getDisplayMetrics();
        int w = displayMetrics.widthPixels;
        int h = displayMetrics.heightPixels;
        configuration = new Constantes(w,h, this);
    }

    //Sseteamos el parametro isconnect para verificar la conexion a internet
    public void VerConnectionInternet()
    {
        isConnect = internetClass.estaConectado();
    }

    //Funcion de Iniciar Sesion mediante la cuenta de Safe Path
    public  void funSingIn()
    {
        VerConnectionInternet();
        if(isConnect)
        { //Te manda al NavigatorMapas
            Intent intent = new Intent(this,NavigatorMapas.class);
            startActivity(intent);
            overridePendingTransition(R.anim.my_fade_in, R.anim.my_fade_out);
            finish();
        }
    }

    //Inicializamos el SDK de Facebook
    public void IniFacebookLogin(){
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
    }

    //Damos funcionamiento al boton de facebook al hacerle click
    public void IniButomFacebook()
    {
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "user_friends", "email", "pages_messaging", "user_birthday"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                ProfileTracker profileTracker = new ProfileTracker() {//Actualiza el Profile
                    @Override
                    protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                        this.stopTracking();
                        Profile.setCurrentProfile(currentProfile);

                        funSingIn();
                    }
                };
                profileTracker.startTracking();
            }
            @Override
            public void onCancel() {
                //info.setText("Login attempt canceled.");
            }
            @Override
            public void onError(FacebookException e) {
                VerConnectionInternet();
            }
        });
    }

    //Verificamos si el usuario ya habia iniciado sesion anteriormente, si ese fuera el caso lo redireccionamos al activity MavigatorMapas
    public void verificarSiYaIniciasteSesion()
    {
        //Si se inicio session abre el nuevo activity
        if (Profile.getCurrentProfile() != null) {

            funSingIn();
        }
    }

    //Pasamos a otro activity - A Registrarse
    public void funRegister(){
        try{
            Intent intent = new Intent(this,Registrarse.class);
            startActivity(intent);
            overridePendingTransition(R.anim.my_fade_in, R.anim.my_fade_out);
            //finish(); //Sirve para cerrar definitivamente el activity actual al pasar a otro
        }catch (Exception e){
            //Error al pasar al actvity Registrarse
        }
    }

    //**************************************

}
