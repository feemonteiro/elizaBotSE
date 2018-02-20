package com.bot.elizaBot;

import java.io.IOException;
import java.util.Scanner;


public class Bot {
    private Saudacao _saudacao;
    private Despedida _despedida;
    private Conversa _conversa;
    private boolean _fimDialogo;

    public Bot() {
        //_saudacao = new Saudacao();
        //_despedida = new Despedida();
        //_conversa = new Conversa();

        System.out.println("Olá, eu me chamo Eliza. Sou a assistente virtual da UFRN, e estou a disposição 24h para te " +
                "ajudar. Clique no endereço abaixo para entender melhor como podemos nos comunicar. Ou digite \"#ajuda\". ");


        // espera
        // if contato recebido, inicia conversa
        Scanner reader = new Scanner(System.in);
        if (reader.hasNextLine()){
            try {
                _conversa = new Conversa(reader.nextLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public String dialogo(String respostaAluno){

        if(_conversa.getTeveSaudacao() == false){
            if(_saudacao.verificaSaudacao(respostaAluno)) {
                _conversa.setTeveSaudacao(true);
                return _saudacao.pickASaudacao();
            }else{
                return "Desculpe, não consegui entender.";
            }

        }

        _fimDialogo = true;
        return "Alguma coisa muito errada aconteceu!";
    }

    public boolean getFimDialogo(){
        return _fimDialogo;
    }



}
