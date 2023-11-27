package Vue;

import Controleur.PayementWindowController;

import javax.swing.*;

public class PayementWindow extends JDialog {
    private JPanel PanierPanel;
    private JButton confirmerButton;
    private JTextField nomTextField;
    private JButton annulerButton;
    private JTextField numCarteTextField;
    private JPanel MainPanel;


    public PayementWindow(JFrame parent, String title) {
        super(parent, title, true);
        this.setContentPane(MainPanel); // Ajouter cette ligne pour fixer MainPanel en tant que content pane
        this.setSize(400, 350);
        this.setResizable(false);
        this.setLocationRelativeTo(parent);
    }



    public String getUserName() {
        return nomTextField.getText();
    }



    public String getCardNumber() {
        return numCarteTextField.getText();
    }



    public void setController(PayementWindowController controller)
    {
        confirmerButton.addActionListener(controller);
        annulerButton.addActionListener(controller);
    }



    public void dialogueMessage(String titre, String message) {
        JOptionPane.showMessageDialog(null, message, titre, JOptionPane.INFORMATION_MESSAGE);
    }



    public void dialogueErreur(String titre, String message) {
        JOptionPane.showMessageDialog(null, message, titre, JOptionPane.ERROR_MESSAGE);
    }
}
