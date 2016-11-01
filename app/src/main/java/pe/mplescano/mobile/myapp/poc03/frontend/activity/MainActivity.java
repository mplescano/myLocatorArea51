package pe.mplescano.mobile.myapp.poc03.frontend.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import javax.inject.Inject;

import pe.mplescano.mobile.myapp.poc03.R;
import pe.mplescano.mobile.myapp.poc03.dao.iface.PointLocationDao;
import pe.mplescano.mobile.myapp.poc03.domain.PointLocation;
import pe.mplescano.mobile.myapp.poc03.frontend.fragment.FormPointLocationFragment;
import pe.mplescano.mobile.myapp.poc03.frontend.fragment.ListPointLocationFragment;
import pe.mplescano.mobile.myapp.poc03.ioc.BaseApplication;
import pe.mplescano.mobile.myapp.poc03.support.Utils;

/**
 * Created by mplescano on 14/10/2016.
 */
public class MainActivity extends AppCompatActivity implements
        AdapterView.OnItemClickListener, View.OnClickListener {

    private static final String LIST_FRAGMENT_TAG = "list_fragment";
    private static final String CONTENT_FRAGMENT_TAG = "content_fragment";
    private static final String TRANSACTION_SHOW_CONTENT_PORTRAIT = "content_portrait";

    private FragmentManager fragmentManager;

    @Inject
    PointLocationDao pointLocationDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseApplication) getApplication()).getMainComponent().inject(this);
        fragmentManager = getSupportFragmentManager();
        if (Utils.isTwoPane(getResources(), R.string.screen_size)) {
            setContentView(R.layout.layout_twopane_main);
        }
        else {
            setContentView(R.layout.layout_onepane_main);
        }

        if (savedInstanceState == null) {
            // Create an instance
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            final ListPointLocationFragment listFragment = new ListPointLocationFragment();
            listFragment.setCallback(this);
            if (Utils.isTwoPane(getResources(), R.string.screen_size)) {
                //two pane
                final FormPointLocationFragment formFragment = new FormPointLocationFragment();
                fragmentTransaction
                        .replace(R.id.frlFirstOfTwoPaneMain, listFragment, LIST_FRAGMENT_TAG)
                        .replace(R.id.frlSecondOfTwoPaneMain, formFragment, CONTENT_FRAGMENT_TAG)
                        .commit();
            }
            else {
                //one pane
                fragmentTransaction
                        .replace(R.id.frlOnePaneMain, listFragment, LIST_FRAGMENT_TAG)
                        .commit();
            }
        }
        else {
            final ListPointLocationFragment listFragment = (ListPointLocationFragment)
                    fragmentManager.findFragmentByTag(LIST_FRAGMENT_TAG);
            listFragment.setCallback(this);

            final FormPointLocationFragment prevFormFragment = (FormPointLocationFragment)
                    fragmentManager.findFragmentByTag(CONTENT_FRAGMENT_TAG);

            if (Utils.isTwoPane(getResources(), R.string.screen_size)) {
                //see http://stackoverflow.com/questions/9906254/illegalstateexception-cant-change-container-id-of-fragment
                //Simulating a Back command by the user
                fragmentManager.popBackStack(TRANSACTION_SHOW_CONTENT_PORTRAIT, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(listFragment);
                if (prevFormFragment != null) {
                    fragmentTransaction.remove(prevFormFragment);
                }
                fragmentTransaction.commit();
                fragmentManager.executePendingTransactions();
                final FormPointLocationFragment formFragment;
                if (prevFormFragment != null) {
                    formFragment = prevFormFragment;
                }
                else {
                    formFragment = new FormPointLocationFragment();
                }
                formFragment.setCallback(this);

                fragmentManager.beginTransaction()
                        .replace(R.id.frlFirstOfTwoPaneMain, listFragment, LIST_FRAGMENT_TAG)
                        .replace(R.id.frlSecondOfTwoPaneMain, formFragment, CONTENT_FRAGMENT_TAG)
                        .commit();
            }
            else {
                fragmentManager.popBackStack(TRANSACTION_SHOW_CONTENT_PORTRAIT, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                final FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.remove(listFragment);
                if (prevFormFragment != null) {
                    ft.remove(prevFormFragment);
                }
                ft.commit();
                fragmentManager.executePendingTransactions();

                fragmentManager.beginTransaction()
                        .replace(R.id.frlOnePaneMain, listFragment, LIST_FRAGMENT_TAG)
                        .commit();

                if (prevFormFragment != null && !prevFormFragment.isEmptyFragmentView()) {
                    //si estaba activo anteriormente, probablemente en modo landscape,
                    // en este modo portrait ponerlo delante del stack
                    fragmentManager.beginTransaction()
                            .addToBackStack(TRANSACTION_SHOW_CONTENT_PORTRAIT)
                            .replace(R.id.frlOnePaneMain, prevFormFragment, CONTENT_FRAGMENT_TAG)
                            .commit();
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PointLocation pl = pointLocationDao.getPointLocation(id);
        updateViewFormFragment(position, pl);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imbSave:
                final ListPointLocationFragment listFragment = (ListPointLocationFragment)
                        fragmentManager.findFragmentByTag(LIST_FRAGMENT_TAG);
                if (Utils.isTwoPane(getResources(), R.string.screen_size)) {
                    listFragment.refresh();
                }
                else {
                    fragmentManager.popBackStack(TRANSACTION_SHOW_CONTENT_PORTRAIT,
                            FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                break;

            default:
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final ListPointLocationFragment listFragment = (ListPointLocationFragment)
                fragmentManager.findFragmentByTag(LIST_FRAGMENT_TAG);
        switch (item.getItemId()) {
            case R.id.mitRefresh:
                //before, you need to know if the fragment is started and visible
                if (listFragment.isVisible()) {
                    listFragment.refresh();
                }
                return true;

            case R.id.mitAddPointLocation:
                int position = -1;
                PointLocation pl = new PointLocation();
                updateViewFormFragment(position, pl);

                return true;

            case R.id.mitDeletePointLocation:
                if (Utils.isTwoPane(getResources(), R.string.screen_size)) {
                    //two panes
                    /////////////////////
                    long[] checkedsIds = listFragment.getListView().getCheckedItemIds();
                    listFragment.deleteItems(checkedsIds);
                    /////////////////////
                    FormPointLocationFragment fplf = (FormPointLocationFragment) fragmentManager
                            .findFragmentByTag(CONTENT_FRAGMENT_TAG);
                    if (fplf != null && fplf.isVisible() && fplf.getBeanView().getId() > 0) {
                        for (long pointLocationId : checkedsIds) {
                            if (pointLocationId == fplf.getBeanView().getId()) {
                                fplf.updateFragmentView(-1, new PointLocation());
                            }
                        }
                    }
                }
                else {
                    if (listFragment.isVisible()) {
                        /////////////////////
                        long[] checkedsIds = listFragment.getListView().getCheckedItemIds();
                        listFragment.deleteItems(checkedsIds);
                        /////////////////////
                    }
                    else {
                        FormPointLocationFragment fplf = (FormPointLocationFragment) fragmentManager
                                .findFragmentByTag(CONTENT_FRAGMENT_TAG);
                        if (fplf != null && fplf.isVisible() && fplf.getBeanView().getId() > 0) {
                            /////////////////////
                            long[] checkedsIds = new long[]{fplf.getBeanView().getId()};
                            listFragment.deleteItems(checkedsIds);
                            /////////////////////
                            fplf.updateFragmentView(-1, new PointLocation());
                        }
                    }
                }

                return true;

            default:

                return super.onOptionsItemSelected(item);
        }
    }

    private void updateViewFormFragment(int position, PointLocation pl) {
        //before, you need to know what it's the orientation
        // and the current fragment in the stack
        FormPointLocationFragment fplf = (FormPointLocationFragment) fragmentManager
                .findFragmentByTag(CONTENT_FRAGMENT_TAG);
        if (Utils.isTwoPane(getResources(), R.string.screen_size)) {
            if (fplf != null) {
                //the fragment was attached, created and started
                fplf.updateFragmentView(position, pl);
            }
            else {
                //improbably but just in case of
                throw new IllegalStateException("it was expected a fragment already started");
            }
        }
        else {
            //one pane
            if (fplf == null) {
                fplf = FormPointLocationFragment.newInstance(position, pl);
                fplf.setCallback(this);
                fragmentManager.beginTransaction()
                        .addToBackStack(TRANSACTION_SHOW_CONTENT_PORTRAIT)
                        .replace(R.id.frlOnePaneMain, fplf, CONTENT_FRAGMENT_TAG)
                        .commit();
            }
            else {
                throw new IllegalStateException("it was expected a null fragment");
            }
        }
    }
}
