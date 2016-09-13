package com.jdenner.model.dao;

import com.jdenner.model.bean.Compra;
import com.jdenner.model.bean.Situacao;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CompraDAO {

    public static void salvar(Compra compra) throws Exception {
        if (compra.getCodigo() == 0) {
            inserir(compra);
        } else {
            alterar(compra);
        }
    }

    private static void inserir(Compra compra) throws Exception {
        Conexao c = new Conexao();
        String sql = "INSERT INTO TBCOMPRA (CODIGOFORNECEDOR, DATACOMPRA, VALORTOTAL, SITUACAO) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = c.getConexao().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, compra.getFornecedor().getCodigo());
        ps.setDate(2, new Date(compra.getDataCompra().getTime()));
        ps.setDouble(3, compra.getValorTotal());
        ps.setInt(4, compra.getSituacao().getId());
        ps.execute();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        int idCompra = rs.getInt(1);
        
        for (Compra.ItemCompra iv : compra.getItens()) {
            sql = "INSERT INTO TBITEMCOMPRA (CODIGOPRODUTO, CODIGOCOMPRA, QUANTIDADE, VALORUNITARIO) VALUES (?, ?, ?, ?)";
            ps = c.getConexao().prepareStatement(sql);
            ps.setInt(1, iv.getProduto().getCodigo());
            ps.setInt(2, idCompra);
            ps.setInt(3, iv.getQuantidade());
            ps.setDouble(4, iv.getValorUnitario());
            ps.execute();

            if (compra.getSituacao() == Situacao.FINALIZADA) {
                ProdutoDAO.entradaEstoque(c, iv.getProduto().getCodigo(), iv.getQuantidade());
            }
        }
        c.confirmar();
    }

    private static void alterar(Compra compra) throws Exception {
        Conexao c = new Conexao();
        String sql = "UPDATE TBCOMPRA SET CODIGOFORNECEDOR=?, DATACOMPRA=?, VALORTOTAL=?, SITUACAO=? WHERE CODIGO=?";
        PreparedStatement ps = c.getConexao().prepareStatement(sql);
        ps.setInt(1, compra.getFornecedor().getCodigo());
        ps.setDate(2, new Date(compra.getDataCompra().getTime()));
        ps.setDouble(3, compra.getValorTotal());
        ps.setInt(4, compra.getSituacao().getId());
        ps.setInt(5, compra.getCodigo());
        ps.execute();

        for (Compra.ItemCompra iv : compra.getItensRemover()) {
            sql = "DELETE FROM TBITEMCOMPRA WHERE CODIGO=?";
            ps = c.getConexao().prepareStatement(sql);
            ps.setInt(1, iv.getCodigo());
            ps.execute();
        }

        for (Compra.ItemCompra iv : compra.getItens()) {
            if (iv.getCodigo() == 0) {
                sql = "INSERT INTO TBITEMCOMPRA (CODIGOPRODUTO, CODIGOCOMPRA, QUANTIDADE, VALORUNITARIO) VALUES (?, ?, ?, ?)";
                ps = c.getConexao().prepareStatement(sql);
                ps.setInt(1, iv.getProduto().getCodigo());
                ps.setInt(2, compra.getCodigo());
                ps.setInt(3, iv.getQuantidade());
                ps.setDouble(4, iv.getValorUnitario());
                ps.execute();
            } else {
                sql = "UPDATE TBITEMCOMPRA SET CODIGOPRODUTO=?, CODIGOCOMPRA=?, QUANTIDADE=?, VALORUNITARIO=? WHERE CODIGO=?";
                ps = c.getConexao().prepareStatement(sql);
                ps.setInt(1, iv.getProduto().getCodigo());
                ps.setInt(2, compra.getCodigo());
                ps.setInt(3, iv.getQuantidade());
                ps.setDouble(4, iv.getValorUnitario());
                ps.setInt(5, iv.getCodigo());
                ps.execute();
            }

            if (compra.getSituacao() == Situacao.FINALIZADA) {
                ProdutoDAO.entradaEstoque(c, iv.getProduto().getCodigo(), iv.getQuantidade());
            }
        }

        c.confirmar();
    }

    public static void excluir(Compra compra) throws Exception {
        Conexao c = new Conexao();
        String sql = "UPDATE TBCOMPRA SET CODIGOFORNECEDOR=?, DATACOMPRA=?, VALORTOTAL=?, SITUACAO=? WHERE CODIGO=?";
        PreparedStatement ps = c.getConexao().prepareStatement(sql);
        ps.setInt(1, compra.getFornecedor().getCodigo());
        ps.setDate(2, new Date(compra.getDataCompra().getTime()));
        ps.setDouble(3, compra.getValorTotal());
        ps.setInt(4, Situacao.CANCELADA.getId());
        ps.setInt(5, compra.getCodigo());
        ps.execute();
        c.confirmar();
    }

    public static int quantidade(String filtro) throws Exception {
        Conexao c = new Conexao();
        String sql = ""
                + " SELECT COUNT(1) "
                + " FROM TBCOMPRA AS C"
                + " INNER JOIN TBFORNECEDOR AS F "
                + " ON F.CODIGO = C.CODIGOFORNECEDOR "
                + " WHERE F.NOME LIKE ? ";
        PreparedStatement ps = c.getConexao().prepareStatement(sql);
        ps.setString(1, "%" + filtro + "%");
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }

        return 0;
    }

    public static ObservableList<Compra> listar(String filtro, int quantidade, int pagina) throws Exception {
        Conexao c = new Conexao();
        String sql = ""
                + " SELECT C.* "
                + " FROM TBCOMPRA AS C"
                + " INNER JOIN TBFORNECEDOR AS F "
                + " ON F.CODIGO = C.CODIGOFORNECEDOR "
                + " WHERE F.NOME LIKE ? "
                + " ORDER BY C.SITUACAO ASC, C.DATACOMPRA DESC "
                + " LIMIT ?,? ";
        PreparedStatement ps = c.getConexao().prepareStatement(sql);
        ps.setString(1, "%" + filtro + "%");
        ps.setInt(2, pagina * quantidade);
        ps.setInt(3, quantidade);
        ResultSet rs = ps.executeQuery();

        ObservableList listaCompras = FXCollections.observableArrayList();
        while (rs.next()) {
            Compra compra = new Compra();
            compra.setCodigo(rs.getInt("CODIGO"));
            compra.setFornecedor(FornecedorDAO.recuperar(rs.getInt("CODIGOFORNECEDOR")));
            compra.setDataCompra(rs.getDate("DATACOMPRA"));
            compra.setSituacao(rs.getInt("SITUACAO"));

            String sqlItem = "SELECT * FROM TBITEMCOMPRA WHERE CODIGOCOMPRA=?";
            PreparedStatement psItem = c.getConexao().prepareStatement(sqlItem);
            psItem.setInt(1, compra.getCodigo());
            ResultSet rsItem = psItem.executeQuery();

            while (rsItem.next()) {
                Compra.ItemCompra iv = compra.new ItemCompra();
                iv.setCodigo(rsItem.getInt("CODIGO"));
                iv.setProduto(ProdutoDAO.recuperar(rsItem.getInt("CODIGOPRODUTO")));
                iv.setQuantidade(rsItem.getInt("QUANTIDADE"));
                iv.setValorUnitario(rsItem.getDouble("VALORUNITARIO"));
                compra.addItem(iv);
            }

            listaCompras.add(compra);
        }

        return listaCompras;
    }

    public static Compra recuperar(int codigo) throws Exception {
        Conexao c = new Conexao();

        String sql = "SELECT * FROM TBCOMPRA WHERE CODIGO=?";
        PreparedStatement ps = c.getConexao().prepareStatement(sql);
        ps.setInt(1, codigo);
        ResultSet rs = ps.executeQuery();

        Compra compra = new Compra();
        if (rs.next()) {
            compra.setCodigo(rs.getInt("CODIGO"));
            compra.setFornecedor(FornecedorDAO.recuperar(rs.getInt("CODIGOFORNECEDOR")));
            compra.setDataCompra(rs.getDate("DATACOMPRA"));
            compra.setSituacao(rs.getInt("SITUACAO"));

            String sqlItem = "SELECT * FROM TBITEMCOMPRA WHERE CODIGOCOMPRA=?";
            PreparedStatement psItem = c.getConexao().prepareStatement(sqlItem);
            psItem.setInt(1, compra.getCodigo());
            ResultSet rsItem = psItem.executeQuery();

            while (rsItem.next()) {
                Compra.ItemCompra iv = compra.new ItemCompra();
                iv.setCodigo(rsItem.getInt("CODIGO"));
                iv.setProduto(ProdutoDAO.recuperar(rsItem.getInt("CODIGOPRODUTO")));
                iv.setQuantidade(rsItem.getInt("QUANTIDADE"));
                iv.setValorUnitario(rsItem.getDouble("VALORUNITARIO"));
                compra.addItem(iv);
            }
        }

        return compra;
    }
}
