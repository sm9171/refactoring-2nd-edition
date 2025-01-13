public class Statement {
    public String statement(Invoice invoice, Plays plays) throws Exception {
        int totalAmount = 0;
        int volumeCredit = 0;
        StringBuilder result = new StringBuilder(String.format("청구내역 (고객명: %s)\n", invoice.getCustomer()));
        for (Performance performance : invoice.getPerformances()) {
            Play play = plays.get(performance);
            int thisAmount = 0;

            switch (play.getType()) {
                case TRAGEDY:
                    thisAmount = 40000;
                    if (performance.getAudience() > 30) {
                        thisAmount += 1000 * (performance.getAudience() - 30);
                    }
                    break;
                case COMEDY:
                    thisAmount = 30000;
                    if (performance.getAudience() > 20) {
                        thisAmount += 10000 + 500 * (performance.getAudience() - 20);
                    }
                    thisAmount += 300 * performance.getAudience();
                    break;
                default:
                    throw new Exception("알 수 없는 장르");
            }

            // 포인트를 적립한다.
            volumeCredit += Math.max(performance.getAudience() - 30, 0);

            // 희극 관객 5명마다 추가 포인트를 제공핟나.
            if (play.getType().equals(PlayType.COMEDY)) {
                volumeCredit += Math.floor(performance.getAudience() / 5);
            }

            // 청구 내역을 출력한다.
            result.append(String.format("%s: $%d %d석\n",play.getName(), thisAmount / 100, performance.getAudience()));
            totalAmount += thisAmount;
        }

        result.append(String.format("총액: $%d\n",totalAmount / 100));
        result.append(String.format("적립 포인트: %d점", volumeCredit));
        return result.toString();
    }
}
