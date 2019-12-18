package com.example.login

import android.content.Context
import android.content.Intent
import android.graphics.Movie
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.d10.Model.Photos
import com.example.login.ui.login.LoginActivity
import kotlinx.android.synthetic.main.layout_blog_list_item.view.*

class RecyclerAdapter(val context: Context) : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    var movieList : List<Photos> = listOf()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_blog_list_item,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.tvMovieName.text = movieList.get(position).title
        holder.blogid.text = movieList.get(position).id.toString()
        holder.blogthumbnail.text = movieList.get(position).thumbnailUrl
        val requestOptions = RequestOptions()
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
        Glide.with(context)
            .applyDefaultRequestOptions(requestOptions)
            .load(movieList.get(position).url)
            .into(holder.image)
        holder.btn.setOnClickListener{
            val intento1 = Intent(context, DatosItem::class.java)
            intento1.putExtra("id", movieList.get(position).id.toString())
            intento1.putExtra("accion", "editar")
            context.startActivity(intento1)


        }
        holder.itemView.setOnClickListener{
            val intento1 = Intent(context, DatosItem::class.java)
            intento1.putExtra("id", movieList.get(position).id.toString())
            intento1.putExtra("accion", "ver")
            context.startActivity(intento1)
            println("dato")

        }
    }

    fun setMovieListItems(movieList: List<Photos>){
        this.movieList = movieList;
        notifyDataSetChanged()
    }

    fun removeItem(viewHolder: MyViewHolder){



    }

    class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        val blogid: TextView = itemView!!.blog_id
        val blogthumbnail: TextView = itemView!!.blog_thumbnailUrl
        val tvMovieName: TextView = itemView!!.blog_title
        val image: ImageView = itemView!!.blog_image
        val btn: Button = itemView!!.btnEditar

    }
}