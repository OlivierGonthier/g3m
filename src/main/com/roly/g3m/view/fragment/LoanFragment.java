package com.roly.g3m.view.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ListView;

import com.roly.g3m.data.Database;
import com.roly.g3m.data.LoanProvider;
import com.roly.g3m.model.Loan;
import com.roly.g3m.utils.LoanCursorReader;
import com.roly.g3m.view.adapter.LoanListItemAdapter;
import com.roly.g3m.view.dialog.UpdateDialog;

public class LoanFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private SimpleCursorAdapter adapter;
    private boolean sens;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new LoanListItemAdapter(getActivity(), null);
        setListAdapter(adapter);
        setListShown(false);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri contentUri = LoanProvider.CONTENT_URI;
        String sortOrder = Database.KEY_PRET_DATE;
        String selection = Database.KEY_PRET_SENS + " = ?";
        String[] selectionArgs = {sens? "1":"0"};
        return new CursorLoader(getActivity(), contentUri, null, selection, selectionArgs, sortOrder);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Cursor cursor = (Cursor) getListView().getItemAtPosition(position);
        Loan loan = LoanCursorReader.getFromPositionnedCursor(cursor);
        UpdateDialog dialog = new UpdateDialog(loan);
        dialog.show(getFragmentManager(), "UpdateDialog");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        setListShown(true);
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    public void setSens(boolean sens) {
        this.sens = sens;
    }

}
