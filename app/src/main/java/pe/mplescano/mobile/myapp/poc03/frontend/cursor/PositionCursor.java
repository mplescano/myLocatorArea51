package pe.mplescano.mobile.myapp.poc03.frontend.cursor;

/**
 * Created by mplescano on 15/10/2016.
 */
public interface PositionCursor<T> {

    long getCount();

    T getItem(long position);

    long getItemId(long position);

    void update();
}
