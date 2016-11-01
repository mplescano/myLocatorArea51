package pe.mplescano.mobile.myapp.poc03.frontend.fragment.poc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import pe.mplescano.mobile.myapp.poc03.R;
import pe.mplescano.mobile.myapp.poc03.support.Utils;

/**
 * Created by mplescano on 03/10/2016.
 */
public class HeadlinesFragment extends ListFragment {

    final static String ARG_POSITION = "position";

    private OnHeadlineSelectedListener callback;

    private int currentPosition = -1;

    public static void updateInstance(final HeadlinesFragment headlinesFragment,
                                      int position) {
        final Bundle arguments = new Bundle();
        arguments.putInt(ARG_POSITION, position);
        headlinesFragment.setArguments(arguments);
    }

    public void setCallback(OnHeadlineSelectedListener mCallback) {
        this.callback = mCallback;
    }

    public interface OnHeadlineSelectedListener {
        void onArticleSelected(int position, Object item);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();


        setListAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_activated_1, Ipsum.Headlines));

        // When in two-pane layout, set the listview to highlight the selected list item
        // (We do this during onStart because at the point the listview is available.)
        if (Utils.isTwoPane(getResources(), R.string.screen_size)) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }

        //los argumentos tambien son persistidos y vueltos a recrear, cuidado
        Bundle args = getArguments();
        if (args != null) {
            //Set the article based on argument passed in
            updateHeadlinesView(args.getInt(ARG_POSITION));
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Object item = getListView().getItemAtPosition(position);
        callback.onArticleSelected(position, item);
        updateHeadlinesView(position);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    private void updateHeadlinesView(int position) {
        currentPosition = position;
        if (currentPosition > -1) {
            getListView().setItemChecked(currentPosition, true);
        }
        else {
            getListView().clearChoices();
        }
    }

}