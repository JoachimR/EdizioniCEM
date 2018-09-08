package de.reiss.edizioni.main.viewpager

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import de.reiss.edizioni.DaysPositionUtil
import de.reiss.edizioni.main.content.DailyTextFragment

class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getCount() = DaysPositionUtil.DAYS_OF_TIME

    override fun getItem(position: Int) = DailyTextFragment.createInstance(position)

}
