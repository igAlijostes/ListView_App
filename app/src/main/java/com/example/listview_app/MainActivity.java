package com.example.listview_app;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // inicializamos nuestras vistas creadas en activity_main.xml dandoles un nombre de variable.
    ListView list;
    Button Addboton;
    EditText valores;
    ArrayList<String> listaElementos;
     ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // le decimos al sistema que carge nuestro layout activity.main (xml) en nuestra app
        setContentView(R.layout.activity_main);

        //instaciamos nuestro ActionBar( mytoolbar) que se encuentra en el archivo activity_main.xml y llamamos al metodo setSupportActionBar
        // y le pasamos nuestra ActionBar como la app de la Actividad
        Toolbar mytoolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mytoolbar);


        // isntanciamos nuestras vistas de la aplicación dandoles un nombre
        list = (ListView) findViewById(R.id.listview1);
        Addboton = (Button) findViewById(R.id.boton1);
        valores = (EditText) findViewById(R.id.edittext1);

        // creamos el ArrayList que contendra nuestros productos de la compra de tipo String
         listaElementos = new ArrayList<String>();

        //Creamos y definimos un diseño de lista customizada a nuestro gusto llamada customizar_adapter) nuestro adaptador para que conecte nuestra lista con nuestra fuente de información
        // de los datos que en nuestro caso es el ArrayList.
        //final ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, listaElementos);
        adapter = new customizar_adapter(this,listaElementos);
                list.setAdapter(adapter);


       // creamos un evento para poner nuestro boton de la App a la espera que le hagan "click" para añadir un nuevo producto a la lista de
        // de la compra que previamente se escribio en el EditText.
        Addboton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listaElementos.add(valores.getText().toString());
                // debemos notificar al adaptador que se han producido cambios en la lista para que los refleje.
                adapter.notifyDataSetChanged();
            }
        });


       // creamos un evento para que nuestra lista se encuentre a la espera de que nosotros hagamos un "click prolongado" en cualquier
       // elemento que la compone y creamos un dialogo de confirmación que lo utilizaremos para solicitar si desea borrar (remove) un
       // elemento de la lista.
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int posc = position;
                AlertDialog.Builder constructor = new AlertDialog.Builder(MainActivity.this);
                constructor.setTitle("Advertencia de Borrado");
                constructor.setMessage("¿ Quieres Borrar esta compra?");
                constructor.setCancelable(false);
                constructor.setNegativeButton("No", null);
                constructor.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listaElementos.remove(posc);
                        adapter.notifyDataSetChanged();
                    }
                });
                constructor.show();
                return false;
            }

        });


    }


   //Creamos un metodo para mostrar o ocultar el menu_main en el ActionBar. Conseguimos el efecto de presionar
    // los tres puntos(...) en nuestra app.
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    //metodo para asignar las funciones a realizar a las opciones del menu del ActionBar de nuestra App.
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            // en caso de que clickemos en la banderita siempre visible de nuestro menu_main del ActionBar no desplegara un
            // dialogo de confirmación si deseamos salir de la aplicación o mantenernos en ella.
            case R.id.action_finish:
                AlertDialog.Builder constructor = new AlertDialog.Builder(MainActivity.this);
                constructor.setTitle("Advertencia de Salida de la APP");
                constructor.setMessage("¿ Quieres Salir de la APP?");
                constructor.setCancelable(false);
                constructor.setNegativeButton("No", null);
                constructor.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                constructor.show();
                break;

            case R.id.action_cargarLista:
                Cargar_Lista();
                break;
            case R.id.action_guardarLista:
               Guardar_Lista();
                break;

        }
        return true;

    }


   public void Cargar_Lista(){
       SharedPreferences preferencias= getSharedPreferences("ListaCompra",MODE_PRIVATE);
       Map<String,?> claves = preferencias.getAll();
       for(Map.Entry<String,?> ele : claves.entrySet()){
           listaElementos.add(ele.getKey()+" : " +ele.getValue().toString());
       }
       //valores.setText("lista",);
       Toast.makeText(this,"lista de Compra se ha cargado",Toast.LENGTH_LONG).show();
    }



    public void Guardar_Lista (){
        SharedPreferences preferencias= getSharedPreferences("ListaCompra",MODE_PRIVATE);
        listaElementos.add(valores.getText().toString());
        adapter.notifyDataSetChanged();
        SharedPreferences.Editor elemento=preferencias.edit();
        elemento.putString("",valores.getText().toString());
        elemento.commit();
        valores.setText("");
        Toast.makeText(this,"lista de Compra guardada",Toast.LENGTH_LONG).show();
    }




}

