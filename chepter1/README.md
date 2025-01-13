# Chepter1. 리팩터링: 첫번째 예시

---

## 1.1 자 시작해보자

다양한 연극을 외주로 받아서 공연하는 극단이 있다고 생각해보자.

공연 요청이 들어오면 연극의 장르와 관객 규모를 기초로 비용을 책정한다.

현재 이 극단은 두 가지 장르, 비극(tragedy) 와 희극(comedy) 만 공연한다.

그리고 공연료와 별개로 포인트(volume credit) 를 지급해서 다음번 의뢰 시 공연료를 할인받을 수 있다.

극단은 공연할 연극 정보를 다음과 같이 각각 객체에 저장한다.
```java
        Play play1 = new Play("hamlet", "Hamlet", PlayType.TRAGEDY);
        Play play2 = new Play("as-like", "As You Like It", PlayType.COMEDY);
        Play play3 = new Play("othello", "Othello", PlayType.TRAGEDY);
        Plays plays = new Plays(Arrays.asList(play1, play2, play3));

        Performance performance1 = new Performance("hamlet", 55);
        Performance performance2 = new Performance("as-like", 35);
        Performance performance3 = new Performance("othello", 40);
        Invoice invoice = new Invoice("BigCo", Arrays.asList(performance1, performance2, performance3));
```

공연료 청구서를 출력하는 코드는 다음과 같이 간단히 Statement 클래스에서 메소드로 구현했다.

```java
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

            // 희극 관객 5명마다 추가 포인트를 제공한다.
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
```

결과는 다음과 같이 출력된다.
```text
청구내역 (고객명: BigCo)
hamlet: $650 55석
As You Like It: $580 35석
Othello: $500 40석
총액: $1730
적립 포인트: 47점
```

---

## 1.2 예시 프로그램을 본 소감

---

## 1.3 리팩터링의 첫 단계

---

## 1.4 statement() 함수 쪼개기

---

## 1.5 중간 점검: 난무하는 중첩 함수

---

## 1.6 계산 단계와 포맷팅 단계 분리 하기

---

## 1.7 중간 점검: 두 파일(과 두단계)로 분리됨

---

## 1.8 다형성을 활용해 계산 코드 재구성하기

---

## 1.9 상태 점검: 다형성을 활용하여 데이터 생성하기

---

## 1.10 마치며