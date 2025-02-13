/*
 * Decompiled with CFR 0.152.
 */
package BlackSteel;

import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.metal.MetalComboBoxUI;

public class BlackSteelComboBoxUI
extends MetalComboBoxUI {
    private static final Border LIST_BORDER = new LineBorder(new Color(40, 40, 40), 1);

    public static ComponentUI createUI(JComponent c) {
        return new BlackSteelComboBoxUI();
    }

    @Override
    protected ComboPopup createPopup() {
        return new BlackSteelComboPopup(this.comboBox);
    }

    private class BlackSteelComboPopup
    extends BasicComboPopup {
        public BlackSteelComboPopup(JComboBox combo) {
            super(combo);
        }

        @Override
        protected void configurePopup() {
            this.setLayout(new BoxLayout(this, 1));
            this.setBorderPainted(true);
            this.setBorder(LIST_BORDER);
            this.setOpaque(false);
            this.add(this.scroller);
            this.setDoubleBuffered(true);
            this.setFocusable(false);
        }
    }
}

