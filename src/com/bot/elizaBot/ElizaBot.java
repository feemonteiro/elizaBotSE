package com.bot.elizaBot;

import java.util.Scanner;

public class ElizaBot {
    public static void main(String[] args){

        Bot _bot = new Bot();
        Scanner reader  = new Scanner(System.in);
        String respostaAluno;
        while(_bot.getFimDialogo()==false){
            respostaAluno = reader.nextLine().trim();
            System.out.println(respostaAluno);
            System.out.println(_bot.dialogo(respostaAluno));
        }

        reader.close();
    }
}
