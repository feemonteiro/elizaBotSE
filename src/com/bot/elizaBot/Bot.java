package com.bot.elizaBot;

public class Bot {
    private Saudacao _saudacao;
    private Despedida _despedida;
    private Conversa _conversa;
    private boolean _fimDialogo;

    public Bot(){
        _saudacao = new Saudacao();
        _despedida = new Despedida();
        _conversa = new Conversa();
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
