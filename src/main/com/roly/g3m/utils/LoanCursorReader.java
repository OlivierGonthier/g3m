package com.roly.g3m.utils;

import android.database.Cursor;

import com.roly.g3m.data.Database;
import com.roly.g3m.model.Loan;

public class LoanCursorReader {

    public static Loan getFromPositionnedCursor(Cursor cursor){
        if(cursor==null || cursor.isClosed() || cursor.isBeforeFirst() || cursor.isAfterLast())
            return null;

        Loan loan = new Loan();
        loan.setId(cursor.getInt(Database.COLUMN_ID));
        loan.setContactName(cursor.getString(Database.COLUMN_NOM));
        loan.setSens(cursor.getInt(Database.COLUMN_SENS) == 1);
        loan.setLastModified(cursor.getLong(Database.COLUMN_DATE));
        loan.setLimitDate(cursor.getLong(Database.COLUMN_DATE_LIMITE));
        Loan.Type type = cursor.getInt(Database.COLUMN_NATURE) == Loan.Type.object.ordinal() ? Loan.Type.object : Loan.Type.money;
        loan.setType(type);
        loan.setValue(cursor.getFloat(Database.COLUMN_VALEUR));
        loan.setObjectName(cursor.getString(Database.COLUMN_NOM_OBJET));

        return loan;
    }

}
