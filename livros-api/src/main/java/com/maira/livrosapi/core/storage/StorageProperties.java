package com.maira.livrosapi.core.storage;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Getter
@Setter
@Component
@ConfigurationProperties("livros.storage")
public class StorageProperties {

    private Local local = new Local();
    private TipoStorage tipo = TipoStorage.LOCAL;

    public enum TipoStorage {
        LOCAL
    }

    @Getter
    @Setter
    public class Local {
        private Path diretorioFotos;
    }
}
