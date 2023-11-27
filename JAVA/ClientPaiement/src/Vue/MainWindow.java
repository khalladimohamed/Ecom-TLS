package Vue;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import Controleur.MainWindowController;

public class MainWindow extends JFrame {
    private JTextField textFieldLogin;
    private JTextField textFieldMdp;
    private JButton loginButton;
    private JButton logoutButton;
    private JCheckBox nouveauEmployeCheckBox;
    private JPanel ConnexionPanel;
    private JPanel MainPanel;
    private JPanel MagasinPanel;
    public JTextField idClientTextField;
    private JPanel PublicitePanel;
    private JPanel PanierPanel;
    private JTable JTableFactures;
    private DefaultTableModel modelFactures;
    private JTable JTableArticles;
    private DefaultTableModel modelArticles;
    private JButton detailFactureButton;
    private JButton payerButton;
    private JScrollPane JScrollFactures;
    private JTextField idFactureTextField;
    private JButton afficherFacturesButton;
    private JScrollPane JScrollArticles;
    private JCheckBox TLScheckBox;


    public MainWindow() {

        JFrame frame = new JFrame("Application de paiement");
        frame.setContentPane(MainPanel);
        frame.setSize(new Dimension(900, 700));
        frame.setVisible(true);
        frame.setResizable(false);

        // Fermeture de l'application
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Fermeture de l'application");
                System.exit(0);
            }
        });

        modelFactures = new DefaultTableModel();
        modelFactures.addColumn("IdFacture");
        modelFactures.addColumn("Date");
        modelFactures.addColumn("Montant");
        modelFactures.addColumn("Statue");

        JTableFactures = new JTable(modelFactures);
        JScrollFactures.setViewportView(JTableFactures);

        modelArticles = new DefaultTableModel();
        modelArticles.addColumn("Article");
        modelArticles.addColumn("Prix à l'unité");
        modelArticles.addColumn("Quantité");

        JTableArticles = new JTable(modelArticles);
        JScrollArticles.setViewportView(JTableArticles);
    }


    public void setController(MainWindowController mainWindowController)
    {
        loginButton.addActionListener(mainWindowController);
        logoutButton.addActionListener(mainWindowController);
        payerButton.addActionListener(mainWindowController);
        afficherFacturesButton.addActionListener(mainWindowController);
        detailFactureButton.addActionListener(mainWindowController);
    }

    public void setNom(String nom)
    {
        textFieldLogin.setText(nom);
    }

    public String getNom()
    {
        return textFieldLogin.getText();
    }

    public void setMotDePasse(String motDePasse)
    {
        textFieldMdp.setText(motDePasse);
    }

    public String getMotDePasse()
    {
        return textFieldMdp.getText();
    }

    public int getNumClient()
    {
        return Integer.parseInt(idClientTextField.getText());
    }

    public int getIdFacture()
    {
        return Integer.parseInt(idFactureTextField.getText());
    }

    public boolean isNouveauEmployeChecked()
    {
        return nouveauEmployeCheckBox.isSelected();
    }

    public boolean isTLS()
    {
        return TLScheckBox.isSelected();
    }

    public void LoginOK()
    {
        loginButton.setEnabled(false);
        logoutButton.setEnabled(true);
        textFieldLogin.setEnabled(false);
        textFieldMdp.setEnabled(false);
        nouveauEmployeCheckBox.setEnabled(false);
        idClientTextField.setEnabled(true);
        afficherFacturesButton.setEnabled(true);

        payerButton.setEnabled(true);
        idFactureTextField.setEnabled(true);
        detailFactureButton.setEnabled(true);
    }

    public void LogoutOK()
    {
        loginButton.setEnabled(true);
        logoutButton.setEnabled(false);
        textFieldLogin.setEnabled(true);
        textFieldMdp.setEnabled(true);
        nouveauEmployeCheckBox.setEnabled(true);
        idClientTextField.setEnabled(false);
        afficherFacturesButton.setEnabled(false);

        payerButton.setEnabled(false);
        idFactureTextField.setEnabled(false);
        detailFactureButton.setEnabled(false);

        setMotDePasse("");
        setNom("");

        if(nouveauEmployeCheckBox.isSelected())
            nouveauEmployeCheckBox.setSelected(false);

        videTableFacture();
        videTableArticle();
    }

    public void ajouteFactureTable(int idFacture, Date date, float montant, boolean paye) {

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy"); // Spécifiez le format de la date
        String dateEnString = format.format(date);

        String p;
        if(paye)
            p = "Paye";
        else
            p = "Non Paye";


        DefaultTableModel model = (DefaultTableModel) JTableFactures.getModel();
        model.addRow(new Object[]{idFacture, dateEnString, String.format("%.2f", montant),p});
    }

    public void videTableFacture() {
        DefaultTableModel model = (DefaultTableModel) JTableFactures.getModel();
        model.setRowCount(0);
    }

    public int getIndiceFactureSelectionne()
    {
        int selectedRow = JTableFactures.getSelectedRow();
        if(modelFactures.getValueAt(selectedRow, 3).toString().equals("Non Paye"))
            return Integer.parseInt(modelFactures.getValueAt(selectedRow, 0).toString());
        else
            return -1;
    }

    public void ajouteArticleTable(String article, float prix, int quantite) {

        DefaultTableModel model = (DefaultTableModel) JTableArticles.getModel();
        model.addRow(new Object[]{article, String.format("%.2f", prix), quantite});
    }



    public void videTableArticle() {
        DefaultTableModel model = (DefaultTableModel) JTableArticles.getModel();
        model.setRowCount(0);
    }


    public void dialogueMessage(String titre, String message) {
        JOptionPane.showMessageDialog(null, message, titre, JOptionPane.INFORMATION_MESSAGE);
    }

    public void dialogueErreur(String titre, String message) {
        JOptionPane.showMessageDialog(null, message, titre, JOptionPane.ERROR_MESSAGE);
    }



}
