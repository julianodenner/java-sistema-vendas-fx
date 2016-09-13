package com.jdenner.model.dao;

import com.jdenner.model.bean.Cliente;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClienteDAO {

    public static void salvar(Cliente cliente) throws Exception {
        if (cliente.getCodigo() == 0) {
            inserir(cliente);
        } else {
            alterar(cliente);
        }
    }

    public static void inserir(Cliente cliente) throws Exception {
        Conexao c = new Conexao();
        String sql = "INSERT INTO TBCLIENTE (NOME, CPF, DATANASCIMENTO) VALUES (?, ?, ?)";
        PreparedStatement ps = c.getConexao().prepareStatement(sql);
        ps.setString(1, cliente.getNome());
        ps.setString(2, cliente.getCpf());
        ps.setDate(3, new Date(cliente.getDataNascimento().getTime()));
        ps.execute();
        c.confirmar();
    }

    public static void alterar(Cliente cliente) throws Exception {
        Conexao c = new Conexao();
        String sql = "UPDATE TBCLIENTE SET NOME=?, CPF=?, DATANASCIMENTO=? WHERE CODIGO=?";
        PreparedStatement ps = c.getConexao().prepareStatement(sql);
        ps.setString(1, cliente.getNome());
        ps.setString(2, cliente.getCpf());
        ps.setDate(3, new Date(cliente.getDataNascimento().getTime()));
        ps.setInt(4, cliente.getCodigo());
        ps.execute();
        c.confirmar();
    }

    public static void excluir(Cliente cliente) throws Exception {
        Conexao c = new Conexao();
        String sql = "DELETE FROM TBCLIENTE WHERE CODIGO=?";
        PreparedStatement ps = c.getConexao().prepareStatement(sql);
        ps.setInt(1, cliente.getCodigo());
        ps.execute();
        c.confirmar();
    }

    public static int quantidade(String filtro) throws Exception {
        Conexao c = new Conexao();
        String sql = "SELECT COUNT(1) FROM TBCLIENTE WHERE NOME LIKE ?";
        PreparedStatement ps = c.getConexao().prepareStatement(sql);
        ps.setString(1, "%" + filtro + "%");
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }

        return 0;
    }

    public static ObservableList<Cliente> listar(String filtro, int quantidade, int pagina) throws Exception {
        Conexao c = new Conexao();
        String sql = "SELECT * FROM TBCLIENTE WHERE NOME LIKE ? ORDER BY NOME LIMIT ?,?";
        PreparedStatement ps = c.getConexao().prepareStatement(sql);
        ps.setString(1, "%" + filtro + "%");
        ps.setInt(2, pagina * quantidade);
        ps.setInt(3, quantidade);
        ResultSet rs = ps.executeQuery();

        ObservableList listaClientes = FXCollections.observableArrayList();
        while (rs.next()) {
            Cliente cliente = new Cliente();
            cliente.setCodigo(rs.getInt("CODIGO"));
            cliente.setNome(rs.getString("NOME"));
            cliente.setCpf(rs.getString("CPF"));
            cliente.setDataNascimento(rs.getDate("DATANASCIMENTO"));
            listaClientes.add(cliente);
        }

        return listaClientes;
    }

    public static Cliente recuperar(int codigo) throws Exception {
        Conexao c = new Conexao();
        String sql = "SELECT * FROM TBCLIENTE WHERE CODIGO=?";
        PreparedStatement ps = c.getConexao().prepareStatement(sql);
        ps.setInt(1, codigo);
        ResultSet rs = ps.executeQuery();

        Cliente cliente = new Cliente();
        if (rs.next()) {
            cliente.setCodigo(rs.getInt("CODIGO"));
            cliente.setNome(rs.getString("NOME"));
            cliente.setCpf(rs.getString("CPF"));
            cliente.setDataNascimento(rs.getDate("DATANASCIMENTO"));
        }

        return cliente;
    }
}
