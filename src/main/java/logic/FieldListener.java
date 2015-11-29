package logic;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Created by psameyko on 27.11.15.
 */
public class FieldListener implements DocumentListener {

    private AppController ctrl;

    public FieldListener(AppController ctrl) {
        this.ctrl = ctrl;
    }
    @Override
    public void insertUpdate(DocumentEvent e) {
        ctrl.onFieldValueChange();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        ctrl.onFieldValueChange();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        ctrl.onFieldValueChange();
    }
}
