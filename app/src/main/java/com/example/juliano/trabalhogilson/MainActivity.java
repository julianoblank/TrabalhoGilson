package com.example.juliano.trabalhogilson;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.security.auth.login.LoginException;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private EditText usuario,senha;
    private String ID;
    public String id,id_cliente,pegaIdCliente,id_prestador,pegaIdPrestador;

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


    public void validaLogin(final View view){
        String url = "http://www.trabalhopdm.top/validaLogin.php";
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
                    if(validar != "false"){
                        verificaLogin(view,ID);
                        Toast.makeText(MainActivity.this, "logado", Toast.LENGTH_SHORT).show();
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

    public void verificaLogin(final  View view,String ID){
        String url = "http://www.trabalhopdm.top/listaCliente.php";
        AsyncHttpClient client = new AsyncHttpClient();
        id = ID;
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try{
                    String id_login;
                    String data = new String(response,"UTF-8");
                    JSONObject res = new JSONObject(data);
                    JSONArray array = res.getJSONArray("cliente");

                    for(int i = 0; i < array.length(); i++){
                        JSONObject json = array.getJSONObject(i);
                        id_login = json.get("id_login").toString();
                        id_cliente = json.get("id_cliente").toString();


                        if(id.equals(id_login)){
                            pegaIdCliente = id_cliente;
                            EnviaIdCliente(view);

                            break;
                        }else{
                            String url = "http://www.trabalhopdm.top/listaPrestador.php";
                            AsyncHttpClient client = new AsyncHttpClient();
                            client.get(url, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                                    try{
                                        String id_login;
                                        String data = new String(response,"UTF-8");
                                        JSONObject res = new JSONObject(data);
                                        JSONArray array = res.getJSONArray("prestador");

                                        for(int i = 0; i < array.length(); i++){
                                            JSONObject json = array.getJSONObject(i);
                                            id_login = json.get("id_login").toString();
                                            id_prestador = json.get("id_prestador").toString();


                                            if(id.equals(id_login)){
                                                pegaIdPrestador = id_prestador;
                                                EnviaIdPrestador(view);

                                                // novoLogadoCliente(view);
                                                //Log.d("aki","novoLogadoCliente");
                                                //Log.d("aki","ID: " + id);
                                                //Log.d("aki","id_login: " + id_login);
                                                break;
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }

    public void EnviaIdCliente(View view){
            Intent novo = new Intent(this, cliente.class);
            Bundle enviaDadosParaOutraActivity = new Bundle();
            enviaDadosParaOutraActivity.putString("id_cliente",pegaIdCliente);
            novo.putExtras(enviaDadosParaOutraActivity);
            startActivity(novo);

    }

    public void EnviaIdPrestador(View view){
        Intent novo = new Intent(this, prestador.class);
        Bundle enviaDadosParaOutraActivity = new Bundle();
        enviaDadosParaOutraActivity.putString("id_prestador",pegaIdPrestador);
        novo.putExtras(enviaDadosParaOutraActivity);
        startActivity(novo);

    }
}
