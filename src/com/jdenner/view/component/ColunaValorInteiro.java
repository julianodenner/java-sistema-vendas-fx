package com.jdenner.view.component;

import java.text.NumberFormat;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class ColunaValorInteiro implements Callback<TableColumn<Object, Integer>, TableCell<Object, Integer>> {

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

                NumberFormat nf = NumberFormat.getIntegerInstance();
                setGraphic(null);
                setText(nf.format(item));
            }
        };
        return cell;
    }

}
