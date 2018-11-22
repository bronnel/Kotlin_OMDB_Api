package com.ayala.manuel.kotlin_omdb_api

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_pelicula.*

class ActivityPelicula : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pelicula)

        val bundle: Bundle? = intent.extras

        if (bundle != null) {
            val imdb = bundle.getString("imdb")
            Log.d("PELICULA-EXTENDIDA", imdb)
            solicitudHTTPVolley(prepararUrl("pelicula", "", 1, imdb))
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
//        val i = Intent(Intent.ACTION_MAIN)
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        finish()
    }

    fun vista(peliculaExtendida: PeliculaExtendida){
        movie_poster.loadUrl(peliculaExtendida.Poster)
        movie_title.text = peliculaExtendida.Title
        movie_year.text = peliculaExtendida.Year
        movie_runtime.text = peliculaExtendida.Runtime
        movie_genre.text = peliculaExtendida.Genre
        movie_director.text = peliculaExtendida.Director
        movie_writer.text = peliculaExtendida.Writer
        movie_actors.text = peliculaExtendida.Actors
        movie_awards.text = peliculaExtendida.Awards
        movie_metascore.text = peliculaExtendida.Metascore
        movie_plot.text = peliculaExtendida.Plot
    }

    fun ImageView.loadUrl(url: String) {
        var imagen = url
        if (imagen.equals("N/A"))
            imagen = "http://iesayala.ddns.net/jaidis/not-found.jpg"
        else
            imagen = imagen.replace("http://", "https://")
        Picasso.with(context).load(imagen).into(this)
    }

    /*
    Libreria para la petición HTTP Volley
    */

    fun solicitudHTTPVolley(url: String) {
        var respuestaJson = ""
        val queue = Volley.newRequestQueue(this)
        val solicitud = StringRequest(Request.Method.GET, url, Response.Listener<String> { response ->
            try {
                Log.d("resultado", response)
                respuestaJson = response.toString()
                val gson = Gson()
                val x = gson.fromJson(respuestaJson, PeliculaExtendida::class.java)
                vista(x)

            } catch (e: Exception) {
                Log.d("solicitudHTTPVolley", e.toString())
            }
        }, Response.ErrorListener { })
        queue.add(solicitud)
    }

    fun prepararUrl(tipoPeticion: String = "", busqueda: String = "", pagina: Int = 1, imdb: String = ""): String {
        var url: String
        var busquedaShadow = busqueda
        if (tipoPeticion.equals("listado")) {
            busquedaShadow = busquedaShadow.replace(" ", "+")
            url = "http://iesayala.ddns.net/jaidis/omdb-movie.php?s=" + busquedaShadow + "&page=" + pagina
        } else {
            url = "http://www.omdbapi.com/?apikey=" + BuildConfig.API_KEY + "&i=" + imdb + "&plot=full"
        }
        return url
    }

}
