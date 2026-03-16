package top.niunaijun.blackbox.fake.frameworks;

import android.os.IBinder;
import android.os.IInterface;

import java.lang.reflect.ParameterizedType;

import top.niunaijun.blackbox.BlackBoxCore;
import top.niunaijun.blackbox.utils.Reflector;

/**
 * Created by BlackBox on 2022/3/23.
 */
public abstract class BlackManager<Service extends IInterface> {
    public static final String TAG = "BlackManager";

    private Service mService;

    protected abstract String getServiceName();

    /*
     * Retrieves the service interface safely.
     * Prevents NullPointerException during binder death notifications.
     */
    public Service getService() {
        if (mService != null && mService.asBinder() != null && mService.asBinder().pingBinder() && mService.asBinder().isBinderAlive()) {
            return mService;
        }
        
        try {
            IBinder rawBinder = BlackBoxCore.get().getService(getServiceName());
            if (rawBinder == null) {
                return null;
            }

            mService = Reflector.on(getTClass().getName() + "$Stub").method("asInterface", IBinder.class)
                    .call(rawBinder);

            if (mService != null && mService.asBinder() != null) {
                // Capture the binder in a final variable to avoid NPE in binderDied
                final IBinder finalBinder = mService.asBinder();
                
                finalBinder.linkToDeath(new IBinder.DeathRecipient() {
                    @Override
                    public void binderDied() {
                        finalBinder.unlinkToDeath(this, 0);
                        mService = null;
                    }
                }, 0);
            }
            return mService;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private Class<Service> getTClass() {
        return (Class<Service>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
