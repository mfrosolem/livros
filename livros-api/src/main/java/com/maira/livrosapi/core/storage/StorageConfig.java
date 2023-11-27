package com.maira.livrosapi.core.storage;

import com.maira.livrosapi.domain.service.FotoStorageService;
import com.maira.livrosapi.infrastructure.service.storage.LocalFotoStorageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

    @Bean
    FotoStorageService fotoStorageService() {
        return new LocalFotoStorageService();
    }
}
