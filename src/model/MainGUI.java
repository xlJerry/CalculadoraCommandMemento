package model;

import javax.swing.SwingUtilities;

public class MainGUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CalculadoraGUI gui = new CalculadoraGUI();
            gui.setVisible(true);
        });
    }
}