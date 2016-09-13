package com.jdenner.model.bean;

import java.text.NumberFormat;

public class Produto {

    private int codigo;
    private String nome;
    private double precoCompra;
    private double precoVenda;
    private int quantidade;

    public Produto() {
        this.codigo = 0;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) throws Exception {
        if (nome.trim().length() < 3 || nome.trim().length() > 200) {
            throw new Exception("Nome inválido!");
        }
        this.nome = nome;
    }

    public double getPrecoCompra() {
        return precoCompra;
    }

    public String getPrecoCompraFormatado() {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        return nf.format(precoCompra);
    }

    public void setPrecoCompra(double precoCompra) throws Exception {
        if (precoCompra < 0) {
            throw new Exception("Preço de compra inválido!");
        }
        this.precoCompra = precoCompra;
    }

    public void setPrecoCompra(String precoCompra) throws Exception {
        NumberFormat nf = NumberFormat.getNumberInstance();
        setPrecoCompra(nf.parse(precoCompra).doubleValue());
    }

    public double getPrecoVenda() {
        return precoVenda;
    }

    public String getPrecoVendaFormatado() {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        return nf.format(precoVenda);
    }

    public void setPrecoVenda(double precoVenda) throws Exception {
        if (precoVenda < 0) {
            throw new Exception("Preço de venda inválido!");
        }
        this.precoVenda = precoVenda;
    }

    public void setPrecoVenda(String precoVenda) throws Exception {
        NumberFormat nf = NumberFormat.getNumberInstance();
        setPrecoVenda(nf.parse(precoVenda).doubleValue());
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return getNome();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Produto) {
            Produto p = (Produto) o;
            if (p.getCodigo() == this.getCodigo()) {
                return true;
            }
        }
        return false;
    }
}
