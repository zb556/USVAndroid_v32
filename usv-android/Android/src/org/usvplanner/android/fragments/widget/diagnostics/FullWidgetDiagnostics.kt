package org.usvplanner.android.fragments.widget.diagnostics

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.usvplanner.android.R
import org.usvplanner.android.fragments.widget.TowerWidget
import org.usvplanner.android.fragments.widget.TowerWidgets
import org.usvplanner.android.view.viewPager.TabPageIndicator

/**
 * Created by Fredia Huya-Kouadio on 8/30/15.
 */
public class FullWidgetDiagnostics : TowerWidget(){

    private val viewAdapter by lazy(LazyThreadSafetyMode.NONE) {
        context?.let { DiagnosticViewAdapter(it, childFragmentManager) }
    }

    private var tabPageIndicator: TabPageIndicator? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_full_widget_diagnostics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        val viewPager = view.findViewById<ViewPager>(R.id.diagnostics_view_pager) as ViewPager
        viewPager?.adapter = viewAdapter
        viewPager?.offscreenPageLimit = 2

        tabPageIndicator = view.findViewById<TabPageIndicator>(R.id.pager_title_strip) as TabPageIndicator
        tabPageIndicator?.setViewPager(viewPager)
    }

    override fun getWidgetType() = TowerWidgets.VEHICLE_DIAGNOSTICS

    override fun onApiConnected() {}

    override fun onApiDisconnected() {}

    public fun setAdapterViewTitle(position: Int, title: CharSequence){
        viewAdapter?.setViewTitles(position, title)
        tabPageIndicator?.notifyDataSetChanged()
    }
}