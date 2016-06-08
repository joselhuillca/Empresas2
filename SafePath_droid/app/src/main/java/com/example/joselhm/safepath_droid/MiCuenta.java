package com.example.joselhm.safepath_droid;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;

public class MiCuenta extends AppCompatActivity {

    private Constantes configuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_cuenta);
        setTitle("Mi Cuenta");

        IniConfiguration();
        IniComponentes();
    }

    //Inicializar componetes
    public void IniComponentes(){
        //seteamos el imageProfileLarge por default
        ImageView imgProfileLarge = (ImageView)findViewById(R.id.imgProfileLarge);
        Drawable imgProf = new BitmapDrawable(this.getResources(),configuration.escalarImagen("icons/userProfile.png",250,250));
        imgProfileLarge.setBackground(imgProf);
    }

    public void IniConfiguration(){
        DisplayMetrics displayMetrics = getBaseContext().getResources().getDisplayMetrics();
        int w = displayMetrics.widthPixels;
        int h = displayMetrics.heightPixels;
        configuration = new Constantes(w,h, this);
    }
}
