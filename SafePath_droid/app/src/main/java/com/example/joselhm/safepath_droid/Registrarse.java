package com.example.joselhm.safepath_droid;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Registrarse extends AppCompatActivity {

    private Constantes configutation;
    private LinearLayout mainLayout;

    public EditText edt_name;
    public EditText edt_lastname;
    public EditText edt_email;
    public EditText edt_passw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Registrate:");

        setContentView(R.layout.activity_registrarse);
        IniConfig();
        IniMainLayout();
        IniComponents();
    }

    public void IniConfig(){
        DisplayMetrics displayMetrics = getBaseContext().getResources().getDisplayMetrics();
        int w = displayMetrics.widthPixels;
        int h = displayMetrics.heightPixels;
        configutation = new Constantes(w,h, this);
    }

    //Inicializamos el mainLayout
    public void IniMainLayout(){
        mainLayout = (LinearLayout)findViewById(R.id.layoutRegister);
        mainLayout.setPadding(configutation.getWidth(50), configutation.getHeight(35), configutation.getWidth(50), configutation.getHeight(35));
        mainLayout.setGravity(Gravity.CENTER_VERTICAL);
        mainLayout.setBackgroundColor(Color.WHITE);
    }

    //Inicializamos los componetes
    public void IniComponents()
    {
        //Tipo de fuente a usar
        final Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");

        edt_name = new EditText(this);
        edt_lastname = new EditText(this);
        edt_email = new EditText(this);
        edt_passw = new EditText(this);
        //Lo colocamos en un List
        List<EditText> listEdt= new ArrayList<EditText>();
        listEdt.add(edt_name);listEdt.add(edt_lastname);listEdt.add(edt_email);listEdt.add(edt_passw);

        //Ejemplo de la lista de strings:  {"Nombres","Apellidos","Correo Electrónico","Contraseña"};
        //Añadimos los componentes al mainLayout
        for(int i=0;i<Constantes.listStringTitleRegister.length;i++){
            TextView tmp = new TextView(this);
            tmp.setText(Constantes.listStringTitleRegister[i]);
            tmp.setTextColor(Color.BLACK);
            tmp.setTextSize(configutation.getHeight(18));

            listEdt.get(i).setHint(Constantes.listStringHitRegister[i]);
            listEdt.get(i).setSingleLine(true);
            listEdt.get(i).setTypeface(tf);

            mainLayout.addView(tmp);
            mainLayout.addView(listEdt.get(i));
        }

        //Colocamos el boton Registraese
        LinearLayout contentButton = new LinearLayout(this);
        contentButton.setPadding(0,configutation.getHeight(65),0,0);
        contentButton.setOrientation(LinearLayout.HORIZONTAL);
        contentButton.setGravity(Gravity.CENTER_HORIZONTAL);
        final Button btn_registrarse = new Button(this);
        btn_registrarse.setTextColor(Color.WHITE);
        btn_registrarse.setText("CREAR CUENTA");
        btn_registrarse.setTextSize(configutation.getHeight(20));
        btn_registrarse.setBackgroundColor(Color.parseColor("#E53935"));
        btn_registrarse.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

        contentButton.addView(btn_registrarse);

        mainLayout.addView(contentButton);
    }
}
