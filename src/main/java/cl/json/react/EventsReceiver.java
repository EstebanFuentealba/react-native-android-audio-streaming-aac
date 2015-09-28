package cl.json.react;

import cl.json.react.AACStreamingModule;
import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import javax.annotation.Nullable;


public class EventsReceiver extends BroadcastReceiver{
  private AACStreamingModule module;
  public EventsReceiver(AACStreamingModule module) {
    this.module = module;

  }
  @Override
  public void onReceive(Context context, Intent intent) {
    WritableMap params = Arguments.createMap();
    params.putString("eventName" , intent.getAction());
    this.module.sendEvent(this.module.getReactApplicationContextModule(), "streamingEvent", params);
    /*
    if (intent.getAction().equals(Mode.BUFFERING_START)) {
      this.module.sendEvent(this.module.getReactApplicationContextModule(), "bufferingStart", params);
    } else if (intent.getAction().equals(Mode.CONNECTING)) {
      this.module.sendEvent(this.module.getReactApplicationContextModule(), "connecting", params);
    } else if (intent.getAction().equals(Mode.START_PREPARING)) {
      this.module.sendEvent(this.module.getReactApplicationContextModule(), "startPreparing", params);
    } else if (intent.getAction().equals(Mode.PREPARED)) {
      this.module.sendEvent(this.module.getReactApplicationContextModule(), "prepared", params);
    } else if (intent.getAction().equals(Mode.STARTED)) {
      this.module.sendEvent(this.module.getReactApplicationContextModule(), "started", params);
    } else if (intent.getAction().equals(Mode.PLAYING)) {
      this.module.sendEvent(this.module.getReactApplicationContextModule(), "playing", params);
    } else if (intent.getAction().equals(Mode.STOPPED)) {
      this.module.sendEvent(this.module.getReactApplicationContextModule(), "stopped", params);
    } else if (intent.getAction().equals(Mode.COMPLETED)) {
      this.module.sendEvent(this.module.getReactApplicationContextModule(), "completed", params);
    } else if (intent.getAction().equals(Mode.ERROR)) {
      this.module.sendEvent(this.module.getReactApplicationContextModule(), "error", params);
    } else if (intent.getAction().equals(Mode.BUFFERING_END)) {
      this.module.sendEvent(this.module.getReactApplicationContextModule(), "bufferingEnd", params);
    } else if (intent.getAction().equals(Mode.ALBUM_UPDATED)) {
      this.module.sendEvent(this.module.getReactApplicationContextModule(), "albumUpdate", params);
    }
    */
  }
}
