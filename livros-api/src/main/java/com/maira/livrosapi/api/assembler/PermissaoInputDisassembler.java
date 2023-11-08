package com.maira.livrosapi.api.assembler;

import com.maira.livrosapi.api.model.input.PermissaoInput;
import com.maira.livrosapi.domain.model.Permissao;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PermissaoInputDisassembler {

    private final ModelMapper modelMapper;

    public PermissaoInputDisassembler(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Permissao toDomainObject(PermissaoInput permissaoInput) {
        return modelMapper.map(permissaoInput, Permissao.class);
    }

    public  void copyToDomainObject(PermissaoInput permissaoInput, Permissao permissao) {
        modelMapper.map(permissaoInput, permissao);
    }
}
