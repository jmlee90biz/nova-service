package com.sktelecom.nova.service.monolith.config;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component("internalApplicationEventPublisher")
@Primary
public class InternalApplicationEventPublisher implements ApplicationEventPublisher {

    private final ApplicationEventPublisher delegate;

    public InternalApplicationEventPublisher(ApplicationEventPublisher delegate) {
        this.delegate = delegate;
    }

    @Override
    public void publishEvent(Object event) {
        //System.out.println("Before publishEvent: " + event);
        delegate.publishEvent(event);
        //System.out.println("After publishEvent: " + event);
    }
}