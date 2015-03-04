package com.moolu.plugins.eps;

import android.content.Context;

/**
 * Created by Nanan on 3/4/2015.
 */
public class DeviceStatusProviderBinderImpl implements DeviceStatusProviderBinder{
    @Override
    public DeviceStatusEPSProvider createProvider(Context context) {
        //return DeviceStatusProviderImpl.getInstance(context);
        return null;
    }
}
