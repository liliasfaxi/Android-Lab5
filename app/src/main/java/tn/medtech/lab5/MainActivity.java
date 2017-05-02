package tn.medtech.lab5;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tn.medtech.lab5.POJO.Model;

public class MainActivity extends AppCompatActivity {

    TextView status, humidity, pressure, city;
    Spinner spinner;
    String[] cities = {"Tunis", "London", "Paris"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city = (TextView) findViewById(R.id.txt_city);
        status = (TextView) findViewById(R.id.txt_status);
        humidity = (TextView) findViewById(R.id.txt_humidity);
        pressure = (TextView) findViewById(R.id.txt_press);
        spinner = (Spinner) findViewById(R.id.sp_city);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, cities);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                callWeatherReport(cities[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        callWeatherReport(cities[0]);

    }

    public void callWeatherReport(String selectedCity){


        RestInterface restInterface = RestInterface.retrofit.create(RestInterface.class);

        Call<Model> call = restInterface.getWeatherReport(selectedCity);

        call.enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                Model report = response.body();
                city.setText("City: " + report.getName());
                status.setText("Status :" + report.getWeather().get(0).getDescription());
                humidity.setText("Humidity :" + report.getMain().getHumidity().toString());
                pressure.setText("Pressure :" + report.getMain().getPressure().toString());

            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {

            }

        });
    }
}