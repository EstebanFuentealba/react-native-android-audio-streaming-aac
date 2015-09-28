package cl.json.react;

import android.content.Intent;

import cl.json.react.Signal;
import cl.json.react.Mode;

import android.content.IntentFilter;
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
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import javax.annotation.Nullable;


public class AACStreamingModule extends ReactContextBaseJavaModule implements ServiceConnection {

  private ReactApplicationContext context;

  private Class<?> clsActivity;
  private static Signal signal;
  private Intent bindIntent;
  private String streamingURL;
  public AACStreamingModule(ReactApplicationContext reactContext, Class<?> cls) {
    super(reactContext);
    this.clsActivity = cls;
    this.context = reactContext;
  }

  public ReactApplicationContext getReactApplicationContextModule() {
    return this.context;
  }
  public Class<?> getClassActivity() {
    return this.clsActivity;
  }
  public void stopOncall(){
		this.signal.stop();
	}
  public Signal getSignal() {
    return signal;
  }
  public void sendEvent(ReactContext reactContext,String eventName, @Nullable WritableMap params) {
    this.context
        .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
        .emit(eventName, params);
  }
  @Override
  public String getName() {
    return "AACStreamingAndroid";
  }
  @Override
	public void onServiceConnected(ComponentName className, IBinder service) {
		signal = ((Signal.RadioBinder) service).getService();
    signal.setURLStreaming(streamingURL); // URL of MP3 or AAC stream
    signal.setData(this.context, this.clsActivity, this);
    WritableMap params = Arguments.createMap();
    sendEvent(this.getReactApplicationContextModule(), "streamingOpen", params);
    signal.showNotification();

	}

	@Override
	public void onServiceDisconnected(ComponentName className) {
		signal = null;
	}


  @ReactMethod
  public void setURLStreaming(String streamingURL) {
    this.streamingURL = streamingURL;

    try {
			bindIntent = new Intent(this.context, Signal.class);
			this.context.bindService(bindIntent, this, Context.BIND_AUTO_CREATE);
		} catch (Exception e) {

		}
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
