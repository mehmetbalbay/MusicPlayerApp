package mehmetbalbay.net.musicplayerapp.model;

import java.io.Serializable;

public class SongInfo implements Serializable {

    private String songName;
    private String artistName;
    private String songUrl;
    private String albumName;
    private String album_id;

    public SongInfo() {
    }

    public SongInfo(String songName, String artistName, String songUrl, String albumName, String album_id) {
        this.songName = songName;
        this.artistName = artistName;
        this.songUrl = songUrl;
        this.albumName = albumName;
        this.album_id = album_id;
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

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }
}
