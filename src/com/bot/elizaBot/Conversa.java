package com.bot.elizaBot;

public class Conversa {

    private boolean _teveSaudacao;

    public Conversa(){
        _teveSaudacao = false;
    }


    boolean getTeveSaudacao(){
        return _teveSaudacao;
    }

    void setTeveSaudacao(boolean status){
        _teveSaudacao = status;
    }


}
