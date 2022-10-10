package com.example.appbanco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class Register extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String [] rols = {"Administrador", "Usuario"};
    String rolSelect;

    EditText email, name, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = (EditText)findViewById(R.id.etemailreg);
        name = (EditText)findViewById(R.id.etnamereg);
        password = (EditText)findViewById(R.id.etpasswordreg);
        Spinner rol = findViewById(R.id.sprolreg);
        Button register = findViewById(R.id.btnregister);
        ArrayAdapter adpRol = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, rols);
        rol.setAdapter(adpRol);
        rol.setOnItemSelectedListener(this);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchCustomer(email.getText().toString(), name.getText().toString(), password.getText().toString(), rolSelect);
            }
        });
    }

    private void searchCustomer(String sEmail, String sName, String sPassword, String srolSelect) {
        ArrayList<String> dataCustomer = new ArrayList<String>();
        sqlBanco ohBanco = new sqlBanco(this, "dbbanco", null, 1);
        SQLiteDatabase db = ohBanco.getReadableDatabase();
        String sql = "Select email From customer Where email = '"+ sEmail +"'";
        Cursor cCustomer = db.rawQuery(sql, null);

        if (!cCustomer.moveToFirst()){
            SQLiteDatabase dbadd = ohBanco.getWritableDatabase();
            try {
                ContentValues cvCustomer = new ContentValues();
                cvCustomer.put("email", sEmail);
                cvCustomer.put("name", sName);
                cvCustomer.put("password", sPassword);
                cvCustomer.put("rol", srolSelect);
                dbadd.insert("customer", null, cvCustomer);
                dbadd.close();
                Toast.makeText(getApplicationContext(), "Cliente agregado correctamente", Toast.LENGTH_SHORT).show();
                if (srolSelect.equals("Administrador")){
                    Intent ventana = new Intent(getApplicationContext(), Cuenta.class);
                    ventana.putExtra("sname", sName);
                    ventana.putExtra("srol", srolSelect);
                    startActivity(ventana);

                    email.setText("");
                    name.setText("");
                    password.setText("");

                } else {
                    startActivity(new Intent(getApplicationContext(), Usuario.class));
                    email.setText("");
                    name.setText("");
                    password.setText("");
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Email existente!. Iténtelo con otro", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        rolSelect = rols[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}