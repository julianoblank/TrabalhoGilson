package com.example.juliano.trabalhogilson;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.*;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class NovoCadastro extends AppCompatActivity {

    private EditText usuario,senha,repeteSenha;
    public String ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_cadastro);

        usuario = findViewById(R.id.edUsuario);
        senha = findViewById(R.id.edSenha);
        repeteSenha = findViewById(R.id.edRepeteSenha);
    }

    public void novo(View view){
        String url = "http://ghelfer-001-site8.itempurl.com/criaLogin.php";
        RequestParams params = new RequestParams();
        params.add("email",usuario.getText().toString());
        params.add("senha",senha.getText().toString());

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {
                    String data = new String(response,"UTF-8");

                    JSONObject teste = new JSONObject(data);
                    JSONObject json = teste.getJSONObject("login");
                    ID = json.getString("id_login");
                    ClienteouPrestador();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] response, Throwable error) {

            }
        });

    }
    public void ClienteouPrestador(){
        Intent novo = new Intent(this, ClienteOuPrestador.class);
        Bundle enviaDadosParaOutraActivity = new Bundle();
        enviaDadosParaOutraActivity.putString("id_login",ID);
        novo.putExtras(enviaDadosParaOutraActivity);
        startActivity(novo);
    }
}
