package com.kdh.eatwd.presenter.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.util.*

class LocationUtil(private val context: Context) : LocationListener {

    //Location Manager는 시스템 위치 서비스에 접근을 제공하는 클래스.
    private var locationManager: LocationManager? = null

    //Location 클래스는 위도, 경도, 고도와 같이 위치에 관련된 정보를 가지고 있는 데이터 클래스.
    private var location: Location? = null
    private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 10F
    private val MIN_TIME_BW_UPDATES = (1000 * 60 * 1).toLong()

    init{
        fetchLocationInfo()
    }

    private fun fetchLocationInfo(): Location? {
        try {
            locationManager =context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            var gpsLocation: Location? = null
            var networkLocation: Location? = null

            val isGPSEnabled: Boolean =
                locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled: Boolean =
                locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (!isGPSEnabled && !isNetworkEnabled) {  
                return null
            } else {
                val hasFineLocationPermission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION // ACCESS_COARSE_LOCATION 보다 더 정밀한 위치 정보를 얻을 수 있습니다.
                )
                val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION // 도시 Block 단위의 정밀도의 위치 정보를 얻을 수 있습니다.
                )
                //만약 위 두 개 권한 없다면 null을 반환합니다.
                if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED ||
                    hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED
                ) return null

                if (isGPSEnabled) {
//                    locationManager?.requestLocationUpdates()
                    locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    gpsLocation = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                }
                if (isNetworkEnabled) {
                    locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    networkLocation = locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                }

                if (gpsLocation != null && networkLocation != null) {
                    if (gpsLocation.accuracy > networkLocation.accuracy) {
                        location = gpsLocation
                    } else {
                        location = networkLocation
                    }
                }else{
                    if(gpsLocation != null) location = gpsLocation
                    if(networkLocation != null) location = networkLocation
                }

            }


        } catch (e: Exception) {

        }
        return location
    }

    fun getCurrentAddress(context : Context,latitude : Double,longitude : Double) : Address? {
        val geocoder = Geocoder(context, Locale.getDefault())

        val addresses : List<Address> = geocoder.getFromLocation(latitude,longitude,10)

        if(addresses.isEmpty()){
            Toast.makeText(context,"해당 위치에 주소가 없습니다.",Toast.LENGTH_SHORT).show()
            return null
        }

        return addresses[0]
    }

    fun getLocationLatitude() : Double{
        return location?.latitude ?: 0.0
    }

    fun getLocationLongitude() : Double{
        return location?.longitude ?: 0.0
    }

    override fun onLocationChanged(location: Location) {
        Log.d("dodo55 ","onLocationChanged")
    }

}