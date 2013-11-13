package com.roly.g3m.view.adapter;

import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import com.roly.GiveMeMyMoney2.R;
import com.roly.g3m.data.Database;

public class LoanListItemAdapter extends SimpleCursorAdapter implements SimpleCursorAdapter.ViewBinder{

    private Context context;

    public LoanListItemAdapter(Context context, Cursor cursor) {
        super(context, R.layout.list_item, cursor,
                new String[]{Database.KEY_PRET_VALEUR, Database.KEY_PRET_NOM_OBJET, Database.KEY_PRET_NOM, Database.KEY_PRET_DATE},
                new int[]{R.id.content, R.id.content, R.id.contactName, R.id.date}, 0);
        this.context = context;
        setViewBinder(this);
    }

    @Override
    public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
        String text = cursor.getString(columnIndex);

        if(text == null){
            return true;
        }

        switch (columnIndex){
            case Database.COLUMN_VALEUR :
                //Valeur
                text+= Currency.getInstance(Locale.getDefault()).getSymbol();
                break;
            case Database.COLUMN_NOM_OBJET :
                //Objet

                break;
            case Database.COLUMN_NOM :
                //Nom

                break;
            case Database.COLUMN_DATE :
                //Date
                Date date = new Date(Long.parseLong(text));
                text = DateFormat.getDateFormat(context).format(date);
                break;
        }
        if (view instanceof TextView) {
            setViewText((TextView) view, text);
        } else {
            throw new IllegalStateException(view.getClass().getName() + " is not a " +
                    " view that can be bounds by this Adapter");
        }
        return true;
    }
}