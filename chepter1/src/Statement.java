public class Statement {
    public String statement(Invoice invoice, Plays plays) throws Exception {
        StringBuilder result = new StringBuilder(String.format("청구내역 (고객명: %s)\n", invoice.getCustomer()));
        for (Performance performance : invoice.getPerformances()) {
            result.append(String.format("%s: $%d %d석\n",playFor(plays,performance).getName(), amountFor(performance, plays) / 100, performance.getAudience()));
        }

        result.append(String.format("총액: $%d\n",totalAmount(invoice, plays, result) / 100));
        result.append(String.format("적립 포인트: %d점", totalVolumeCredits(invoice, plays)));
        return result.toString();
    }

    private static int totalAmount(final Invoice invoice, final Plays plays, final StringBuilder result) throws Exception {
        int totalAmount = 0;
        for (Performance performance : invoice.getPerformances()) {
            totalAmount += amountFor(performance, plays);
        }
        return totalAmount;
    }

    private static int totalVolumeCredits(final Invoice invoice, final Plays plays) {
        int volumeCredit = 0;
        for (Performance performance : invoice.getPerformances()) {
            volumeCredit = volumeCreditFor(plays, performance);
        }
        return volumeCredit;
    }

    private static int volumeCreditFor(final Plays plays, final Performance performance) {
        int volumeCredit = 0;

        // 포인트를 적립한다.
        volumeCredit += Math.max(performance.getAudience() - 30, 0);

        // 희극 관객 5명마다 추가 포인트를 제공한다.
        if (playFor(plays, performance).equals(PlayType.COMEDY)) {
            volumeCredit += Math.floor(performance.getAudience() / 5);
        }
        return volumeCredit;
    }

    private static int amountFor(final Performance ePerformance, final Plays plays) throws Exception {
        int result = 0;
        switch (playFor(plays, ePerformance).getType()) {
            case TRAGEDY:
                result = 40000;
                if (ePerformance.getAudience() > 30) {
                    result += 1000 * (ePerformance.getAudience() - 30);
                }
                break;
            case COMEDY:
                result = 30000;
                if (ePerformance.getAudience() > 20) {
                    result += 10000 + 500 * (ePerformance.getAudience() - 20);
                }
                result += 300 * ePerformance.getAudience();
                break;
            default:
                throw new Exception("알 수 없는 장르");
        }
        return result;
    }

    private static Play playFor(Plays plays, Performance performance) {
        return plays.get(performance);
    }
}
