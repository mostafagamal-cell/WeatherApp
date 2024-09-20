package com.example.weatherapp.AppViews

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.weatherapp.databinding.FragmentMapBinding
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
    lateinit var marker: Marker
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireContext().getSharedPreferences(map, Context.MODE_PRIVATE)
        Configuration.getInstance().load(requireContext(), sharedPreferences)
        mymap = db.map
        mymap.setMultiTouchControls(true)
        val startPoint = GeoPoint(48.8583, 2.2944)
        mymap.controller.setZoom(15.0)
        mymap.controller.setCenter(startPoint)
        marker = Marker(mymap)
        marker.position = startPoint
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = "Eiffel Tower"
        mymap.overlays.add(marker)


        mymap.overlays.add(MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                Log.e("MapView", "normal click")
                return true
            }
            override fun longPressHelper(p: GeoPoint): Boolean {
                val latitude = p.latitude
                val longitude = p.longitude
                Toast.makeText(requireContext(), "Lat: $latitude, Lon: $longitude", Toast.LENGTH_LONG).show()
                marker = Marker(mymap)
                marker.position = p
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                marker.title = "Selected Location"
                mymap.overlays.add(marker)
                mymap.invalidate()
                return true
            }
        }))
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
