package com.example.appbanco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText user, pass;
    private Cursor infoUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView reglink = findViewById(R.id.tvregister);
        user = (EditText)findViewById(R.id.etemail);
        pass = (EditText)findViewById(R.id.etpassword);

        reglink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });

    }

    public void IniciarSesion(View view){
        sqlBanco admin = new sqlBanco(this,"dbbanco",null,1);
        SQLiteDatabase db = admin.getWritableDatabase();
        String usuario = user.getText().toString();
        String contrasena = pass.getText().toString();

        infoUser = db.rawQuery("select email, name, password, rol from customer where email='"+
                usuario +"' and password ='"+ contrasena+"'",null);

        try {
            if(infoUser.moveToFirst()){
                String usu = infoUser.getString(0);
                String name = infoUser.getString(1);
                String psw = infoUser.getString(2);
                String rol = infoUser.getString(3);
                if (usuario.equals(usu) && contrasena.equals(psw)){
                    if ("Administrador".equals(rol)){
                        Intent ventana = new Intent(this, Cuenta.class);
                        ventana.putExtra("sname", name);
                        ventana.putExtra("srol", rol);
                        startActivity(ventana);
                        user.setText("");
                        pass.setText("");
                    } else {
                        Intent ventana = new Intent(this, Usuario.class);
                        startActivity(ventana);
                        user.setText("");
                        pass.setText("");
                    }
                }
            }
            else {
                Toast.makeText(getApplicationContext(), "Email y/o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }
}