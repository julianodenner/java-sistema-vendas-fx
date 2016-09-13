package com.jdenner.model.dao;

import com.jdenner.model.bean.Fornecedor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FornecedorDAO {

    public static void salvar(Fornecedor fornecedor) throws Exception {
        if (fornecedor.getCodigo() == 0) {
            inserir(fornecedor);
        } else {
            alterar(fornecedor);
        }
    }

    public static void inserir(Fornecedor fornecedor) throws Exception {
        Conexao c = new Conexao();
        String sql = "INSERT INTO TBFORNECEDOR (NOME, CNPJ) VALUES (?, ?)";
        PreparedStatement ps = c.getConexao().prepareStatement(sql);
        ps.setString(1, fornecedor.getNome());
        ps.setString(2, fornecedor.getCnpj());
        ps.execute();
        c.confirmar();
    }

    public static void alterar(Fornecedor fornecedor) throws Exception {
        Conexao c = new Conexao();
        String sql = "UPDATE TBFORNECEDOR SET NOME=?, CNPJ=? WHERE CODIGO=?";
        PreparedStatement ps = c.getConexao().prepareStatement(sql);
        ps.setString(1, fornecedor.getNome());
        ps.setString(2, fornecedor.getCnpj());
        ps.setInt(3, fornecedor.getCodigo());
        ps.execute();
        c.confirmar();
    }

    public static void excluir(Fornecedor fornecedor) throws Exception {
        Conexao c = new Conexao();
        String sql = "DELETE FROM TBFORNECEDOR WHERE CODIGO=?";
        PreparedStatement ps = c.getConexao().prepareStatement(sql);
        ps.setInt(1, fornecedor.getCodigo());
        ps.execute();
        c.confirmar();
    }

    public static int quantidade(String filtro) throws Exception {
        Conexao c = new Conexao();
        String sql = "SELECT COUNT(1) FROM TBFORNECEDOR WHERE NOME LIKE ?";
        PreparedStatement ps = c.getConexao().prepareStatement(sql);
        ps.setString(1, "%" + filtro + "%");
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }

        return 0;
    }

    public static ObservableList<Fornecedor> listar(String filtro, int quantidade, int pagina) throws Exception {
        Conexao c = new Conexao();
        String sql = "SELECT * FROM TBFORNECEDOR WHERE NOME LIKE ? ORDER BY NOME LIMIT ?,?";
        PreparedStatement ps = c.getConexao().prepareStatement(sql);
        ps.setString(1, "%" + filtro + "%");
        ps.setInt(2, pagina * quantidade);
        ps.setInt(3, quantidade);
        ResultSet rs = ps.executeQuery();

        ObservableList listaFornecedors = FXCollections.observableArrayList();
        while (rs.next()) {
            Fornecedor fornecedor = new Fornecedor();
            fornecedor.setCodigo(rs.getInt("CODIGO"));
            fornecedor.setNome(rs.getString("NOME"));
            fornecedor.setCnpj(rs.getString("CNPJ"));
            listaFornecedors.add(fornecedor);
        }

        return listaFornecedors;
    }

    public static Fornecedor recuperar(int codigo) throws Exception {
        Conexao c = new Conexao();
        String sql = "SELECT * FROM TBFORNECEDOR WHERE CODIGO=?";
        PreparedStatement ps = c.getConexao().prepareStatement(sql);
        ps.setInt(1, codigo);
        ResultSet rs = ps.executeQuery();

        Fornecedor fornecedor = new Fornecedor();
        if (rs.next()) {
            fornecedor.setCodigo(rs.getInt("CODIGO"));
            fornecedor.setNome(rs.getString("NOME"));
            fornecedor.setCnpj(rs.getString("CNPJ"));
        }

        return fornecedor;
    }
}
