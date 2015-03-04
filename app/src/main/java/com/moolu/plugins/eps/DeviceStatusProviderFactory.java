package com.moolu.plugins.eps;

import android.content.Context;

/**
 * Used to create an instance of {@link DeviceStatusProvider}. This is done by attempted
 * to load an instance of a class named {@value #DEVICESTATUS_PROVIDER_BINDER_CLASS_NAME}
 * from the class path.
 *
 * Created by Nanan on 3/4/2015.
 */
public class DeviceStatusProviderFactory {

    private final static String DEVICESTATUS_PROVIDER_BINDER_CLASS_NAME = "com.moolu.plugins.eps.DeviceStatusProviderBinderImpl";

    /**
     * DeviceStatusProviderFactoryHolder is loaded on the first execution of
     * Singleton.getInstance() or the first access to
     * DeviceStatusProviderFactoryHolder.INSTANCE, not before.
     */
    private static class DeviceStatusProviderFactoryHolder {
        public static final DeviceStatusProviderFactory INSTANCE = new DeviceStatusProviderFactory();
    }

    public static DeviceStatusProviderFactory getInstance() {
        return DeviceStatusProviderFactoryHolder.INSTANCE;
    }

    private final DeviceStatusProviderBinder binder;

    // Private constructor prevents instantiation from other classes
    private DeviceStatusProviderFactory() {
        binder = instantiateBinder();
    }

    /**
     * @return An instance of {@link DeviceStatusProvider}
     */
    public DeviceStatusEPSProvider getProvider(Context context) {
        return binder.createProvider(context);
    }

    /**
     * Attempts to load {@value #DEVICESTATUS_PROVIDER_BINDER_CLASS_NAME} from the class
     * path. Throws an {@link IllegalStateException} if this isn't possible.
     *
     * @return an instance of {@link DeviceStatusProviderBinder}.
     */
    private DeviceStatusProviderBinder instantiateBinder() {
        try {
            @SuppressWarnings("unchecked")
            Class<DeviceStatusProviderBinder> binderClazz = (Class<DeviceStatusProviderBinder>) Class
                    .forName(DEVICESTATUS_PROVIDER_BINDER_CLASS_NAME);

            return binderClazz.newInstance();
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(DEVICESTATUS_PROVIDER_BINDER_CLASS_NAME
                    + " is not available", e);
        } catch (InstantiationException e) {
            throw new IllegalStateException("Unable to instantiate "
                    + DEVICESTATUS_PROVIDER_BINDER_CLASS_NAME, e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Unable to instantiate "
                    + DEVICESTATUS_PROVIDER_BINDER_CLASS_NAME, e);
        }
    }
}
