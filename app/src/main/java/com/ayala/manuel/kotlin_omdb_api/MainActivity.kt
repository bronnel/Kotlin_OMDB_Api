package com.ayala.manuel.kotlin_omdb_api

//              ¡¡¡ Atención !!!
import com.ayala.manuel.kotlin_omdb_api.BuildConfig;
// Importar la variable donde esta almacenada la Api Key de OMDB

import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, FragmentPeliculas.OnFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

/*        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }*/

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.content_main, FragmentHome.newInstance("Nothing to do here"), "rageComicList")
                .commit()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

/*    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }*/

/*    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }*/

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.inicio -> {
//                Snackbar.make(nav_view, "Inicio", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show()
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.content_main, FragmentHome.newInstance("Nothing to do here"), "rageComicList")
                        .commit()
            }
            R.id.buscarPeliculas -> {
                if (Network.comprobarRed(this)) {
                    supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.content_main, FragmentPeliculas.newInstance("Nothing to do here"), "rageComicList")
                            .commit()
                } else {
                    //Mostrar mensaje de error
                    Snackbar.make(nav_view, R.string.not_internet, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                }
            }
            R.id.buscarSeries -> {
                if (Network.comprobarRed(this)) {
                    supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.content_main, FragmentSeries.newInstance("Nothing to do here"), "rageComicList")
                            .commit()
                } else {
                    //Mostrar mensaje de error
                    Snackbar.make(nav_view, R.string.not_internet, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                }
            }
            R.id.favoritos -> {
                if (Network.comprobarRed(this)) {
                    supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.content_main, FragmentFavoritos.newInstance("Nothing to do here"), "rageComicList")
                            .commit()
                } else {
                    //Mostrar mensaje de error
                    Snackbar.make(nav_view, R.string.not_internet, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                }
            }
            /*  R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }*/
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
