package mehmetbalbay.net.musicplayerapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ComposeShader;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.jackandphantom.blurimage.BlurImage;

import java.io.IOException;

import mehmetbalbay.net.musicplayerapp.R;
import mehmetbalbay.net.musicplayerapp.model.SongInfo;

public class MusicActivity extends AppCompatActivity {

    private ImageView back_btn_Image,image_back_blur, main_Image;
    private TextView player_title,player_album_name,player_artist_name;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private int mCornerRadius;
    private int mMargin;

    private static final int CORNER_RADIUS = 100;
    private static final int MARGIN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        final float density = getApplicationContext().getResources().getDisplayMetrics().density;
        mCornerRadius = (int) (CORNER_RADIUS * density + 0.5f);
        mMargin = (int) (MARGIN * density + 0.5f);

        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.full);

        //StreamDrawable d = new StreamDrawable(mBitmap , mCornerRadius, mMargin);

        player_title = findViewById(R.id.player_title);
        player_album_name = findViewById(R.id.player_album_name);
        player_artist_name = findViewById(R.id.player_artist_name);
        seekBar = findViewById(R.id.player_seekbar);
        main_Image = findViewById(R.id.main_image);

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
        SongInfo songInfo = (SongInfo) intent.getSerializableExtra("serialize_data");

        String song_name = songInfo.getSongName();
        String artist_name = songInfo.getArtistName();
        String song_url = songInfo.getSongUrl();
        if (song_name != null){
            Log.d("Serialize_data",song_url);
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(song_url);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        //mp.start();
                        seekBar.setProgress(0);
                        seekBar.setMax(mp.getDuration());
                        }
                        });
            }catch (IOException e) { }

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
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mediaPlayer != null) {
                    seekBar.post(new Runnable() {
                        @Override
                        public void run() {
                            seekBar.setProgress(mediaPlayer.getCurrentPosition());
                        }
                    });
                }
            }


        }
    }

    public void backButtonFucktion(View v) {
        back_btn_Image = findViewById(R.id.back_btn_Image);
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

    /*

    class StreamDrawable extends Drawable {
        private static final boolean USE_VIGNETTE = true;

        private final float mCornerRadius;
        private final RectF mRect = new RectF();
        private final BitmapShader mBitmapShader;
        private final Paint mPaint;
        private final int mMargin;

        StreamDrawable(Bitmap bitmap, float cornerRadius, int margin) {
            mCornerRadius = cornerRadius;

            mBitmapShader = new BitmapShader(bitmap,
                    Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setShader(mBitmapShader);

            mMargin = margin;
        }

        @Override
        protected void onBoundsChange(Rect bounds) {
            super.onBoundsChange(bounds);
            mRect.set(mMargin, mMargin, bounds.width() - mMargin, bounds.height() - mMargin);

            if (USE_VIGNETTE) {
                RadialGradient vignette = new RadialGradient(
                        mRect.centerX(), mRect.centerY() * 1.0f / 0.7f, mRect.centerX() * 1.3f,
                        new int[] { 0, 0, 0x7f000000 }, new float[] { 0.0f, 0.7f, 1.0f },
                        Shader.TileMode.CLAMP);
                Matrix oval = new Matrix();
                oval.setScale(1.0f, 0.7f);
                vignette.setLocalMatrix(oval);

                mPaint.setShader(
                        new ComposeShader(mBitmapShader, vignette, PorterDuff.Mode.SRC_OVER));
            }
        }

        @Override
        public void draw(@NonNull Canvas canvas) {
            canvas.drawRoundRect(mRect, mCornerRadius, mCornerRadius, mPaint);
        }

        @Override
        public void setAlpha(int alpha) {
            mPaint.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(@Nullable ColorFilter cf) {
            mPaint.setColorFilter(cf);
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }
    }

    */
}
