package mehmetbalbay.net.musicplayerapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;
import java.util.ArrayList;
import mehmetbalbay.net.musicplayerapp.activity.MusicActivity;
import mehmetbalbay.net.musicplayerapp.adapter.SongAdapter;
import mehmetbalbay.net.musicplayerapp.model.SongInfo;

public class MainActivity extends AppCompatActivity {

    private ArrayList<SongInfo> songs = new ArrayList<SongInfo>();
    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

        songAdapter = new SongAdapter(this, songs);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(recyclerView.getContext(), lm.getOrientation());
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(songAdapter);

        songAdapter.setOnItemClickListener(new SongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final Button b, View v, final SongInfo obj, int position) {

                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, MusicActivity.class);
                        intent.putExtra("serialize_data",obj);
                        startActivity(intent);
                    }
                };
                handler.postDelayed(r, 100);
            }
        });
        CheckPermission();
    }

    private void CheckPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},123);
                return;
            }else {
                finish();
            }
        }else {
            loadSongs();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 123:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadSongs();
                }else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    CheckPermission();
                }
                break;

                default:
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void loadSongs() {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC+"!=0";
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);

        if (cursor!= null) {
            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));

                    SongInfo s = new SongInfo(name,artist,url,album);
                    songs.add(s);
                }while(cursor.moveToNext());
            }
            cursor.close();
            songAdapter = new SongAdapter(this,songs);
        }
    }

}
