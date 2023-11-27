package Controleur;

import ObjetsVESPAP.ReponsePayFacture;
import ObjetsVESPAP.RequetePayFacture;
import Vue.PayementWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PayementWindowController implements ActionListener {

    private PayementWindow dialog;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private String username;
    private String cardnumber;
    private int idFacture;

    public PayementWindowController(PayementWindow dialog, Socket socket, ObjectOutputStream oos, ObjectInputStream ois, int idFacture )
    {
        this.socket = socket;
        this.dialog = dialog;
        this.oos = oos;
        this.ois = ois;
        this.idFacture = idFacture;
    }




    @Override
    public void actionPerformed(ActionEvent e) {

        JButton source = (JButton) e.getSource();

        if (source.getText().equals("Confirmer")) {
            username = dialog.getUserName();
            cardnumber = dialog.getCardNumber();

            try {

                RequetePayFacture requete = new RequetePayFacture(idFacture, username, cardnumber);
                oos.writeObject(requete);
                ReponsePayFacture reponse = (ReponsePayFacture) ois.readObject();

                if (reponse.isValide()) {
                    dialog.dialogueMessage("PAYEMENT", reponse.getMessage());
                    dialog.setVisible(false);

                } else {
                    dialog.dialogueErreur("PAYEMENT", reponse.getMessage());
                }
            }
            catch (IOException | ClassNotFoundException ex)
            {
                System.out.println(ex.getMessage());
            }
        } else if (source.getText().equals("Annuler")) {
            dialog.setVisible(false);
        }

    }
}
