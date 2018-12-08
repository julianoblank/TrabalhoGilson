package com.example.juliano.trabalhogilson;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

public class listaContrato extends AppCompatActivity {

    String id_contrato,id_cliente,id_prestador,dt_inicio,local,latitude,longitude,duracao,preco,avaliacao;
    private ListView listView;
    List<Map<String, Object>> lista;

    String[] de = {"id_contrato","local","duracao","preco","avaliacao"};
    int[] para = {R.id.tvIdContrato,R.id.tvLocal,R.id.tvDuracao,R.id.tvPreco,R.id.tvAvaliacao};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contrato);

        listView = findViewById(R.id.lvContratos);
        lista = new ArrayList<>();

        String url = "http://www.trabalhopdm.top/listaContrato.php";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try{
                    String data = new String(response,"UTF-8");
                    JSONObject res = new JSONObject(data);
                    JSONArray array = res.getJSONArray("contrato");

                    for(int i = 0; i < array.length(); i++){
                        Map<String, Object> mapa = new HashMap<>();
                        JSONObject json = array.getJSONObject(i);
                        id_contrato = json.get("id_contrato").toString();
                        id_cliente = json.get("id_cliente").toString();
                        id_prestador = json.get("id_prestador").toString();
                        dt_inicio = json.get("dt_inicio").toString();
                        local = json.get("local").toString();
                        latitude = json.get("latitude").toString();
                        longitude = json.get("longitude").toString();
                        duracao = json.get("duracao").toString();
                        preco = json.get("preco").toString();
                        avaliacao = json.get("avaliacao").toString();

                        mapa.put("id_contrato", "Id do Contrato: " + id_contrato);
                        mapa.put("local","Local: " + local);
                        mapa.put("duracao","Duração: " + duracao);
                        mapa.put("preco","Preço: " + preco);
                        mapa.put("avaliacao","Avaliação: " + avaliacao);

                        lista.add(mapa);

                    }

                }catch (Exception e) {
                    e.printStackTrace();
                }

                SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), lista, R.layout.line_item_contratos, de, para);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

}
