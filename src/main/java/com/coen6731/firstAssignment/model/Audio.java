package com.coen6731.firstAssignment.model;

public class Audio {
    private String artist_name;
    private String track_title;
    private Integer track_number;

    private Integer year;
    private Integer reviews_count;
    private Integer copies_sold;

    public Audio(String artist_name, String track_title, int track_number, int year, int reviews_count, int copies_sold) {
        super();
        this.artist_name = artist_name;
        this.track_title = track_title;
        this.track_number = track_number;
        this.year = year;
        this.reviews_count = reviews_count;
        this.copies_sold = copies_sold;
    }


    public Audio() {
    }

    public String getArtist_name() {
        return artist_name;
    }

    public void setArtist_name(String johnDoe) {
        this.artist_name = artist_name;
    }

    public String getTrack_title() {
        return track_title;
    }

    public void setTrack_title(String track_title) {
        this.track_title = track_title;
    }

    public Integer getTrack_number() {
        return track_number;
    }

    public void setTrack_number(Integer track_number) {
        this.track_number = track_number;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getReviews_count() {
        return reviews_count;
    }

    public void setReviews_count(Integer reviews_count) {
        this.reviews_count = reviews_count;
    }

    public Integer getCopies_sold() {
        return copies_sold;
    }

    public void setCopies_sold(Integer copies_sold) {
        this.copies_sold = copies_sold;
    }


    @Override
    public String toString() {
        return "Audio{" +
                "artist_name='" + artist_name + '\'' +
                ", track_title='" + track_title + '\'' +
                ", track_number=" + track_number +
                ", year=" + year +
                ", Reviews_count=" + reviews_count +
                ", copies_sold=" + copies_sold +
                '}';
    }
}
