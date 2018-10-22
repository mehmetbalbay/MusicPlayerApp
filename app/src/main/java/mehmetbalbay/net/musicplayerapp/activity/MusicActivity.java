package mehmetbalbay.net.musicplayerapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jackandphantom.blurimage.BlurImage;
import java.io.IOException;
import java.util.ArrayList;
import mehmetbalbay.net.musicplayerapp.R;
import mehmetbalbay.net.musicplayerapp.model.SongInfo;

public class MusicActivity extends AppCompatActivity {

    private ImageView back_btn_Image,image_back_blur, main_Image;
    private TextView player_title,player_song_name,player_album_name,player_artist_name,player_current_time,player_full_time;
    private ImageView play_button, previous_button, skip_next_button, replay_button, shuffle_button;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private AnimatedVectorDrawable tickToCross,crossToTick,drawable;
    private boolean tick = true;
    private ArrayList<SongInfo> songs;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        // Resmi bitmape çevirdik.
        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.full);

        // Tanımlamalar.
        player_title = findViewById(R.id.player_title);
        player_song_name = findViewById(R.id.player_song_name);
        player_album_name = findViewById(R.id.player_album_name);
        player_artist_name = findViewById(R.id.player_artist_name);
        player_current_time = findViewById(R.id.player_current_time);
        player_full_time = findViewById(R.id.player_full_time);
        seekBar = findViewById(R.id.player_seekbar);
        main_Image = findViewById(R.id.main_image);

        // Buttons
        play_button = findViewById(R.id.play_button);
        previous_button = findViewById(R.id.previous_button);
        skip_next_button = findViewById(R.id.skip_next_button);
        replay_button = findViewById(R.id.replay_button);
        shuffle_button = findViewById(R.id.shuffle_button);

        // Tickcross
        tickToCross = (AnimatedVectorDrawable) getDrawable(R.drawable.avd_tick_to_cross);
        crossToTick = (AnimatedVectorDrawable) getDrawable(R.drawable.avd_cross_to_tick);


        main_Image.setImageBitmap(getCircleBitmap(mBitmap));


        if (Build.VERSION.SDK_INT >= 19) {
            Typeface typeface = ResourcesCompat.getFont(this,R.font.opensansbold);
            player_title.setTypeface(typeface);
            player_artist_name.setTypeface(typeface);
            player_album_name.setTypeface(typeface);
        }

        image_back_blur = findViewById(R.id.image_back_blur);
        BlurImage.with(getApplicationContext()).load(R.drawable.full).intensity(20).Async(true).into(image_back_blur);

        Intent intent = getIntent();
        songs = (ArrayList<SongInfo>) intent.getSerializableExtra("songList");
        position = intent.getIntExtra("position", 0);

        sarkiCal(position);

        click_skip_next_button();

    }

    public void sarkiCal(int position) {

        final String song_name = songs.get(position).getSongName();
        final String artist_name = songs.get(position).getArtistName();
        String song_url = songs.get(position).getSongUrl();
        final String album_name = songs.get(position).getAlbumName();

        if (song_name != null){
            Log.d("Serialize_data",song_url);
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(song_url);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        seekBar.setProgress(0);
                        seekBar.setMax(mp.getDuration());
                        player_full_time.setText(String.valueOf(getTimeString(mp.getDuration())));

                        player_song_name.setText(song_name);
                        player_artist_name.setText(artist_name);
                        player_album_name.setText(album_name);

                        click_play_button();

                        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                                mediaPlayer.seekTo(seekBar.getProgress());
                            }
                        });
                    }
                });
            }catch (IOException e) {
                Toast.makeText(this, "Bir Hata Oluştu.", Toast.LENGTH_SHORT).show();
            }

        } else {
            Log.d("Serialize_data", "Hata");
        }

        Thread t = new MyThread();
        t.start();

    }

    public class MyThread extends Thread {
        @Override
        public void run() {
            while(true) {
                try{
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mediaPlayer != null) {
                    seekBar.post(new Runnable() {
                        @Override
                        public void run() {
                            seekBar.setProgress(mediaPlayer.getCurrentPosition());
                            player_current_time.setText(String.valueOf(getTimeString(mediaPlayer.getCurrentPosition())));

                        }
                    });
                }
            }
        }
    }

    public void backButtonFucktion(View v) {
        back_btn_Image = findViewById(R.id.back_button);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha);
        back_btn_Image.startAnimation(animation);
    }

    public Bitmap getCircleBitmap(Bitmap source) {
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), source);
        roundedBitmapDrawable.setCircular(false);
        roundedBitmapDrawable.setCornerRadius(150);
        return drawableToBitmap(roundedBitmapDrawable);
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public void click_play_button() {
        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                drawable = tick ? tickToCross : crossToTick;
                play_button.setImageDrawable(drawable);
                drawable.start();
                tick = !tick;

                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }else if (!mediaPlayer.isPlaying()){
                    mediaPlayer.start();
                }

            }
        });
    }

    public void click_skip_next_button() {
        skip_next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    position = position +1;
                    sarkiCal(position);
                }


            }
        });
    }

    private String getTimeString(long millis) {
        StringBuffer buf = new StringBuffer();

        int hours = (int) (millis/ (1000 * 60 * 60));
        int minutes = (int) ((millis % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) (((millis % (1000 * 60 * 60)) %(1000 * 60)) / 1000);

        buf
                .append(String.format("%02d", minutes))
                .append(":")
                .append(String.format("%02d", seconds));

        return buf.toString();

    }
}
