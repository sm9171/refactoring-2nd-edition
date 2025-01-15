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
사람은 설계가 나쁜 시스템을 수정하기 어렵다.

그래서 수백 줄짜리 코드를 수정할 때면 먼저 프로그램의 작동 방식을 더 쉽게 파악할 수 있도록 코드를 여러 `함수의 요소로 재구성`한다.

프로그램의 구조가 빈약하다면 대체로 구조부터 바로 잡은 뒤에 기능을 수정하는 편이 훨씬 수월하다.

> 프로그램이 새로운 기능을 추가하기에 편한 구조가 아니라면, 먼저 기능을 추가하기 쉬운 형태로 리팩토링하고 나서 원하는 기능을 추가한다.

이번에는 두 가지 변경 사항이 생겼다.

청구 내역을 HTML 로도 출력하는 기능이 필요하다면 우선 HTML 태그를 삽입해야 하고 statement() 함수에서 조건문에 따라서 어떤건 HTML 로 어떤건 텍스트로 출력하도록 추가해야 한다.
기존에 존재하는 statement() 함수의 복사본을 만들어서 htmlStatement() 로 만들어서 사용하기도 할 것이다. 이렇게 되면 코드의 중복이 발생하고 변경 포인트가 두 개가 된다. 즉 DRY 원칙에 위반된다.

두 번째 변경 사항으로 배우들이 사극, 전원극, 전원 희극, 역사 전원극, 역사 비극, 회비 역사 전원극, 장면 변화가 없는 고전극 등 더 많은 장르를 연기하고자 한다.
언제 어떤 연극을 할지는 아직 결정하지 못했지만 이 변경은 공연료와 적립 포인트 계산법에 영향을 줄 것이다.
---

## 1.3 리팩터링의 첫 단계

리팩터링의 첫 단계는 항상 똑같다. 리팩터링할 코드 영역을 꼼꼼하게 검사해줄 테스트 코드들부터 마련해야 한다.

테스트 결과는 눈으로 보지말고 시스템이 판단하도록 한다. (테스트 결과를 성공하면 초록불이 뜨도록 실패하면 빨간불이 드는 JUnit 과 같이)

> 리팩토링하기 전에 제대로 된 테스트를 마련한다. 테스트는 반드시 자가진단하도록 한다.
---

## 1.4 statement() 함수 쪼개기
### 함수 추출하기
1. 기존의 switch문을 `amountFor()` 함수로 추출한다.
2. `amountFor()` 함수에 thisAmount 의 이름은 result 로 변경하는게 가능하다.
3. `amountFor()` 함수의 매개변수를 performance 에서 ePerformance 로 변경한다.(자바에는 타입이 명확하기 때문에 변경할 필요는 없다.)
4. play변수를 제거하고 plays를 매개변수로 받는다.
   1. play 변수는 performance 로 부터 계산되는 변수기 때문에 애초에 변수로 만들 필요는 없다.
5. thisAmount 변수를 amountFor() 함수로 인라인한다.
6. 적립 포인트 계산 부분을 volumeCreditFor 메소드를 추출한다.
7. volumeCredits 변수를 제거한다.
   1. 문장 슬라이스 라는 방법을 이용해서 변수의 위치를 옮긴다음 totalVolumeCredits() 메소드로 추출한다.
   2. 반복문을 쪼개서 성능이 느려지지 않을까 라는 생각이 들수도 있지만 성능에 미치는 영향이 미비할때가 많다.
8. volumeCredit 변수를 인라인 기법을 사용하여 totalVolumeCredits() 메소드로 인라인한다.
9. totalAmount 도 앞에서와 똑같은 절차로 제거한다.
   1. 먼저 반복문을 쪼개고, 변수 초기화 문장을 옮긴 다음에 함수를 추출한다.
   2. 그 다음 인라인 함수로 만들면 된다.
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