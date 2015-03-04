package com.moolu.plugins.eps;

import android.content.Context;

/**
 * Interface class of DeviceStatus.
 * Created by Nanan on 3/4/2015.
 */
public interface DeviceStatusEPSProvider {
    /**
     * Call recalculation before get the device status
     * @return Device status info. Should check {@link DeviceStatusEPS#statusCalculatedSuccessfully} before use
     */
    DeviceStatusEPS calculateDeviceStatus();

    boolean init(Context context);

    boolean providerFinalize();
    /**
     * Get the saved device status without recalculation
     * @return Device status info. Should check {@link DeviceStatusEPS#statusCalculatedSuccessfully} before use
     */
    DeviceStatusEPS getDeviceStatus();
}
