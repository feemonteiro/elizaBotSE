package com.bot.elizaBot;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;


public class Conversa {

    private Saudacao _saudacao;
    private boolean _teveSaudacao;
    private String _atualMsg;
    private String _intencao;
    private Scanner reader = new Scanner(System.in);
    private String _msgErro = "Desculpe, mão consigo compreender. Tente de novo, por favor. Ou digite \"fim\" para" +
            " encerrar a conversa";
    private ArrayList<String> entidade;

    public Conversa(){
        _teveSaudacao = false;
    }
    public Conversa(String msg){
        carregarEntidades();

        _atualMsg = msg;
        _saudacao = new Saudacao();
        _teveSaudacao = _saudacao.verificaSaudacao(_atualMsg);
        if (_teveSaudacao){
            System.out.println(_saudacao.pickASaudacao());
            System.out.println("Como posso te ajudar?");
            if (reader.hasNextLine())
                _atualMsg = reader.nextLine();
        }
        _intencao = getSignificado(_atualMsg);

        if (_intencao.equals("error")){
            System.out.println(_msgErro);
            while(_intencao.equals("error")){
                _atualMsg = reader.nextLine();
                if (_atualMsg.equals("fim")) break;
                _intencao = getSignificado(_atualMsg);
                System.out.println(_msgErro);
            }
        }
        if (!_intencao.equals("error")) {
            System.out.println("Entendi!");
        }

    }

    boolean getTeveSaudacao(){
        return _teveSaudacao;
    }

    void setTeveSaudacao(boolean status){
        _teveSaudacao = status;
    }

    private String getSignificado(String msg){
        if (msg.equals("quem é você?")) return "sobre_eliza";
        else if (msg.equals("qual a minha próxima aula?")) return "prox_aula";
        else if (msg.equals("qual o próximo feriado?")) return "prox_feriado";
        else return "error";
    }

    static ArrayList<String> extracaoEntidades(String msg, String modelo){
        String[] ms = msg.split("\\s+");
        String[] md = modelo.split("\\s+");
        ArrayList<String> ent = new ArrayList<>();
        String temp = "";
        int a, c = 0;
        for (int i = 0; i < md.length; i++){
            System.out.println(ms[i+c] + " "+ md[i]);
            if ( !ms[i+c].equals(md[i]) ){
                a = i+c;
                do{
                    temp+=ms[a];
                    temp+=" ";
                    System.out.println(temp);
                    a++;
                }while( ! ms[a].equals(md[i+1]) );
                c = c+a-i-1;
                ent.add(temp);
                temp = "";
            }
        }
        return ent;
    }

    ArrayList<String> getEntidade(String msg){
        ArrayList<String> entidades = new ArrayList<>();
        ArrayList<String> tokens = new ArrayList<>();
        List<String> temp = Arrays.asList(msg.split("\\s+"));
        tokens.addAll(temp);

        return entidades;
    }

    void carregarEntidades(){
        String csvFile = "./entity/disciplina.csv";
        BufferedReader br = null;
        String line;
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] disc = line.split(cvsSplitBy);

                System.out.println("Disciplina[0]= " + disc[0]);

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
