import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

// JUnit Test
// Mockito : 단위 테스트를 위한 Java mocking framework
// 실제 객체를 만들어 사용하기에 시간, 비용 등이 높거나 혹은 객체 서로간의 의존성이 강해 구현하기 힘들 경우 가짜 객체를 만들어 사용하는 방법
// Mock(진짜 객체 처럼 동작하지만 프로그래머가 직접 컨트롤 할 수 있는 객체)을 지원하는 프레임워크.
// Mock 객체를 만들고 관리하고 검증 할 수 있는 방법 제공.(가짜 객체를 만들어준다고 생각)
// 구현체가 아직 없는 경우나, 구현체가 있더라도 특정 단위만 테스트 하고 싶을 경우 주로 사용한다.
// Mocking 처리 : 특정한 객체를 어떤 메소드가 호출할 때 원하는 결과값을 리턴시키기
@ExtendWith(MockitoExtension.class) // mocking 환경으로 확장
public class DollarCalculatorTest {

    @Mock // mock 처리할 객체
    public MarketApi marketApi;

    @BeforeEach // test 이전에 실행
    public void init() {
        // 앞에 leninet를 붙이게 되면 해당 stubbing은 optional한 성격을 가지게 되어서 사용하지 않더라도 예외를 발생하지 않는다.
        // 모의 객체 생성 및 모의 객체의 동작을 지정하는 것을 Stubbing이라고 한다.
        // marketApi.connect()가 일어날 때 내가 원하는 금액을 리턴
        // api를 호출하지 않아도 응답을 지정해줄 수 있다.
        // 이 후엔 내가 만든 메소드들이 내가 기대하는데로 동작하는지 결과를 확인하는 것 -> 단위 테스트
        Mockito.lenient().when(marketApi.connect()).thenReturn(3000);
    }

    @Test
    public void testHello() {
        System.out.println("hello");
    }

    @Test
    public void dollar() {

        MarketApi marketApi = new MarketApi();
        DollarCalculator dollarCalculator = new DollarCalculator(marketApi);
        dollarCalculator.init();

        Calculator dollar = new Calculator(dollarCalculator);
        // 기대값과 실행값을 비교하는 테스트
        Assertions.assertEquals(22000, dollar.sum(10, 10));
    }

    @Test
    public void mock() {

        DollarCalculator dollarCalculator = new DollarCalculator(marketApi);
        dollarCalculator.init();

        Calculator dollar = new Calculator(dollarCalculator);

        // 기대값과 실행값을 비교하는 테스트
        Assertions.assertEquals(60000, dollar.sum(10, 10));
    }
}
