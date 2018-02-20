package com.bot.elizaBot;

import naiveBayes.NaiveBayes;
import naiveBayes.NaiveBayesKnowledgeBase;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

import static org.apache.commons.io.FilenameUtils.removeExtension;


public class Conversa {

    private Saudacao _saudacao;
    private boolean _teveSaudacao;
    private String _atualMsg;
    private String _intencao;
    private Scanner reader = new Scanner(System.in);
    private String _msgErro = "Desculpe, mão consigo compreender. Tente de novo, por favor. Ou digite \"fim\" para" +
            " encerrar a conversa";
    private Map<String, String[]> exemplosTreinamento;
    private NaiveBayes _nb;
    private Map<String, ArrayList< ArrayList<String> > > entidade = new HashMap<>();

    public Conversa(){
        _teveSaudacao = false;
    }
    public Conversa(String msg) throws IOException {
        final File folder = new File("C:\\Users\\Marcio\\IdeaProjects\\elizaBotSE\\src\\main\\resources\\entity");
        System.out.println(folder.getPath());
        carregarEntidades(folder);

        treinarDados();

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

        System.out.println("Eu entendi que você está interessado em " + _intencao);

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

    private void treinarDados() throws IOException {
        // Mapa de arquivos de ações/intenções
        Map<String, URL> arquivosTreinamento = new HashMap<String, URL>();

        // Arquivos de ações/intenções
        arquivosTreinamento.put("prox_prova", this.getClass().getResource("/intent/prox_prova.txt"));
        arquivosTreinamento.put("prox_aula", this.getClass().getResource("/intent/prox_aula.txt"));
        arquivosTreinamento.put("sobre_eliza", this.getClass().getResource("/intent/sobre_eliza.txt"));

        // Para cada linha de um arquivo, treinar esta linha
        exemplosTreinamento = new HashMap<String, String[]>();
        for(Map.Entry<String, URL> entry : arquivosTreinamento.entrySet()) {
            exemplosTreinamento.put(entry.getKey(), readLines(entry.getValue()));
        }

        // Treinar o classificador Naive Bayes
        _nb = new NaiveBayes();
        _nb.setChisquareCriticalValue(6.63); //0.01 pvalue
        _nb.train(exemplosTreinamento);
    }

    public static String[] readLines(URL url) throws IOException {
        System.out.println(url.toString());
        Reader fileReader = new InputStreamReader(url.openStream(), Charset.forName("ISO-8859-1"));
        List<String> lines;
        try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            lines = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines.toArray(new String[lines.size()]);
    }

    boolean getTeveSaudacao(){
        return _teveSaudacao;
    }

    void setTeveSaudacao(boolean status){
        _teveSaudacao = status;
    }

    private String getSignificado(String msg){

        NaiveBayesKnowledgeBase knowledgeBase = _nb.getKnowledgeBase();

        _nb = new NaiveBayes(knowledgeBase);
        String intent = _nb.predict(msg);

        ArrayList<String> entidades = extracaoEntidades(msg, intent);
        return intent;
//        if (msg.equals("quem é você?")) return "sobre_eliza";
//        else if (msg.equals("qual a minha próxima aula?")) return "prox_aula";
//        else if (msg.equals("qual o próximo feriado?")) return "prox_feriado";
//        else return "error";
    }
    ArrayList<String> extracaoEntidades(String msg, String intent){
        ArrayList<String> lista_de_entidades = new ArrayList<>();
        if(intent.equals("prox_prova")){
            lista_de_entidades.add(buscador(msg, "disciplina"));
            System.out.println(Respostas.proxAula(lista_de_entidades));
        }

        return lista_de_entidades;
    }

    String buscador(String msg, String tipo){
        String[] ms = msg.split("\\s+");
        for(List l : entidade.get(tipo)){
            for(String e : ms){
                if(l.contains(e))
                    return e;
            }
        }
        return "";
    }

    static ArrayList<String> extracaoEntidades(String msg, String modelo, int error_avoider){
        String[] ms = msg.split("\\s+");
        String[] md = modelo.split("\\s+");
        ArrayList<String> ent = new ArrayList<String>();
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

    void carregarEntidades(final File folder){
        File endereco = new File("");
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                carregarEntidades(fileEntry);
            } else {
                System.out.println(fileEntry.getPath());
                endereco = new File(fileEntry.getPath());

                String csvFile = endereco.getPath();
                BufferedReader br = null;
                String line;
                String cvsSplitBy = ",";

                try {

                    br = new BufferedReader(new FileReader(csvFile));
                    String ent = endereco.getName();
                    ent = removeExtension(ent);
                    ArrayList<String> sinonimos = new ArrayList<>();
                    ArrayList<ArrayList<String> > linhas = new ArrayList<>();
                    while ((line = br.readLine()) != null) {

                        // use comma as separator
                        String[] partes = line.split(cvsSplitBy);
                        sinonimos.toArray(partes);

                        linhas.add(sinonimos);
                        sinonimos.clear();
                    }
                    System.out.println(ent.toString());
                    System.out.println(linhas);
                    entidade.put(ent, linhas);
                    System.out.println(entidade);


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


    }


}
