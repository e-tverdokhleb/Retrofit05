package com.example.hp.retrofit05;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private final String URL = "https://translate.yandex.net";
    private final String KEY ="trnsl.1.1.20160608T162754Z.bc9e46be8771c2b2.dd6876d0dfff3cef15c0970c70c4e402592b5e8f";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(URL)
                .build();
        final Link intf = retrofit.create(Link.class);

        Button btnFetch = (Button) findViewById(R.id.btnFetch);
        btnFetch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                EditText etInput = (EditText) findViewById(R.id.etInput);
                TextView tvShow = (TextView) findViewById(R.id.tvShow);

                Map<String, String> map = new HashMap<String, String>();
                map.put("key", KEY);
                map.put("text",etInput.getText().toString());
                map.put("lang","en-ru");

                Call<Object> call = intf.translate(map);

                try {
                    Response<Object> response = call.execute();

                    Map<String, String> mapGson = gson.fromJson(response.body().toString(), Map.class);
                    for(Map.Entry e : mapGson.entrySet()){
                        tvShow.setText(e.toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
