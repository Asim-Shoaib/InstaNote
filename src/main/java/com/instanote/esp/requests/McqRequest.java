package com.esp.instanote.requests;

public class McqRequest {
    private String url;
    private int numQuestions;
    private boolean isPlaylist;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getNumQuestions() {
        return numQuestions;
    }

    public void setNumQuestions(int numQuestions) {
        this.numQuestions = numQuestions;
    }

    public boolean isPlaylist() {
        return isPlaylist;
    }

    public void setPlaylist(boolean isPlaylist) {
        this.isPlaylist = isPlaylist;
    }
}
