package com.bot.elizaBot;

import queryAPI.queryAPI;

import java.util.ArrayList;
import java.util.HashMap;

public class Respostas {
    static String proxAula(ArrayList<String> lista){
        HashMap<String, String> args = new HashMap<>();
        if(lista.get(0).length() > 0){
            queryAPI.queryAPI("curso", "componentes-curriculares", args);
            return "A próxima aula de %s é Segunda às 13h.";
        }
        else{
            args.put("nome", lista.get(0));
            queryAPI.queryAPI("curso", "componentes-curriculares", args);
            return "Sua próxima aula será Direito Interplanetário, hoje às 16h50";
        }
    }
}
