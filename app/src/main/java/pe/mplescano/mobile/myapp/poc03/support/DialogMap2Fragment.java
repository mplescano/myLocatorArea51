package pe.mplescano.mobile.myapp.poc03.support;

import android.app.DialogFragment;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import pe.mplescano.mobile.myapp.poc03.R;

/**
 * Created by mplescano on 31/10/2016.
 */
public class DialogMap2Fragment extends DialogFragment implements OnMapReadyCallback,
        GoogleMap.OnMyLocationChangeListener {

    private MapView mapView;

    private GoogleMap googleMap;

    private boolean cameraUpdated = false;

    public DialogMap2Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_map_2, container, false);

        mapView = (MapView) view.findViewById(R.id.mapDialogView);

        //getDialog().setTitle("");

        mapView.getMapAsync(this);

        return view;
    }

    /*public SupportMapFragment getFragment() {
        return fragment;
    }*/

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationChangeListener(this);
    }

    @Override
    public void onMyLocationChange(Location location) {

    }
}
