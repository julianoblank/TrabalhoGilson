package com.example.juliano.trabalhogilson;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class listaPrestador extends AppCompatActivity {
    String id_prestador, id_login,nome,dt_nasc,email,telefone,cpf,end_rua,end_numero,end_complemento,end_bairro,end_cidade,end_estado,end_cep,tipo_servico,preco_hora,biografia;
    private ListView listView;
    List<Map<String, Object>> lista;

    String[] de = {"nome","email","telefone","tipo_servico","preco_hora"};
    int[] para = {R.id.tvNome,R.id.tvEmail,R.id.tvTelefone,R.id.tvTipoServico,R.id.tvPrecoHora};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_prestador);

        listView = findViewById(R.id.lvPrestadores);
        lista = new ArrayList<>();

        String url = "http://ghelfer-001-site8.itempurl.com/listaPrestador.php";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try{
                    String data = new String(response,"UTF-8");
                    JSONObject res = new JSONObject(data);
                    JSONArray array = res.getJSONArray("prestador");

                    for(int i = 0; i < array.length(); i++){
                        Map<String, Object> mapa = new HashMap<>();
                        JSONObject json = array.getJSONObject(i);
                        id_prestador = json.get("id_prestador").toString();
                        id_login = json.get("id_login").toString();
                        nome = json.get("nome").toString();
                        dt_nasc = json.get("dt_nasc").toString();
                        email = json.get("email").toString();
                        telefone = json.get("telefone").toString();
                        cpf = json.get("cpf").toString();
                        end_rua = json.get("end_rua").toString();
                        end_numero = json.get("end_numero").toString();
                        end_complemento = json.get("end_complemento").toString();
                        end_bairro = json.get("end_bairro").toString();
                        end_cidade = json.get("end_cidade").toString();
                        end_estado = json.get("end_estado").toString();
                        end_cep = json.get("end_cep").toString();
                        tipo_servico = json.get("tipo_servico").toString();
                        preco_hora = json.get("preco_hora").toString();
                        biografia = json.get("biografia").toString();

                        mapa.put("id_prestador",id_prestador);
                        mapa.put("id_login",id_login);
                        mapa.put("nome","Nome: " + nome);
                        mapa.put("dt_nasc","Data de Nascimento: " + dt_nasc);
                        mapa.put("email","Email:" + email);
                        mapa.put("telefone",telefone);
                        mapa.put("cpf",cpf);
                        mapa.put("end_rua",end_rua);
                        mapa.put("end_numero",end_numero);
                        mapa.put("end_complemento",end_complemento);
                        mapa.put("end_bairro",end_bairro);
                        mapa.put("end_cidade",end_cidade);
                        mapa.put("end_estado",end_estado);
                        mapa.put("end_cep",end_cep);
                        mapa.put("tipo_servico","Tipo de Serviço:" + tipo_servico);
                        mapa.put("preco_hora","Preço da hora: " + preco_hora);
                        mapa.put("biografia",biografia);
                        lista.add(mapa);

                    }

                }catch (Exception e) {
                    e.printStackTrace();
                }

                SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), lista, R.layout.line_item_prestador, de, para);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
