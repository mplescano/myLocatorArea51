package pe.mplescano.mobile.myapp.poc03.frontend.cursor;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import pe.mplescano.mobile.myapp.poc03.domain.ILabeledEntity;

/**
 * Created by mplescano on 15/10/2016.
 */
public abstract class AbsEntityListAdapter<T extends ILabeledEntity> extends BaseAdapter {

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
    private PositionCursor<T> mPositionCursor;

    /**
     * Constructor
     *
     * @param context The current context.
     * @param resource The resource ID for a layout file containing a layout to use when
     *                 instantiating views.
     * @param positionCursor The objects to represent in the ListView.
     */
    public AbsEntityListAdapter(@NonNull Context context, @LayoutRes int resource,
                                @NonNull PositionCursor<T> positionCursor) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mResource = resource;
        mPositionCursor = positionCursor;
    }

    @Override
    public int getCount() {
        return (int) mPositionCursor.getCount();
    }

    @Override
    public T getItem(int position) {
        return mPositionCursor.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return mPositionCursor.getItemId(position);
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView,
                                 @NonNull ViewGroup parent) {
        if (!(parent instanceof AbsListView)) {
            throw new IllegalArgumentException("it's expected the parent to be a ListView instance.");
        }
        return createViewFromResource(mInflater, position, convertView, (AbsListView) parent,
                mResource);
    }

    protected abstract @NonNull View createViewFromResource(@NonNull LayoutInflater inflater,
                                                 final int position, @Nullable View convertView,
                                                 @NonNull AbsListView parentListView, int resource);

    public PositionCursor getPositionCursor() {
        return mPositionCursor;
    }

    public boolean hasStableIds() {
        return true;
    }
}