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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class listaCliente extends AppCompatActivity {

    String id_cliente, id_login,nome,dt_nasc,telefone;
    private ListView listView;
    List<Map<String, Object>> lista;

    String[] de = {"nome","telefone"};
    int[] para = {R.id.tvNome,R.id.tvTelefone};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_cliente);

        listView = findViewById(R.id.lvClientes);
        lista = new ArrayList<>();

        String url = "http://ghelfer-001-site8.itempurl.com/listaCliente.php";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try{
                    String data = new String(response,"UTF-8");
                    JSONObject res = new JSONObject(data);
                    JSONArray array = res.getJSONArray("cliente");

                    for(int i = 0; i < array.length(); i++){
                        Map<String, Object> mapa = new HashMap<>();
                        JSONObject json = array.getJSONObject(i);
                        id_cliente = json.get("id_cliente").toString();
                        id_login = json.get("id_login").toString();
                        nome = json.get("nome").toString();
                        dt_nasc = json.get("dt_nasc").toString();
                        telefone = json.get("telefone").toString();
                        mapa.put("id_cliente",id_cliente);
                        mapa.put("id_login",id_login);
                        mapa.put("nome",nome);
                        mapa.put("dt_nasc",dt_nasc);
                        mapa.put("telefone",telefone);
                        lista.add(mapa);

                    }

                }catch (Exception e) {
                    e.printStackTrace();
                }

                SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), lista, R.layout.line_item, de, para);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public void listarContratos(View view){
        Intent novo = new Intent(this, listaContrato.class);
        startActivity(novo);
    }

}
