package com.ayala.manuel.kotlin_omdb_api

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_peliculas.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [RecyclerFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [RecyclerFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class FragmentPeliculas : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var listener: OnFragmentInteractionListener? = null

    var datos: ArrayList<Pelicula> = ArrayList()
    var datosPeliculas: PeliculaArray = PeliculaArray()

    var contador: Int = 1
    var busqueda: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_peliculas, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //addDatos()
        recyclerViewFragment.layoutManager = LinearLayoutManager(context)
        recyclerViewFragment.layoutManager = GridLayoutManager(context, 1)
        recyclerViewFragment.adapter = DatosAdapter(datos, context!!)
        button_previous.setOnClickListener { botonMenosPeliculas() }
        button_next.setOnClickListener { botonMasPeliculas() }
        buttonSearch.setOnClickListener { botonBuscar() }
        editTextSearch.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER ) {
                //Perform Code
                botonBuscar()
                return@OnKeyListener true
            }
            false
        })
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    fun botonBuscar() {
        contador = 1
        busqueda = editTextSearch.text.toString()
        datos = ArrayList()
        recyclerViewFragment.adapter = DatosAdapter(ArrayList(), context!!)
        solicitudHTTPVolley(prepararUrl("listado", busqueda, contador, ""))
    }

    fun botonMenosPeliculas() {
        if (!busqueda.equals("")) {
            contador--
            if (contador <= 1)
                contador = 1
            solicitudHTTPVolley(prepararUrl("listado", busqueda, contador, ""))
        }
    }

    fun botonMasPeliculas() {
        if (!busqueda.equals("")) {
            contador++
            solicitudHTTPVolley(prepararUrl("listado", busqueda, contador, ""))
        }
    }

    fun addDatos(lista: PeliculaArray) {
        datos = ArrayList()
        for (item in lista.peliculas!!.iterator()) {
            var pelicula: Pelicula = Pelicula(item.Title, item.Year, item.imdbID, item.Type, item.Poster)
            datos.add(pelicula)
        }
        recyclerViewFragment.adapter = DatosAdapter(datos, context!!)
    }

    /*
    Libreria para la petición HTTP Volley
    */

    fun solicitudHTTPVolley(url: String) {
        var respuestaJson = ""
        val queue = Volley.newRequestQueue(getActivity()?.getApplicationContext())
        val solicitud = StringRequest(Request.Method.GET, url, Response.Listener<String> { response ->
            try {
//                Log.d("resultado", response)
                respuestaJson = response.toString()
                val gson = Gson()
                datosPeliculas = gson.fromJson(respuestaJson, PeliculaArray::class.java)
                addDatos(datosPeliculas)

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RecyclerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
                FragmentPeliculas().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                    }
                }
    }
}
