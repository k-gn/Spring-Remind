package com.code.design.object;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Entity
@Table(name = "refund")
@Getter
@NoArgsConstructor
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Embedded
    private Account account;

    @Embedded
    private CreditCard creditCard;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Account, CreditCard 둘 중 하나만 받아야 하는 구존데 둘 다 받는 경우는 디자인이 좋지 않다는 것
    // 상호 배타적인 파라미터가 존재한다면 적절히 분리하는 것이 좋다. -> 책임과 의도 표현

    // 계좌 번호 기반 환불, Builder 이름을 부여해서 그에 따른 책임 부여, 그에 따른 필수 인자값 명확
    @Builder(builderClassName = "ByAccountBuilder", builderMethodName = "ByAccountBuilder")
    public Refund(Account account, Order order) {
        Assert.notNull(account, "account must not be null");
        Assert.notNull(order, "order must not be null");

        this.order = order;
        this.account = account;
    }

    // 신용 카드 기반 환불, Builder 이름을 부여해서 그에 따른 책임 부여, 그에 따른 필수 인자값 명확
    @Builder(builderClassName = "ByCreditBuilder", builderMethodName = "ByCreditBuilder")
    public Refund(CreditCard creditCard, Order order) {
        Assert.notNull(creditCard, "creditCard must not be null");
        Assert.notNull(order, "order must not be null");

        this.order = order;
        this.creditCard = creditCard;
    }
}