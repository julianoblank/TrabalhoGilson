package com.example.juliano.trabalhogilson;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class ClienteOuPrestador extends AppCompatActivity {
private RadioGroup radioGroup;
EditText nome,nasc,email,fone,endereco,cpf,tpServico,preco_hora;
TextView tv,tv2;
private String ID,id_cliente,id_login,pegaIdCliente;
private boolean flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_ou_prestador);
        nome = findViewById(R.id.etNome);
        nasc = findViewById(R.id.etNascimento);
        email = findViewById(R.id.etEmail);
        fone = findViewById(R.id.etTelefone);
        endereco = findViewById(R.id.etEndereco);
        cpf = findViewById(R.id.etCpf);
        tpServico = findViewById(R.id.etPrestador);
        tv = findViewById(R.id.tvPrestador);
        tv2 = findViewById(R.id.tvPrecoHora);
        preco_hora = findViewById(R.id.etPrecoHora);

        Intent recebeDados = getIntent();
        Bundle recebendoDados = recebeDados.getExtras();
        ID = recebendoDados.getString("id_login");

        tpServico.setVisibility(View.INVISIBLE);
        tv.setVisibility(View.INVISIBLE);
        preco_hora.setVisibility(View.INVISIBLE);
        tv2.setVisibility(View.INVISIBLE);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.clearCheck();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);

                    switch(rb.getId()) {
                        case R.id.radio_prestador:
                            if (null != rb && checkedId > -1) {
                                preco_hora.setVisibility(View.VISIBLE);
                                tv2.setVisibility(View.VISIBLE);
                                tpServico.setVisibility(View.VISIBLE);
                                tv.setVisibility(View.VISIBLE);
                                flag = true;
                                break;
                            }
                        case R.id.radio_cliente:
                            if (null != rb && checkedId > -1) {
                                preco_hora.setVisibility(View.INVISIBLE);
                                tpServico.setVisibility(View.INVISIBLE);
                                tv.setVisibility(View.INVISIBLE);
                                tv2.setVisibility(View.INVISIBLE);
                                flag = false;
                                break;
                            }


                    }


            }
        });
    }
    public void novoLogadoCliente(View view){
        Intent novo = new Intent(this, listaPrestador.class);
        startActivity(novo);
    }

    public void novoLogadoPrestador(View view){
        Intent novo = new Intent(this, listaCliente.class);
        startActivity(novo);
    }

    public void onClear(View v) {
        /* Clears all selected radio buttons to default */
        radioGroup.clearCheck();
    }

    public void onSubmit(View v) {
        RadioButton rb = (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());

    }

    public void salvar(final View view){
        if(flag == false) {
            String url = "http://ghelfer-001-site8.itempurl.com/criaCliente.php";
            RequestParams params = new RequestParams();
            params.add("id_login", ID);
            params.add("nome", nome.getText().toString());
            params.add("dt_nasc", nasc.getText().toString());
            params.add("email", email.getText().toString());
            params.add("telefone", fone.getText().toString());


            AsyncHttpClient client = new AsyncHttpClient();
            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    try {
                        String data = new String(response, "UTF-8");
                        //Log.d("teste",data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //novoLogadoCliente(view);
                    listarContratoClientes(view);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] response, Throwable error) {

                }
            });
        }else if (flag == true){
            String url = "http://ghelfer-001-site8.itempurl.com/criaPrestador.php";
            RequestParams params = new RequestParams();
            params.add("id_login", ID);
            params.add("nome", nome.getText().toString());
            params.add("dt_nasc", nasc.getText().toString());
            params.add("email", email.getText().toString());
            params.add("telefone", fone.getText().toString());
            params.add("cpf", cpf.getText().toString());
            params.add("tipo_servico", tpServico.getText().toString());
            params.add("preco_hora", preco_hora.getText().toString());

            AsyncHttpClient client = new AsyncHttpClient();
            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    try {
                        String data = new String(response, "UTF-8");
                        //Log.d("teste",data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //novoLogadoPrestador(view);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] response, Throwable error) {

                }
            });
        }
    }

    public void listarContratoClientes(final View view){
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
                        JSONObject json = array.getJSONObject(i);
                        id_login = json.get("id_login").toString();
                        id_cliente = json.get("id_cliente").toString();
                        if(id_login.equals(ID)){
                            pegaIdCliente = id_cliente;
                            enviaClienteId(view);

                        }


                    }

                }catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public void enviaClienteId(View view){
        Intent novo = new Intent(this, cliente.class);
        Bundle enviaDadosParaOutraActivity = new Bundle();
        enviaDadosParaOutraActivity.putString("id_cliente",pegaIdCliente);
        novo.putExtras(enviaDadosParaOutraActivity);
        startActivity(novo);
    }



}
