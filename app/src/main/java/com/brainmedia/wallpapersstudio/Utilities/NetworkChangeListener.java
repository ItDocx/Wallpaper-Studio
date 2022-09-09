package com.brainmedia.wallpapersstudio.Utilities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.brainmedia.wallpapersstudio.R;

public class NetworkChangeListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Commons.isconnectedToInternet(context)){

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View layout_Inflater = LayoutInflater.from(context).inflate(R.layout.check_internet,null);
            builder.setView(layout_Inflater);

            Button btn_Retry = layout_Inflater.findViewById(R.id.btn_retry);

            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.setCancelable(false);
            dialog.getWindow().setGravity(Gravity.CENTER);

            btn_Retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                    onReceive(context,intent);
                }
            });


        }
    }
}
