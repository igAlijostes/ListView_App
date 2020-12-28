package com.example.listview_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;
import java.util.List;

 class customizar_adapter extends ArrayAdapter<String> {


     // // creamos el adaptador para mostrar nuestra lista creada a nuestro gusto en el xml "customizar filas" y lo asciamos a
     // nuestro ArrayList
     public customizar_adapter(@NonNull Context context, ArrayList<String> listaElementos) {
         super(context,R.layout.customizar_filas, listaElementos);
     }

     @NonNull
     // inflamos nuestra lista xml creada y le asociamos la imagen y el texto con su diseño previamente creado por
     // nosotros mediante el metodo "getView"
     @Override
     public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
         LayoutInflater inflater=LayoutInflater.from(getContext());
         View custom_vista=inflater.inflate(R.layout.customizar_filas,parent,false);

         String single_compra_item = getItem(position);
         TextView item_text=(TextView) custom_vista.findViewById(R.id.texto_lista);
         ImageView item_img= (ImageView) custom_vista.findViewById(R.id.imagen_lista);

         item_text.setText(single_compra_item);
         item_img.setImageResource(R.drawable.carritodecompras);
         return custom_vista;
     }
 }
