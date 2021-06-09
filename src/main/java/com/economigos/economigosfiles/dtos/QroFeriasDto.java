package com.economigos.economigosfiles.dtos;

import java.util.List;

public class QroFeriasDto {
    private List<ContabilUltimasAtividades> contabilUltimasAtividadesDtos;

    public QroFeriasDto() {
    }

    public QroFeriasDto(List<ContabilUltimasAtividades> contabilUltimasAtividadesDtos) {
        this.contabilUltimasAtividadesDtos = contabilUltimasAtividadesDtos;
    }

    public List<ContabilUltimasAtividades> getContabilUltimasAtividadesDtos() {
        return contabilUltimasAtividadesDtos;
    }

    public void setContabilUltimasAtividadesDtos(List<ContabilUltimasAtividades> contabilUltimasAtividadesDtos) {
        this.contabilUltimasAtividadesDtos = contabilUltimasAtividadesDtos;
    }
}