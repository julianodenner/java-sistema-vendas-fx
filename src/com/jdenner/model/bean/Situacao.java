package com.jdenner.model.bean;

public enum Situacao {

    ABERTA(1, "Aberta"),
    FINALIZADA(2, "Finalizada"),
    CANCELADA(3, "Cancelada");

    private final int ID;
    private final String DESCRICAO;

    private Situacao(int id, String descricao) {
        this.ID = id;
        this.DESCRICAO = descricao;
    }

    public int getId() {
        return ID;
    }

    @Override
    public String toString() {
        return this.DESCRICAO;
    }
}
