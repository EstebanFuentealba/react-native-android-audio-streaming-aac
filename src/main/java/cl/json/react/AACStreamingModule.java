package cl.json.react;

import android.content.Context;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import android.media.AudioTrack;
import com.spoledge.aacdecoder.MultiPlayer;
import com.spoledge.aacdecoder.PlayerCallback;

public class AACStreamingModule extends ReactContextBaseJavaModule implements PlayerCallback {

  private Context context;
  private MultiPlayer aacPlayer;
  public AACStreamingModule(ReactApplicationContext reactContext) {
    super(reactContext);

    this.context = (Context) reactContext;
    this.aacPlayer = new MultiPlayer(this);
  }

  @Override
  public String getName() {
    return "AACStreamingAndroid";
  }
  @Override
  public void playerStarted() {
    //  TODO
  }
  @Override
  public void playerPCMFeedBuffer(boolean isPlaying, int bufSizeMs, int bufCapacityMs) {
    float percent = bufSizeMs * 100 / bufCapacityMs;
  }
  @Override
  public void playerException( final Throwable t) {
    //  TODO
  }
  @Override
  public void playerMetadata( final String key, final String value ) {
    //  TODO
  }
  @Override
  public void playerAudioTrackCreated( AudioTrack atrack ) {
    //  TODO
  }
  @Override
  public void playerStopped(int perf) {
    //  TODO
  }

  @ReactMethod
  public void setURLStreaming(String streamingURL) {
    aacPlayer.playAsync(streamingURL); // URL of MP3 or AAC stream
  }
}
