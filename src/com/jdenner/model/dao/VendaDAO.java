package com.jdenner.model.dao;

import com.jdenner.model.bean.Situacao;
import com.jdenner.model.bean.Venda;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class VendaDAO {

    public static void salvar(Venda venda) throws Exception {
        if (venda.getCodigo() == 0) {
            inserir(venda);
        } else {
            alterar(venda);
        }
    }

    public static void inserir(Venda venda) throws Exception {
        Conexao c = new Conexao();
        String sql = "INSERT INTO TBVENDA (CODIGOCLIENTE, DATAVENDA, VALORTOTAL, SITUACAO) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = c.getConexao().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, venda.getCliente().getCodigo());
        ps.setDate(2, new Date(venda.getDataVenda().getTime()));
        ps.setDouble(3, venda.getValorTotal());
        ps.setInt(4, venda.getSituacao().getId());
        ps.execute();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        int idVenda = rs.getInt(1);

        for (Venda.ItemVenda iv : venda.getItens()) {
            sql = "INSERT INTO TBITEMVENDA (CODIGOPRODUTO, CODIGOVENDA, QUANTIDADE, VALORUNITARIO) VALUES (?, ?, ?, ?)";
            ps = c.getConexao().prepareStatement(sql);
            ps.setInt(1, iv.getProduto().getCodigo());
            ps.setInt(2, idVenda);
            ps.setInt(3, iv.getQuantidade());
            ps.setDouble(4, iv.getValorUnitario());
            ps.execute();

            if (venda.getSituacao() == Situacao.FINALIZADA) {
                ProdutoDAO.saidaEstoque(c, iv.getProduto().getCodigo(), iv.getQuantidade());
            }
        }
        c.confirmar();
    }

    public static void alterar(Venda venda) throws Exception {
        Conexao c = new Conexao();
        String sql = "UPDATE TBVENDA SET CODIGOCLIENTE=?, DATAVENDA=?, VALORTOTAL=?, SITUACAO=? WHERE CODIGO=?";
        PreparedStatement ps = c.getConexao().prepareStatement(sql);
        ps.setInt(1, venda.getCliente().getCodigo());
        ps.setDate(2, new Date(venda.getDataVenda().getTime()));
        ps.setDouble(3, venda.getValorTotal());
        ps.setInt(4, venda.getSituacao().getId());
        ps.setInt(5, venda.getCodigo());
        ps.execute();

        for (Venda.ItemVenda iv : venda.getItensRemover()) {
            sql = "DELETE FROM TBITEMVENDA WHERE CODIGO=?";
            ps = c.getConexao().prepareStatement(sql);
            ps.setInt(1, iv.getCodigo());
            ps.execute();
        }

        for (Venda.ItemVenda iv : venda.getItens()) {
            if (iv.getCodigo() == 0) {
                sql = "INSERT INTO TBITEMVENDA (CODIGOPRODUTO, CODIGOVENDA, QUANTIDADE, VALORUNITARIO) VALUES (?, ?, ?, ?)";
                ps = c.getConexao().prepareStatement(sql);
                ps.setInt(1, iv.getProduto().getCodigo());
                ps.setInt(2, venda.getCodigo());
                ps.setInt(3, iv.getQuantidade());
                ps.setDouble(4, iv.getValorUnitario());
                ps.execute();
            } else {
                sql = "UPDATE TBITEMVENDA SET CODIGOPRODUTO=?, CODIGOVENDA=?, QUANTIDADE=?, VALORUNITARIO=? WHERE CODIGO=?";
                ps = c.getConexao().prepareStatement(sql);
                ps.setInt(1, iv.getProduto().getCodigo());
                ps.setInt(2, venda.getCodigo());
                ps.setInt(3, iv.getQuantidade());
                ps.setDouble(4, iv.getValorUnitario());
                ps.setInt(5, iv.getCodigo());
                ps.execute();
            }

            if (venda.getSituacao() == Situacao.FINALIZADA) {
                ProdutoDAO.saidaEstoque(c, iv.getProduto().getCodigo(), iv.getQuantidade());
            }
        }

        c.confirmar();
    }

    public static void excluir(Venda venda) throws Exception {
        Conexao c = new Conexao();
        String sql = "UPDATE TBVENDA SET CODIGOCLIENTE=?, DATAVENDA=?, VALORTOTAL=?, SITUACAO=? WHERE CODIGO=?";
        PreparedStatement ps = c.getConexao().prepareStatement(sql);
        ps.setInt(1, venda.getCliente().getCodigo());
        ps.setDate(2, new Date(venda.getDataVenda().getTime()));
        ps.setDouble(3, venda.getValorTotal());
        ps.setInt(4, Situacao.CANCELADA.getId());
        ps.setInt(5, venda.getCodigo());
        ps.execute();
        c.confirmar();
    }

    public static int quantidade(String filtro) throws Exception {
        Conexao c = new Conexao();
        String sql = ""
                + " SELECT COUNT(1) "
                + " FROM TBVENDA AS V"
                + " INNER JOIN TBCLIENTE AS C "
                + " ON C.CODIGO = V.CODIGOCLIENTE "
                + " WHERE C.NOME LIKE ? ";
        PreparedStatement ps = c.getConexao().prepareStatement(sql);
        ps.setString(1, "%" + filtro + "%");
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }

        return 0;
    }

    public static ObservableList<Venda> listar(String filtro, int quantidade, int pagina) throws Exception {
        Conexao c = new Conexao();
        String sql = ""
                + " SELECT V.* "
                + " FROM TBVENDA AS V"
                + " INNER JOIN TBCLIENTE AS C "
                + " ON C.CODIGO = V.CODIGOCLIENTE "
                + " WHERE C.NOME LIKE ? "
                + " ORDER BY V.SITUACAO ASC, V.DATAVENDA DESC "
                + " LIMIT ?,? ";
        PreparedStatement ps = c.getConexao().prepareStatement(sql);
        ps.setString(1, "%" + filtro + "%");
        ps.setInt(2, pagina * quantidade);
        ps.setInt(3, quantidade);
        ResultSet rs = ps.executeQuery();

        ObservableList listaVendas = FXCollections.observableArrayList();
        while (rs.next()) {
            Venda venda = new Venda();
            venda.setCodigo(rs.getInt("CODIGO"));
            venda.setCliente(ClienteDAO.recuperar(rs.getInt("CODIGOCLIENTE")));
            venda.setDataVenda(rs.getDate("DATAVENDA"));
            venda.setSituacao(rs.getInt("SITUACAO"));

            String sqlItem = "SELECT * FROM TBITEMVENDA WHERE CODIGOVENDA=?";
            PreparedStatement psItem = c.getConexao().prepareStatement(sqlItem);
            psItem.setInt(1, venda.getCodigo());
            ResultSet rsItem = psItem.executeQuery();

            while (rsItem.next()) {
                Venda.ItemVenda iv = venda.new ItemVenda();
                iv.setCodigo(rsItem.getInt("CODIGO"));
                iv.setProduto(ProdutoDAO.recuperar(rsItem.getInt("CODIGOPRODUTO")));
                iv.setQuantidade(rsItem.getInt("QUANTIDADE"));
                iv.setValorUnitario(rsItem.getDouble("VALORUNITARIO"));
                venda.addItem(iv);
            }

            listaVendas.add(venda);
        }

        return listaVendas;
    }

    public static Venda recuperar(int codigo) throws Exception {
        Conexao c = new Conexao();
        String sql = "SELECT * FROM TBVENDA WHERE CODIGO=?";
        PreparedStatement ps = c.getConexao().prepareStatement(sql);
        ps.setInt(1, codigo);
        ResultSet rs = ps.executeQuery();

        Venda venda = new Venda();
        if (rs.next()) {
            venda.setCodigo(rs.getInt("CODIGO"));
            venda.setCliente(ClienteDAO.recuperar(rs.getInt("CODIGOCLIENTE")));
            venda.setDataVenda(rs.getDate("DATAVENDA"));
            venda.setSituacao(rs.getInt("SITUACAO"));

            String sqlItem = "SELECT * FROM TBITEMVENDA WHERE CODIGOVENDA=?";
            PreparedStatement psItem = c.getConexao().prepareStatement(sqlItem);
            psItem.setInt(1, venda.getCodigo());
            ResultSet rsItem = psItem.executeQuery();

            while (rsItem.next()) {
                Venda.ItemVenda iv = venda.new ItemVenda();
                iv.setCodigo(rsItem.getInt("CODIGO"));
                iv.setProduto(ProdutoDAO.recuperar(rsItem.getInt("CODIGOPRODUTO")));
                iv.setQuantidade(rsItem.getInt("QUANTIDADE"));
                iv.setValorUnitario(rsItem.getDouble("VALORUNITARIO"));
                venda.addItem(iv);
            }
        }

        return venda;
    }
}
