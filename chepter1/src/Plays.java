import java.util.List;

public class Plays {
    List<Play> plays;

    public Plays(List<Play> plays) {
        this.plays = plays;
    }

    public Play get(final Performance performance) {
        return plays.stream()
                .filter(play -> play.getPlayId().equals(performance.getPlayId()))
                .findFirst()
                .orElse(null);
    }
}
