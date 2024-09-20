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
import org.osmdroid.util.BoundingBox
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
    private lateinit var startPoint:GeoPoint
    lateinit var mymap: MapView
     var marker: Marker?=null
    var res=""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mymap = db.map
        startPoint = GeoPoint(48.8583, 2.2944)

        if (savedInstanceState!=null){
            if (savedInstanceState.containsKey("res")&&savedInstanceState.containsKey("lat")&&savedInstanceState.containsKey("lon")){
            val la=savedInstanceState.getDouble("lat")
            val lo=savedInstanceState.getDouble("lon")
            res=savedInstanceState.getString("res","")
            startPoint=GeoPoint(la, lo)
            marker=Marker(mymap)
        }
        }
        requireActivity().getSharedPreferences(map, Context.MODE_PRIVATE).edit().clear().apply()
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = requireContext().getSharedPreferences(map, Context.MODE_PRIVATE)
        Configuration.getInstance().load(requireContext(), sharedPreferences)
        if (marker==null){
            set_invis(db.floatingActionButton2)
            set_invis(db.floatingActionButton3)
        }else{
            set_vis(db.floatingActionButton2)
            set_vis(db.floatingActionButton3)
            marker?.position = startPoint
            marker?.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            try {
                marker?.title = res
                Toast.makeText(requireContext(), res, Toast.LENGTH_LONG).show()
            }catch (e:Exception){
                Toast.makeText(requireContext(), "No connection", Toast.LENGTH_LONG).show()

            }
            mymap.overlays.add(marker)
        }
        mymap.setScrollableAreaLimitDouble(BoundingBox(85.0, 180.0, -85.0, -180.0))
        mymap.maxZoomLevel = 20.0;
        mymap.minZoomLevel = 3.0;
        mymap.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.DEFAULT_TILE_SOURCE)
        mymap.setMultiTouchControls(true)
        mymap.isHorizontalMapRepetitionEnabled = false
        mymap.isVerticalMapRepetitionEnabled = false
        mymap.controller.setZoom(3.0)
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
                try {
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
           findNavController().popBackStack()
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (marker!=null){
            outState.putString("res", marker!!.title)
            outState.putDouble("lat", marker!!.position.latitude)
            outState.putDouble("lon", marker!!.position.longitude)
        }
    }
}
