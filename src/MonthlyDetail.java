import java.math.BigDecimal;
import java.time.LocalDate;

public class MonthlyDetail {

    private LocalDate paymentDate;
    private BigDecimal baseAmount;
    private BigDecimal interestAmount;
    private BigDecimal totalAmount;

    public MonthlyDetail() {
    }

    public MonthlyDetail(LocalDate paymentDate, BigDecimal baseAmount, BigDecimal interestAmount, BigDecimal totalAmount) {
        this.paymentDate = paymentDate;
        this.baseAmount = baseAmount;
        this.interestAmount = interestAmount;
        this.totalAmount = totalAmount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(BigDecimal baseAmount) {
        this.baseAmount = baseAmount;
    }

    public BigDecimal getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(BigDecimal interestAmount) {
        this.interestAmount = interestAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
