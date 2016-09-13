package com.jdenner.view.component;

import java.text.NumberFormat;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class ColunaValor implements Callback<TableColumn<Object, Double>, TableCell<Object, Double>> {

    @Override
    public TableCell<Object, Double> call(TableColumn<Object, Double> param) {
        TableCell<Object, Double> cell = new TableCell<Object, Double>() {

            @Override
            public void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                NumberFormat nf = NumberFormat.getNumberInstance();
                nf.setMinimumFractionDigits(2);
                nf.setMaximumFractionDigits(2);
                setGraphic(null);
                setText(nf.format(item));
            }
        };
        return cell;
    }

}
