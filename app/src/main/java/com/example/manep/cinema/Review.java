package com.example.manep.cinema;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.R.attr.name;

/**
 * Created by manep on 9/8/2017.
 */

public class Review extends AppCompatActivity {
    ListView listview;
    ReviewAsyncTask reviewAsyncTask = new ReviewAsyncTask();
    JSONObject jsonObject;

    JSONArray jsonArray;
    String content;
    String author;

  @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reviewlist);
        String reviewurl = getIntent().getStringExtra("Urlname");
      listview = (ListView)findViewById(R.id.list_review);
        reviewAsyncTask.execute(reviewurl);
      Toast.makeText(this, "Wait till Loading", Toast.LENGTH_SHORT).show();
      listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              alert();
          }
      });
    }
    private void alert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(Review.this);
        dialog.setCancelable(false);
        dialog.setTitle(author +" Review");
        dialog.setMessage(content);
        dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //Action for "Delete".
            }
        });
               /* .setNegativeButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Action for "Cancel".
                    }
                });*/

        final AlertDialog alert = dialog.create();
        alert.show();
    }
    public class ReviewAsyncTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String result;
                String nextline;

                while ((nextline = bufferedReader.readLine()) != null) {
                    stringBuilder.append(nextline);
                }
                result = stringBuilder.toString();
                jsonObject = new JSONObject(result);
                //Log.d("Tag", " " + result);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try {
                jsonArray = jsonObject.getJSONArray("results");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            class ReviewAdapter extends BaseAdapter {

                @Override
                public int getCount() {
                    return jsonArray.length();
                }

                @Override
                public Object getItem(int position) {
                    Object a = null;
                    try {
                        a = jsonArray.getJSONObject(position);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return a;
                }

                @Override
                public long getItemId(int position) {
                    return 0;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    convertView = getLayoutInflater().inflate(R.layout.review, null);

                    JSONObject object = new JSONObject();
                    TextView review = (TextView) convertView.findViewById(R.id.review_textView);
                    try {
                        object = jsonArray.getJSONObject(position);
                        try {
                            author =object.getString("author");
                            content = object.getString("content");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //  Log.d("poster", "poster" + posterpath);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // String url = "http://image.tmdb.org/t/p/w185/" + posterpath;
                    review.setText(author +" Review");
                    return convertView;
                }

            }

            ReviewAdapter reviewAdapter = new ReviewAdapter();
            listview.setAdapter(reviewAdapter);
        }

     }

 }

