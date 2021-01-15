package com.example.listview_app;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
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
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.app.Notification.*;

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
                // aqui implementados en metodo creado más abajo para cargar una lista guardada al dar dl boton
                // de cargar en el menu del ActionBar
                Cargar_Lista();
                // estableceremos el icono y el texto a mostrar en la barra de estado, y el titulo de la notificación
                //. Con estos datos construiremos un objeto Notification llamado nBuilder
                NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.finishflag)
                        .setContentTitle(" ALERTA!! Datos Lista CARGADOS!!")
                        .setContentText("Los Datos de la Lista cargados!!") ;
                        Notification notificacion = nBuilder.build();
                        // Como opciones adicionales, también podemos indicar por ejemplo que nuestra notificació
                // desaparezca automáticamente de la bandeja del sistema cuando se pulsa sobre ella (FLAG_AUTO_CANCEL.)
                // También podríamos indicar que al generarse la notificación el dispositivo debe emitir un sonido,
                //vibrar o encender el LED de estado presente en muchos terminales (DEFAULT_SOUND,
                //DEFAULT_VIBRATE o DEFAULT_LIGHTS)
                notificacion.flags |= FLAG_AUTO_CANCEL;
                notificacion.defaults |= DEFAULT_SOUND;
                notificacion.defaults |= DEFAULT_VIBRATE;
                //se lanza en la misma actividad principal
                Intent notIntent = new Intent(MainActivity.this, MainActivity.class);
                PendingIntent contIntent = PendingIntent.getActivity(MainActivity.this, 0, notIntent, 0);
                nBuilder.setContentIntent(contIntent);
                //Para generar notificaciones en la barra de estado del sistema, lo primero que debemos hacer es
                //obtener una referencia al servicio de notificaciones de Android, a través de la clase
               // NotificationManager. Utilizaremos para ello el método getSystemService() indicando como
                //parámetro el identificador del servicio correspondiente, en este casoContext.NOTIFICATION_SERVICE
                NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                nManager.notify(0, nBuilder.build());
                break;
            case R.id.action_guardarLista:
                // aqui implementados en metodo creado más abajo para guardar una lista realizada por nosotros al dar dl boton
                // de guardar en el menu del ActionBar
               Guardar_Lista();
                // estableceremos el icono y el texto a mostrar en la barra de estado, y el titulo de la notificación
                //. Con estos datos construiremos un objeto Notification llamado nBuilder
                NotificationCompat.Builder nBuilder2 = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.finishflag)
                        .setContentTitle(" ALERTA!! Datos Lista GUARDADOS")
                        .setContentText("Los Datos de la Lista se ha guardado!!") ;
                Notification notificacion2 = nBuilder2.build();
                // Como opciones adicionales, también podemos indicar por ejemplo que nuestra notificació
                // desaparezca automáticamente de la bandeja del sistema cuando se pulsa sobre ella (FLAG_AUTO_CANCEL.)
                // También podríamos indicar que al generarse la notificación el dispositivo debe emitir un sonido,
                //vibrar o encender el LED de estado presente en muchos terminales (DEFAULT_SOUND,
                //DEFAULT_VIBRATE o DEFAULT_LIGHTS)
                notificacion2.flags |= FLAG_AUTO_CANCEL;
                notificacion2.defaults |= DEFAULT_SOUND;
                notificacion2.defaults |= DEFAULT_VIBRATE;
                //se lanza en la misma actividad principal
                Intent notIntent2 = new Intent(MainActivity.this, MainActivity.class);
                PendingIntent contIntent2 = PendingIntent.getActivity(MainActivity.this, 0, notIntent2, 0);
                nBuilder2.setContentIntent(contIntent2);
                //Para generar notificaciones en la barra de estado del sistema, lo primero que debemos hacer es
                //obtener una referencia al servicio de notificaciones de Android, a través de la clase
                // NotificationManager. Utilizaremos para ello el método getSystemService() indicando como
                //parámetro el identificador del servicio correspondiente, en este casoContext.NOTIFICATION_SERVICE
                NotificationManager nManager2 = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                nManager2.notify(0, nBuilder2.build());
                break;

        }
        return true;

    }

// metodo que carga una lista previamente guardada
   public void Cargar_Lista(){
       SharedPreferences preferencias= getSharedPreferences("ListaCompra",MODE_PRIVATE);
       listaElementos.clear();
       for(int i=0;i <= listaElementos.size();i++) {
           String productos_lista=preferencias.getString(String.valueOf(i),"productos_lista");
       }
       }



       // metodo que guarda una lista creada anteriormente
    public void Guardar_Lista (){
        ArrayList<String> productos_lista=new ArrayList<String>();
        SharedPreferences preferencias= getSharedPreferences("ListaCompra",MODE_PRIVATE);
        SharedPreferences.Editor miEditor=preferencias.edit();
        miEditor.clear();
        for(int i=0;i <= listaElementos.size();i++) {
            miEditor.putString(String.valueOf(i),valores.getText().toString());
            productos_lista.add(valores.getText().toString());
        }
        miEditor.commit();
    }

}

