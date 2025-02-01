package com.judamie_manager.map

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.LOCATION_SERVICE
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.judamie_manager.R
import com.judamie_manager.activity.ServiceActivity
import com.judamie_manager.databinding.FragmentPickupGoogleMapBinding
import com.judamie_manager.firebase.model.PickupLocationModel
import com.judamie_manager.firebase.service.PickupLocationService
import com.judamie_manager.ui.component.ConfirmDialogFragment
import com.judamie_manager.util.ServiceFragmentName
import com.judamie_manager.viewmodel.fragmentviewmodel.PickupGoogleMapViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale

class PickupGoogleMapFragment : Fragment() {

    lateinit var fragmentPickupGoogleMapBinding: FragmentPickupGoogleMapBinding
    lateinit var serviceActivity: ServiceActivity

    // 권한 확인을 위한 런처
    lateinit var permissionCheckLauncher: ActivityResultLauncher<Array<String>>

    // 위치 정보 관리 객체
    lateinit var locationManager: LocationManager
    // 위치 측정을 하면 반응하는 리스너
    lateinit var myLocationListener: MyLocationListener
    // 사용자의 현재 위치를 담을 변수
    var userLocation: Location? = null
    // 구글 지도 객체를 담을 변수를 선언한다.
    lateinit var mainGoogleMap: GoogleMap

    // 다이얼로그 열렸나 닫혔나 확인하는 변수
    private var isDialogOpen = false

//    // 임시 픽업지 주소 리스트
//    val addressList = listOf(
//        "03900, 서울 마포구 하늘공원로 84 (상암동)",
//        "59735, 전남 여수시 이순신광장로 5 (교동)",
//        "서울 서대문구 증가로8길 42-2 (남가좌동)",
//        "대전 대덕구 계족로 532"
//
//    )

    // 픽업지 리스트
    var pickupList = mutableListOf<PickupLocationModel>()
    // 픽업지 주소 리스트
    var addressList = mutableListOf<String>()
    // 픽업지 id
    var pickupLocDocumentID = mutableListOf<String>()
    // 서버에서 받아온 데이터를 담을 변수
    lateinit var pickupLocationModel: PickupLocationModel

    // 확인할 권한 목록
    val permissionList = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // 구글 지도에 표시되는 맵 이미지를 최신 버전 이미지로 사용하도록 설정한다.
        MapsInitializer.initialize(requireContext(), MapsInitializer.Renderer.LATEST, null)

        fragmentPickupGoogleMapBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pickup_google_map, container, false)
        fragmentPickupGoogleMapBinding.pickupGoogleMapViewModel = PickupGoogleMapViewModel(this@PickupGoogleMapFragment)
        fragmentPickupGoogleMapBinding.lifecycleOwner = this@PickupGoogleMapFragment

        serviceActivity = activity as ServiceActivity

        // 구글 지도를 설정하는 함수 호출
        settingGoogleMap()
        // 권한 확인을 위한 런처 생성 메서드 호출
        createPermissionCheckLauncher()
        // 툴바를 구성하는 메서드 호출
        settingToolbar()
        // 픽업지 데이터를 가져오는 메서드
        gettingPickupLocData()

        // 권한 확인을 위한 런처 가동
        permissionCheckLauncher.launch(permissionList)

//        // 위치 아이콘 클릭 이벤트 처리 (내 위치로,,,)
//        fragmentPickupGoogleMapBinding.root.findViewById<ImageView>(R.id.btnGetMyLocation).setOnClickListener {
//            getMyLocation()
//        }


        return fragmentPickupGoogleMapBinding.root
    }

    // 픽업지 데이터를 가져오는 메서드
    fun gettingPickupLocData(){
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                PickupLocationService.gettingPickupLocationList()
            }
            pickupList = work1.await()
            addressList = pickupList.map { it.pickupLocStreetAddress }.toMutableList()

            // 픽업지 ID 리스트 가져오기
            pickupLocDocumentID = pickupList.map { it.pickupLocDocumentID }.toMutableList()

        }
    }

    // 권한 확인을 위해 사용할 런처 생성
    fun createPermissionCheckLauncher() {
        // 런처를 등록한다.
        permissionCheckLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            // 모든 권한에 대해 확인한다.
            permissionList.forEach { permissionName ->
                // 현재 권한이 허용되어 있지 않다면 다이얼로그를 띄운다.
                if (result[permissionName] == false) {
                    // 설정 화면을 띄우는 메서드를 호출한다.
                    startSettingActivity()
                    // 함수 종료
                    return@registerForActivityResult
                }
            }
        }
    }

    // 다시 화면을 업데이트하는 메서드
    override fun onResume() {
        super.onResume()

        // 다이얼로그가 열려 있으면 위치 갱신을 막음
        if (isDialogOpen) {
            return
        }

        // 권한이 승인되었으면 지도와 위치 정보를 다시 갱신
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // 지도 초기화 및 위치 갱신
            settingGoogleMap()
        } else {
            // 권한이 없으면 권한 요청
            permissionCheckLauncher.launch(permissionList)
        }
    }

    // 애플리케이션의 설정화면을 실행시키는 메서드
    fun startSettingActivity() {
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
        materialAlertDialogBuilder.setTitle("권한 확인 요청")
        materialAlertDialogBuilder.setMessage("권한을 모두 허용해줘야 정상적은 서비스 이용이 가능합니다")
        materialAlertDialogBuilder.setPositiveButton("설정 화면으로 가기") { _, _ ->
            // 앱 설정 화면으로 이동
            val uri = Uri.fromParts("package", requireContext().packageName, null)
            val permissionIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri)
            permissionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(permissionIntent)
        }

        // 다이얼로그 띄우고, 앱이 종료되지 않도록
        materialAlertDialogBuilder.setCancelable(false)
        materialAlertDialogBuilder.show()
    }


    // 지도에 현재 위치를 표시하는 메서드
    fun setMyLocation(location: Location) {
        // 사용자의 현재 위치를 변수에 담아준다.
        userLocation = location
        // 현재 위치 측정을 중단한다.
        locationManager.removeUpdates(myLocationListener)

        // 위도와 경도를 관리하는 객체를 생성한다.
        val loc1 = LatLng(location.latitude, location.longitude)

        // 지도를 업데이트할 수 있는 객체를 생성한다.
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(loc1, 15.0f)
        mainGoogleMap.animateCamera(cameraUpdate)
    }

    // 현재 위치를 측정하는 메서드
    fun getMyLocation() {
        // 권한 허용 여부 확인
        val check1 = ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
        val check2 = ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)

        // 권한이 없다면 권한 요청
        if (check1 == PackageManager.PERMISSION_DENIED || check2 == PackageManager.PERMISSION_DENIED) {
            permissionCheckLauncher.launch(permissionList)
            return
        }

        // GPS 프로바이더 사용이 가능하다면
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0.0f, myLocationListener)
        }
        // Network 프로바이더 사용이 가능하다면
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0.0f, myLocationListener)
        }
    }

    // 구글 맵에 대한 설정을 하는 함수
    fun settingGoogleMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync {
            mainGoogleMap = it
            mainGoogleMap.uiSettings.isZoomControlsEnabled = true
            mainGoogleMap.mapType = GoogleMap.MAP_TYPE_NORMAL

            locationManager = requireContext().getSystemService(LOCATION_SERVICE) as LocationManager
            myLocationListener = MyLocationListener()

            // 권한 확인
            val check1 = ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            val check2 = ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)

            if (check1 == PackageManager.PERMISSION_DENIED || check2 == PackageManager.PERMISSION_DENIED) {
                permissionCheckLauncher.launch(permissionList)
                return@getMapAsync
            }

            val gpsSavedLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val networkSavedLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            val passiveSavedLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)

            if (gpsSavedLocation != null) {
                setMyLocation(gpsSavedLocation)
            } else if (networkSavedLocation != null) {
                setMyLocation(networkSavedLocation)
            } else if (passiveSavedLocation != null) {
                setMyLocation(passiveSavedLocation)
            }

            // 현재 위치 측정을 시작한다.
            // getMyLocation()

//            // 주소 리스트의 모든 주소를 처리
//            addressList.forEach { address ->
//                addMarker(address)
//            }




            // 주소 리스트와 픽업지 ID 리스트의 모든 항목을 처리
            for (i in addressList.indices) {
                val address = addressList[i]
                val documentID = pickupLocDocumentID[i]
                addMarker(address, documentID)
            }
        }
    }

    // 지도에 마커 추가하는 메서드
    fun addMarker(address: String, documentID: String) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            // 주소를 위도와 경도로 변환
            val addressList = geocoder.getFromLocationName(address, 1)
            if (addressList != null && addressList.isNotEmpty()) {
                val location = addressList[0]
                val latLng = LatLng(location.latitude, location.longitude)

                val markerOptions = MarkerOptions()
                    .position(latLng)
                    .title(address)

                // mainGoogleMap.addMarker(markerOptions)
                val marker = mainGoogleMap.addMarker(markerOptions)
                // 마커에 문서 ID를 태그로 저장
                marker?.tag = documentID

                // 마커 클릭 리스너 설정
                setupMarkerClickListener(mainGoogleMap)


            } else {
                Log.e("Geocoder", "주소를 찾을 수 없습니다: $address")
            }
        } catch (e: IOException) {
            Log.e("Geocoder", "Geocoder 사용 중 오류 발생", e)
        }
    }

    // 위치 측정에 성공하면 동작하는 리스너
    inner class MyLocationListener : LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.d("test100", "위도 : ${location.latitude}, 경도 : ${location.longitude}")
            setMyLocation(location)
        }
    }

    // 마커 클릭하면 다이얼로그 띄워주는 메서드
    fun setupMarkerClickListener(mainGoogleMap: GoogleMap) {
        mainGoogleMap.setOnMarkerClickListener { marker ->
            // 클릭된 마커에 대한 작업 수행
            onMarkerClick(marker)
            true // true로 반환하면 기본 동작(카메라 이동)을 막습니다.
        }
    }

    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment() {
        serviceActivity.removeFragment(ServiceFragmentName.PICKUP_GOOGLE_MAP_FRAGMENT)
    }

    // 툴바를 구성하는 메서드
    fun settingToolbar() {
        fragmentPickupGoogleMapBinding.pickupGoogleMapViewModel?.toolbarGoogleMapTitle?.value = "픽업지 지도보기"
        fragmentPickupGoogleMapBinding.toolbarGoogleMap.inflateMenu(R.menu.menu_pickup_google_map_toolbar)
        fragmentPickupGoogleMapBinding.toolbarGoogleMap.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.btnGetMyLocation ->{
                    // 내 위치로
                    getMyLocation()
                }
            }
            true
        }
    }


    fun onMarkerClick(marker: Marker) {
        // 다이얼로그가 열리면 위치 갱신을 막음
        isDialogOpen = true

        // 마커에서 저장한 ID를 가져온다.
        val documentID = marker.tag as? String

        // 아이디를 통해 데이터를 가져온다
        if (documentID != null) {
            CoroutineScope(Dispatchers.Main).launch {
                val pickupLocationModel = withContext(Dispatchers.IO) {
                    // 비동기적으로 데이터 가져오기
                    PickupLocationService.selectPickupLocationDataOneById(documentID)
                }

                // 데이터를 정상적으로 받아왔는지 확인
                if (pickupLocationModel != null) {
                    // 다이얼로그에 띄울 변수들
                    val title = pickupLocationModel.pickupLocName
                    val context = pickupLocationModel.pickupLocInfomation
                    val phoneNumber = pickupLocationModel.pickupLocPhoneNumber

                    // 다이얼로그 생성 및 표시
                    val dialog = ConfirmDialogFragment(title, context, phoneNumber)
                    dialog.isCancelable = false
                    activity?.let { dialog.show(it.supportFragmentManager, "ConfirmDialog") }
                } else {
                    Log.e("PickupGoogleMapFragment", "Failed to load data for document ID: $documentID")
                }
            }
        }
    }

}
