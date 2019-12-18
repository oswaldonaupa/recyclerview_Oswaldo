package com.example.login

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.d10.Interface.JsonPlacerHolderApi
import com.example.d10.Model.Photos
import com.example.login.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_blog_list_item.*
import kotlinx.android.synthetic.main.layout_blog_list_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var listViewModel: ListViewModel



    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter: RecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loaddatos()
        swipeejemplo.setOnRefreshListener {
            // refresh your list contents somehow
            loaddatos()

            swipeejemplo.isRefreshing = false   // reset the SwipeRefreshLayout (stop the loading spinner)
        }


        listViewModel = ViewModelProviders.of(this)[ListViewModel::class.java]

        listViewModel.getlistItems().observe(this, Observer<List<Photos>> { lista->
            recyclerAdapter.setMovieListItems(lista)
        })

    }

    fun loaddatos(){
        recyclerView = findViewById(R.id.recycler_viwe)
        recycler_viwe.apply{val topSpacingItemDecoration = TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingItemDecoration)}

        recyclerAdapter = RecyclerAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = recyclerAdapter



        val itemTouchHelperCallback = object :ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {
                println("eliminado")
                println(viewHolder.itemView.blog_id.text)
                val dialogBuilder = AlertDialog.Builder(this@MainActivity)

                // set message of alert dialog
                dialogBuilder.setMessage("Desea eliminar el registro?")
                    // if the dialog is cancelable
                    .setCancelable(false)
                    // positive button text and action
                    .setPositiveButton("Eliminar", DialogInterface.OnClickListener {
                            dialog, id -> eliminar(viewHolder.itemView.blog_id.text.toString().toInt())





                    })
                    // negative button text and action
                    .setNegativeButton("Cancelar", DialogInterface.OnClickListener {
                            dialog, id -> dialog.cancel()
                    })

                // create dialog box
                val alert = dialogBuilder.create()
                // set title for alert dialog box
                alert.setTitle("Cuadro de confirmacion")
                // show alert dialog
                alert.show()
            }

        }
        //val itemTouchHelperCallback2 = object :ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT ){
          //  override fun onMove(
            //    recyclerView: RecyclerView,
              //  viewHolder: RecyclerView.ViewHolder,
                //target: RecyclerView.ViewHolder
            //): Boolean {
            //    return false
            //}

            //override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {
              //  println("editar")
            //}

        //}


        val myHelper = ItemTouchHelper(itemTouchHelperCallback)
        myHelper.attachToRecyclerView(recyclerView)
        //val myHelper2 = ItemTouchHelper(itemTouchHelperCallback2)
        //myHelper2.attachToRecyclerView(recyclerView)

        val apiInterface = JsonPlacerHolderApi.create().getPosts()

        //apiInterface.enqueue( Callback<List<Movie>>())
        apiInterface.enqueue( object : Callback<List<Photos>> {
            override fun onResponse(call: Call<List<Photos>>?, response: Response<List<Photos>>?) {

                if(response?.body() != null)
                    recyclerAdapter.setMovieListItems(response.body()!!)
                listViewModel.setlistItems(response?.body()!!)
                Log.d("ae","aaa")
            }

            override fun onFailure(call: Call<List<Photos>>?, t: Throwable?) {

            }
        })
    }
    fun eliminar(id:Int){
        val apiInterface = JsonPlacerHolderApi.create().deletePost(id)

        apiInterface.enqueue( object : Callback<List<Photos>> {
            override fun onResponse(call: Call<List<Photos>>?, response: Response<List<Photos>>?) {
                println("elimnado correctamente")
                if(response?.body() != null)
                    println("elimnado correctamente")

            }

            override fun onFailure(call: Call<List<Photos>>?, t: Throwable?) {
                println("elimnado correctamente")
            }
        })
    }
}