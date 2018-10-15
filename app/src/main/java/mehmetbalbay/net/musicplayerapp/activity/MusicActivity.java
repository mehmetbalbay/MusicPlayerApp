package mehmetbalbay.net.musicplayerapp.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.jackandphantom.blurimage.BlurImage;

import mehmetbalbay.net.musicplayerapp.R;
import mehmetbalbay.net.musicplayerapp.model.SongInfo;

public class MusicActivity extends AppCompatActivity {

    private ImageView backImage,image_back_blur;
    private TextView player_title,player_album_name,player_artist_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        player_title = findViewById(R.id.player_title);
        player_album_name = findViewById(R.id.player_album_name);
        player_artist_name = findViewById(R.id.player_artist_name);

        if (Build.VERSION.SDK_INT >= 19) {
            Typeface typeface = ResourcesCompat.getFont(this,R.font.opensansbold);
            player_title.setTypeface(typeface);
            player_artist_name.setTypeface(typeface);
            player_album_name.setTypeface(typeface);
        }

        image_back_blur = findViewById(R.id.image_back_blur);
        BlurImage.with(getApplicationContext()).load(R.drawable.full).intensity(20).Async(true).into(image_back_blur);

        Intent intent = getIntent();
        SongInfo songInfo = (SongInfo) intent.getSerializableExtra("serialize_data");

        String song_name = songInfo.getSongName();
        String artist_name = songInfo.getArtistName();
        String song_url = songInfo.getSongUrl();
        if (song_name != null)
            Log.d("Serialize_data",song_url);
        else
            Log.d("Serialize_data","Hata");




    }

    public void backButtonFucktion(View v) {
        backImage = findViewById(R.id.back_Image);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha);
        backImage.startAnimation(animation);
    }
}
