package com.jdenner.control;

import com.jdenner.model.bean.Fornecedor;
import com.jdenner.model.dao.FornecedorDAO;
import com.jdenner.util.ConversorData;
import com.jdenner.view.component.BotaoEditar;
import com.jdenner.view.component.BotaoExcluir;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;
import javafx.util.Callback;

public class FornecedorController implements Initializable, Controller {

    private Fornecedor fornecedor;

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
    private Label rotuloCnpj;

    @FXML
    private TextField campoCnpj;

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
        this.fornecedor = new Fornecedor();
        campoNome.setText("");
        campoCnpj.setText("");
        trocar(true);
    }

    @FXML
    private void filtrar() {
        atualizarGrade(0);
    }

    @Override
    public void editar(int codigo) {
        try {
            this.fornecedor = this.fornecedor = FornecedorDAO.recuperar(codigo);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        campoNome.setText(fornecedor.getNome());
        campoCnpj.setText(fornecedor.getCnpj());
        trocar(true);
    }

    @Override
    public void excluir(int codigo) {
        try {
            this.fornecedor = FornecedorDAO.recuperar(codigo);
            FornecedorDAO.excluir(fornecedor);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        atualizarGrade(0);
    }

    @FXML
    private void salvar() {
        rotuloNome.setTextFill(Paint.valueOf("#333333"));
        rotuloCnpj.setTextFill(Paint.valueOf("#333333"));

        boolean erro = false;

        try {
            fornecedor.setNome(campoNome.getText().trim());
        } catch (Exception e) {
            rotuloNome.setTextFill(Paint.valueOf("red"));
            erro = true;
        }
        
        try {
            fornecedor.setCnpj(campoCnpj.getText());
        } catch (Exception e) {
            rotuloCnpj.setTextFill(Paint.valueOf("red"));
            erro = true;
        }
        
        if (erro) {
            return;
        }

        try {
            FornecedorDAO.salvar(fornecedor);
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
            paginacao.setPageCount((int) Math.ceil(((double) FornecedorDAO.quantidade(filtro.getText())) / QUANTIDADE_PAGINA));
            tabela.setItems(FornecedorDAO.listar(filtro.getText(), QUANTIDADE_PAGINA, pagina));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
