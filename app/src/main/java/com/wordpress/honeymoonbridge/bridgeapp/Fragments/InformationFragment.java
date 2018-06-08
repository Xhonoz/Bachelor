package com.wordpress.honeymoonbridge.bridgeapp.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import org.w3c.dom.Text;

public class InformationFragment extends DialogFragment {

public static final InformationFragment newInstance(String message){
    InformationFragment i =  new InformationFragment();
    Bundle bundle = new Bundle(1);
    bundle.putString("message", message);
    i.setArguments(bundle);
    return i;
}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        String message = getArguments().getString("message");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }

}
