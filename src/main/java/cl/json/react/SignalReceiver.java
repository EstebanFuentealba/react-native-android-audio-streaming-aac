package cl.json.react;
import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;
class SignalReceiver extends BroadcastReceiver {
  private Signal signal;
  public SignalReceiver(Signal signal) {
    super();
    this.signal = signal;
  }
  @Override
  public void onReceive(Context context, Intent intent)
  {
      String action = intent.getAction();
      if (action.equals(Signal.BROADCAST_PLAYBACK_PLAY)) {
        if(!this.signal.isPlaying){
          this.signal.play();
          this.signal.isPlaying = true;
        }
      } else if (action.equals(Signal.BROADCAST_EXIT)) {
        this.signal.getNotifyManager().cancelAll();
        this.signal.stop();
        this.signal.exitNotification();
      }
  }
}
