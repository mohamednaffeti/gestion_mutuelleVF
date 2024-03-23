package com.cpg.mutuelle.entities.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FormatEmailDTO {
    private String to ;
    private String Subject ;



    public String getBody(String firstName, String lastName, String matricule,
                          String genderUser,
                          String type, String codeVerification) {

        String salutationUser = (genderUser.equalsIgnoreCase("Homme")) ? "Monsieur" : "Madame";

        String cherForUser = (genderUser.equalsIgnoreCase("Homme")) ? "Chèr" : "Chère";

        if (type.equals("AddingAccount")) {
            return "Bonjour " + cherForUser + " " + salutationUser + " " + firstName +" "+lastName+" . Nous espérons que ce message vous trouve bien.\n" +
                    "Suite à votre tentative de création d'un nouveau compte avec ce matricule "+matricule+" , nous vous envoyons un code de vérification pour nos protocoles de sécurité sur notre plateforme.\n " +
                    codeVerification + "\n Cordialement,";
        }else{
            return null;
        }
    }
}
