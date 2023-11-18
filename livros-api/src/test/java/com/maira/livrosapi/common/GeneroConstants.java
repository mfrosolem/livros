package com.maira.livrosapi.common;

import com.maira.livrosapi.api.model.input.GeneroInput;
import com.maira.livrosapi.domain.model.Genero;

import java.util.ArrayList;
import java.util.List;

public class GeneroConstants {

    public static final Genero ROMANCE = new Genero(1L, "Romance");
    public static final Genero FICCAO = new Genero(2L, "Ficção");
    public static final Genero JORNALISMO = new Genero(3L, "Jornalismo Literário");
    public static final GeneroInput GENERO_INPUT = GeneroInput.builder().descricao("Fantasia").build();

    public static final List<Genero> GENEROS = new ArrayList<>() {
        {
            add(ROMANCE);
            add(FICCAO);
            add(JORNALISMO);
        }
    };

    private GeneroConstants() {}
}
