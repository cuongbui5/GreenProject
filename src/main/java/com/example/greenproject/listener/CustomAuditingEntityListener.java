package com.example.greenproject.listener;

import com.example.greenproject.model.BaseEntity;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Configuration
@NoArgsConstructor
public class CustomAuditingEntityListener extends AuditingEntityListener {
    public CustomAuditingEntityListener(ObjectFactory<AuditingHandler> handler) {
        super.setAuditingHandler(handler);
    }



    @Override
    public void touchForCreate(Object target) {
        if (target instanceof BaseEntity entity) {
            if (entity.getCreatedAt() == null) {
                super.touchForCreate(entity);
            } else if (entity.getUpdatedAt() == null) {
                super.touchForUpdate(entity);
            }
        } else {
            throw new RuntimeException("Unsupported target type: " + target.getClass());
        }

    }

    @Override
    public void touchForUpdate(Object target) {
        if (target instanceof BaseEntity entity) {
            super.touchForUpdate(entity);
        } else {
            throw new RuntimeException("Unsupported target type: " + target.getClass());
        }

    }
}
