package com.bot.elizaBot;

import java.util.Scanner;

public class Conversa {

    private boolean _teveSaudacao;
    private String _atualMsg;
    private String _intencao;
    private Scanner reader = new Scanner(System.in);
    private String _msgErro = "Desculpe, mão consigo compreender. Tente de novo, por favor. Ou digite \"fim\" para" +
            " encerrar a conversa";

    public Conversa(){
        _teveSaudacao = false;
    }
    public Conversa(String msg){
        _atualMsg = msg;
        Saudacao _saudacao = new Saudacao();
        _teveSaudacao = _saudacao.verificaSaudacao(_atualMsg);
        if (_teveSaudacao){
            System.out.println(_saudacao.pickASaudacao());
            System.out.println("Como posso te ajudar?");
            if (reader.hasNextLine())
                _atualMsg = reader.nextLine();
        }
        _intencao = getIntencao(_atualMsg);

        if (_intencao.equals("error")){
            System.out.println(_msgErro);
            while(_intencao.equals("error")){
                _atualMsg = reader.nextLine();
                if (_atualMsg.equals("fim")) break;
                _intencao = getIntencao(_atualMsg);
                System.out.println(_msgErro);
            }
        }
        if (!_intencao.equals("error")) {
            System.out.println("Entendi!");

            System.out.println("Mais alguma coisa? (S/N)");


        }

    }

    boolean getTeveSaudacao(){
        return _teveSaudacao;
    }

    void setTeveSaudacao(boolean status){
        _teveSaudacao = status;
    }

    String getIntencao(String msg){
        if (msg.equals("quem é você?")) return "sobre_eliza";
        else if (msg.equals("qual a minha próxima aula?")) return "prox_aula";
        else if (msg.equals("qual o próximo feriado?")) return "prox_feriado";
        else return "error";
    }


}
