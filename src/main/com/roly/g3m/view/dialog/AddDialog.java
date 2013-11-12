package com.roly.g3m.view.dialog;

import java.util.Date;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.roly.g3m.R;
import com.roly.g3m.data.Database;
import com.roly.g3m.data.LoanProvider;
import com.roly.g3m.model.Loan;

public class AddDialog extends SherlockDialogFragment {

    private static final String TAG = AddDialog.class.getSimpleName();

    private EditText editNom;
    private EditText editValue;
    private EditText editDescription;

    private RadioGroup radioSens;
    private RadioButton radioSensToMe;
    private RadioButton radioSensFromMe;
    private RadioGroup radioNature;
    private RadioButton radioNatureMoney;
    private RadioButton radioNatureObject;
    private View zoneMoney;
    private View zoneObjet;

    void clickNewMoney() {
        zoneObjet.setVisibility(View.GONE);
        zoneMoney.setVisibility(View.VISIBLE);
    }

    void clickNewObject(){
        zoneObjet.setVisibility(View.VISIBLE);
        zoneMoney.setVisibility(View.GONE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_add, null);

        initViews(view);

        dialogBuilder.setView(view)
                .setPositiveButton(R.string.dialog_add_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setNegativeButton(R.string.dialog_add_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getDialog().cancel();
                    }
                });
        return dialogBuilder.create();
    }

    private void initViews(View view) {
        zoneMoney = view.findViewById(R.id.zone_money);
        zoneObjet = view.findViewById(R.id.zone_objet);
        editNom = (EditText) view.findViewById(R.id.nom);
        editValue = (EditText) view.findViewById(R.id.value);
        editDescription = (EditText) view.findViewById(R.id.description);
        radioSensToMe = (RadioButton) view.findViewById(R.id.new_tome);
        radioSensFromMe = (RadioButton) view.findViewById(R.id.newtoanother);
        radioSens = (RadioGroup) view.findViewById(R.id.sens);
        radioNature = (RadioGroup) view.findViewById(R.id.nature);
        radioNatureMoney = (RadioButton) view.findViewById(R.id.new_money);
        radioNatureMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickNewMoney();
            }
        });
        radioNatureObject = (RadioButton) view.findViewById(R.id.new_object);
        radioNatureObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickNewObject();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog) getDialog();
        dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editNom.getText().length()==0
                        || (radioNature.getCheckedRadioButtonId() == radioNatureMoney.getId() && TextUtils.isEmpty(editValue.getText()))
                        || (radioNature.getCheckedRadioButtonId() == radioNatureObject.getId() && TextUtils.isEmpty(editDescription.getText()))) {
                    Toast.makeText(getActivity(), getString(R.string.dialog_add_incomplete_error), Toast.LENGTH_SHORT).show();
                    return;
                }
                performInsert();
            }
        });
    }

    private void performInsert(){
        Log.v(TAG, "performInsert()");

        ContentValues contentValues = buildContentValues();
        AsyncQueryHandler asyncQueryHandler = new AsyncQueryHandler(getActivity().getContentResolver()){
            @Override
            protected void onInsertComplete(int token, Object cookie, Uri uri) {
                dismiss();
            }
        };
        asyncQueryHandler.startInsert(0, null, LoanProvider.CONTENT_URI, contentValues);
    }

    private ContentValues buildContentValues() {
        String nom = editNom.getText().toString();
        boolean tome= radioSens.getCheckedRadioButtonId() == radioSensToMe.getId();
        ContentValues contentValues = new ContentValues();
        if(radioNature.getCheckedRadioButtonId() == radioNatureMoney.getId()){
            float value = Float.parseFloat(editValue.getText().toString());
            contentValues.put(Database.KEY_PRET_VALEUR, value);
            contentValues.put(Database.KEY_PRET_NATURE, Loan.Type.money.ordinal());
        }else{
            String description = editDescription.getText().toString();
            contentValues.put(Database.KEY_PRET_NOM_OBJET, description);
            contentValues.put(Database.KEY_PRET_NATURE, Loan.Type.object.ordinal());
        }
        contentValues.put(Database.KEY_PRET_NOM, nom);
        contentValues.put(Database.KEY_PRET_SENS, tome);
        contentValues.put(Database.KEY_PRET_DATE, new Date().getTime());
        return contentValues;
    }
}
