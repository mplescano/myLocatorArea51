package pe.mplescano.mobile.myapp.poc03.frontend.fragment;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import pe.mplescano.mobile.myapp.poc03.R;
import pe.mplescano.mobile.myapp.poc03.dao.iface.PointLocationDao;
import pe.mplescano.mobile.myapp.poc03.domain.PointLocation;
import pe.mplescano.mobile.myapp.poc03.ioc.BaseApplication;
import pe.mplescano.mobile.myapp.poc03.support.DialogMapFragment;
import pe.mplescano.mobile.myapp.poc03.support.Utils;
import pe.mplescano.mobile.myapp.poc03.support.ValidationHolderError;

/**
 * Created by mplescano on 14/10/2016.
 */
public class FormPointLocationFragment extends Fragment implements View.OnClickListener,
        GoogleMap.OnMapClickListener {

    private static final String ARG_POSITION = "position";
    private static final String ARG_POINT_LOCATION = "point_location";
    public static final String USER_DATE_FORMAT = "EEE MMM yyyy-MM-dd hh:mm a";

    @Inject
    PointLocationDao pointLocationDao;

    private int currentPosition = -1;

    private PointLocation pointLocation;

    private TextView txvTitle;

    private TextView txvDateCreation;

    private EditText edtDateCreation;

    private EditText edtAddress;

    private EditText edtDescription;

    private EditText edtPosition;

    private ImageButton imbPosition;

    private ImageButton imbSave;

    private List<ValidationHolderError> lstErrors;

    private View.OnClickListener mCallback;

    private DialogMapFragment mDialogMap;

    public static FormPointLocationFragment newInstance(int position, PointLocation pointLocation) {
        final FormPointLocationFragment fplf = new FormPointLocationFragment();
        if (position > -1) {
            final Bundle arguments = new Bundle();
            arguments.putInt(ARG_POSITION, position);
            arguments.putParcelable(ARG_POINT_LOCATION, pointLocation);
            fplf.setArguments(arguments);
        }

        return  fplf;
    }

    public void setCallback(View.OnClickListener mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            //restoring previous state
            pointLocation = savedInstanceState.getParcelable(ARG_POINT_LOCATION);
            currentPosition = savedInstanceState.getInt(ARG_POSITION);
        }

        //los argumentos tambien son persistidos y vueltos a recrear, cuidado
        //the arguments can only be set before to attach to its activity
        Bundle args = getArguments();
        if (args != null && !args.isEmpty()) {
            if (args.containsKey(ARG_POINT_LOCATION) &&
                    args.getParcelable(ARG_POINT_LOCATION) != null) {
                pointLocation = args.getParcelable(ARG_POINT_LOCATION);
            }
            if (args.containsKey(ARG_POSITION)) {
                currentPosition = args.getInt(ARG_POSITION);
            }
            args.clear();//limpiar para asegurar
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_form_point_location,
                container, false);

        txvTitle = (TextView) view.findViewById(R.id.txvTitle);
        edtDateCreation = (EditText) view.findViewById(R.id.edtDateCreation);
        edtAddress = (EditText) view.findViewById(R.id.edtAddress);
        edtDescription = (EditText) view.findViewById(R.id.edtDescription);
        edtPosition = (EditText) view.findViewById(R.id.edtPosition);
        imbPosition = (ImageButton) view.findViewById(R.id.imbPosition);
        imbSave = (ImageButton) view.findViewById(R.id.imbSave);

        imbPosition.setOnClickListener(this);
        imbSave.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((BaseApplication) getActivity().getApplication()).getMainComponent().inject(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (pointLocation == null) {
            pointLocation = new PointLocation();
        }
        updateFragmentView(currentPosition, pointLocation);
    }

    public void updateFragmentView(int position, PointLocation paramPointLocation) {
        if (paramPointLocation.getId() > 0) {
            txvTitle.setText(String.format(getString(R.string.lblTitleEditRecord),
                    paramPointLocation.getId()));
            edtDateCreation.setText(Utils.formatDate(paramPointLocation.getDateCreation(),
                    USER_DATE_FORMAT));
            edtAddress.setText(paramPointLocation.getAddress());
            edtDescription.setText(paramPointLocation.getDescription());
            edtPosition.setText(String.format(getString(R.string.lblPosition),
                    paramPointLocation.getLatitude(), paramPointLocation.getLongitude()));
        }
        else {
            txvTitle.setText(getString(R.string.lblTitleNewRecord));
            edtDateCreation.setText(Utils.formatDate(new Date(), USER_DATE_FORMAT));
            edtAddress.setText("");
            edtDescription.setText("");
            edtPosition.setText("");
        }
        imbSave.setTag(paramPointLocation);
        imbPosition.setTag(paramPointLocation);
        currentPosition = position;
        pointLocation = paramPointLocation;
    }

    public PointLocation getBeanView() {
        return pointLocation;
    }

    private boolean validateBeanView() {
        if (lstErrors == null) {
            lstErrors = new ArrayList<>();
        }
        else {
            for (ValidationHolderError objError : lstErrors) {
                objError.getErrorView().setError(null);
            }
            lstErrors.clear();
        }

        //idview, idmessage
        if (TextUtils.isEmpty(edtDateCreation.getText().toString())) {
            lstErrors.add(new ValidationHolderError(edtDateCreation.getId(), edtDateCreation,
                    R.string.errorEmptyDateCreation));
            edtDateCreation.setError("");
        }
        if (TextUtils.isEmpty(edtPosition.getText().toString())) {
            lstErrors.add(new ValidationHolderError(edtPosition.getId(), edtPosition,
                    R.string.errorEmptyPosition));
        }
        String[] positions = edtPosition.getText().toString().split(",");
        if (positions.length == 0 || positions.length == 1) {
            lstErrors.add(new ValidationHolderError(edtPosition.getId(), edtPosition,
                    R.string.errorSeparatedPositions));
        }

        return !(lstErrors.size() > 0);
    }

    private void updateBeanView() {
        if (pointLocation.getId() > 0) {
            pointLocation.setDateModification(new Date());
        }
        else {
            pointLocation.setDateCreation(Utils.parseDate(edtDateCreation.getText().toString(), USER_DATE_FORMAT));
        }

        pointLocation.setAddress(edtAddress.getText().toString());
        pointLocation.setDescription(edtDescription.getText().toString());

        String[] positions = edtPosition.getText().toString().split(",");
        pointLocation.setLatitude(Double.parseDouble(positions[0]));
        pointLocation.setLongitude(Double.parseDouble(positions[1]));
    }

    public boolean isEmptyFragmentView() {
        return (edtPosition == null || edtAddress == null ||
                (TextUtils.isEmpty(edtPosition.getText().toString()) &&
                TextUtils.isEmpty(edtAddress.getText().toString())));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(ARG_POINT_LOCATION, pointLocation);
        outState.putInt(ARG_POSITION, currentPosition);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imbSave:
                if (validateBeanView()) {
                    //update the bean
                    updateBeanView();
                    pointLocationDao.savePointLocation(pointLocation);
                    updateFragmentView(currentPosition, pointLocation);
                    Toast.makeText(getContext(), getString(R.string.successSavedPosition),
                            Toast.LENGTH_SHORT).show();
                    if (mCallback != null) {
                        mCallback.onClick(v);
                    }
                }
                else {
                    StringBuilder stMessages = new StringBuilder();
                    for (ValidationHolderError objError : lstErrors) {
                        String errorMessage = getString(objError.getMessageId());
                        objError.getErrorView().setError(errorMessage);
                        stMessages.append(errorMessage).append("\n");
                    }
                    Toast.makeText(getContext(), stMessages.toString(), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.imbPosition:
                /*if (mCallback != null) {
                    mCallback.onClick(v);
                }*/
                /*DialogMap2Fragment dialogMap = new DialogMap2Fragment();
                dialogMap.show(getFragmentManager(), "DIALOG_MAP");*/
                if (mDialogMap != null && mDialogMap.isVisible()) {
                    mDialogMap.dismiss();
                }
                mDialogMap = new DialogMapFragment();
                if (!TextUtils.isEmpty(edtPosition.getText().toString())) {
                    String[] arrPos = edtPosition.getText().toString().split(",");
                    LatLng latLngArg = new LatLng(Double.parseDouble(arrPos[0]),
                            Double.parseDouble(arrPos[1]));
                    Bundle args = new Bundle();
                    args.putParcelable(DialogMapFragment.ARG_CURRENT_POSITION, latLngArg);
                    mDialogMap.setArguments(args);
                }


                mDialogMap.show(getFragmentManager(), DialogMapFragment.DIALOG_MAP_FRAGMENT_TAG);
                mDialogMap.setCancelable(false);
                mDialogMap.setCallback(this);

                break;
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mDialogMap != null && mDialogMap.isVisible()) {
            mDialogMap.dismiss();
            mDialogMap = null;
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (edtPosition != null) {
            edtPosition.setText(String.format(getString(R.string.lblPosition),
                    latLng.latitude, latLng.longitude));
        }
        if (mDialogMap != null && mDialogMap.isVisible()) {
            mDialogMap.dismiss();
            mDialogMap = null;
        }
    }
}
