package com.example.prototypes;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class SectionStatePagerAdapter extends FragmentStatePagerAdapter {

    private final ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
    private final ArrayList<String> fragmentTitleList = new ArrayList<String>();
    public SectionStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
