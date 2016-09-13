package com.jdenner.model.bean;

import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Compra {

    private int codigo;
    private Fornecedor fornecedor;
    private Date dataCompra;
    private Situacao situacao;
    private ObservableList<ItemCompra> itens;
    private ObservableList<ItemCompra> itensRemover;

    public Compra() {
        this.codigo = 0;
        this.itens = FXCollections.observableArrayList();
        this.itensRemover = FXCollections.observableArrayList();
    }

    public Compra(int codigo) {
        this.codigo = codigo;
        this.itens = FXCollections.observableArrayList();
        this.itensRemover = FXCollections.observableArrayList();
    }

    public int getCodigo() {
        return codigo;
    }

    public int getCodigoEdicao() {
        if (getSituacao() == Situacao.ABERTA) {
            return codigo;
        } else {
            return 0;
        }
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Date getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(Date dataCompra) {
        this.dataCompra = dataCompra;
    }

    public double getValorTotal() {
        double total = 0;
        for (ItemCompra iv : itens) {
            total += (iv.getValorUnitario() * iv.getQuantidade());
        }
        return total;
    }

    public Situacao getSituacao() {
        return situacao;
    }

    public void setSituacao(Situacao situacao) {
        this.situacao = situacao;
    }

    public void setSituacao(int situacao) {
        if (situacao == Situacao.ABERTA.getId()) {
            setSituacao(Situacao.ABERTA);
        } else if (situacao == Situacao.FINALIZADA.getId()) {
            setSituacao(Situacao.FINALIZADA);
        } else if (situacao == Situacao.CANCELADA.getId()) {
            setSituacao(Situacao.CANCELADA);
        }
    }

    public ObservableList<ItemCompra> getItens() {
        return itens;
    }

    public ObservableList<ItemCompra> getItensRemover() {
        return itensRemover;
    }

    public void addItem(ItemCompra itemCompra) {
        itens.add(itemCompra);
    }

    public void removeItem(ItemCompra itemCompra) {
        itens.remove(itemCompra);
        if (itemCompra.getCodigo() != 0) {
            itensRemover.add(itemCompra);
        }
    }

    public int quantidadeItens() {
        return itens.size();
    }

    @Override
    public String toString() {
        return String.valueOf(this.codigo);
    }

    public class ItemCompra {

        private int codigo;
        private Produto produto;
        private int quantidade;
        private double valorUnitario;

        public ItemCompra() {
            this.codigo = 0;
        }

        public ItemCompra(int codigo) {
            this.codigo = codigo;
        }

        public int getCodigo() {
            return codigo;
        }

        public void setCodigo(int codigo) {
            this.codigo = codigo;
        }

        public Produto getProduto() {
            return produto;
        }

        public void setProduto(Produto produto) {
            this.produto = produto;
        }

        public int getQuantidade() {
            return quantidade;
        }

        public void setQuantidade(int quantidade) {
            this.quantidade = quantidade;
        }

        public double getValorUnitario() {
            return valorUnitario;
        }

        public void setValorUnitario(double valorUnitario) {
            this.valorUnitario = valorUnitario;
        }

        public double getSubtotal() {
            return valorUnitario * quantidade;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Compra.ItemCompra) {
                Compra.ItemCompra i = (Compra.ItemCompra) obj;
                return i.getCodigo() == getCodigo();
            }
            return false;
        }
    }

}
