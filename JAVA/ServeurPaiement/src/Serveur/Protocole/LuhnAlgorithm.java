package Serveur.Protocole;

public class LuhnAlgorithm {

    public static boolean isValidLuhnNumber(String number) {
        // Supprimez les espaces et les caractères non numériques
        number = number.replaceAll("\\D", "");

        int sum = 0;
        boolean doubleDigit = false;

        // Parcourez le numéro de droite à gauche
        for (int i = number.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(number.charAt(i));

            if (doubleDigit) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }

            sum += digit;
            doubleDigit = !doubleDigit;
        }

        return (sum % 10 == 0);
    }

    public static void main(String[] args) {
        String creditCardNumber = "4532015112830366";

        if (isValidLuhnNumber(creditCardNumber)) {
            System.out.println("Le numéro de carte de crédit est valide.");
        } else {
            System.out.println("Le numéro de carte de crédit n'est pas valide.");
        }
    }
}
