package com.example.lamusica;

public class MusicModel {
    private String title ;
    private String artist ;
    private String songUri ;
    private String imageUri ;
    private String albumName ;
    private String duration ;


    public MusicModel(String artist , String albumName , String title , String songUri , String duration , String imageUri ){
        this.artist = artist ;
        this.albumName = albumName ;
        this.title = title ;
        this.songUri = songUri ;
        this.duration = duration ;
        this.imageUri = imageUri ;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSongUri() {
        return songUri;
    }

    public void setSongUri(String songUri) {
        this.songUri = songUri;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
