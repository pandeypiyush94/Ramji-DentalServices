package ramjidentalcom.ramjidentalservices;

import android.app.Application;

/**
 * Created by Rak on 02-Aug-16.
 */
public class ReceiverInitiator extends Application {

    private static ReceiverInitiator mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized ReceiverInitiator getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}


