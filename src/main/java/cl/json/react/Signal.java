package cl.json.react;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.app.Service;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;
import android.content.Intent;
import android.content.IntentFilter;
import android.app.PendingIntent;
import android.app.NotificationManager;
import android.content.ServiceConnection;

import com.spoledge.aacdecoder.MultiPlayer;
import com.spoledge.aacdecoder.PlayerCallback;


public class Signal extends Service implements OnErrorListener,
                                                    OnCompletionListener,
                                                    OnPreparedListener,
                                                    OnInfoListener,
                                                    PlayerCallback {



  // Notification
  private Class<?> clsActivity;
  private static final int NOTIFY_ME_ID = 696969;
  private NotificationCompat.Builder notifyBuilder;
  private NotificationManager notifyManager = null;
  public static RemoteViews remoteViews;
  private MultiPlayer aacPlayer;

  private static final int AAC_BUFFER_CAPACITY_MS = 2500;
	private static final int AAC_DECODER_CAPACITY_MS = 700;

  public static final String  BROADCAST_PLAYBACK_STOP = "stop",
                              BROADCAST_PLAYBACK_PLAY = "pause",
                              BROADCAST_EXIT = "exit";

  private final Handler handler = new Handler();
	private final IBinder binder = new RadioBinder();
  private final SignalReceiver receiver = new SignalReceiver(this);
  private Context context;
  private String streamingURL;
  private isPlaying = false;


  public void setData(Context context, Class<?> clsActivity) {
    this.context = context;
    this.clsActivity = clsActivity;
  }
  @Override
	public void onCreate() {

    IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_PLAYBACK_STOP);
        intentFilter.addAction(BROADCAST_PLAYBACK_PLAY);
        intentFilter.addAction(BROADCAST_EXIT);
        registerReceiver(this.receiver, intentFilter);
    try {
        this.aacPlayer = new MultiPlayer(this, AAC_BUFFER_CAPACITY_MS, AAC_DECODER_CAPACITY_MS);
    } catch (UnsatisfiedLinkError e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    this.notifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    try {
      java.net.URL.setURLStreamHandlerFactory( new java.net.URLStreamHandlerFactory(){
          public java.net.URLStreamHandler createURLStreamHandler( String protocol ) {
              if ("icy".equals( protocol )) {
                  return new com.spoledge.aacdecoder.IcyURLStreamHandler();
              }
              return null;
          }
      });
    } catch (Throwable t) {
    }

  }
  public void setURLStreaming(String streamingURL) {
    this.streamingURL = streamingURL;
  }
  public void play() {
    this.aacPlayer.playAsync(this.streamingURL);
  }
  public void stop() {
    if (this.isPlaying) {
      this.aacPlayer.stop();
    }
  }
  public NotificationManager getNotifyManager() {
    return notifyManager;
  }

  public class RadioBinder extends Binder {
    public Signal getService() {
      return Signal.this;
    }
  }

  public void showNotification() {

    Bitmap bitlogo= BitmapFactory.decodeResource(context.getResources(),
  			    R.drawable.streaming_notification_default_icon);
    //Bitmap bitplay= BitmapFactory.decodeResource(context.getResources(),R.drawable.streaming_notification_default_play);
    //Bitmap bitstop= BitmapFactory.decodeResource(context.getResources(),R.drawable.streaming_notification_default_stop);
		remoteViews =new RemoteViews(context.getPackageName(),
	            R.layout.streaming_notification_player);
		notifyBuilder = new NotificationCompat.Builder(this.context)
				.setSmallIcon(R.drawable.streaming_notification_default_icon)
				.setContentText("")
        .setOngoing(true)
				.setContent(remoteViews);

		Intent resultIntent = new Intent(this.context, this.clsActivity);
		resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this.context);
		stackBuilder.addParentStack(this.clsActivity);
		stackBuilder.addNextIntent(resultIntent);

		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);

		notifyBuilder.setContentIntent(resultPendingIntent);
		//to use custom notification
		remoteViews.setOnClickPendingIntent(R.id.btn_streaming_notification_play, makePendingIntent(BROADCAST_PLAYBACK_PLAY));
		remoteViews.setOnClickPendingIntent(R.id.btn_streaming_notification_stop, makePendingIntent(BROADCAST_EXIT));
		//remoteViews.setTextViewText(R.id.textView1, information.RadioName);
		remoteViews.setImageViewBitmap(R.id.streaming_icon, bitlogo);
    //remoteViews.setImageViewBitmap(R.id.btn_streaming_notification_play, bitplay);
    //remoteViews.setImageViewBitmap(R.id.btn_streaming_notification_stop, bitstop);
		notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notifyManager.notify(NOTIFY_ME_ID, notifyBuilder.build());
  }

  private PendingIntent makePendingIntent(String broadcast) {
      Intent intent = new Intent(broadcast);
      return PendingIntent.getBroadcast(this.context, 0, intent, 0);
  }
  public void clearNotification() {
		if (notifyManager != null)
      notifyManager.cancel(NOTIFY_ME_ID);
	}

	public void exitNotification() {
    notifyManager.cancelAll();
		clearNotification();
		notifyBuilder = null;
		notifyManager = null;
	}
  public boolean isConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

  @Override
	public IBinder onBind(Intent intent) {
        return binder;
	}

  @Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return Service.START_NOT_STICKY;
	}
  @Override
	public void onPrepared(MediaPlayer _mediaPlayer) {

	}

	@Override
	public void onCompletion(MediaPlayer mediaPlayer) {

	}
  @Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {

		return false;
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		switch (what) {
		case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
			//Log.v("ERROR", "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK "	+ extra);
			break;
		case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
			//Log.v("ERROR", "MEDIA ERROR SERVER DIED " + extra);
			break;
		case MediaPlayer.MEDIA_ERROR_UNKNOWN:
			//Log.v("ERROR", "MEDIA ERROR UNKNOWN " + extra);
			break;
		}
		return false;
	}

  @Override
  public void playerStarted() {
  //  TODO
  }
  @Override
  public void playerPCMFeedBuffer(boolean isPlaying, int bufSizeMs, int bufCapacityMs) {
    float percent = bufSizeMs * 100 / bufCapacityMs;
    this.isPlaying = isPlaying;
    if (this.isPlaying == true) {
      if (bufSizeMs < AAC_BUFFER_CAPACITY_MS) {
        this.isPlaying = false;
        //buffering
      } else {
        this.isPlaying = true;
        //playing
      }
    } else {
      //buffering
    }
  }
  @Override
  public void playerException( final Throwable t) {
    this.isPlaying = false;
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
    this.isPlaying = false;
  //  TODO
  }


}
