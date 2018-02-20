package queryAPI;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class queryAPI {

    static private String URLBASE = "https://apitestes.info.ufrn.br";
    static private String ClientId = "ufrnbot-id";
    static private String ClientSecret = "segredo";
    static private String Authorization = "bearer 68590848-2b89-4c13-8ad0-a183bcb77e6b";
    static private String XApiKey = "Er0fGUvWn8eBlh80XfFLK6fKuxxEyOp7tl03ClkW";
    static private String version = "v0.1";


    static public String queryAPI(String ambiente, String classe, HashMap<String, String> filtros){
        String base_query = String.format("%s/%s/%s/%s", URLBASE, ambiente, version, classe);
        if (filtros.isEmpty())
            return base_query;
        else{
            base_query += "?";
            Iterator it = filtros.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry pair = (Map.Entry)it.next();
                String s = "&" + pair.getKey() + "=" + pair.getValue();
                System.out.println(pair.getKey() + " = " + pair.getValue());
                base_query += s;
                it.remove(); // avoids a ConcurrentModificationException
            }
        }

        String token = logar();
        getData(base_query, token);

        return base_query;
    }


    static private void sendPost(String url) throws Exception {

        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

    }
    static private String logar() {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format(URLBASE + "authz-server/oauth/authorize?client_id=%s&client_secret=%s&response_type=code&redirect_uri=http://enderecoapp.com.br/pagina", ClientId, ClientSecret);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, null, String.class);
        if ( responseEntity.getStatusCode() == HttpStatus.OK ) {
            System.out.println(responseEntity.getBody());
            JSONObject jsonObject = new JSONObject(responseEntity.getBody());
            return (String) jsonObject.get("access_token");
        }
        return null;
    }

    static private void getData(String query, String token) {
        RestTemplate restTemplate = new RestTemplate();

        String url = query;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "bearer " + token);
        headers.add("x-api-key", XApiKey);
        JSONArray arr;
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        if ( responseEntity.getStatusCode() == HttpStatus.OK ) {
            System.out.println( responseEntity.getBody());
            arr = new JSONArray(responseEntity.getBody());
            System.out.println( arr.getJSONObject(0) );
            //return arr;
        }
    }

    static private void testeAPI(){
        try {
            sendPost("https://apitestes.info.ufrn.br/authz-server/oauth/token?client_id=ufrnbot-id&client_secret=segredo&grant_type=client_credentials");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static private String testeDados() throws IOException {

        URL url = new URL("http://dados.ufrn.br/dataset/49d3d2c9-d373-4f10-80b1-9b8e141138b2/resource/171d9fba-1c3f-413d-8e8c-428286eae5e9/download/telefones.csv");
        URLConnection connection = url.openConnection();

        InputStreamReader input = new InputStreamReader(connection.getInputStream());
        BufferedReader br = null;
        String line;
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(input);
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] disc = line.split(cvsSplitBy);

                return disc[0].toString();

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

        return "no";
    }
}