package com.maira.livrosapi.common;

import com.maira.livrosapi.api.model.input.EditoraInput;
import com.maira.livrosapi.domain.model.Editora;

import java.util.ArrayList;
import java.util.List;

public class EditoraConstants {

    public static final Editora AUTENTICA = new Editora(1L,"Autêntica","","","","");
    public static final Editora OBJETIVA = new Editora(2L,"Objetiva","","","","");
    public static final Editora COMPANHIA = new Editora(3L,"Grupo Companhia das Letras","","","","");

    public static final EditoraInput EDITORA_INPUT = EditoraInput.builder().nome("Nova Fronteira").build();

    public static final List<Editora> EDITORAS = new ArrayList<>() {
        {
            add(AUTENTICA);
            add(OBJETIVA);
            add(COMPANHIA);
        }
    };

    private EditoraConstants() {}
}
