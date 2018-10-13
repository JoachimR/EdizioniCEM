package de.reiss.edizioni.main.viewpager

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.ViewGroup
import de.reiss.edizioni.DaysPositionUtil
import de.reiss.edizioni.main.content.DailyTextFragment

class ViewPagerAdapter(fragmentManager: FragmentManager,
                       private val listener: DailyTextFragmentFocusChangeListener) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getCount() = DaysPositionUtil.DAYS_OF_TIME

    override fun getItem(position: Int) = DailyTextFragment.createInstance(position)

    override fun setPrimaryItem(container: ViewGroup, position: Int, item: Any) {
        super.setPrimaryItem(container, position, item)
        if (item is DailyTextFragment) {
            listener.onPrimaryItemChange(item)
        }
    }
}
