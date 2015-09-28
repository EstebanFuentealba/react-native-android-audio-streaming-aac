package cl.json.react;

import android.content.Intent;

import cl.json.react.Signal;
import android.content.ServiceConnection;
import android.content.ComponentName;
import android.os.IBinder;
import android.content.Context;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class AACStreamingModule extends ReactContextBaseJavaModule implements ServiceConnection {

  private ReactApplicationContext context;
  private Class<?> clsActivity;
  private static Signal signal;
  private Intent bindIntent;


  public AACStreamingModule(ReactApplicationContext reactContext, Class<?> cls) {
    super(reactContext);
    this.clsActivity = cls;
    this.context = reactContext;
    try {
			bindIntent = new Intent(this.context, Signal.class);
			this.context.bindService(bindIntent, this, Context.BIND_AUTO_CREATE);
		} catch (Exception e) {

		}
  }
  @Override
	public void onServiceConnected(ComponentName className, IBinder service) {
		signal = ((Signal.RadioBinder) service).getService();
    signal.setData(this.context, clsActivity);
    signal.showNotification();
	}

	@Override
	public void onServiceDisconnected(ComponentName className) {
		signal = null;
	}
  @Override
  public String getName() {
    return "AACStreamingAndroid";
  }

  private void sendEvent(ReactContext reactContext,String eventName, @Nullable WritableMap params) {
    this.context
        .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
        .emit(eventName, params);
  }

  @ReactMethod
  public void setURLStreaming(String streamingURL) {
    signal.setURLStreaming(streamingURL); // URL of MP3 or AAC stream
  }
  @ReactMethod
  public void play() {
    signal.play();
  }
  @ReactMethod
  public void stop() {
    signal.stop();
  }
  @ReactMethod
  public void destroyNotification() {
    signal.exitNotification();
  }


}
