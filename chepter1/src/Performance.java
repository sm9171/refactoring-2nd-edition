public class Performance {
    private String playId;
    private int audience;

    public Performance(final String playId, final int audience) {
        this.playId = playId;
        this.audience = audience;
    }

    public int getAudience() {
        return audience;
    }

    public String getPlayId() {
        return playId;
    }
}
