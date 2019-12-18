package com.example.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.d10.Interface.JsonPlacerHolderApi
import com.example.d10.Model.Photos
import kotlinx.android.synthetic.main.activity_datos_item.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DatosItem : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datos_item)
        val datos = this.intent.extras
        val id = datos?.getString("id")
        val accion = datos?.getString("accion")

        txtcodigo.setText(id)

        //Titulo y visibilidad
        if(accion.equals("ver")) {
            txtcodigo.setEnabled(false)
            txttitulo.setEnabled(false)
            btnEditar.setVisibility(View.GONE)
            txtTitulo.setText("Datos del Registro")
        }
        else{
            txtTitulo.setText("Datos para Editar")
        }


       // devolucion de datos
        val apiInterface = JsonPlacerHolderApi.create().getPostsItem(id.toString().toInt())

        //apiInterface.enqueue( Callback<List<Movie>>())
        apiInterface.enqueue( object : Callback<Photos> {
            override fun onResponse(call: Call<Photos>?, response: Response<Photos>?) {
                println("respuesta")
                if(response?.body() != null)
                    println(response.body()!!)
                    txttitulo.setText(response?.body()!!.title.toString())

                   // recyclerAdapter.setMovieListItems(response.body()!!)
                //listViewModel.setlistItems(response?.body()!!)
                Log.d("ae","aaa")
            }

            override fun onFailure(call: Call<Photos>?, t: Throwable?) {
                println("fallo")
            }
        })

        btnVolver.setOnClickListener{
            val intento1 = Intent(this, MainActivity::class.java)
            startActivity(intento1)

        }



    }
}
