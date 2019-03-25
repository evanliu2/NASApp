package com.example.nasapp;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {

    private TextView title1;
    private TextView date1;
    private TextView explanation1;
    private ImageView pic;
    private EditText year;
    private EditText month;
    private EditText day;
    private Button search;
    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        wireWidgets();

        getAPOD();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String yearPls = year.getText().toString();
                String monthPls = month.getText().toString();
                String dayPls = day.getText().toString();
                if (checkDateValidity(yearPls, monthPls, dayPls) == true) {
                    getAPODbyDay(dateMaker(yearPls, monthPls, dayPls));
                } else {
                    Toast.makeText(SearchActivity.this, "You did it wrong, user!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void wireWidgets() {
        title1 = findViewById(R.id.textview_title);
        date1 = findViewById(R.id.textview_date);
        explanation1 = findViewById(R.id.textview_explanation);
        pic = findViewById(R.id.imageView_apod);
        year = findViewById(R.id.editText_year);
        month = findViewById(R.id.editText_month);
        day = findViewById(R.id.editText_day);
        search = findViewById(R.id.button_search);
    }

    private void getAPOD() {
        // need GSON and converter-gson libraries for this step
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.nasa.gov")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NasaService service =
                retrofit.create(NasaService.class);

        Call<NasaResponse> nasaResponseCall =
                service.searchToday(Credentials.api_key);

        nasaResponseCall.enqueue(new Callback<NasaResponse>() {
            @Override
            public void onResponse(Call<NasaResponse> call,
                                   Response<NasaResponse> response) {
                date1.setText(response.body().getDate());
                title1.setText(response.body().getTitle());
                explanation1.setText(response.body().getExplanation());
                Log.d(TAG, response.body().getUrl());
                Picasso.get()
                        .load(response.body().getUrl())
                        .into(pic);
            }

            @Override
            public void onFailure(Call<NasaResponse> call,
                    Throwable t) {
                Toast.makeText(SearchActivity.this, "failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Boolean checkDateValidity(String year, String month, String day) {
        int year1 = Integer.parseInt(year);
        int month1 = Integer.parseInt(month);
        int day1 = Integer.parseInt(day);
        if (year1 >= 2000 && year1 <= Calendar.getInstance().get(Calendar.YEAR)) {
            if (year1 < Calendar.getInstance().get(Calendar.YEAR)) {
                if (month1 >= 1 && month1 <= 12) {
                    if (day1 >= 1 && day1 <= getDaysinMonth(month1, year1)) {
                        return true;
                    }
                }
            } else {
                if (month1 >= 1 && month1 <= Calendar.getInstance().get(Calendar.MONTH)) {
                    if (day1 >= 1 && day1 <= Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private int getDaysinMonth(int month, int year) {
        if (month == 1 || month == 3 || month == 5 || month == 7
                || month == 8 || month == 10 || month == 12) {
            return 31;
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        } else {
            if (year%4 == 0) {
                return 29;
            } else {
                return 28;
            }
        }
    }

    private String dateMaker(String year, String month, String day) {
        return year + "-" + month + "-" + day;
    }

    private void getAPODbyDay(String date) {
        // need GSON and converter-gson libraries for this step
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.nasa.gov")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NasaService service =
                retrofit.create(NasaService.class);

        Call<NasaResponse> nasaResponseCall =
                service.searchByDate(Credentials.api_key, date);

        nasaResponseCall.enqueue(new Callback<NasaResponse>() {
            @Override
            public void onResponse(Call<NasaResponse> call,
                                   Response<NasaResponse> response) {
                date1.setText(response.body().getDate());
                title1.setText(response.body().getTitle());
                explanation1.setText(response.body().getExplanation());
                Log.d(TAG, response.body().getUrl());
                Picasso.get()
                        .load(response.body().getUrl())
                        .into(pic);
            }

            @Override
            public void onFailure(Call<NasaResponse> call,
                                  Throwable t) {
                Toast.makeText(SearchActivity.this, "failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
