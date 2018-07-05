package net.poquesoft.appio.view.dialogs;

import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import net.poquesoft.appio.view.listeners.YesNoListener;

/**
 * Class with static methods to show dialogs on screen
 *
 * Created by edi on 11/01/17.
 */

public class AppioDialogs {

	private static final String TAG = "AppioDialogs";

    /**
     * Shows a message in a popup dialog
     *
     * @param context Context
     * @param icon Resource ID for the icon to show (0 shows no icon)
     * @param message Message to display
     */
	public static void showMessage(final Context context, int icon, String message) {

        MaterialDialog md = new MaterialDialog.Builder(context)
                .content(message)
                .positiveText("Aceptar")
                .show();

		if (icon > 0) md.setIcon(icon);
	}

    /**
     * Shows a message in a popup dialog
     *
     * @param context Context
     * @param icon Resource ID for the icon to show (0 shows no icon)
     * @param message Message to display
     */
    public static void askYesNo(final Context context, int icon, String message, final YesNoListener yesNoListener) {

        MaterialDialog md = new MaterialDialog.Builder(context)
                .content(message)
                .positiveText("Sí")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        yesNoListener.onYes();
                    }
                })
                .negativeText("No")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        yesNoListener.onNo();
                    }
                })
                .show();

        if (icon > 0) md.setIcon(icon);
    }

    /**
     * About notice
     *
     * @param context Context
     */
    public static void about(final Context context, int stringResource) {
        MaterialDialog md = new MaterialDialog.Builder(context)
                .title("RUN4EVER")
                .content(stringResource)
                .positiveText("Aceptar")
                .show();
        if (md.getContentView()!=null) md.getContentView().setTextSize(10);
    }

    /**
     * Shows a progress dialog when doing tasks
     *
     * @param context Context
     * @param message Message to display
     */
    public static MaterialDialog showProgress(final Context context, String message) {

        return new MaterialDialog.Builder(context)
                .progress(true,100)
                .content(message)
                .show();
    }
}
