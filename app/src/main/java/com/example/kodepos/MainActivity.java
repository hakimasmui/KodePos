package com.example.kodepos;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kodepos.Handler.HttpHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog pDialog;

    private String addUrlKota,addUrlKec = "";

    private String[] dataProv,dataKota,listKodepos;

    private String urlProv = "https://kodepos-2d475.firebaseio.com/list_propinsi.json?print=pretty";

    Spinner prov,kota;
    ListView kodepos;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prov = (Spinner) findViewById(R.id.provinsi);
        kota = (Spinner) findViewById(R.id.kota);
        kodepos = (ListView) findViewById(R.id.kodepos);

        getDataProv(urlProv);
    }

    private void getDataProv(final String url)
    {
        class GetDataProv extends AsyncTask<Void, Void, String>
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();

                pDialog = new ProgressDialog(MainActivity.this);
                pDialog.setMessage("Sedang mengambil data provinsi");
                pDialog.setCancelable(false);
                pDialog.show();
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);

                pDialog.dismiss();
                tampilkanProvinsi(s);
            }

            @Override
            protected String doInBackground(Void... params)
            {
                HttpHandler httpHandler = new HttpHandler();
                String s = httpHandler.makeServiceCall(url);
                return s;
            }
        }

        GetDataProv gdp = new GetDataProv();
        gdp.execute();
    }

    private void getDataKota(final String url)
    {
        class GetDataKota extends AsyncTask<Void, Void, String>
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();

                pDialog = new ProgressDialog(MainActivity.this);
                pDialog.setMessage("Sedang mengambil data kota");
                pDialog.setCancelable(false);
                pDialog.show();
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);

                pDialog.dismiss();
                tampilkanKota(s);
            }

            @Override
            protected String doInBackground(Void... params)
            {
                HttpHandler httpHandler = new HttpHandler();
                String s = httpHandler.makeServiceCall(url);
                return s;
            }
        }

        GetDataKota gdk = new GetDataKota();
        gdk.execute();
    }

    private void getDataKec(final String url)
    {
        class GetDataKec extends AsyncTask<Void, Void, String>
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();

                pDialog = new ProgressDialog(MainActivity.this);
                pDialog.setMessage("Sedang mengambil data kode pos");
                pDialog.setCancelable(false);
                pDialog.show();
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);

                pDialog.dismiss();
                tampilkanKecamatan(s);
            }

            @Override
            protected String doInBackground(Void... params)
            {
                HttpHandler httpHandler = new HttpHandler();
                String s = httpHandler.makeServiceCall(url);
                return s;
            }
        }

        GetDataKec gdk = new GetDataKec();
        gdk.execute();
    }

    private void tampilkanProvinsi(String jsonStr)
    {
        if(jsonStr != null)
        {
            try {
                JSONObject hasil = new JSONObject(jsonStr);

                dataProv = new String[hasil.length()];

                final Map<String, String> data = new HashMap<String, String>();
                Iterator iter = hasil.keys();
                int i = 0;
                while (iter.hasNext())
                {
                    String idProv = (String) iter.next();
                    dataProv[i] = (String) hasil.getString(idProv);
                    data.put(dataProv[i], idProv);
                    i++;
                }
                Arrays.sort(dataProv);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, dataProv);
                prov.setAdapter(adapter);

                prov.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        addUrlKota = data.get(dataProv[position]);
                        String urlKota = "https://kodepos-2d475.firebaseio.com/list_kotakab/"+addUrlKota+".json?print=pretty";
                        getDataKota(urlKota);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }
        else
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "Pastikan anda memiliki koneksi internet", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    private void tampilkanKota(String jsonStr)
    {
        if(jsonStr != null)
        {
            try {
                JSONObject hasil = new JSONObject(jsonStr);

                dataKota = new String[hasil.length()];

                final Map<String, String> data = new HashMap<String, String>();
                Iterator iter = hasil.keys();
                int i = 0;
                while (iter.hasNext())
                {
                    String idKota = (String) iter.next();
                    dataKota[i] = (String) hasil.getString(idKota);
                    data.put(dataKota[i], idKota);
                    i++;
                }
                Arrays.sort(dataKota);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, dataKota);
                kota.setAdapter(adapter);
                kota.setVisibility(View.VISIBLE);

                kota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        addUrlKec = data.get(dataKota[position]);
                        String urlKec = "https://kodepos-2d475.firebaseio.com/kota_kab/"+addUrlKec+".json?print=pretty";
                        getDataKec(urlKec);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        else
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "Pastikan anda memiliki koneksi internet", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void tampilkanKecamatan(String jsonStr)
    {
        if(jsonStr != null)
        {
            try {
                JSONArray hasil = new JSONArray(jsonStr);

                listKodepos = new String[hasil.length()];

                for (int i = 0; i < hasil.length(); i++)
                {
                    JSONObject c = hasil.getJSONObject(i);

                    String kecamatan = c.getString("kecamatan");
                    String kodepos = c.getString("kodepos");
                    String kelurahan = c.getString("kelurahan");
                    listKodepos[i] = kecamatan+" - "+kodepos+" - "+kelurahan;
                }
                Arrays.sort(listKodepos);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, listKodepos);
                kodepos.setAdapter(adapter);
            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }
        else
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "Pastikan anda memiliki koneksi internet", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
}
