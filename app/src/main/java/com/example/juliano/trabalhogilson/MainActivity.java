package com.example.juliano.trabalhogilson;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private EditText usuario,senha;
    private String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuario = findViewById(R.id.edUsuario);
        senha = findViewById(R.id.edSenha);


    }

    public void novoCadastro(View view){
        Intent novo = new Intent(this, NovoCadastro.class);
        startActivity(novo);
    }

    public void novoLogado(View view){
        Intent novo = new Intent(this, listaPrestador.class);
        startActivity(novo);
    }


    public void validaLogin(final View view){
        String url = "http://ghelfer-001-site8.itempurl.com/validaLogin.php";
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
                    String validar = teste.getString("success");
                    ID = teste.getString("id_login");
                    verificaLogin();
                    if(validar != "false"){
                    Toast.makeText(MainActivity.this, "logado", Toast.LENGTH_SHORT).show();
                    novoLogado(view);
                    }else {
                        Toast.makeText(MainActivity.this, "Login Incorreto !", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] response, Throwable error) {

            }
        });

    }

    public void verificaLogin(){
        String url = "http://ghelfer-001-site8.itempurl.com/listaCliente.php";
        RequestParams params = new RequestParams();


        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override

            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {
                    String data = new String(response,"UTF-8");
                    JSONObject teste = new JSONObject(data);
                    String validar = teste.getString("id_login");
                    Toast.makeText(MainActivity.this, validar, Toast.LENGTH_SHORT).show();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] response, Throwable error) {

            }
        });

    }

}
