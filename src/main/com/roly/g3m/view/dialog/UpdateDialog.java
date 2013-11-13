package com.roly.g3m.view.dialog;

import java.util.Date;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.roly.GiveMeMyMoney2.R;
import com.roly.g3m.data.Database;
import com.roly.g3m.data.LoanProvider;
import com.roly.g3m.model.Loan;

public class UpdateDialog extends DialogFragment{

    private final Loan loan;
    private CheckBox partiallyCheckbox;
    private EditText mPartValue;

    public UpdateDialog(Loan loan){
        this.loan = loan;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_modify, null);
        prepareViewContent(view);

        dialogBuilder.setView(view).setTitle(getString(R.string.dialog_update_title))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        performUpdate();
                        getDialog().dismiss();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getDialog().cancel();
                    }
                });
        return dialogBuilder.create();
    }

    private void prepareViewContent(View view) {
        partiallyCheckbox = (CheckBox) view.findViewById(R.id.partially_checkbox);
        if(loan.getType().equals(Loan.Type.object)){
            partiallyCheckbox.setEnabled(false);
        }else {
            mPartValue = (EditText) view.findViewById(R.id.part_value);
            partiallyCheckbox.setEnabled(true);
            partiallyCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mPartValue.setEnabled(isChecked);
                }
            });
        }
    }

    private void performUpdate() {
        AsyncQueryHandler queryHandler = new AsyncQueryHandler(getActivity().getContentResolver()) {};
        Uri contentUri = LoanProvider.CONTENT_URI.buildUpon().appendPath(String.valueOf(loan.getId())).build();
        Log.e("UpdateDialog", contentUri.toString());
        if (partiallyCheckbox.isChecked()) {
            float result = getValue();
            if(result==0){
                delete(queryHandler, contentUri);
            } else if(result < 0){
                updateValueAndSens(queryHandler, contentUri, result);
            } else {
                updateValue(queryHandler, contentUri, result);
            }
        } else {
            delete(queryHandler, contentUri);
        }
    }

    private void updateValue(AsyncQueryHandler queryHandler, Uri contentUri, float result) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Database.KEY_PRET_VALEUR, result);
        contentValues.put(Database.KEY_PRET_DATE, new Date().getTime());
        queryHandler.startUpdate(2, null, contentUri, contentValues, null, null);
    }

    private void updateValueAndSens(AsyncQueryHandler queryHandler, Uri contentUri, float result) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Database.KEY_PRET_VALEUR, -result);
        contentValues.put(Database.KEY_PRET_SENS, !loan.getSens());
        contentValues.put(Database.KEY_PRET_DATE, new Date().getTime());
        queryHandler.startUpdate(1, null, contentUri, contentValues, null, null);
    }

    private void delete(AsyncQueryHandler queryHandler, Uri contentUri) {
        queryHandler.startDelete(0, null, contentUri, null, null);
    }

    private float getValue() {
        String valueText = mPartValue.getText().toString();
        float newValue = Float.parseFloat(valueText);
        float actualValue = loan.getValue();
        return actualValue - newValue;
    }

}