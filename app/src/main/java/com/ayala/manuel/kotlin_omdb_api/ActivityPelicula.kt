package com.ayala.manuel.kotlin_omdb_api

import android.content.Intent
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_pelicula.*
import org.json.JSONObject

class ActivityPelicula : AppCompatActivity() {

    var favorito: Boolean = false

    var type = ""
    var poster = ""
    var imdbid = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pelicula)

        val bundle: Bundle? = intent.extras

        if (bundle != null) {
            val imdb = bundle.getString("imdb")
//            Log.d("PELICULA-EXTENDIDA", imdb)
            imdbid = imdb
            VolleyGet("http://www.omdbapi.com/?apikey=" + BuildConfig.API_KEY + "&i=" + imdb + "&plot=full")
            VolleyGetId("http://iesayala.ddns.net/jaidis/omdb-favoritos.php?id=" + imdbid)
        }

        saveFavorite.setOnClickListener { setButton() }

        //Realizar petición a la base de datos para comprobar que el item esta guardado en favoritos
    }

    override fun onBackPressed() {
        super.onBackPressed()
//        val i = Intent(Intent.ACTION_MAIN)
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        finish()
    }

    fun vista(peliculaExtendida: PeliculaExtendida) {
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
        type = peliculaExtendida.Type
        poster = peliculaExtendida.Poster
    }

    fun ImageView.loadUrl(url: String) {
        var imagen = url
        if (imagen.equals("N/A")) {
            poster = "N/A"
            imagen = "http://iesayala.ddns.net/jaidis/not-found.jpg"
        } else {
            poster = imagen
            imagen = imagen.replace("http://", "https://")
        }

        Picasso.with(context).load(imagen).into(this)
    }

    fun setButton() {
        if (favorito) {
            saveFavorite.setImageResource(android.R.drawable.btn_star_big_off)
            favorito = false
            VolleyPost()
        } else {
            saveFavorite.setImageResource(android.R.drawable.btn_star_big_on)
            favorito = true
            VolleyPost()
        }
    }
    /*
    Libreria para la petición HTTP Volley
    */

    fun VolleyGet(url: String) {
        var respuestaJson = ""
        val queue = Volley.newRequestQueue(this)
        val solicitud = StringRequest(Request.Method.GET, url, Response.Listener<String> { response ->
            try {
//                Log.d("resultado", response)
                respuestaJson = response.toString()
                val gson = Gson()
                val x = gson.fromJson(respuestaJson, PeliculaExtendida::class.java)
                vista(x)

            } catch (e: Exception) {
                Log.d("VolleyGet", e.toString())
            }
        }, Response.ErrorListener { })
        queue.add(solicitud)
    }

    fun VolleyGetId(url: String) {
        val queue = Volley.newRequestQueue(this)
        val solicitud = StringRequest(Request.Method.GET, url, Response.Listener<String> { response ->
            try {
                val x: JSONObject = JSONObject(response)
                if (x["data"].equals("yes")) {
                    favorito = true
                    saveFavorite.setImageResource(android.R.drawable.btn_star_big_on)
                }

            } catch (e: Exception) {
                Log.d("VolleyGet", e.toString())
            }
        }, Response.ErrorListener { })
        queue.add(solicitud)
    }

    fun VolleyPost() {
        val queue = Volley.newRequestQueue(this)
        val url = "http://iesayala.ddns.net/jaidis/omdb-favoritos.php"
        val solicitud = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { response ->
            try {
                Log.d("Serverput", response)
                val x: JSONObject = JSONObject(response)
                Log.d("Serverput", x["data"].toString())

            } catch (e: Exception) {
                Log.d("VolleyGet", e.toString())
            }

        }, Response.ErrorListener { }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("title", movie_title.text.toString())
                params.put("year", movie_year.text.toString())
                params.put("imdbID", imdbid)
                params.put("type", type)
                params.put("poster", poster)
                if (favorito)
                    params.put("favorite", "yes")
                else
                    params.put("favorite", "no")

                return params
            }
        }

        queue.add(solicitud)
    }

}
