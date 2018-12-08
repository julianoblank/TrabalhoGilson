package com.example.juliano.trabalhogilson;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class listaPrestador extends AppCompatActivity {
    String id_prestador, id_login,nome,dt_nasc,email,telefone,cpf,end_rua,end_numero,end_complemento,end_bairro,end_cidade,end_estado,end_cep,tipo_servico,preco_hora,biografia;
    private ListView listView;
    List<Map<String, Object>> lista;
    private EditText busca;
    String pegaIdPrestador,pegaIdCliente,longitude,latitude;
    LocationManager loc;
    SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
    Date data = new Date();
    String dataFormatada = formataData.format(data);
    String[] de = {"nome","email","telefone","tipo_servico","preco_hora"};
    int[] para = {R.id.tvNome,R.id.tvEmail,R.id.tvTelefone,R.id.tvTipoServico,R.id.tvPrecoHora};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_prestador);

        listView = findViewById(R.id.lvPrestadores);
        lista = new ArrayList<>();
        busca = findViewById(R.id.edBusca);

        Intent recebeDados = getIntent();
        Bundle recebendoDados = recebeDados.getExtras();
        pegaIdCliente = recebendoDados.getString("id_cliente");

        loc = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Listening listener = new Listening();

        long time = 0;
        float dist = 0;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //loc.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, time, dist, listener);
        loc.requestLocationUpdates(LocationManager.GPS_PROVIDER,time, dist, listener);
        loc.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, time, dist, listener);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String,Object> mapa = lista.get(position);
                String item = mapa.get("id_prestador").toString();
                //Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show();
                pegaIdPrestador = item;
            }
        });

    }

    public void buscarServico(View view) {
        String url = "http://www.trabalhopdm.top/listaPrestador.php";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {
                    String data = new String(response, "UTF-8");
                    JSONObject res = new JSONObject(data);
                    JSONArray array = res.getJSONArray("prestador");

                    for (int i = 0; i < array.length(); i++) {
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
                        if (busca.getText().toString().equals(tipo_servico)) {
                            mapa.put("id_prestador", id_prestador);
                            mapa.put("id_login", id_login);
                            mapa.put("nome", "Nome: " + nome);
                            mapa.put("dt_nasc", "Data de Nascimento: " + dt_nasc);
                            mapa.put("email", "Email:" + email);
                            mapa.put("telefone", telefone);
                            mapa.put("cpf", cpf);
                            mapa.put("end_rua", end_rua);
                            mapa.put("end_numero", end_numero);
                            mapa.put("end_complemento", end_complemento);
                            mapa.put("end_bairro", end_bairro);
                            mapa.put("end_cidade", end_cidade);
                            mapa.put("end_estado", end_estado);
                            mapa.put("end_cep", end_cep);
                            mapa.put("tipo_servico", "Tipo de Serviço:" + tipo_servico);
                            mapa.put("preco_hora", "Preço da hora: " + preco_hora);
                            mapa.put("biografia", biografia);
                            preco_hora = json.get("preco_hora").toString();
                            biografia = json.get("biografia").toString();
                            lista.add(mapa);
                        }
                    }

                } catch (Exception e) {
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
    public void limpar(View view){
        listView.setAdapter(null);
        busca.setText("");
    }

    public void criaContrato(final View view){
        String url = "http://www.trabalhopdm.top/criaContrato.php";
        RequestParams params = new RequestParams();
        params.add("id_cliente",pegaIdCliente);
        params.add("id_prestador",pegaIdPrestador);
        params.add("dt_inicio",dataFormatada);
        params.add("local","santa cruz do sul");
        params.add("latitude",latitude);
        params.add("longitude",longitude);
        //Toast.makeText(listaPrestador.this, "ID Cliente: " + pegaIdCliente, Toast.LENGTH_SHORT).show();
        //Toast.makeText(listaPrestador.this, "ID Prestador: " + pegaIdPrestador, Toast.LENGTH_SHORT).show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override

            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {
                    String data = new String(response,"UTF-8");
                    JSONObject teste = new JSONObject(data);
                    String validar = teste.getString("success");
                    if(validar != "false") {
                        Toast.makeText(listaPrestador.this, "Contrato Criado com Sucesso", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    //Toast.makeText(listaPrestador.this, "nao deu", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] response, Throwable error) {
                Toast.makeText(listaPrestador.this, "falhou", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class Listening implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }


}
