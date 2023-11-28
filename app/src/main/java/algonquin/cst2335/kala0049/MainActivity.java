package algonquin.cst2335.kala0049;


import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import algonquin.cst2335.kala0049.databinding.ActivityMainBinding;
public class MainActivity extends AppCompatActivity {

    protected String cityName;
    protected String stringURL;
    protected String imageUrl;
    protected RequestQueue queue;
    protected String iconName;
    Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        queue = Volley.newRequestQueue(this);

        binding.forecastButton.setOnClickListener(click -> {
            cityName = binding.editTextCity.getText().toString();

            try {
                stringURL = "https://api.openweathermap.org/data/2.5/weather?q="
                        + URLEncoder.encode(cityName, "UTF-8")
                        + "&appid=f9e5419d0a68c12c70ce28d80f35df9c&units=metric";
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringURL, null,
                    (response) -> {
                        try {
                            JSONObject weather = response.getJSONArray("weather").getJSONObject(0);
                            iconName = weather.getString("icon");
                            String description = weather.getString("description");

                            JSONObject main = response.getJSONObject("main");
                            double currentTemp = main.getDouble("temp");
                            double minTemp = main.getDouble("temp_min");
                            double maxTemp = main.getDouble("temp_max");
                            int humidity = main.getInt("humidity");

                            // Set text and visibility for all TextViews
                            runOnUiThread( (  )  -> {
                                binding.temp.setText("The Current Temperature is: " + currentTemp);
                                binding.temp.setVisibility(View.VISIBLE);

                                binding.minTemp.setText("The Max Temperature is: " + minTemp);
                                binding.minTemp.setVisibility(View.VISIBLE);

                                binding.maxTemp.setText("The Min Temperature is: " + maxTemp);
                                binding.maxTemp.setVisibility(View.VISIBLE);

                                binding.humidity.setText("The Humidity is:: " + humidity);
                                binding.humidity.setVisibility(View.VISIBLE);

                                binding.description.setText(description);
                                binding.description.setVisibility(View.VISIBLE);
                            });

                            String pathname = getFilesDir() + "/" + iconName + ".png";
                            File file = new File(pathname);

                            if(file.exists()) {
                                image = BitmapFactory.decodeFile(pathname);
                                runOnUiThread(() -> {
                                    binding.icon.setImageBitmap(image);
                                    binding.icon.setVisibility(View.VISIBLE);
                                });
                            } else {
                                ImageRequest imgReq = new ImageRequest("http://openweathermap.org/img/w/" + iconName + ".png",
                                        bitmap -> {
                                            try {
                                                image = bitmap;
                                                image.compress(Bitmap.CompressFormat.PNG, 100, openFileOutput(iconName + ".png", Activity.MODE_PRIVATE));
                                                runOnUiThread(() -> {
                                                    binding.icon.setImageBitmap(image);
                                                    binding.icon.setVisibility(View.VISIBLE);
                                                });
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }, 1024, 1024, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565,
                                        error -> Log.e("ImageRequestError", error.toString()));
                                queue.add(imgReq);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }, error -> Log.e("JsonObjectRequestError", error.toString()));

            queue.add(request);
        });
    }
}
