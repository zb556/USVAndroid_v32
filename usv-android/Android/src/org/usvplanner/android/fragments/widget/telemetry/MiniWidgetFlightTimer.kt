package org.usvplanner.android.fragments.widget.telemetry

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.o3dr.services.android.lib.drone.attribute.AttributeEvent
import com.o3dr.services.android.lib.drone.attribute.AttributeEventExtra
import com.o3dr.services.android.lib.drone.attribute.AttributeType
import com.o3dr.services.android.lib.drone.mission.Mission
import com.o3dr.services.android.lib.drone.mission.MissionItemType
import com.o3dr.services.android.lib.drone.mission.item.MissionItem
import com.o3dr.services.android.lib.drone.mission.item.command.DoJump
import org.usvplanner.android.R
import org.usvplanner.android.dialogs.SupportYesNoDialog
import org.usvplanner.android.fragments.widget.TowerWidget
import org.usvplanner.android.fragments.widget.TowerWidgets
import java.lang.String

/**
 * Created by Fredia Huya-Kouadio on 9/20/15.
 */
public class MiniWidgetFlightTimer : TowerWidget(), SupportYesNoDialog.Listener {

    companion object {
        private val FLIGHT_TIMER_PERIOD = 1000L; //1 second

        @JvmStatic protected val RESET_TIMER_TAG = "reset_timer_tag"

        private val filter = initFilter()
        private var mission: Mission? = null
        private var missionCircleCnt:Int? = 1
        private var missionNow:Int? = 0
        private fun initFilter(): IntentFilter {
            val temp = IntentFilter()
            temp.addAction(AttributeEvent.STATE_UPDATED)
            temp.addAction(AttributeEvent.MISSION_ITEM_UPDATED)
            temp.addAction(AttributeEvent.MISSION_UPDATED)
            temp.addAction(AttributeEvent.MISSION_RECEIVED)
            return temp
        }
    }

    private val receiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context, intent: Intent){
            when(intent.action){
                AttributeEvent.STATE_UPDATED -> updateFlightTimer()
                AttributeEvent.MISSION_UPDATED, AttributeEvent.MISSION_RECEIVED -> updateMissionTotalCircles(true)
                AttributeEvent.MISSION_ITEM_UPDATED -> updateMissionNowCircle(intent)
            }
        }
    }

    private val flightTimeUpdater = object : Runnable{
        override fun run(){
            handler.removeCallbacks(this)
            val drone = drone
            if(!drone.isConnected)
                return

            val timeInSecs = drone.flightTime
            val mins = timeInSecs / 60L
            val secs = timeInSecs % 60L
            flightTimer?.text = String.format("%02d:%02d", mins, secs)

            handler.postDelayed(this, FLIGHT_TIMER_PERIOD)
        }
    }

    private val missionCirclesUpdater = object : Runnable{
        override fun run(){
            handler.removeCallbacks(this)
            val drone = drone
            if(!drone.isConnected)
                return
            missinoCircles?.text = String.format("%02d/%02d", missionNow, missionCircleCnt)

            handler.postDelayed(this, FLIGHT_TIMER_PERIOD)
        }
    }

    private val handler = Handler()

    private var flightTimer : TextView? = null
    private var missinoCircles : TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_mini_widget_flight_timer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        val context = activity?.applicationContext

        flightTimer = view.findViewById<TextView>(R.id.flight_timer) as TextView
        flightTimer?.setOnClickListener {
            //Bring up a dialog allowing the user to reset the timer.
            val resetTimerDialog = SupportYesNoDialog.newInstance(context, RESET_TIMER_TAG,
                    context?.getString(R.string.label_widget_flight_timer),
                    context?.getString(R.string.description_reset_flight_timer))
            resetTimerDialog.show(childFragmentManager, RESET_TIMER_TAG)
        }

        missinoCircles = view.findViewById(R.id.mission_circles) as TextView
    }

    override fun onDialogNo(dialogTag: kotlin.String?) {
    }

    override fun onDialogYes(dialogTag: kotlin.String?) {
        when(dialogTag){
            RESET_TIMER_TAG -> {
                drone.resetFlightTimer()
                updateFlightTimer()
            }
        }
    }

    override fun getWidgetType() = TowerWidgets.FLIGHT_TIMER

    override fun onApiConnected() {
        updateFlightTimer()
        updateMissionTotalCircles(false)
        broadcastManager.registerReceiver(receiver, filter)
    }

    override fun onApiDisconnected() {
        broadcastManager.unregisterReceiver(receiver)
    }

    private fun updateFlightTimer(){
        handler.removeCallbacks(flightTimeUpdater)
        if(drone.isConnected)
            flightTimeUpdater.run()
        else
            flightTimer?.text = "00:00"
    }
    /**
     * 更新当前圈数
     */
    private fun updateMissionNowCircle(intent: Intent){
        handler.removeCallbacks(missionCirclesUpdater)
        if(drone.isConnected) {
            mission = drone.getAttribute(AttributeType.MISSION)
            var nextWaypoint = intent.getIntExtra(AttributeEventExtra.EXTRA_MISSION_CURRENT_WAYPOINT, 0)
            if(nextWaypoint == 3) {
                missionNow = missionNow?.plus(1)
            }
            missionCirclesUpdater.run()
        }
        else
            missinoCircles?.text = "0/0"
    }

    /**
     * 更新总圈数
     */
    private fun updateMissionTotalCircles(initNowMission: Boolean){
        handler.removeCallbacks(missionCirclesUpdater)
        if(drone.isConnected) {
            val missionProxy = missionProxy
            if (missionProxy != null) {
                if(initNowMission) {
                    missionNow = 0;
                }
                if(missionProxy.missionItems.size > 0) {
                    var missionItem: MissionItem = missionProxy.missionItems[missionProxy.lastWaypoint - 1]
                    if (missionItem.type == MissionItemType.DO_JUMP) {
                        val item = missionItem as DoJump
                        missionCircleCnt = missionItem.repeatCount + 1
                    } else {
                        if(missionProxy.missionItems.size > 1) {
                            var missionItem: MissionItem = missionProxy.missionItems[missionProxy.lastWaypoint - 2]
                            if (missionItem.type == MissionItemType.DO_JUMP) {
                                val item = missionItem as DoJump
                                missionCircleCnt = missionItem.repeatCount + 1
                            } else {
                                missionCircleCnt = 1;
                            }
                        }
                    }
                }

            }
            missionCirclesUpdater.run()
        }
        else
            missinoCircles?.text = "0/0"
    }
}