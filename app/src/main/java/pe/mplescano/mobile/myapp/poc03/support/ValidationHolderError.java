package pe.mplescano.mobile.myapp.poc03.support;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.widget.TextView;

/**
 * Created by mplescano on 31/10/2016.
 */

public class ValidationHolderError {

    @IdRes
    private int viewId;

    private TextView errorView;

    @StringRes
    private int messageId;

    public ValidationHolderError(@IdRes int viewId, @NonNull TextView errorView, @StringRes int messageId) {
        this.viewId = viewId;
        this.messageId = messageId;
        this.errorView = errorView;
    }

    public int getViewId() {
        return viewId;
    }

    public int getMessageId() {
        return messageId;
    }

    public TextView getErrorView() {
        return errorView;
    }
}
