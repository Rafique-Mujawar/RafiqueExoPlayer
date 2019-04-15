package media.rafique.com.rafiqueexoplayer;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Player.EventListener,
    View.OnClickListener {

  private static final String TAG = MainActivity.class.getName();
  private RenderersFactory renderersFactory;
  private LoadControl loadControl;
  private Handler mainHandler;
  private ExtractorsFactory extractorsFactory;
  private BandwidthMeter bandwidthMeter;
  private DataSource.Factory dataSourceFactory;
  private TrackSelection.Factory trackSelectionFactory;
  private TrackSelector trackSelector;
  private SimpleExoPlayer player;
  private ConcatenatingMediaSource concatenatingMediaSource;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Log.i(TAG, "onCreate: ");
    findViewById(R.id.button1).setOnClickListener(this);
    findViewById(R.id.button2).setOnClickListener(this);
    findViewById(R.id.button3).setOnClickListener(this);
    findViewById(R.id.resume).setOnClickListener(this);
    findViewById(R.id.pause).setOnClickListener(this);
    findViewById(R.id.seekTo).setOnClickListener(this);

    mainHandler = new Handler();
    extractorsFactory = new DefaultExtractorsFactory();

    bandwidthMeter = new DefaultBandwidthMeter();
    concatenatingMediaSource = new ConcatenatingMediaSource();
    trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
    dataSourceFactory = new DefaultHttpDataSourceFactory("ExoplayerDemo");
    trackSelector = new DefaultTrackSelector(trackSelectionFactory);
    player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
    player.addListener(this);
    player.setPlayWhenReady(true);
    Log.i(TAG, "onCreate: player setup");
    List<String> songList = new ArrayList<>();
/*    songList.add("https://api.soundcloud" +
        ".com/tracks/271133753/stream?client_id=2797f3bc1ab30d3502ef8f532134d797");
    songList.add("https://api.soundcloud" +
        ".com/tracks/304814801/stream?client_id=2797f3bc1ab30d3502ef8f532134d797");
    songList.add("https://api.soundcloud" +
        ".com/tracks/369694814/stream?client_id=2797f3bc1ab30d3502ef8f532134d797");*/
    songList.add("https://api.soundcloud" +
        ".com/tracks/273533952/stream?client_id=2797f3bc1ab30d3502ef8f532134d797");
    songList.add("https://api.soundcloud" +
        ".com/tracks/509362701/stream?client_id=2797f3bc1ab30d3502ef8f532134d797");
    songList.add("https://api.soundcloud" +
        ".com/tracks/346376118/stream?client_id=2797f3bc1ab30d3502ef8f532134d797");
    songList.add("https://api.soundcloud" +
        ".com/tracks/306222432/stream?client_id=2797f3bc1ab30d3502ef8f532134d797");
    List<MediaSource> mediaSources = new ArrayList<>();
    for (String songUri : songList) {
      ExtractorMediaSource source =
          new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(songUri));
      mediaSources.add(source);
    }

    concatenatingMediaSource.addMediaSources(mediaSources);
    Log.i(TAG, "onCreate: create media source");
    player.prepare(concatenatingMediaSource);
    Log.i(TAG, "onCreate: prepare for play");
  }

  /**
   * Called when a view has been clicked.
   *
   * @param v The view that was clicked.
   */
  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.button1:
        Log.i(TAG, "onClick: button1");
        int currentTrack = player.getCurrentWindowIndex();
        Log.i(TAG, "onClick: windowInded:" + currentTrack);
        if (currentTrack > 0) {
          player.seekTo(currentTrack - 1, 0);
        }

        break;
      case R.id.button2:
        Log.i(TAG, "onClick: button2");
        if (null == v.getTag()) {
          player.setVolume(0f);
          v.setTag("Muted");
          ((Button) v).setText("UN-MUTE");
          Log.i(TAG, "onClick: mute");
        } else {
          player.setVolume(1f);
          v.setTag(null);
          ((Button) v).setText("MUTE");
          Log.i(TAG, "onClick: unmute");
        }
        break;
      case R.id.button3:
        Log.i(TAG, "onClick: button3");
        int thistrack = player.getCurrentWindowIndex();
        Log.i(TAG, "onClick: windowInded:" + thistrack);
        if (thistrack < concatenatingMediaSource.getSize() - 1) {
          player.seekTo(thistrack + 1, 0);
        }
        break;
      case R.id.resume:
        player.setPlayWhenReady(true);
        Log.i(TAG, "onClick: resume");
        break;
      case R.id.pause:
        player.setPlayWhenReady(false);
        Log.i(TAG, "onClick: pause");
        break;
      case R.id.seekTo:
        long position = player.getContentPosition();
        Log.i(TAG, "seekTo: current position:" + position);
        player.seekTo(position + 20 * 1000);
        Log.i(TAG, "seekTo: new position:" + player.getContentPosition());
        break;
    }
  }


  @Override
  protected void onDestroy() {
    super.onDestroy();
    player.setPlayWhenReady(false);
    player.release();
  }

  /**
   * Called when the timeline and/or manifest has been refreshed.
   * <p>
   * Note that if the timeline has changed then a position discontinuity may also have occurred.
   * For example, the current period index may have changed as a result of periods being added or
   * removed from the timeline. This will <em>not</em> be reported via a separate call to
   * {@link #onPositionDiscontinuity(int)}.
   *
   * @param timeline The latest timeline. Never null, but may be empty.
   * @param manifest The latest manifest. May be null.
   * @param reason   The {@link Player.TimelineChangeReason} responsible for this timeline change.
   */
  @Override
  public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
    Log.i(TAG, "onTimelineChanged: reason:" + reason);
  }


  /**
   * Called when the available or selected tracks change.
   *
   * @param trackGroups     The available tracks. Never null, but may be of length zero.
   * @param trackSelections The track selections for each renderer. Never null and always of
   *                        length {@link #TrackSelectionArray}, but may contain null elements.
   */
  @Override
  public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
    Log.i(TAG, "onTracksChanged: groupLength:" + trackGroups.length + " Trakarray:" +
        trackSelections.length);
  }

  /**
   * Called when the player starts or stops loading the source.
   *
   * @param isLoading Whether the source is currently being loaded.
   */
  @Override
  public void onLoadingChanged(boolean isLoading) {
    Log.i(TAG, "onLoadingChanged: isLoading:" + isLoading);
  }

  /**
   * Called when the value returned from either {@link #getPlayWhenReady()} or
   * {@link #getPlaybackState()} changes.
   *
   * @param playWhenReady Whether playback will proceed when ready.
   * @param playbackState One of the {@code STATE} constants.
   */
  @Override
  public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
    Log.i(TAG,
        "onPlayerStateChanged:playWhenReady: " + playWhenReady + " playbackState:" + playbackState);
    Log.i(TAG, "onPlayerStateChanged: window:" + player.getCurrentWindowIndex());
    Log.i(TAG, "onPlayerStateChanged: periodindex:" + player.getCurrentPeriodIndex());
  }

  /**
   * Called when the value of {@link #getRepeatMode()} changes.
   *
   * @param repeatMode The {@link Player.RepeatMode} used for playback.
   */
  @Override
  public void onRepeatModeChanged(int repeatMode) {
    Log.i(TAG, "onRepeatModeChanged: " + repeatMode);
  }

  /**
   * Called when the value of {@link #getShuffleModeEnabled()} changes.
   *
   * @param shuffleModeEnabled Whether shuffling of windows is enabled.
   */
  @Override
  public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
    Log.i(TAG, "onShuffleModeEnabledChanged: " + shuffleModeEnabled);
  }

  /**
   * Called when an error occurs. The playback state will transition to {@link #STATE_IDLE}
   * immediately after this method is called. The player instance can still be used, and
   * {@link #release()} must still be called on the player should it no longer be required.
   *
   * @param error The error.
   */
  @Override
  public void onPlayerError(ExoPlaybackException error) {
    Log.i(TAG, "onPlayerError: " + error.getMessage());
  }

  /**
   * Called when a position discontinuity occurs without a change to the timeline. A position
   * discontinuity occurs when the current window or period index changes (as a result of playback
   * transitioning from one period in the timeline to the next), or when the playback position
   * jumps within the period currently being played (as a result of a seek being performed, or
   * when the source introduces a discontinuity internally).
   * <p>
   * When a position discontinuity occurs as a result of a change to the timeline this method is
   * <em>not</em> called. {@link #onTimelineChanged(Timeline, Object, int)} is called in this
   * case.
   *
   * @param reason The {@link Player.DiscontinuityReason} responsible for the discontinuity.
   */
  @Override
  public void onPositionDiscontinuity(int reason) {
    Log.i(TAG, "onPositionDiscontinuity: " + reason);
  }

  /**
   * Called when the current playback parameters change. The playback parameters may change due to
   * a call to {@link #setPlaybackParameters(PlaybackParameters)}, or the player itself may change
   * them (for example, if audio playback switches to passthrough mode, where speed adjustment is
   * no longer possible).
   *
   * @param playbackParameters The playback parameters.
   */
  @Override
  public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
    Log.i(TAG, "onPlaybackParametersChanged: ");
  }

  /**
   * Called when all pending seek requests have been processed by the player. This is guaranteed
   * to happen after any necessary changes to the player state were reported to
   * {@link #onPlayerStateChanged(boolean, int)}.
   */
  @Override
  public void onSeekProcessed() {
    Log.i(TAG, "onSeekProcessed: ");
  }
}
