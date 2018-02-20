package com.bot.elizaBot;

import java.util.ArrayList;
import java.util.Random;

public class Saudacao {

    private ArrayList<String> saudacoes = new ArrayList();

    public Saudacao() {
        saudacoesInit();

    }

    void saudacoesInit(){
        saudacoes.add("Oi!");
        saudacoes.add("Olá!");
        saudacoes.add("E aí");
        saudacoes.add("Bom Dia!");
        saudacoes.add("Boa Tarde!");
        saudacoes.add("Boa Noite!");
    }

    boolean verificaSaudacao(String saudacaoUsuario){

        for (String s:saudacoes) {

            if(s.equalsIgnoreCase(saudacaoUsuario)==true){
                return true;
            }

        }

        return false;
    }

    String pickASaudacao(){
        int tamanho = saudacoes.size();
        Random randIndex = new Random();
        int rand = randIndex.nextInt(tamanho);
        if(rand < tamanho){

            return saudacoes.get(rand);
        }
        return saudacoes.get(1);
    }
}
