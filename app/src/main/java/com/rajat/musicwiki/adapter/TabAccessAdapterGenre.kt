package com.rajat.musicwiki.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.rajat.musicwiki.fragments.Albums
import com.rajat.musicwiki.fragments.Artists
import com.rajat.musicwiki.fragments.Tracks

class TabAccessAdapterGenre (fm : FragmentManager, private val tag : String) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        if(position==0){
            return Albums(tag)
        }
        else if (position==1){
            return Artists(tag)
        }
        else if(position == 2){
            return Tracks(tag)
        }
        else{
            return Albums(tag)
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        if(position == 0){
            return "Albums"
        }
        else if (position == 1){
            return "Artists"
        }
        else if(position == 2){
            return "Tracks"
        }
        else{
            return null
        }
    }
}