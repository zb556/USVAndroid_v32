package org.usvplanner.android.fragments.widget.video

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Matrix
import android.graphics.SurfaceTexture
import android.hardware.usb.UsbDevice
import android.os.Bundle
import android.util.Log
import android.view.Surface
import android.view.TextureView
import android.view.TextureView.SurfaceTextureListener
import android.view.View
import android.widget.TextView
import com.o3dr.services.android.lib.drone.attribute.AttributeEvent
import com.shenyaocn.android.UartVideo.VideoClient
import com.shenyaocn.android.UartVideo.VideoClient.ClientListener
import com.shenyaocn.android.UartVideo.usb.DeviceFilter
import com.shenyaocn.android.UartVideo.usb.USBMonitor
import org.usvplanner.android.R
import org.usvplanner.android.dialogs.UVCDialog
import org.usvplanner.android.fragments.widget.TowerWidget
import org.usvplanner.android.fragments.widget.TowerWidgets
import com.shenyaocn.android.OpenH264.Decoder
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

abstract class BaseUVCVideoWidget : TowerWidget(){

    companion object {

        private val filter = initFilter()

        private fun initFilter(): IntentFilter {
            val temp = IntentFilter()
            temp.addAction(AttributeEvent.STATE_CONNECTED)
            return temp
        }

    }

    protected val DEBUG = false

    protected val TAG = "BaseUVCVideoWidget"

    override fun getWidgetType() = TowerWidgets.UVC_VIDEO

    // for thread pool
    protected val CORE_POOL_SIZE = 1   // initial/minimum threads
    protected val MAX_POOL_SIZE = 4    // maximum threads
    protected val KEEP_ALIVE_TIME = 10 // time periods while keep the idle thread
    protected val EXECUTER: ThreadPoolExecutor = ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME.toLong(),
            TimeUnit.SECONDS, LinkedBlockingQueue<Runnable>())

    //Aspect ratio
    protected val ASPECT_RATIO_4_3: Float = 3f / 4f
    protected val ASPECT_RATIO_16_9: Float = 9f / 16f
    protected val ASPECT_RATIO_21_9: Float = 9f / 21f
    protected val ASPECT_RATIO_1_1: Float = 1f / 1f
    protected var aspectRatio: Float = ASPECT_RATIO_4_3

    // for accessing USB and USB camera
    protected var mUSBMonitor: USBMonitor? = null
    private var video: VideoClient? = null
//    protected var mUVCCamera: Video? = null
    protected var isPreview:Boolean = false
    protected var usbDevice: UsbDevice? = null
    protected var mPreviewSurface: Surface? = null
    private var decoder: Decoder? = null


    protected val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                AttributeEvent.STATE_CONNECTED -> {
                    startVideoStreaming()
                }
            }
        }
    }

    protected val textureView by lazy(LazyThreadSafetyMode.NONE) {
        view?.findViewById<TextureView>(R.id.uvc_video_view) as TextureView
    }

    protected val videoStatus by lazy(LazyThreadSafetyMode.NONE) {
        view?.findViewById<TextView>(R.id.uvc_video_status) as TextView
    }

    override fun onApiConnected() {
        if (DEBUG) Log.v(TAG, "onApiConnected:")

        broadcastManager.registerReceiver(receiver, filter)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        textureView?.setSurfaceTextureListener(mSurfaceTextureListener)

        video = VideoClient(activity)

        video?.setClientListener(object : ClientListener {
            override fun onH264Received(h264: ByteArray) { // Raw H.264 data callback
                if (decoder == null) {
                    decoder = Decoder()
                }
                if (!decoder?.hasCreated()!!) {
                    if (mPreviewSurface != null) {
                        mPreviewSurface?.release()
                        mPreviewSurface = null
                    }
                    val st = textureView.surfaceTexture
                    if (st != null) {
                        mPreviewSurface = Surface(st)
                        decoder?.create(mPreviewSurface)
                    }
                }
                decoder?.decode(h264, h264.size)
            }

            override fun onGPSReceived(bytes: ByteArray) { // GPS data callback
            }

            override fun onDataReceived(bytes: ByteArray) { // Transmission data callback
            }

            override fun onDebugReceived(bytes: ByteArray) { // Debug data callback
            }

            override fun onError(error: String) { // Error data callback
            }
        })

        mUSBMonitor = USBMonitor(context, mOnDeviceConnectListener)
        val filters = DeviceFilter.getDeviceFilters(activity, R.xml.uvc_device_filter)
        mUSBMonitor?.setDeviceFilter(filters)
        mUSBMonitor?.register()

        if (DEBUG) Log.v(TAG, "onViewCreated:")

    }

    override fun onResume() {
        super.onResume()
        if (DEBUG) Log.v(TAG, "onResume:")
        mUSBMonitor = USBMonitor(context, mOnDeviceConnectListener)
        val filters = DeviceFilter.getDeviceFilters(activity, R.xml.uvc_device_filter)
        mUSBMonitor?.setDeviceFilter(filters)
        mUSBMonitor?.register()
        aspectRatio = appPrefs.uvcVideoAspectRatio
    }

    override fun onPause() {
        super.onPause()
        if (DEBUG) Log.v(TAG, "onPause:")

        mUSBMonitor?.unregister()
        mUSBMonitor?.destroy()
        mUSBMonitor = null
        appPrefs.uvcVideoAspectRatio = aspectRatio
    }

    override fun onDestroy() {
        super.onDestroy()
        if (DEBUG) Log.v(TAG, "onDestroy:")

        disconnected()


        isPreview = false
        if (mUSBMonitor != null) {
            mUSBMonitor?.unregister()
            mUSBMonitor?.destroy()
            mUSBMonitor = null
        }
    }

    override fun onApiDisconnected() {
        broadcastManager.unregisterReceiver(receiver)
        if (DEBUG) Log.v(TAG, "onApiDisconnected:")
    }


    private val mOnDeviceConnectListener = object : USBMonitor.OnDeviceConnectListener {
        override fun onAttach(device: UsbDevice?) {
            if (DEBUG) Log.v(TAG, "onAttach:")

            if (deviceHasConnected(device) || usbDevice != null) return

            try {
                if (device == null) {
                    val devices = mUSBMonitor?.deviceList
                    if (devices?.size == 1) {
                        mUSBMonitor?.requestPermission(devices[0])
                    }
                } else {
                    UVCDialog.showDialog(activity, mUSBMonitor)
                    mUSBMonitor?.requestPermission(device)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun onConnect(device: UsbDevice, ctrlBlock: USBMonitor.UsbControlBlock, createNew: Boolean) {
            if (DEBUG) Log.v(TAG, "onConnect:")

            if(!deviceIsUartVideoDevice(device)) {
                return;
            }
            if(deviceHasConnected(device)) {
                return;
            }
            synchronized (this) {
                if(deviceIsUartVideoDevice(device)) {
                    if (video != null) {
                        if(video?.openUsbSerial(device)!!)
                            usbDevice = device
                    };
                }
            }
            videoStatus?.visibility = View.GONE

//            mUVCCamera?.destroy()
//            mUVCCamera = UVCCamera()
//
//            videoStatus?.visibility = View.GONE
//
//            EXECUTER.execute(object : Runnable {
//                override fun run() {
//                    mUVCCamera?.open(ctrlBlock)
//
//                    if (DEBUG) Log.i(TAG, "supportedSize:" + mUVCCamera?.getSupportedSize())
//
//                    mPreviewSurface?.release()
//                    mPreviewSurface = null
//
//                    try {
//                        mUVCCamera?.setPreviewSize(UVCCamera.DEFAULT_PREVIEW_WIDTH, UVCCamera.DEFAULT_PREVIEW_HEIGHT, UVCCamera.FRAME_FORMAT_MJPEG)
//                    } catch (e: IllegalArgumentException) {
//                        try {
//                            // fallback to YUV mode
//                            mUVCCamera?.setPreviewSize(UVCCamera.DEFAULT_PREVIEW_WIDTH, UVCCamera.DEFAULT_PREVIEW_HEIGHT, UVCCamera.DEFAULT_PREVIEW_MODE)
//                        } catch (e1: IllegalArgumentException) {
//                            mUVCCamera?.destroy()
//                            mUVCCamera = null
//                        }
//                    }
//
//                    if (mUVCCamera != null) {
//                        val st = textureView?.getSurfaceTexture()
//                        if (st != null) {
//                            mPreviewSurface = Surface(st)
//                            mUVCCamera?.setPreviewDisplay(mPreviewSurface)
//                            mUVCCamera?.startPreview()
//                            isPreview = true
//                        }
//
//                    }
//                }
//            })
        }

        override fun onDisconnect(device: UsbDevice, ctrlBlock: USBMonitor.UsbControlBlock) {
            if (DEBUG) Log.v(TAG, "onDisconnect:")
            if (!deviceIsUartVideoDevice(device)) {
                return
            }

            if (!deviceHasConnected(device)) {
                return
            }

            disconnected()
            mPreviewSurface?.release()
            mPreviewSurface = null
            isPreview = false

            videoStatus?.visibility = View.VISIBLE
        }

        override fun onDettach(device: UsbDevice) {
            if (DEBUG) Log.v(TAG, "onDettach:")

            if (!deviceIsUartVideoDevice(device)) {
                return
            }

            if (!deviceHasConnected(device)) {
                return
            }

            disconnected()
            mPreviewSurface?.release()
            mPreviewSurface = null
            isPreview = false

            videoStatus?.visibility = View.VISIBLE
        }

        override fun onCancel() {
            videoStatus?.visibility = View.VISIBLE
        }
    }
    protected fun deviceHasConnected(device: UsbDevice?): Boolean {
        return device != null && device == usbDevice
    }

    protected fun deviceIsUartVideoDevice(device: UsbDevice?): Boolean {
        return device != null && device.vendorId == 4292 && device.productId == 60000
    }

    @Synchronized
    protected fun disconnected() {
        video?.stopPlayback()
        usbDevice = null
        if (decoder != null) {
            decoder?.destroy()
            decoder = null
        }
    }
    protected fun startVideoStreaming(){
        if (DEBUG) Log.v(TAG, "startVideoStreaming:")

        if (usbDevice != null) {
            mUSBMonitor?.requestPermission(usbDevice)
        } else {
            //UVC Device Filter
            val uvcFilter = DeviceFilter.getDeviceFilters(activity, R.xml.uvc_device_filter)
            val uvcDevices = mUSBMonitor?.getDeviceList(uvcFilter[0])
            if (uvcDevices == null || uvcDevices.isEmpty()) {
                if (DEBUG) Log.v(TAG, getString(R.string.uvc_device_no_device))
            } else {
                if (uvcDevices.size.compareTo(1) == 0) {
                    mUSBMonitor?.requestPermission(uvcDevices.get(0))
                } else {
                    UVCDialog.showDialog(activity, mUSBMonitor)
                }
            }
        }

    }

    private val mSurfaceTextureListener: SurfaceTextureListener = object : SurfaceTextureListener {
        override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {}
        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {}
        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
            if (mPreviewSurface != null) {
                mPreviewSurface?.release()
                mPreviewSurface = null
            }
            return true
        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}
    }

    protected fun adjustAspectRatio(textureView: TextureView) {
        val viewWidth = textureView.width
        val viewHeight = textureView.height

        val newWidth: Int
        val newHeight: Int
        if (viewHeight > (viewWidth * aspectRatio)) {
            //limited by narrow width; restrict height
            newWidth = viewWidth
            newHeight = (viewWidth * aspectRatio).toInt()
        } else {
            //limited by short height; restrict width
            newWidth = (viewHeight / aspectRatio).toInt();
            newHeight = viewHeight
        }

        val xoff = (viewWidth - newWidth) / 2f
        val yoff = (viewHeight - newHeight) / 2f

        val txform = Matrix();
        textureView.getTransform(txform);
        txform.setScale((newWidth.toFloat() / viewWidth), newHeight.toFloat() / viewHeight);

        txform.postTranslate(xoff, yoff);
        textureView.setTransform(txform);
    }



}