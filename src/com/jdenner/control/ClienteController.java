package com.jdenner.control;

import com.jdenner.model.bean.Cliente;
import com.jdenner.model.dao.ClienteDAO;
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

public class ClienteController implements Initializable, Controller {

    private Cliente cliente;

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
    private Label rotuloCpf;

    @FXML
    private TextField campoCpf;

    @FXML
    private Label rotuloDataNascimento;

    @FXML
    private DatePicker campoDataNascimento;

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
        this.cliente = new Cliente();
        campoNome.setText("");
        campoCpf.setText("");
        campoDataNascimento.setValue(LocalDate.of(2000, 1, 1));
        trocar(true);
    }

    @FXML
    private void filtrar() {
        atualizarGrade(0);
    }

    @Override
    public void editar(int codigo) {
        try {
            this.cliente = this.cliente = ClienteDAO.recuperar(codigo);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        campoNome.setText(cliente.getNome());
        campoCpf.setText(cliente.getCpf());
        campoDataNascimento.setValue(ConversorData.converter(cliente.getDataNascimento()));
        trocar(true);
    }

    @Override
    public void excluir(int codigo) {
        try {
            this.cliente = ClienteDAO.recuperar(codigo);
            ClienteDAO.excluir(cliente);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        atualizarGrade(0);
    }

    @FXML
    private void salvar() {
        rotuloNome.setTextFill(Paint.valueOf("#333333"));
        rotuloCpf.setTextFill(Paint.valueOf("#333333"));
        rotuloDataNascimento.setTextFill(Paint.valueOf("#333333"));

        boolean erro = false;

        try {
            cliente.setNome(campoNome.getText().trim());
        } catch (Exception e) {
            rotuloNome.setTextFill(Paint.valueOf("red"));
            erro = true;
        }
        try {
            cliente.setCpf(campoCpf.getText());
        } catch (Exception e) {
            rotuloCpf.setTextFill(Paint.valueOf("red"));
            erro = true;
        }
        try {
            cliente.setDataNascimento(ConversorData.converter(campoDataNascimento.getValue()));
        } catch (Exception e) {
            rotuloDataNascimento.setTextFill(Paint.valueOf("red"));
            erro = true;
        }

        if (erro) {
            return;
        }

        try {
            ClienteDAO.salvar(cliente);
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
            paginacao.setPageCount((int) Math.ceil(((double) ClienteDAO.quantidade(filtro.getText())) / QUANTIDADE_PAGINA));
            tabela.setItems(ClienteDAO.listar(filtro.getText(), QUANTIDADE_PAGINA, pagina));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
