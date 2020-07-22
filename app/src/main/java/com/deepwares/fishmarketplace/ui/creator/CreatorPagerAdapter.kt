package com.deepwares.fishmarketplace.ui.creator

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class CreatorPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {
    override fun getItem(position: Int): Fragment {
        return AddInventoryStepFragment(CreatorSteps.values()[position].layout)
    }

    override fun getCount(): Int {
        return CreatorSteps.values().size
    }

}