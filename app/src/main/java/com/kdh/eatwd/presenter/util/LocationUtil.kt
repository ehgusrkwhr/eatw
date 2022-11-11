package com.kdh.eatwd.presenter.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat

class LocationUtil(private val context: Context) {

    //Location Manager는 시스템 위치 서비스에 접근을 제공하는 클래스.
    private val locationManager: LocationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    //Location 클래스는 위도, 경도, 고도와 같이 위치에 관련된 정보를 가지고 있는 데이터 클래스.
    private var location: Location? = null

    private fun fetchLocationInfo(): Location? {
        try {
            var gpsLocation: Location? = null
            var networkLocation: Location? = null

            val isGPSEnabled: Boolean =
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled: Boolean =
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

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
                    gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                }
                if (isNetworkEnabled) {
                    networkLocation =
                        locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                }

                if (gpsLocation != null && networkLocation != null) {
                    return if (gpsLocation.accuracy > networkLocation.accuracy) {
                        gpsLocation
                    } else {
                        networkLocation
                    }
                }else{
                    if(gpsLocation != null) return gpsLocation
                    if(networkLocation != null) return networkLocation
                }

            }


        } catch (e: Exception) {

        }
        return null
    }

    fun getLocationLatitude() : Double{
        return location?.latitude ?: 0.0
    }

    fun getLocationLongitude() : Double{
        return location?.longitude ?: 0.0
    }

}