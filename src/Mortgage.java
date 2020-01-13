import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class Mortgage {

    private LocalDate birthDate;
    private int period;
    private BigDecimal amount;
    private BigDecimal yearlyInterest;
    private LocalDate startDate;
    private BigDecimal homePrice;
    private BigDecimal initialAmount;
    private BigDecimal interestAmount;
    private LocalDate firstPaymentDate;
    private LocalDate lastPaymentDate;
    private List<MonthlyDetail> monthlyDetailList ;



    public Mortgage() {
    }

    public Mortgage(LocalDate birthDate, int period, BigDecimal yearlyInterest, LocalDate startDate, BigDecimal homePrice, BigDecimal initialAmount) {
        this.birthDate = birthDate;
        this.period = period;
        this.yearlyInterest = yearlyInterest;
        this.startDate = startDate;
        this.homePrice = homePrice;
        this.initialAmount = initialAmount;
        this.amount = homePrice.subtract(initialAmount);

        if (applyForMortgage()){
            loanInfo();
            monthlyDetail();
        } else {
            System.out.println(" Your application was rejected");
        }
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getYearlyInterest() {
        return yearlyInterest;
    }

    public void setYearlyInterest(BigDecimal yearlyInterest) {
        this.yearlyInterest = yearlyInterest;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public BigDecimal getHomePrice() {
        return homePrice;
    }

    public void setHomePrice(BigDecimal homePrice) {
        this.homePrice = homePrice;
    }

    public BigDecimal getInitialAmount() {
        return initialAmount;
    }

    public void setInitialAmount(BigDecimal initialAmount) {
        this.initialAmount = initialAmount;
    }

    public BigDecimal getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(BigDecimal interestAmount) {
        this.interestAmount = interestAmount;
    }

    public LocalDate getFirstPaymentDate() {
        return firstPaymentDate;
    }

    public void setFirstPaymentDate(LocalDate firstPaymentDate) {
        this.firstPaymentDate = firstPaymentDate;
    }

    public LocalDate getLastPaymentDate() {
        return lastPaymentDate;
    }

    public void setLastPaymentDate(LocalDate lastPaymentDate) {
        this.lastPaymentDate = lastPaymentDate;
    }

    public List<MonthlyDetail> getMonthlyDetailList() {
        return monthlyDetailList;
    }

    public void setMonthlyDetailList(List<MonthlyDetail> monthlyDetailList) {
        this.monthlyDetailList = monthlyDetailList;
    }

    private boolean applyForMortgage(){

        Period p = Period.between(birthDate, startDate);
        int age = p.getYears();

        boolean check = false;
        int a = homePrice.compareTo(BigDecimal.valueOf(10000));
        int b = homePrice.compareTo(BigDecimal.valueOf(150000));
        int c = homePrice
                .multiply(BigDecimal.valueOf(15))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
                .compareTo(initialAmount);
        int d = BigDecimal.valueOf(150000)
                .multiply(BigDecimal.valueOf(15))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
                .add(homePrice.subtract(BigDecimal.valueOf(150000)))
                .compareTo(initialAmount);

        if (a >= 0){
            if (b <= 0 && c <= 0){
                check = true;
            } else check = b > 0 && d <= 0;
        }

        return age < 65 && age >= 18 && check
                && period <= 65 - age && period <= 25
                && period >= 1 && !startDate.isBefore(LocalDate.now());
    }

    private BigDecimal findMonthlyPayment(){

        BigDecimal rate = yearlyInterest.divide(BigDecimal.valueOf(1200), 3, RoundingMode.HALF_UP);
        BigDecimal one = BigDecimal.valueOf(1);
        BigDecimal constant = one.add(rate).pow(period*12);
        return amount
                .multiply(rate)
                .multiply(constant)
                .divide(constant.subtract(one), 3, RoundingMode.HALF_UP);
    }

    private void loanInfo(){

        BigDecimal totalAmount = findMonthlyPayment().multiply(BigDecimal.valueOf(period*12));
        interestAmount = totalAmount.subtract(amount);
        lastPaymentDate = startDate.plusYears((long) period);
        firstPaymentDate = startDate.plusMonths(1);

        System.out.println("amount: " + amount
                + ", interestAmount: " + interestAmount
                + ", totalAmount: " + totalAmount
                + ", startDate: " + startDate
                + ", endDate: " + lastPaymentDate);
    }

    private void monthlyDetail(){

        BigDecimal monthlyInterest = yearlyInterest.divide(BigDecimal.valueOf(1200), 3, RoundingMode.HALF_UP);
        LocalDate date = startDate;
        BigDecimal balance = amount;
        BigDecimal interest;
        BigDecimal main;
        MonthlyDetail monthlyDetail;
        monthlyDetailList = new ArrayList<>();

        for (int l = 0; l < period*12; l++){
            monthlyDetail = new MonthlyDetail();
            interest = balance.multiply(monthlyInterest);
            MathContext m = new MathContext(6);
            interest = interest.round(m);
            main = findMonthlyPayment().subtract(interest);
            date =date.plusMonths(1);

            if (l == period * 12 - 1){
                BigDecimal lastMonthPoint = main.subtract(balance);
                main = main.subtract(lastMonthPoint);
                interest = interest.add(lastMonthPoint);
            }

            balance = balance.subtract(main);
            monthlyDetail.setPaymentDate(date);
            monthlyDetail.setTotalAmount(findMonthlyPayment());
            monthlyDetail.setBaseAmount(main);
            monthlyDetail.setInterestAmount(interest);
            monthlyDetailList.add(monthlyDetail);

            System.out.println("montlyPayment: " + findMonthlyPayment()
                    + " interest: " + interest
                    + " main: " + main
                    + " balance: " + balance
                    + " date: "+ date);
        }
    }
}
