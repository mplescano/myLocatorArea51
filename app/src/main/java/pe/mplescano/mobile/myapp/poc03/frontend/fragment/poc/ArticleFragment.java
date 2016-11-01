package pe.mplescano.mobile.myapp.poc03.frontend.fragment.poc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pe.mplescano.mobile.myapp.poc03.R;

/**
 * Created by mplescano on 30/09/2016.
 */
public class ArticleFragment extends Fragment {

    final static String ARG_TITLE = "title";

    final static String ARG_CONTENT = "content";

    final static String ARG_POSITION = "position";

    int currentPosition = -1;

    String currentTitle;

    String currentContent;

    TextView txvTitle;

    TextView txvContent;

    public static void updateInstance(final ArticleFragment articleFragment,
                                                 int position, String title, String content) {
        if (position > -1 && articleFragment.isAdded()) {
            articleFragment.updateArticleView(position, title, content);
        }
    }

    public static ArticleFragment newInstance(int position, String title, String content) {
        final ArticleFragment articleFragment = new ArticleFragment();
        if (position > -1) {
            final Bundle arguments = new Bundle();
            arguments.putInt(ARG_POSITION, position);
            arguments.putString(ARG_TITLE, title);
            arguments.putString(ARG_CONTENT, content);
            articleFragment.setArguments(arguments);
        }
        return articleFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if activity recreated (such as from screen rotate),
        // restores the previous article selection set by onSaveInstanceState().
        //This is primarily necessary when is in the two-pane layout
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(ARG_POSITION);
            currentTitle = savedInstanceState.getString(ARG_TITLE);
            currentContent = savedInstanceState.getString(ARG_CONTENT);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_article, container, false);
        txvContent = (TextView) view.findViewById(R.id.txvContent);
        txvTitle  = (TextView) view.findViewById(R.id.txvTitle);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        //During startup, check if there are arguments passed to the fragment.
        //onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method below
        // that sets the article text.
        if (currentPosition != -1) {
            updateArticleView(currentPosition, currentTitle, currentContent);
        }
        else {
            //los argumentos tambien son persistidos y vueltos a recrear, cuidado
            Bundle args = getArguments();
            if (args != null) {
                //Set the article based on argument passed in
                updateArticleView(args.getInt(ARG_POSITION), args.getString(ARG_TITLE), args.getString(ARG_CONTENT));
                getArguments().clear();
            }
        }
    }

    private void updateArticleView(int position, String title, String content) {
        txvTitle.setText(title);
        txvContent.setText(content);
        currentPosition = position;
        currentTitle = title;
        currentContent = content;
    }

    public boolean isEmptyFragmentView() {
        return currentPosition == -1 || currentTitle == null || "".equals(currentTitle);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //Save the current article selection in case we need to recreate the fragment
        outState.putInt(ARG_POSITION, currentPosition);
        outState.putString(ARG_TITLE, currentTitle);
        outState.putString(ARG_CONTENT, currentContent);
    }

    public int getPosition() {
        return currentPosition;
    }
}