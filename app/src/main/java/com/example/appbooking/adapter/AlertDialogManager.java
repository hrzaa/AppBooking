package com.example.appbooking.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.appbooking.R;


public class AlertDialogManager {
    public void showAlertDialog(Context context, String title, String message, Boolean status){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);

        if (status != null)
            alertDialog.setIcon((status) ? com.example.appbooking.R.drawable.ic_baseline_check_circle_24 : R.drawable.ic_baseline_cancel_24);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }
}
