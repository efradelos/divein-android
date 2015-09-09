package com.example.efradelos.divein.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.example.efradelos.divein.R;
import com.example.efradelos.divein.divers.DiversFragment;
import com.example.efradelos.divein.dives.DivesFragment;

import java.util.ArrayList;

public class MainPagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<String> titles = new ArrayList<>();

    public MainPagerAdapter(Context cx, FragmentManager fm) {
        super(fm);
        fragments.add(new DiversFragment());
        fragments.add(new DivesFragment());
        titles.add(cx.getString(R.string.action_divers));
        titles.add(cx.getString(R.string.action_dives));
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();

    }

}
