package com.roly.g3m.view.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.roly.g3m.model.Loan;
import com.roly.g3m.view.fragment.LoanFragment;

public class MainPagerAdapter extends FragmentPagerAdapter {

    private final LoanFragment fromMe;
    private final LoanFragment toMe;

    public MainPagerAdapter(Context context, FragmentManager manager) {
        super(manager);
        fromMe = (LoanFragment) LoanFragment.instantiate(context, LoanFragment.class.getName());
        toMe = (LoanFragment) LoanFragment.instantiate(context, LoanFragment.class.getName());
        fromMe.setSens(Loan.SENS_FROM_ME);
        toMe.setSens(Loan.SENS_TO_ME);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return toMe;
            case 1: return fromMe;
        }
        throw new IllegalStateException();
    }

    @Override
    public int getCount() {
        return 2;
    }
}
