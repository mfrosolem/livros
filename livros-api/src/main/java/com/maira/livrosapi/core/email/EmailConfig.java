package com.maira.livrosapi.core.email;

import com.maira.livrosapi.domain.service.EnvioEmailService;
import com.maira.livrosapi.infrastructure.service.email.FakeEnvioEmailService;
import com.maira.livrosapi.infrastructure.service.email.SmtpEnvioEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class EmailConfig {

    private final EmailProperties emailProperties;

    @Bean
    public EnvioEmailService envioEmailService() {
        switch (emailProperties.getImpl()) {
            case FAKE:
                return new FakeEnvioEmailService();
            case SMTP:
                return new SmtpEnvioEmailService();
            default:
                return null;
        }
    }


}
