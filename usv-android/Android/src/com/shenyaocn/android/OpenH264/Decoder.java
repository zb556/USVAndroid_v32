package com.shenyaocn.android.OpenH264;

import android.view.Surface;

/**
 * Created by ShenYao on 2015/12/17.
 */
public class Decoder {
	private long id = 0;

	public boolean hasCreated() {
		return (id != 0);
	}

	public void create(final Surface surface) {
		id = createDecoder(surface);
	}

	@Override
	public void finalize() {
		destroy();
	}

	public synchronized void destroy() {
		if(id != 0) {
			destroyDecoder(id);
		}
		id = 0;
	}

	public synchronized void decode(byte[] h264, int h264Length) {
		if(id == 0)
			return;

		decodeFrame(id, h264, h264Length);
	}

	private native long createDecoder(final Surface surface);
	private native void destroyDecoder(long id);

	private native void decodeFrame(long id, byte[] h264, int h264Length);

	static {
		System.loadLibrary("openh264");
		System.loadLibrary("openh264jni");
	}
}
