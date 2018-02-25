package swidgets;

import scala.runtime.BoxedUnit;
import sodium.*;

import javax.swing.*;

public class SLabel extends JLabel
{
    public SLabel(Cell<String> text) {
        super("");
        l = text.updates().listen(t -> {
            if (SwingUtilities.isEventDispatchThread())
                setText(t);
            else
                SwingUtilities.invokeLater(() -> {
                    setText(t);
                });
            return BoxedUnit.UNIT;
        });
        // Set the text at the end of the transaction so SLabel works
        // with CellLoops.
        Transaction.post(
            () -> SwingUtilities.invokeLater(() -> {
                setText(text.sample());
            })
        );
    }

    private final Listener l;

    public void removeNotify() {
        l.unlisten();
        super.removeNotify();
    }
}

