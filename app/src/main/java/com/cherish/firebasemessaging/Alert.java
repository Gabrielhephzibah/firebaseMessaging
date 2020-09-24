package com.cherish.firebasemessaging;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import es.dmoral.toasty.Toasty;

public class Alert {
    public static String extra = "collection";
    public static void showSuccess(Context context, String  message){
        Toasty.success(context, message, Toast.LENGTH_SHORT, true).show();
    }

    public static void showFailed(Context context,String  message){
        Toasty.error(context, message, Toast.LENGTH_SHORT, true).show();
    }

    public static void showWarning(Context context,String  message){
        Toasty.warning(context, message, Toast.LENGTH_SHORT, true).show();
    }

    public static void showInfo(Context context,String  message){
        Toasty.info(context, message, Toast.LENGTH_SHORT, true).show();
    }

    public static void showNormalWithoutIcon(Context context,String  message){
        Toasty.normal(context, message).show();
    }

    public static void showNormalWithIcon(Context context, String  message, Drawable drawable){
        Toasty.normal(context, message, drawable).show();
    }


    public static void showPositivePopup(Context context, String title,String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static void showPositiveAndNegativePopup(Context context, String title,String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
