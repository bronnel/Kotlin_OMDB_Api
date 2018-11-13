package com.ayala.manuel.kotlin_omdb_api


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_datos.view.*


class DatosAdapter(val items : ArrayList<Pelicula>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    // Obtiene el número de datos
    override fun getItemCount(): Int {
        return items.size
    }

    //infla el layout activity_datos
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_datos, parent, false))
    }

    // carga datos del ArrayList aL TEXTVIEW view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.tvDatosA?.text = items.get(position).Title
        holder?.tvDatosB?.text = items.get(position).Year
        holder?.tvDatosC?.text = "Imdb id: " + items.get(position).imdbID
        holder?.ivDatos?.loadUrl(items.get(position).Poster)
        holder?.itemView?.setOnClickListener(View.OnClickListener { Toast.makeText(context,  items.get(position).imdbID, Toast.LENGTH_SHORT).show() })
    }
    fun ImageView.loadUrl(url: String) {
        Picasso.with(context).load(url).into(this)
    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Mantiene el TextView
    val tvDatosA = view.textViewTitulo
    val tvDatosB = view.textViewAnno
    val tvDatosC = view.textViewId
    val ivDatos= view.ivDato



}
