package com.example.appbanco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class addAccount extends AppCompatActivity {

    TextView snumberc;
    EditText emailc, fechac, balancec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        emailc = (EditText)findViewById(R.id.etemailc);
        fechac = (EditText)findViewById(R.id.etfechac);
        balancec = (EditText)findViewById(R.id.etbalancec);
        Button agregarc = findViewById(R.id.btnagregarc);
        snumberc = (TextView) findViewById(R.id.tvnumberc);

        agregarc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarCuenta(emailc.getText().toString(), fechac.getText().toString(), balancec.getText().toString(), snumberc.getText().toString());
            }
        });
    }

    private boolean validarCampos(){
        if (emailc.getText().toString().isEmpty() ||fechac.getText().toString().isEmpty() ||balancec.getText().toString().isEmpty() ){
            return false;
        }else {
            return true;
        }
    }

    private void agregarCuenta(String semail, String sdate, String sbalance, String numeroc) {

        boolean validar = validarCampos();

        if(validar == true){
            String bal = balancec.getText().toString();
            int balance = Integer.parseInt(bal);
            if(balance >= 1000000 && balance <= 200000000){
                ArrayList<String> dataAccount = new ArrayList<String>();
                sqlBanco ohBanco = new sqlBanco(this, "dbbanco", null, 1);
                SQLiteDatabase db = ohBanco.getReadableDatabase();
                String sql = "Select email From account Where email = '"+ semail +"'";
                Cursor cAccount = db.rawQuery(sql, null);

                if (!cAccount.moveToFirst()){
                    SQLiteDatabase dbadd = ohBanco.getWritableDatabase();
                    try {
                        ContentValues cvAccount = new ContentValues();
                        cvAccount.put("email", semail);
                        cvAccount.put("date", sdate);
                        cvAccount.put("balance", sbalance);
                        dbadd.insert("account", null, cvAccount);
                        Toast.makeText(getApplicationContext(), "Cuenta agregada correctamente", Toast.LENGTH_SHORT).show();

                        Log.i("number cuenta", "la tabla cuenta " + cvAccount);

                        String acsql = "Select accountnumber from account order by accountnumber desc limit 1;";
                        Cursor sacNumber = db.rawQuery(acsql,null);
                        if (sacNumber.moveToFirst()){
                            String numero = sacNumber.getString(0);
                            snumberc.setText(numero);
                            emailc.setText("");
                            fechac.setText("");
                            balancec.setText("");
                        }

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Este email ya tine una cuenta o no se ha registrado", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(getApplicationContext(), "Debes ingresar entre 1 millon y 200 millones en el Balance", Toast.LENGTH_SHORT).show();
            }

        }else {
           Toast.makeText(getApplicationContext(), "Todos los campos son requeridos", Toast.LENGTH_SHORT).show();
        }
    }
}