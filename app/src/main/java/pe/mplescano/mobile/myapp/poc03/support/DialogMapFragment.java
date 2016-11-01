package pe.mplescano.mobile.myapp.poc03.support;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import pe.mplescano.mobile.myapp.poc03.R;

/**
 * Created by mplescano on 31/10/2016.
 */
public class DialogMapFragment extends DialogFragment implements OnMapReadyCallback,
        GoogleMap.OnMyLocationChangeListener, View.OnClickListener,
        GoogleMap.OnMapClickListener {

    public static final String  ARG_CURRENT_POSITION = "current_position";

    public static final String  DIALOG_MAP_FRAGMENT_TAG = "dialog_map";

    private SupportMapFragment fragment;

    private GoogleMap googleMap;

    private boolean cameraUpdated = false;

    private Button btnCancel;

    private LatLng latLngArg;

    private GoogleMap.OnMapClickListener mCallback;

    public DialogMapFragment() {
        fragment = new SupportMapFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_map, container, false);
        getDialog().setTitle(R.string.lblSelectPosition);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.frlMapView, fragment).commit();

        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);

        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey(ARG_CURRENT_POSITION)) {
                latLngArg = args.getParcelable(ARG_CURRENT_POSITION);
            }
            args.clear();
        }

        fragment.getMapAsync(this);
        fragment.setHasOptionsMenu(true);
        return view;
    }

    /*public SupportMapFragment getFragment() {
        return fragment;
    }*/

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMyLocationEnabled(true);
        if (latLngArg == null) {
            googleMap.setOnMyLocationChangeListener(this);
        }
        else {
            moveToLocation(latLngArg, true);
            googleMap.addMarker(new MarkerOptions().position(latLngArg));
        }

        googleMap.setOnMapClickListener(this);
    }

    @Override
    public void onMyLocationChange(Location location) {
        if (!cameraUpdated) {
            moveToLocation(location, true);
            cameraUpdated = true;
        }
    }

    private void moveToLocation(final Location location, boolean animate) {
        final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        moveToLocation(latLng, animate);
    }

    private void moveToLocation(final LatLng latLng, boolean animate) {
        final CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(15)
                /*.bearing(70)*/
                /*.tilt(40)*/
                .build();
        final CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        if (animate) {
            googleMap.animateCamera(cameraUpdate, 5000, null);
        } else {
            googleMap.moveCamera(cameraUpdate);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel:
                dismiss();
                break;
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (mCallback != null) {
            mCallback.onMapClick(latLng);
        }
    }

    public void setCallback(GoogleMap.OnMapClickListener callback) {
        this.mCallback = callback;
    }
}
