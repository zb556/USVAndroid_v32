package org.usvplanner.android.tlog.interfaces

import org.usvplanner.android.tlog.viewers.TLogViewer

/**
 * @author ne0fhyk (Fredia Huya-Kouadio)
 */
interface TLogDataProvider {
    fun registerForTLogDataUpdate(subscriber: TLogViewer)
    fun unregisterForTLogDataUpdate(subscriber: TLogViewer)
}