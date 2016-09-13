package com.jdenner.model.dao;

import com.jdenner.model.bean.Produto;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProdutoDAO {

    public static void salvar(Produto produto) throws Exception {
        if (produto.getCodigo() == 0) {
            inserir(produto);
        } else {
            alterar(produto);
        }
    }

    public static void inserir(Produto produto) throws Exception {
        Conexao c = new Conexao();
        String sql = "INSERT INTO TBPRODUTO (NOME, PRECOCOMPRA, PRECOVENDA, QUANTIDADEESTOQUE) VALUES (?, ?, ?, 0)";
        PreparedStatement ps = c.getConexao().prepareStatement(sql);
        ps.setString(1, produto.getNome());
        ps.setDouble(2, produto.getPrecoCompra());
        ps.setDouble(3, produto.getPrecoVenda());
        ps.execute();
        c.confirmar();
    }

    public static void alterar(Produto produto) throws Exception {
        Conexao c = new Conexao();
        String sql = "UPDATE TBPRODUTO SET NOME=?, PRECOCOMPRA=?, PRECOVENDA=? WHERE CODIGO=?";
        PreparedStatement ps = c.getConexao().prepareStatement(sql);
        ps.setString(1, produto.getNome());
        ps.setDouble(2, produto.getPrecoCompra());
        ps.setDouble(3, produto.getPrecoVenda());
        ps.setInt(4, produto.getCodigo());
        ps.execute();
        c.confirmar();
    }

    public static void entradaEstoque(Conexao c, int codigo, int quantidade) throws Exception {
        String sql = "UPDATE TBPRODUTO SET QUANTIDADEESTOQUE= QUANTIDADEESTOQUE  + ? WHERE CODIGO=?";
        PreparedStatement ps = c.getConexao().prepareStatement(sql);
        ps.setInt(1, quantidade);
        ps.setInt(2, codigo);
        ps.execute();
    }

    public static void saidaEstoque(Conexao c, int codigo, int quantidade) throws Exception {
        String sql = "UPDATE TBPRODUTO SET QUANTIDADEESTOQUE= QUANTIDADEESTOQUE  - ? WHERE CODIGO=?";
        PreparedStatement ps = c.getConexao().prepareStatement(sql);
        ps.setInt(1, quantidade);
        ps.setInt(2, codigo);
        ps.execute();
    }

    public static void excluir(Produto produto) throws Exception {
        Conexao c = new Conexao();
        String sql = "DELETE FROM TBPRODUTO WHERE CODIGO=?";
        PreparedStatement ps = c.getConexao().prepareStatement(sql);
        ps.setInt(1, produto.getCodigo());
        ps.execute();
        c.confirmar();
    }

    public static int quantidade(String filtro) throws Exception {
        Conexao c = new Conexao();
        String sql = "SELECT COUNT(1) FROM TBPRODUTO WHERE NOME LIKE ?";
        PreparedStatement ps = c.getConexao().prepareStatement(sql);
        ps.setString(1, "%" + filtro + "%");
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }

        return 0;
    }

    public static ObservableList<Produto> listar(String filtro, int quantidade, int pagina) throws Exception {
        Conexao c = new Conexao();
        String sql = "SELECT * FROM TBPRODUTO WHERE NOME LIKE ? ORDER BY NOME LIMIT ?,?";
        PreparedStatement ps = c.getConexao().prepareStatement(sql);
        ps.setString(1, "%" + filtro + "%");
        ps.setInt(2, pagina * quantidade);
        ps.setInt(3, quantidade);
        ResultSet rs = ps.executeQuery();

        ObservableList<Produto> listaProdutos = FXCollections.observableArrayList();
        while (rs.next()) {
            Produto produto = new Produto();
            produto.setCodigo(rs.getInt("CODIGO"));
            produto.setNome(rs.getString("NOME"));
            produto.setPrecoCompra(rs.getDouble("PRECOCOMPRA"));
            produto.setPrecoVenda(rs.getDouble("PRECOVENDA"));
            produto.setQuantidade(rs.getInt("QUANTIDADEESTOQUE"));
            listaProdutos.add(produto);
        }

        return listaProdutos;
    }

    public static Produto recuperar(int codigo) throws Exception {
        Conexao c = new Conexao();
        String sql = "SELECT * FROM TBPRODUTO WHERE CODIGO=?";
        PreparedStatement ps = c.getConexao().prepareStatement(sql);
        ps.setInt(1, codigo);
        ResultSet rs = ps.executeQuery();

        Produto produto = new Produto();
        if (rs.next()) {
            produto.setCodigo(rs.getInt("CODIGO"));
            produto.setNome(rs.getString("NOME"));
            produto.setPrecoCompra(rs.getDouble("PRECOCOMPRA"));
            produto.setPrecoVenda(rs.getDouble("PRECOVENDA"));
            produto.setQuantidade(rs.getInt("QUANTIDADEESTOQUE"));
        }

        return produto;
    }
}
