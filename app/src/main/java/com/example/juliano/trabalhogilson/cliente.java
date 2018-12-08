package com.example.juliano.trabalhogilson;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class cliente extends AppCompatActivity {
String id_cliente,ID,id_contrato,dt_inicio,local,latitude,longitude,duracao,preco,avaliacao;
    List<Map<String, Object>> lista;
    private ListView listView;

    String[] de = {"id_contrato","dt_inicio","local","latitude","longitude","duracao","preco","avaliacao"};
    int[] para = {R.id.tvIdContrato,R.id.tvDataInicio,R.id.tvLocal,R.id.tvLatitude,R.id.tvLongitude,R.id.tvDuracao,R.id.tvPreco,R.id.tvAvaliacao};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);

        Intent recebeDados = getIntent();
        Bundle recebendoDados = recebeDados.getExtras();
        ID = recebendoDados.getString("id_cliente");
        listView = findViewById(R.id.lvContrato);
        lista = new ArrayList<>();


    }

    public void meusContratos(View view){
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
                        id_cliente = json.get("id_cliente").toString();
                        id_contrato = json.get("id_contrato").toString();
                        dt_inicio = json.get("dt_inicio").toString();
                        local = json.get("local").toString();
                        latitude = json.get("latitude").toString();
                        longitude = json.get("longitude").toString();
                        duracao = json.get("duracao").toString();
                        preco = json.get("preco").toString();
                        avaliacao = json.get("avaliacao").toString();
                        if(ID.equals(id_cliente)){
                            mapa.put("id_contrato", "Id do Contrato: " + id_contrato);
                            mapa.put("dt_inicio", "Data de inicio: " + dt_inicio);
                            mapa.put("local", "Local: " + local);
                            mapa.put("latitude", "Latitude: " + latitude);
                            mapa.put("longitude", "Longitude: " + longitude);
                            mapa.put("duracao", "Duração: " + duracao);
                            mapa.put("preco", "Preco: " + preco);
                            mapa.put("avaliacao", "Avalição: " + avaliacao);
                            lista.add(mapa);
                        }


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

    public void listarServicos(View view){
        Intent novo = new Intent(this, listaPrestador.class);
        Bundle enviaDadosParaOutraActivity = new Bundle();
        enviaDadosParaOutraActivity.putString("id_cliente",ID);
        novo.putExtras(enviaDadosParaOutraActivity);
        startActivity(novo);
    }

    public void sair(View view){
        Intent novo = new Intent(this, MainActivity.class);
        startActivity(novo);

    }


}
