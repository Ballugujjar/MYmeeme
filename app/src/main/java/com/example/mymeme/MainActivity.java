package com.example.mymeme;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {
    ImageView mImageView;
    ImageView share;
    TextView heading;
    ProgressBar progressBar;
     String url ="https://meme-api.herokuapp.com/gimme";
    String meme_url = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
        mImageView = findViewById(R.id.imageView);
        heading = findViewById(R.id.heading);
        loadmeme(url);
    }


    public void update_meme(String url){
    RequestQueue requestQueue = Volley.newRequestQueue(this);
        ImageRequest
                imageRequest
                = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            progressBar.onVisibilityAggregated(false);
                        }
                        mImageView.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        mImageView.setImageResource(R.drawable.princelogo);
                    }

                });
        requestQueue.add(imageRequest);
    }

   @RequiresApi(api = Build.VERSION_CODES.N)
   public void loadmeme(String url){
        progressBar.onVisibilityAggregated(true);
  RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest
                jsonObjectRequest
                = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        String new_object = null;
                        try {
                            new_object = response.getString("title");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                                meme_url = response.getString("url") ;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        heading.setText("Title = " + new_object);
                        update_meme(meme_url);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                    }
                });
        queue.add(jsonObjectRequest);
    }

    public void nextmeme(View view) {
        loadmeme(url);

    }

    public void sharememe(View view) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "check this meme " + meme_url );
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, null));
        startActivity(sendIntent);
    }
}