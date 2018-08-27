package net.poquesoft.appio.view.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;

import com.afollestad.materialdialogs.MaterialDialog;

import net.poquesoft.appio.view.listeners.IntegerListener;
import net.poquesoft.appio.view.listeners.SimpleListener;
import net.poquesoft.appio.view.listeners.StringListener;
import net.poquesoft.appio.view.listeners.YesNoListener;
import net.poquesoft.libs.SweetAlert.SweetAlertDialog;


/**
 * Class with static methods to show dialogs on screen
 *
 * Created by edi on 11/01/17.
 */

public class AppioDialogs {

	private static final String TAG = "AppioDialogs";

    public static void successMessage(final Context context, String message) {
        successMessage(context,message,null);
    }

    /**
     * Shows a message in a popup dialog
     *  @param context Context
     * @param message Message to display
     * @param listener
     */
	public static void successMessage(final Context context, String message, final SimpleListener listener) {
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(message)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        if (listener != null) listener.onAction();
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .show();
	}

    /**
     * Shows a message in a popup dialog
     *  @param context Context
     * @param message Message to display
     * @param listener
     */
    public static void normalMessage(final Context context, String message, final SimpleListener listener) {

        new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText(message)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        if (listener != null) listener.onAction();
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    /**
     * Asks for confirmation and executes result if accepted
     * @param context Context
     * @param message Message to display
     * @param listenerAccept
     */
    public static void confirm(final Context context, String message, final SimpleListener listenerAccept) {
        confirm(context, message, listenerAccept, null);
    }
    /**
     * Asks for confirmation and executes result if accepted
     * @param context Context
     * @param message Message to display
     * @param listenerAccept
     * @param listenerCancel
     */
    public static void confirm(final Context context, String message, final SimpleListener listenerAccept, final SimpleListener listenerCancel) {

        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(message)
                .setConfirmText(context.getString(android.R.string.ok))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        if (listenerAccept != null) listenerAccept.onAction();
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .setCancelText(context.getString(android.R.string.cancel))
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        if (listenerCancel != null) listenerCancel.onAction();
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .showCancelButton(true)
                .show();
    }

    /**
     * Ask for data to be entered
     * @param context Context
     * @param message Message to display
     * @param listener
     */
    public static void ask(final Context context, String message, final StringListener listener) {

        new SweetAlertDialog(context, SweetAlertDialog.ASK_TYPE)
                .setTitleText(message)
                .setConfirmText(context.getString(android.R.string.ok))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        if (listener != null) listener.onAction(sweetAlertDialog.getEditText());
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .show();
    }


    /**
     * Ask for rating to be entered
     * @param context Context
     * @param message Message to display
     * @param listener
     */
    public static void rate(final Context context, String message, final IntegerListener listener) {

        new SweetAlertDialog(context, SweetAlertDialog.RATING_TYPE)
                .setTitleText(message)
                .setConfirmText(context.getString(android.R.string.ok))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        if (listener != null) listener.onAction(sweetAlertDialog.getStars());
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    public static void errorMessage(final Context context, String message, final SimpleListener listener) {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(message)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        if (listener != null) listener.onAction();
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    public static void errorMessage(final Context context, String title, String message) {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .show();
    }

    /**
     * Shows a message in a popup dialog
     *
     * @param context Context
     * @param message Message to display
     */
    public static void askYesNo(final Context context, String title, String message, final YesNoListener yesNoListener) {

        new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .showCancelButton(true)
                .setCancelText("No")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        yesNoListener.onNo();
                        sweetAlertDialog.cancel();
                    }
                })
                .setConfirmText("Sí")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        yesNoListener.onYes();
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .show();

/*
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
*/
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
    public static Dialog showProgress(final Context context, String message) {
/*
        return new MaterialDialog.Builder(context)
                .progress(true,100)
                .content(message)
                .show();*/
        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(message);
        pDialog.setCancelable(false);
        pDialog.show();
        return pDialog;
    }

}
