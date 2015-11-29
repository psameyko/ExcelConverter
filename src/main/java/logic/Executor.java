package logic;

import javax.swing.*;
import javax.swing.tree.ExpandVetoException;

/**
 * Created by psameyko on 29.11.15.
 */
public abstract class Executor {


    public void execute() {
        SwingWorker s = new SwingWorker() {
            @Override
            protected void done() {
//                onSuccess();
            }

            @Override
            protected Object doInBackground() throws Exception {
                try {
                    action();
                    onSuccess();
                } catch (Exception e) {
                    onFail(e);
                    throw e;
                }
                return null;
            }
        };
        s.execute();
    }

    protected abstract void action() throws Exception;

    public abstract void onFail(Exception e);

    public abstract void onSuccess();
}
