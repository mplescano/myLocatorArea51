package pe.mplescano.mobile.myapp.poc03.frontend.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import pe.mplescano.mobile.myapp.poc03.R;
import pe.mplescano.mobile.myapp.poc03.frontend.fragment.poc.ArticleFragment;
import pe.mplescano.mobile.myapp.poc03.frontend.fragment.poc.HeadlinesFragment;
import pe.mplescano.mobile.myapp.poc03.frontend.fragment.poc.Ipsum;
import pe.mplescano.mobile.myapp.poc03.support.Utils;

public class Poc03MainActivity extends AppCompatActivity implements HeadlinesFragment.OnHeadlineSelectedListener{

    private static final String LIST_FRAGMENT_TAG = "list_fragment";

    private static final String CONTENT_FRAGMENT_TAG = "content_fragment";

    private static final String TRANSACTION_SHOW_CONTENT_PORTRAIT = "content_portrait";

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        if (Utils.isTwoPane(getResources(), R.string.screen_size)) {
            setContentView(R.layout.layout_twopane_main);
        }
        else {
            setContentView(R.layout.layout_onepane_main);
        }

        if (savedInstanceState == null) {
            // Create an instance of ExampleFragment
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            final HeadlinesFragment firstFragment = new HeadlinesFragment();
            firstFragment.setCallback(this);
            if (Utils.isTwoPane(getResources(), R.string.screen_size)) {
                //two pane
                final ArticleFragment articleFragment = ArticleFragment.newInstance(-1, null, null);
                fragmentTransaction
                        .replace(R.id.frlFirstOfTwoPaneMain, firstFragment, LIST_FRAGMENT_TAG)
                        .replace(R.id.frlSecondOfTwoPaneMain, articleFragment, CONTENT_FRAGMENT_TAG);
            }
            else {
                //one pane
                fragmentTransaction
                        .replace(R.id.frlOnePaneMain, firstFragment, LIST_FRAGMENT_TAG);
            }
            fragmentTransaction.commit();
        }
        else {
            final HeadlinesFragment firstFragment = (HeadlinesFragment) fragmentManager.findFragmentByTag(LIST_FRAGMENT_TAG);
            firstFragment.setCallback(this);

            ArticleFragment prevArticleFragment = (ArticleFragment) fragmentManager.findFragmentByTag(CONTENT_FRAGMENT_TAG);

            if (Utils.isTwoPane(getResources(), R.string.screen_size)) {
                //see http://stackoverflow.com/questions/9906254/illegalstateexception-cant-change-container-id-of-fragment
                //Simulating a Back command by the user
                fragmentManager.popBackStack(TRANSACTION_SHOW_CONTENT_PORTRAIT, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(firstFragment);
                if (prevArticleFragment != null) {
                    fragmentTransaction.remove(prevArticleFragment);
                }
                fragmentTransaction.commit();
                fragmentManager.executePendingTransactions();
                final ArticleFragment articleFragment;
                if (prevArticleFragment != null) {
                    articleFragment = prevArticleFragment;
                }
                else {
                    articleFragment = ArticleFragment.newInstance(-1, null, null);
                }
                HeadlinesFragment.updateInstance(firstFragment, articleFragment.getPosition());

                //Register a listener for changes to the back stack
                /*fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        fragmentManager.beginTransaction()
                                .replace(R.id.frlFirstOfTwoPaneMain, firstFragment, LIST_FRAGMENT_TAG)
                                .replace(R.id.frlSecondOfTwoPaneMain, articleFragment, CONTENT_FRAGMENT_TAG)
                                .commit();
                        fragmentManager.removeOnBackStackChangedListener(this);
                    }
                });*/

                fragmentManager.beginTransaction()
                        .replace(R.id.frlFirstOfTwoPaneMain, firstFragment, LIST_FRAGMENT_TAG)
                        .replace(R.id.frlSecondOfTwoPaneMain, articleFragment, CONTENT_FRAGMENT_TAG)
                        .commit();
            }
            else {
                fragmentManager.popBackStack(TRANSACTION_SHOW_CONTENT_PORTRAIT, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(firstFragment);
                if (prevArticleFragment != null) {
                    fragmentTransaction.remove(prevArticleFragment);
                }
                fragmentTransaction.commit();
                fragmentManager.executePendingTransactions();
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.frlOnePaneMain, firstFragment, LIST_FRAGMENT_TAG)
                        .commit();

                if (prevArticleFragment != null && !prevArticleFragment.isEmptyFragmentView()) {
                    //si estaba activo anteriormente, probablemente en modo landscape,
                    // en este modo portrait ponerlo delante del stack
                    Log.d("prevArticleFragment", ".replace(R.id.frlOnepaneMain, prevArticleFragment:" + prevArticleFragment);
                    fragmentManager
                            .beginTransaction()
                            .addToBackStack(TRANSACTION_SHOW_CONTENT_PORTRAIT)
                            .replace(R.id.frlOnePaneMain, prevArticleFragment, CONTENT_FRAGMENT_TAG)
                            .commit();
                }
            }
        }


        /*TextView txvHello = (TextView) findViewById(R.id.txvHello);

        int dpi = getResources().getDisplayMetrics().densityDpi;

        txvHello.setText(txvHello.getText() + ", dp: " + dpi);*/


    }

    @Override
    public void onArticleSelected(int position, Object item) {
        ArticleFragment articleFragment = (ArticleFragment) fragmentManager.findFragmentByTag(CONTENT_FRAGMENT_TAG);
        if (Utils.isTwoPane(getResources(), R.string.screen_size)) {
            if (articleFragment == null) {
                //improbable pero por si aca
                final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                articleFragment = ArticleFragment.newInstance(position, Ipsum.Headlines[position],
                        Ipsum.Articles[position]);
                fragmentTransaction
                        .replace(R.id.frlSecondOfTwoPaneMain, articleFragment, CONTENT_FRAGMENT_TAG)
                        .commit();
            }
            else {
                //fragment has been created
                ArticleFragment.updateInstance(articleFragment, position, Ipsum.Headlines[position],
                        Ipsum.Articles[position]);
            }
        }
        else {
            if (articleFragment == null) {
                articleFragment = ArticleFragment.newInstance(position, Ipsum.Headlines[position],
                        Ipsum.Articles[position]);
                final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction
                        .addToBackStack(TRANSACTION_SHOW_CONTENT_PORTRAIT)
                        .replace(R.id.frlOnePaneMain, articleFragment, CONTENT_FRAGMENT_TAG)
                        .commit();
            }
            else {
                throw new RuntimeException("wtf!!, se suponia que en modo portrait al hacer back, el fragment del content se destruía");
            }
        }
    }
}
