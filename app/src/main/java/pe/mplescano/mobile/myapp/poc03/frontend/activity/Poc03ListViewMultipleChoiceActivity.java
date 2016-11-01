package pe.mplescano.mobile.myapp.poc03.frontend.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.Arrays;

import pe.mplescano.mobile.myapp.poc03.R;
import pe.mplescano.mobile.myapp.poc03.domain.ImplLabeledEntity;
import pe.mplescano.mobile.myapp.poc03.frontend.cursor.PocAdapter;

/**
 * Created by mplescano on 15/10/2016.
 */
public class Poc03ListViewMultipleChoiceActivity extends AppCompatActivity {

    private ListView listView;

    private String[] arrItems = {"Android", "iPhone", "BlackBerry",
            "AndroidPeople", "J2ME", "Listview", "ArrayAdapter", "ListItem",
            "Us", "UK", "India"};

    private ImplLabeledEntity[] arrItems2 = {
            new ImplLabeledEntity(100, "Android"),
            new ImplLabeledEntity(230, "iPhone"),
            new ImplLabeledEntity(250, "BlackBerry"),
            new ImplLabeledEntity(260, "AndroidPeople"),
            new ImplLabeledEntity(270, "J2ME"),
            new ImplLabeledEntity(280, "Listview"),
            new ImplLabeledEntity(300, "ArrayAdapter"),
            new ImplLabeledEntity(310, "ListItem"),
            new ImplLabeledEntity(320, "Us"),
            new ImplLabeledEntity(350, "UK"),
            new ImplLabeledEntity(400, "India"),
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_poc03_list_view_multiple_choice);

        listView = (ListView) findViewById(R.id.lsvCheckable);
        //listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);//has to be activated in layout or in code before to set the adapter
        /*listView.setAdapter(new ArrayAdapter<String>(this, R.layout.layout_poc03_item_view,
                R.id.txvText, arrItems));*/
        listView.setAdapter(new PocAdapter<>(this,
                R.layout.layout_poc03_item_view,
                Arrays.asList(arrItems2), listView));

        /*listView.setItemsCanFocus(true);
        listView.setFocusable(false);
        listView.setFocusableInTouchMode(false);
        listView.setClickable(false);*/





    }
}
