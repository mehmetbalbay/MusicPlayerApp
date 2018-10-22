package mehmetbalbay.net.musicplayerapp.model;

import java.io.Serializable;

public class SongInfo implements Serializable {

    private String songName;
    private String artistName;
    private String songUrl;
    private String albumName;

    public SongInfo() {
    }

    public SongInfo(String songName, String artistName, String songUrl, String albumName) {
        this.songName = songName;
        this.artistName = artistName;
        this.songUrl = songUrl;
        this.albumName = albumName;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }
}
