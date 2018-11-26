package com.ayala.manuel.kotlin_omdb_api

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_favoritos.*


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
class FragmentFavoritos : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var listener: OnFragmentInteractionListener? = null

    var datos: ArrayList<Favoritos> = ArrayList()
    var datosFavoritos: FavoritosArray = FavoritosArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favoritos, container, false)
    }

    override fun onResume() {
        super.onResume()
        buscarFavoritos()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerViewFragmentFavoritos.layoutManager = LinearLayoutManager(context)
        recyclerViewFragmentFavoritos.layoutManager = GridLayoutManager(context, 1)
        recyclerViewFragmentFavoritos.adapter = DatosAdapterFavoritos(datos, context!!)
        buscarFavoritos()
    }


    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    fun buscarFavoritos() {
        datos = ArrayList()
        recyclerViewFragmentFavoritos.adapter = DatosAdapterSeries(ArrayList(), context!!)
        solicitudHTTPVolleyGet("http://iesayala.ddns.net/jaidis/omdb-favoritos.php")
    }

    fun addDatos(lista: FavoritosArray) {
        datos = ArrayList()
        for (item in lista.favoritos!!.iterator()) {
            var favoritos: Favoritos = Favoritos(item.Title, item.Year, item.imdbID, item.Type, item.Poster)
            datos.add(favoritos)
        }
        recyclerViewFragmentFavoritos.adapter = DatosAdapterFavoritos(datos, context!!)
    }

    /*
    Libreria para la petición HTTP Volley
    */

    fun solicitudHTTPVolleyGet(url: String) {
        var respuestaJson = ""
        val queue = Volley.newRequestQueue(getActivity()?.getApplicationContext())
        val solicitud = StringRequest(Request.Method.GET, url, Response.Listener<String> { response ->
            try {
//                Log.d("resultado", response)
                respuestaJson = response.toString()
                val gson = Gson()
                datosFavoritos = gson.fromJson(respuestaJson, FavoritosArray::class.java)
                addDatos(datosFavoritos)

            } catch (e: Exception) {
                Log.d("solicitudHTTPVolley", e.toString())
            }
        }, Response.ErrorListener { })
        queue.add(solicitud)
    }


//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        if (context is OnFragmentInteractionListener) {
//            listener = context
//        } else {
//            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
//        }
//    }

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
                FragmentFavoritos().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                    }
                }
    }
}
