public class Main {

    public static void main(String[] args) {

        System.out.println("hello JUnit");

        Calculator krw = new Calculator(new KrwCalculator());
        System.out.println(krw.sum(10, 10));

        MarketApi marketApi = new MarketApi();
        DollarCalculator dollarCalculator = new DollarCalculator(marketApi);
        dollarCalculator.init();

        Calculator dollar = new Calculator(dollarCalculator);
        System.out.println(dollar.sum(10, 10));
    }
}
