package mx.itesm.alertify;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import mx.itesm.alertify.R;

import static android.content.Context.LOCATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapaFrag extends Fragment implements OnMapReadyCallback, LocationListener {

    private ArrayList<Alerta> arrReportes; //Datos para la lista de reportes

    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;


    public MapaFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Mapa");
        arrReportes = new ArrayList<>();
    }

    private void descargarReportes(final GoogleMap googleMap) {

        mGoogleMap = googleMap;

        FirebaseDatabase bd = FirebaseDatabase.getInstance();
        DatabaseReference ruta = bd.getReference("/User/");
        Log.i("RUTA: ", ruta.toString());

        ruta.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrReportes.clear();

                for(DataSnapshot user: dataSnapshot.getChildren()) {
                    HashMap dUser = (HashMap) user.getValue();
                    Log.i("HASHMAP: ", dUser.toString());

                    Log.i("CHUCHO: ", dUser.get("Reportes")+"");

                    ArrayList<HashMap> reportes = (ArrayList<HashMap>) dUser.get("Reportes");
                    Log.i("REPORTE: ", reportes.get(0).get("titulo")+"");

                    for(int i=0; i<reportes.size(); i++){
                        String titulo = reportes.get(i).get("titulo").toString();
                        Double latitud = Double.parseDouble(reportes.get(i).get("latitud").toString());
                        Double longitud = Double.parseDouble(reportes.get(i).get("longitud").toString());
                        String fecha = reportes.get(i).get("fecha").toString();
                        Alerta nuevaAlerta = new Alerta(titulo, latitud, longitud, fecha);
                        arrReportes.add(nuevaAlerta);

                    }
                    for( Alerta s : arrReportes){
                        Log.i("ARR REPORTES: ", s.toString());
                    }

                }
                //Crear pins

                for(Alerta al: arrReportes){
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(al.getLatitud(), al.getLongitud())).title("ALERTA").snippet(al.getTitulo() + " - " + al.getFecha()));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_mapa, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) mView.findViewById(R.id.mapa);

        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());

        mGoogleMap = googleMap;

        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true); // false to disable

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true); // true to enable

        descargarReportes(googleMap);

        CameraPosition alerta1 = CameraPosition.builder().target(new LatLng(19.596813, -99.226647)).zoom(16).bearing(0).tilt(45).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(alerta1));
        ////

    }



    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
