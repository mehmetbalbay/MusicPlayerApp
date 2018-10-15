package mehmetbalbay.net.musicplayerapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import mehmetbalbay.net.musicplayerapp.R;
import mehmetbalbay.net.musicplayerapp.model.SongInfo;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> {

    private ArrayList<SongInfo> songs;
    private Context context;

    private OnItemClickListener onItemClickListener;

    public SongAdapter(Context context, ArrayList<SongInfo> songs) {
        this.context = context;
        this.songs = songs;
    }

    public interface  OnItemClickListener {
        void onItemClick(Button b, View v, SongInfo obj, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public SongHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View myView = LayoutInflater.from(context).inflate(R.layout.row_song,viewGroup,false);
        return new SongHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull final SongHolder songHolder, final int i) {

        final SongInfo c = songs.get(i);
        songHolder.songName.setText(c.getSongName());
        songHolder.artistName.setText(c.getArtistName());
        songHolder.btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(songHolder.btnAction, v,c,i);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class SongHolder extends RecyclerView.ViewHolder {

        private TextView songName,artistName;
        private Button btnAction;

        public SongHolder(@NonNull View itemView) {
            super(itemView);
            songName = itemView.findViewById(R.id.tvSongName);
            artistName = itemView.findViewById(R.id.tvArtistName);
            btnAction = itemView.findViewById(R.id.btnAction);
        }
    }
}
