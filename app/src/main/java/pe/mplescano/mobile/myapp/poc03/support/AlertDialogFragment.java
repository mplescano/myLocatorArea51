package pe.mplescano.mobile.myapp.poc03.support;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.StringRes;

/**
 * Created by mplescano on 30/10/2016.
 */
public class AlertDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

    DialogInterface.OnClickListener mCallback;

    public static AlertDialogFragment newInstance(String title, String message,
                                                  DialogInterface.OnClickListener callback) {
        return newInstance(title, message, callback, new int[]{android.R.string.ok});
    }

    public static AlertDialogFragment newInstance(String title, String message,
                                                  DialogInterface.OnClickListener callback,
                                                  @StringRes int[] buttons) {
        AlertDialogFragment frag = new AlertDialogFragment();
        frag.setCallback(callback);
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        args.putIntArray("buttons", buttons);
        frag.setArguments(args);
        frag.setCancelable(false);
        //frag.setTargetFragment();
        return frag;
    }

    public void setCallback(DialogInterface.OnClickListener callback) {
        mCallback = callback;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        String title = arguments.getString("title");
        String message = arguments.getString("message");
        int[] buttons = arguments.getIntArray("buttons");

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity())
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false);

        for (int resButton : buttons) {
            if (resButton == android.R.string.ok || resButton == android.R.string.yes) {
                alertDialogBuilder.setPositiveButton(resButton, this);
            }
            else if (resButton == android.R.string.no) {
                alertDialogBuilder.setNegativeButton(resButton, this);
            }
            //TODO neutral
        }

        return alertDialogBuilder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (mCallback != null) {
            mCallback.onClick(dialog, which);
        }
    }
}
