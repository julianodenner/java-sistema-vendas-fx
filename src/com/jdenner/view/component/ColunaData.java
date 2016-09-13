package com.jdenner.view.component;

import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class ColunaData implements Callback<TableColumn<Object, Date>, TableCell<Object, Date>> {

    @Override
    public TableCell<Object, Date> call(TableColumn<Object, Date> param) {
        TableCell<Object, Date> cell = new TableCell<Object, Date>() {

            @Override
            public void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                setGraphic(null);
                setText(sdf.format(item));
            }
        };
        return cell;
    }

}
