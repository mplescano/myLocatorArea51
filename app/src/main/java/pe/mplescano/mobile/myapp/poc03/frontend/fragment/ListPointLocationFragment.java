package pe.mplescano.mobile.myapp.poc03.frontend.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import pe.mplescano.mobile.myapp.poc03.R;
import pe.mplescano.mobile.myapp.poc03.dao.PointLocationEntityDao;
import pe.mplescano.mobile.myapp.poc03.dao.iface.PointLocationDao;
import pe.mplescano.mobile.myapp.poc03.domain.ImplLabeledEntity;
import pe.mplescano.mobile.myapp.poc03.frontend.cursor.AbsEntityListAdapter;
import pe.mplescano.mobile.myapp.poc03.frontend.cursor.ImplPositionCursor;
import pe.mplescano.mobile.myapp.poc03.ioc.BaseApplication;
import pe.mplescano.mobile.myapp.poc03.support.Utils;

/**
 * Created by mplescano on 14/10/2016.
 */
public class ListPointLocationFragment extends ListFragment {

    private static final String ARG_PARAMETERS = "parameters";

    private static final String ARG_ITEMS_BY_PAGE = "itemsByPage";

    private Map<String, Object> mpParameters = new HashMap<>();

    private int itemsByPage = 10;

    @Inject
    PointLocationDao pointLocationDao;

    private AdapterView.OnItemClickListener mCallback;

    public void setCallback(AdapterView.OnItemClickListener mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mpParameters = (Map<String, Object>) savedInstanceState.getSerializable(ARG_PARAMETERS);
            itemsByPage = savedInstanceState.getInt(ARG_ITEMS_BY_PAGE);
        }

        Bundle args = getArguments();
        if (args != null && !args.isEmpty()) {
            if (args.containsKey(ARG_PARAMETERS) && args.get(ARG_PARAMETERS) != null) {
                mpParameters = (Map<String, Object>) args.get(ARG_PARAMETERS);
            }
            if (args.getInt(ARG_ITEMS_BY_PAGE) > 0) {
                itemsByPage = args.getInt(ARG_ITEMS_BY_PAGE);
            }
            args.clear();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((BaseApplication) getActivity().getApplication()).getMainComponent().inject(this);

        // When in two-pane or one-pane layout, set the listview to highlight all
        // the selected list items
        //it has to set the mode before to set the adapter
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        setListAdapter(new AbsEntityListAdapter<ImplLabeledEntity>(getContext(),
                R.layout.fragment_item_view_point_location,
                getImplPositionCursor(mpParameters, itemsByPage)) {
            @NonNull
            @Override
            protected View createViewFromResource(@NonNull LayoutInflater inflater, final int position,
                                                  @Nullable View convertView,
                                                  @NonNull final AbsListView parentListView, int resource) {
                final View view;
                final ImplLabeledEntity item = getItem(position);

                if (convertView == null) {
                    view = inflater.inflate(resource, parentListView, false);
                    view.setTag(R.id.chkSelect, view.findViewById(R.id.chkSelect));
                    view.setTag(R.id.txvTitle, view.findViewById(R.id.txvTitle));
                    view.setTag(R.id.txvDetail01, view.findViewById(R.id.txvDetail01));
                    view.setTag(R.id.btnEdit, view.findViewById(R.id.btnEdit));
                }
                else {
                    view = convertView;
                }

                TextView txvTitle = (TextView) view.getTag(R.id.txvTitle);
                TextView txvDetail01 = (TextView) view.getTag(R.id.txvDetail01);
                CheckBox chkSelect = (CheckBox) view.getTag(R.id.chkSelect);
                chkSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        parentListView.setItemChecked(position, isChecked);
                    }
                });
                ImageButton btnEdit = (ImageButton) view.getTag(R.id.btnEdit);
                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCallback.onItemClick(parentListView, view, position, item.getId());
                    }
                });

                txvTitle.setText(item.getLabel());
                txvDetail01.setText(item.getDetail01());
                chkSelect.setChecked(parentListView.isItemChecked(position));

                return view;
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();


    }

    private ImplPositionCursor getImplPositionCursor(Map<String, Object> mpParameters,
                                                     int itemsByPage) {
        return new ImplPositionCursor<>(mpParameters,
                new PointLocationEntityDao(pointLocationDao), itemsByPage);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(ARG_PARAMETERS, (HashMap) mpParameters);
        outState.putInt(ARG_ITEMS_BY_PAGE, itemsByPage);
    }

    public void refresh() {
        ((AbsEntityListAdapter) getListAdapter()).getPositionCursor().update();
        ((AbsEntityListAdapter) getListAdapter()).notifyDataSetChanged();
    }

    public int deleteItems(long[] arrIds) {
        int deletedPointLocations = 0;
        if (arrIds != null) {
            for (long pointLocationId : arrIds) {
                pointLocationDao.deletePointLocation(pointLocationId);
                deletedPointLocations++;
            }
            refresh();
            Toast.makeText(getContext(), "Deleted " + deletedPointLocations + " records",
                    Toast.LENGTH_SHORT).show();
        }
        return deletedPointLocations;
    }
}
