package com.radiuk.user_management_service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResetPasswordPublisher {

    public void publish(ResetPasswordEvent resetPasswordEvent) {
        System.out.println("Publisher!!!");
    }
}
