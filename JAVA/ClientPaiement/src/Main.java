import com.formdev.flatlaf.themes.FlatMacLightLaf;
import Controleur.MainWindowController;
import Vue.MainWindow;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        FlatMacLightLaf.setup();

        // Affichage de la fenêtre
        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        MainWindow mainWindow = new MainWindow();
        MainWindowController mainWindowController = new MainWindowController(mainWindow);
        mainWindow.setController(mainWindowController);

    }
}