package org.usvplanner.android.fragments.widget



import android.os.Bundle
import android.view.*
import org.usvplanner.android.R
import org.usvplanner.android.fragments.widget.video.BaseUVCVideoWidget


public class MiniWidgetUVCLinkVideo : BaseUVCVideoWidget() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_mini_widget_uvc_video, container, false)
        aspectRatio = ASPECT_RATIO_16_9
        adjustAspectRatio(textureView as TextureView)
    }

}