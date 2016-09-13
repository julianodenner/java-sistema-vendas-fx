package com.jdenner.view.component;

import com.jdenner.control.Itens;
import java.text.ParseException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

/**
 *
 * @author Juliano
 */
public class BotaoRemoverItem implements Callback<TableColumn<Object, Integer>, TableCell<Object, Integer>> {

    private Itens itens;

    public BotaoRemoverItem(Itens controller) {
        this.itens = controller;
    }

    @Override
    public TableCell<Object, Integer> call(TableColumn<Object, Integer> param) {
        TableCell<Object, Integer> cell = new TableCell<Object, Integer>() {

            @Override
            public void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                Button btn = new Button();
                Image img = new Image(getClass().getResourceAsStream("/com/jdenner/view/img/Excluir.png"));
                btn.setGraphic(new ImageView(img));
                btn.setStyle("-fx-background-color: transparent;");
                btn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            itens.removerItem(item);
                        } catch (ParseException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                if (item == 0) {
                    btn.setDisable(true);
                }
                setGraphic(btn);
                setText(null);
            }
        };
        return cell;
    }

}
