package jp.shts.android.storyprogressbar;

public class Slide {
    int imgRes;
    String videoRes;

    public Slide(int imgRes, String videoRes) {
        this.imgRes = imgRes;
        this.videoRes = videoRes;
    }

    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }

    public String getVideoRes() {
        return videoRes;
    }

    public void setVideoRes(String videoRes) {
        this.videoRes = videoRes;
    }
}
