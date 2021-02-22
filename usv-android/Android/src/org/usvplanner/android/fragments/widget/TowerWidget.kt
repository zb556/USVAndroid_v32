package org.usvplanner.android.fragments.widget

import org.usvplanner.android.fragments.helpers.ApiListenerFragment

/**
 * Created by Fredia Huya-Kouadio on 8/28/15.
 */
public abstract class TowerWidget : ApiListenerFragment() {

    abstract fun getWidgetType(): TowerWidgets
}