package com.example.weatherapp.AppViews

import android.content.Context
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.databinding.FragmentMapBinding
import com.example.weatherapp.lat
import com.example.weatherapp.longite
import com.example.weatherapp.map
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker


class MapFragment : Fragment() {
    lateinit var db: FragmentMapBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        db = FragmentMapBinding.inflate(layoutInflater)
        return db.root
    }

    lateinit var mymap: MapView
     var marker: Marker?=null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = requireContext().getSharedPreferences(map, Context.MODE_PRIVATE)
        Configuration.getInstance().load(requireContext(), sharedPreferences)
        mymap = db.map
        mymap.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.HIKEBIKEMAP)
        mymap.setMultiTouchControls(true)
        val startPoint = GeoPoint(48.8583, 2.2944)
        mymap.controller.setZoom(15.0)
        mymap.controller.setCenter(startPoint)
        mymap.overlays.add(MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                Log.e("MapView", "normal click")
                return true
            }
            override fun longPressHelper(p: GeoPoint): Boolean {
                val latitude = p.latitude
                val longitude = p.longitude
                val geocoder= Geocoder(requireContext())
                mymap.overlays.remove(marker)
                marker = Marker(mymap)
                marker?.position = p
                marker?.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                var res=""
                try {
                   val x= geocoder.getFromLocation(latitude,longitude,1)?.get(0)
                    res= geocoder.getFromLocation(latitude,longitude,1)?.get(0)?.getAddressLine(0)?:""
                    marker?.title = res
                    Toast.makeText(requireContext(), res, Toast.LENGTH_LONG).show()
                }catch (e:Exception){
                    Toast.makeText(requireContext(), "No connection", Toast.LENGTH_LONG).show()

                }
                mymap.overlays.add(marker)
                mymap.invalidate()
                set_vis(db.floatingActionButton2)
                set_vis(db.floatingActionButton3)
                return true
            }
        }))
       db.floatingActionButton2.setOnClickListener {
           val ysharedPreferences = requireContext().getSharedPreferences(map, Context.MODE_PRIVATE)
           val editor = ysharedPreferences.edit()
           val mylat=marker?.position?.latitude?.toFloat()
           val mylong=marker?.position?.latitude?.toFloat()
           editor.putFloat(lat,mylat!!)
           editor.putFloat(longite,mylong!!)
           editor.apply()
           findNavController().navigateUp()
       }
       db.floatingActionButton3.setOnClickListener {
            set_invis(db.floatingActionButton2)
            set_invis(db.floatingActionButton3)
            mymap.overlays.remove(marker)
            mymap.invalidate()
            marker=null
       }
    }
    private fun set_vis(view:View){
        view.isEnabled=true
        view.visibility=View.VISIBLE
    }
    private fun set_invis(view:View){
        view.isEnabled=false
        view.visibility=View.INVISIBLE
    }

    override fun onResume() {
        super.onResume()
        mymap.onResume() // Needed for osmdroid lifecycle
    }

    override fun onPause() {
        super.onPause()
        mymap.onPause() // Needed for osmdroid lifecycle
    }
}
