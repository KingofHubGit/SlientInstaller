package com.wxpayface.os;

/**
 * @hide
 */
interface IWxDeviceManager {

	
    /**
     * @param mode
     *          1: update apk
     *          2: remove apk
     * @param source
     *          the source to be updated or to be removed.
     * @return  0:      OK
     *          -99:    permission denied
     *
     * **********************************************************************
     * NOTE: *** This method may block, so DO NOT called update() in Main Thread ***
     * **********************************************************************
     */
    int update(int mode, String source);

}
