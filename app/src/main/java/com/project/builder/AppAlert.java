package com.project.builder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.project.R;

public final class AppAlert {
    public static AlertDialog createYesNoDialog(String message, String title, DialogInterface.OnClickListener handler, Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setTitle(title)
                .setCancelable(true)
                .setPositiveButton(R.string.yesString, handler)
                .setNegativeButton(R.string.noString, handler);

        return builder.create();
    }

    public static AlertDialog createMessageDialog(String message, String title, Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setTitle(title)
                .setCancelable(true)
                .setPositiveButton(R.string.yesString, null);

        return builder.create();
    }

    public static Toast createShortToast(String message, Context context){
        return Toast.makeText(context, message, Toast.LENGTH_SHORT);
    }

    public static Toast createLongToast(String message, Context context){
        return Toast.makeText(context, message, Toast.LENGTH_LONG);
    }
}
