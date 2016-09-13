package com.jdenner.view.component;

import com.jdenner.model.bean.Fornecedor;
import com.jdenner.model.dao.FornecedorDAO;
import javafx.event.Event;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ComboFornecedor {

    private ComboBox cmb;

    public ComboFornecedor(ComboBox cmb) {
        this.cmb = cmb;
        this.cmb.getEditor().setOnKeyReleased(this::handleOnKeyReleased);
        this.cmb.setOnHidden(this::handleOnHiding);
    }

    public void handleOnKeyReleased(KeyEvent e) {

        if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.DOWN) {
            return;
        }

        if (!cmb.getSelectionModel().isEmpty() && (e.getCode() == KeyCode.BACK_SPACE || e.getCode() == KeyCode.ESCAPE || e.getCode() == KeyCode.DELETE)) {
            cmb.getEditor().setText("");
            cmb.getSelectionModel().clearSelection();
            cmb.hide();
        }

        try {
            cmb.setItems(FornecedorDAO.listar(cmb.getEditor().getText(), 10, 0));
            cmb.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleOnHiding(Event e) {
        try {
            Fornecedor s = (Fornecedor) cmb.getSelectionModel().getSelectedItem();
            cmb.getSelectionModel().select(s);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
