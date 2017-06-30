package br.com.chagasappandroid.vipmensagem.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import br.com.chagasappandroid.vipmensagem.fragments.ContatosFragment;
import br.com.chagasappandroid.vipmensagem.fragments.ConversasFragment;


public class TabAdapter extends FragmentStatePagerAdapter {

    private String[] tabs = {"CONVERSAS", "CONTATOS"};

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){

            case 0:
                fragment = new ConversasFragment();
                break;

            case 1:
                fragment = new ContatosFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return tabs.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }
}
