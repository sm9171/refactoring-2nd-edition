public class Play {
    private String playId;
    private String name;
    private PlayType type;

    public Play(final String playId, final String name, final PlayType type) {
        this.playId = playId;
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public PlayType getType() {
        return type;
    }

    public String getPlayId() {
        return playId;
    }
}
