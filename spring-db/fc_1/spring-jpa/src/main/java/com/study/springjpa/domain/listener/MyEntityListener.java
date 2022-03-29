package com.study.springjpa.domain.listener;

import java.time.LocalDateTime;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

// 리스너 클래스, 감지된 이벤트가 동작한다.
public class MyEntityListener {

    @PrePersist
    public void prePersist(Object o) {
        if (o instanceof Auditable) { // Auditable 를 구현한 이유
            ((Auditable) o).setCreatedAt(LocalDateTime.now());
            ((Auditable) o).setUpdatedAt(LocalDateTime.now());
        }
    }

    @PreUpdate
    public void preUpdate(Object o) {
        if (o instanceof Auditable) {
            ((Auditable) o).setUpdatedAt(LocalDateTime.now());
        }
    }
}
