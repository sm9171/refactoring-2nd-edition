import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {
        Statement statement = new Statement();

        Play play1 = new Play("hamlet", "Hamlet", PlayType.TRAGEDY);
        Play play2 = new Play("as-like", "As You Like It", PlayType.COMEDY);
        Play play3 = new Play("othello", "Othello", PlayType.TRAGEDY);
        Plays plays = new Plays(Arrays.asList(play1, play2, play3));

        Performance performance1 = new Performance("hamlet", 55);
        Performance performance2 = new Performance("as-like", 35);
        Performance performance3 = new Performance("othello", 40);
        Invoice invoice = new Invoice("BigCo", Arrays.asList(performance1, performance2, performance3));

        String result = statement.statement(invoice, plays);

        System.out.println(result);
    }
}
