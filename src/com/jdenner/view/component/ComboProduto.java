package com.jdenner.view.component;

import com.jdenner.model.bean.Produto;
import com.jdenner.model.dao.ProdutoDAO;
import javafx.event.Event;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ComboProduto {

    private ComboBox cmb;

    public ComboProduto(ComboBox cmb) {
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
            cmb.setItems(ProdutoDAO.listar(cmb.getEditor().getText(), 10, 0));
            cmb.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleOnHiding(Event e) {
        try {
            Produto s = (Produto) cmb.getSelectionModel().getSelectedItem();
            cmb.getSelectionModel().select(s);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
