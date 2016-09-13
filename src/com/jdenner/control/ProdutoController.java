package com.jdenner.control;

import com.jdenner.model.bean.Produto;
import com.jdenner.model.dao.ProdutoDAO;
import com.jdenner.view.component.BotaoEditar;
import com.jdenner.view.component.BotaoExcluir;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;
import javafx.util.Callback;

public class ProdutoController implements Initializable, Controller {

    private Produto produto;

    @FXML
    private Parent consulta;

    @FXML
    private TableView tabela;

    @FXML
    private TextField filtro;

    @FXML
    private TableColumn colunaNome;

    @FXML
    private TableColumn colunaEditar;

    @FXML
    private TableColumn colunaExcluir;

    @FXML
    private Pagination paginacao;

    @FXML
    private Parent formulario;

    @FXML
    private Label rotuloNome;

    @FXML
    private TextField campoNome;

    @FXML
    private Label rotuloPrecoCompra;

    @FXML
    private TextField campoPrecoCompra;

    @FXML
    private Label rotuloPrecoVenda;

    @FXML
    private TextField campoPrecoVenda;

    @FXML
    private Button botaoSalvar;

    @FXML
    private Button botaoCancelar;

    private final int QUANTIDADE_PAGINA = 9;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaEditar.setCellFactory(new BotaoEditar(this));
        colunaEditar.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colunaExcluir.setCellFactory(new BotaoExcluir(this));
        colunaExcluir.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        paginacao.setPageFactory(new Callback<Integer, Node>() {

            @Override
            public Node call(Integer pagina) {
                atualizarGrade(pagina);
                return tabela;
            }
        });
        trocar(false);
        atualizarGrade(0);
    }

    private void trocar(boolean form) {
        formulario.setVisible(form);
        consulta.setVisible(!form);
    }

    @FXML
    private void novo() {
        this.produto = new Produto();
        campoNome.setText("");
        campoPrecoCompra.setText("");
        campoPrecoVenda.setText("");
        trocar(true);
    }

    @FXML
    private void filtrar() {
        atualizarGrade(0);
    }

    @Override
    public void editar(int codigo) {
        try {
            this.produto = this.produto = ProdutoDAO.recuperar(codigo);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        campoNome.setText(produto.getNome());
        campoPrecoCompra.setText(produto.getPrecoCompraFormatado());
        campoPrecoVenda.setText(produto.getPrecoVendaFormatado());
        trocar(true);
    }

    @Override
    public void excluir(int codigo) {
        try {
            this.produto = ProdutoDAO.recuperar(codigo);
            ProdutoDAO.excluir(produto);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        atualizarGrade(0);
    }

    @FXML
    private void salvar() {
        rotuloNome.setTextFill(Paint.valueOf("#333333"));
        rotuloPrecoCompra.setTextFill(Paint.valueOf("#333333"));
        rotuloPrecoVenda.setTextFill(Paint.valueOf("#333333"));

        boolean erro = false;

        try {
            produto.setNome(campoNome.getText().trim());
        } catch (Exception e) {
            rotuloNome.setTextFill(Paint.valueOf("red"));
            erro = true;
        }
        try {
            produto.setPrecoCompra(campoPrecoCompra.getText());
        } catch (Exception e) {
            rotuloPrecoCompra.setTextFill(Paint.valueOf("red"));
            erro = true;
        }
        try {
            produto.setPrecoVenda(campoPrecoVenda.getText());
        } catch (Exception e) {
            rotuloPrecoVenda.setTextFill(Paint.valueOf("red"));
            erro = true;
        }

        if (erro) {
            return;
        }

        try {
            ProdutoDAO.salvar(produto);
        } catch (Exception e) {
            e.printStackTrace();
        }

        trocar(false);
        atualizarGrade(0);
    }

    @FXML
    private void cancelar() {
        trocar(false);
        atualizarGrade(0);
    }

    private void atualizarGrade(int pagina) {
        try {
            paginacao.setPageCount((int) Math.ceil(((double) ProdutoDAO.quantidade(filtro.getText())) / QUANTIDADE_PAGINA));
            tabela.setItems(ProdutoDAO.listar(filtro.getText(), QUANTIDADE_PAGINA, pagina));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
