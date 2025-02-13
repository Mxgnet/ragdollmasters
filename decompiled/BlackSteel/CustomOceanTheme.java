/*
 * Decompiled with CFR 0.152.
 */
package BlackSteel;

import java.awt.Color;
import java.awt.Insets;
import java.util.Arrays;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.OceanTheme;

public class CustomOceanTheme
extends OceanTheme {
    private final String font;
    private static final ColorUIResource WHITE = new ColorUIResource(new Color(15, 15, 15));
    private static ColorUIResource PRIMARY1 = new ColorUIResource(new Color(185, 0, 0));
    private static final ColorUIResource PRIMARY2 = new ColorUIResource(new Color(60, 60, 60));
    private static final ColorUIResource PRIMARY3 = new ColorUIResource(new Color(130, 130, 130));
    private static final ColorUIResource SECONDARY1 = new ColorUIResource(new Color(60, 60, 60));
    private static final ColorUIResource SECONDARY2 = new ColorUIResource(new Color(25, 25, 25));
    private static final ColorUIResource SECONDARY3 = new ColorUIResource(Color.black);
    private static ColorUIResource CONTROL_TEXT_COLOR = new ColorUIResource(new Color(204, 0, 0));
    private static final ColorUIResource INACTIVE_CONTROL_TEXT_COLOR = new ColorUIResource(new Color(45, 45, 45));
    private static final ColorUIResource MENU_DISABLED_FOREGROUND = new ColorUIResource(new Color(45, 45, 45));
    private static ColorUIResource OCEAN_BLACK = new ColorUIResource(new Color(185, 0, 0));
    private static final ColorUIResource OCEAN_DROP = new ColorUIResource(Color.cyan);

    public CustomOceanTheme(Color c) {
        this.font = UIManager.getFont("button.font") == null ? "FORCED SQUARE" : UIManager.getFont("button.font").getFontName();
        PRIMARY1 = new ColorUIResource(c);
        CONTROL_TEXT_COLOR = new ColorUIResource(c.brighter());
        OCEAN_BLACK = new ColorUIResource(c);
    }

    public CustomOceanTheme(Color c, String fontName) {
        this.font = fontName;
        PRIMARY1 = new ColorUIResource(c);
        CONTROL_TEXT_COLOR = new ColorUIResource(c.brighter());
        OCEAN_BLACK = new ColorUIResource(c);
    }

    @Override
    public void addCustomEntriesToTable(UIDefaults table) {
        super.addCustomEntriesToTable(table);
        Color focusBorder = Color.magenta;
        List<Object> buttonGradient = Arrays.asList(Float.valueOf(0.4f), Float.valueOf(0.0f), new ColorUIResource(new Color(10, 10, 10)), new Color(10, 10, 10), this.getSecondary2());
        ColorUIResource cccccc = new ColorUIResource(Color.pink);
        ColorUIResource c8ddf2 = new ColorUIResource(Color.magenta);
        List<Object> sliderGradient = Arrays.asList(Float.valueOf(0.3f), Float.valueOf(0.2f), new ColorUIResource(new Color(15, 15, 15)), new Color(15, 15, 15), this.getSecondary2());
        Object[] defaults = new Object[]{"Button.gradient", buttonGradient, "Button.toolBarBorderBackground", INACTIVE_CONTROL_TEXT_COLOR, "Button.border", BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(20, 20, 20)), BorderFactory.createEmptyBorder(3, 0, 3, 0)), "Button.font", new FontUIResource(this.font, 0, 13), "CheckBox.gradient", buttonGradient, "CheckBoxMenuItem.gradient", buttonGradient, "Label.disabledForeground", this.getInactiveControlTextColor(), "MenuBar.gradient", Arrays.asList(Float.valueOf(1.0f), Float.valueOf(0.0f), new ColorUIResource(new Color(10, 10, 10)), new Color(10, 10, 10), this.getSecondary2()), "InternalFrame.activeTitleGradient", buttonGradient, "List.focusCellHighlightBorder", focusBorder, "List.selectionBackground", MENU_DISABLED_FOREGROUND, "PasswordField.font", new FontUIResource(this.font, 0, 13), "ProgressBar.foreground", this.getPrimary1().darker(), "RadioButton.gradient", buttonGradient, "RadioButtonMenuItem.gradient", buttonGradient, "ScrollBar.gradient", buttonGradient, "Slider.altTrackColor", this.getPrimary2(), "Slider.gradient", sliderGradient, "Slider.focusGradient", sliderGradient, "SplitPane.oneTouchButtonsOpaque", Boolean.FALSE, "SplitPane.dividerFocusColor", c8ddf2, "TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0), "TabbedPane.tabsOverlapBorder", true, "TabbedPane.darkShadow", new Color(45, 45, 45), "TabbedPane.darkShadow", new Color(45, 45, 45), "TabbedPane.unselectedHighlight", new Color(30, 30, 30), "TabbedPane.tabsOpaque", true, "TabbedPane.selected", new Color(30, 30, 30), "TabbedPane.unselectedBackground", new Color(30, 30, 30), "TabbedPane.selectHighlight", new Color(45, 45, 45), "TabbedPane.borderHightlightColor", new Color(60, 60, 60), "Table.focusCellHighlightBorder", focusBorder, "Table.gridColor", SECONDARY1, "TableHeader.focusCellBackground", c8ddf2, "Table.selectionBackground", MENU_DISABLED_FOREGROUND, "TextField.font", new FontUIResource(this.font, 0, 13), "ToggleButton.gradient", buttonGradient, "ToolBar.borderColor", cccccc, "Tree.selectionBorderColor", this.getPrimary1(), "Tree.dropLineColor", this.getPrimary1(), "Table.dropLineColor", this.getPrimary1(), "Table.dropLineShortColor", OCEAN_BLACK, "Table.dropCellBackground", OCEAN_DROP, "Tree.dropCellBackground", OCEAN_DROP, "List.dropCellBackground", OCEAN_DROP, "List.dropLineColor", this.getPrimary1(), "ComboBoxUI", "BlackSteel.BlackSteelComboBoxUI", "CheckBox.font", new FontUIResource(this.font, 0, 13), "CheckBoxMenuItem.font", new FontUIResource(this.font, 0, 13), "ColorChooser.font", new FontUIResource(this.font, 0, 13), "ComboBox.font", new FontUIResource(this.font, 0, 13), "DesktopIcon.font", new FontUIResource(this.font, 0, 13), "EditorPane.font", new FontUIResource(this.font, 0, 13), "FormattedTextField.font", new FontUIResource(this.font, 0, 13), "Label.font", new FontUIResource(this.font, 0, 13), "List.font", new FontUIResource(this.font, 0, 13), "Menu.font", new FontUIResource(this.font, 0, 13), "MenuBar.font", new FontUIResource(this.font, 0, 13), "MenuItem.font", new FontUIResource(this.font, 0, 13), "OptionPane.font", new FontUIResource(this.font, 0, 13), "Panel.font", new FontUIResource(this.font, 0, 13), "PopupMenu.font", new FontUIResource(this.font, 0, 13), "ProgressBar.font", new FontUIResource(this.font, 0, 13), "RadioButton.font", new FontUIResource(this.font, 0, 13), "RadioButtonMenuItem.font", new FontUIResource(this.font, 0, 13), "ScrollPane.font", new FontUIResource(this.font, 0, 13), "Slider.font", new FontUIResource(this.font, 0, 13), "Spinner.font", new FontUIResource(this.font, 0, 13), "TabbedPane.font", new FontUIResource(this.font, 0, 13), "Table.font", new FontUIResource(this.font, 0, 13), "TableHeader.font", new FontUIResource(this.font, 0, 13), "TextArea.font", new FontUIResource(this.font, 0, 13), "TextPane.font", new FontUIResource(this.font, 0, 13), "TitledBorder.font", new FontUIResource(this.font, 0, 13), "ToggleButton.font", new FontUIResource(this.font, 0, 13), "ToolBar.font", new FontUIResource(this.font, 0, 13), "ToolTip.font", new FontUIResource(this.font, 0, 13), "Tree.font", new FontUIResource(this.font, 0, 13), "Viewport.font", new FontUIResource(this.font, 0, 13)};
        table.putDefaults(defaults);
    }

    @Override
    protected ColorUIResource getPrimary1() {
        return PRIMARY1;
    }

    @Override
    protected ColorUIResource getPrimary2() {
        return PRIMARY2;
    }

    @Override
    protected ColorUIResource getPrimary3() {
        return PRIMARY3;
    }

    @Override
    protected ColorUIResource getSecondary1() {
        return SECONDARY1;
    }

    @Override
    protected ColorUIResource getSecondary2() {
        return SECONDARY2;
    }

    @Override
    protected ColorUIResource getSecondary3() {
        return SECONDARY3;
    }

    @Override
    protected ColorUIResource getBlack() {
        return OCEAN_BLACK;
    }

    @Override
    public ColorUIResource getDesktopColor() {
        return new ColorUIResource(Color.white);
    }

    @Override
    public ColorUIResource getInactiveControlTextColor() {
        return INACTIVE_CONTROL_TEXT_COLOR;
    }

    @Override
    public ColorUIResource getControlTextColor() {
        return CONTROL_TEXT_COLOR;
    }

    @Override
    public ColorUIResource getMenuDisabledForeground() {
        return MENU_DISABLED_FOREGROUND;
    }

    @Override
    protected ColorUIResource getWhite() {
        return WHITE;
    }
}

