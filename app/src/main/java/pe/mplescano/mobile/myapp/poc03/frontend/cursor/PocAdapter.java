package pe.mplescano.mobile.myapp.poc03.frontend.cursor;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import pe.mplescano.mobile.myapp.poc03.R;
import pe.mplescano.mobile.myapp.poc03.domain.ILabeledEntity;

/**
 * Created by mplescano on 15/10/2016.
 */
public class PocAdapter<T extends ILabeledEntity> extends BaseAdapter {

    private final LayoutInflater mInflater;

    private final Context mContext;

    /**
     * The resource indicating what views to inflate to display the content of this
     * array adapter.
     */
    private final int mResource;

    /**
     * Contains the list of objects that represent the data of this ArrayAdapter.
     * The content of this list is referred to as "the array" in the documentation.
     */
    private List<T> mObjects;

    private final ListView mlistView;

    /**
     * Constructor
     *
     * @param context The current context.
     * @param resource The resource ID for a layout file containing a layout to use when
     *                 instantiating views.
     * @param objects The objects to represent in the ListView.
     */
    public PocAdapter(@NonNull Context context, @LayoutRes int resource,
                      @NonNull List<T> objects, ListView listView) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mResource = resource;
        mObjects = objects;
        mlistView = listView;
    }


    @Override
    public int getCount() {
        return mObjects.size();
    }

    @Override
    public T getItem(int position) {
        return mObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mObjects.get(position).getId();
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView,
                                 @NonNull ViewGroup parent) {
        return createViewFromResource(mInflater, position, convertView, parent, mResource);
    }

    private @NonNull View createViewFromResource(@NonNull LayoutInflater inflater,
                                                 final int position, @Nullable View convertView,
                                                 @NonNull ViewGroup parent, int resource) {
        final View view;
        final T item = getItem(position);

        if (convertView == null) {
            view = inflater.inflate(resource, parent, false);
            view.setTag(R.id.txvText, view.findViewById(R.id.txvText));
            view.setTag(R.id.chkSelect, view.findViewById(R.id.chkSelect));
        } else {
            view = convertView;
        }

        if (parent == mlistView) {
            Log.d("createViewFromResource", "parent is the same to mlistView");
        }

        TextView txvText = (TextView) view.getTag(R.id.txvText);
        txvText.setText(item.getLabel());

        //setear primero el listener para actualizar a la posicion actual y despues ya
        //setear su valor del check
        CheckBox chkSelect = (CheckBox) view.getTag(R.id.chkSelect);
        chkSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("onCheckedChanged", "position:" + position + "; check:" + isChecked);
                mlistView.setItemChecked(position, isChecked);
            }
        });
        Log.d("mlistView.isItemChecked", "position:" + position + "; check:" + mlistView.isItemChecked(position));
        chkSelect.setChecked(mlistView.isItemChecked(position));


        /*try {
            if (mFieldId == 0) {
                //  If no custom field is assigned, assume the whole resource is a TextView
                text = (TextView) view;
            } else {
                //  Otherwise, find the TextView field within the layout
                text = (TextView) view.findViewById(mFieldId);

                if (text == null) {
                    throw new RuntimeException("Failed to find view with ID "
                            + mContext.getResources().getResourceName(mFieldId)
                            + " in item layout");
                }
            }
        } catch (ClassCastException e) {
            Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
            throw new IllegalStateException(
                    "ArrayAdapter requires the resource ID to be a TextView", e);
        }

        final T item = getItem(position);
        if (item instanceof CharSequence) {
            text.setText((CharSequence) item);
        } else {
            text.setText(item.toString());
        }*/





        return view;
    }
}
