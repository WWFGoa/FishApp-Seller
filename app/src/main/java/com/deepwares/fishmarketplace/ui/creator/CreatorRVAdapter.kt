package com.deepwares.fishmarketplace.ui.creator

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter

class CreatorRVAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return CreatorSteps.values().size
    }

    override fun createFragment(position: Int): Fragment {
      return  AddInventoryStepFragment(CreatorSteps.values()[position].layout)
    }

}