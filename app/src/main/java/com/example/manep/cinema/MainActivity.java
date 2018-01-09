package com.example.manep.cinema;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
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


public class MainActivity extends AppCompatActivity {
    JSONArray jsonArray;
    JSONObject jsonObject;
    //ProgressDialog progressDialog;
    ConnectionChecker connectionChecker;
    GridView gridView;
    MyAsyncTask asyncTask;
    String posterpath;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = (GridView) findViewById(R.id.gridView);
        /*progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("");*/
        connectionChecker = new ConnectionChecker(this);
        asyncTask = new MyAsyncTask();
        if (connectionChecker.isConnected()) {
            asyncTask.execute("https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=a59665f89fb051a511ba2c8342fb1a14");
        } else {
            Toast.makeText(MainActivity.this, "Check your Internet", Toast.LENGTH_LONG).show();
        }
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long number) {
                Intent intent = new Intent(MainActivity.this,MovieDetails.class);
                try {
                    intent.putExtra("ImageDetail",jsonArray.getJSONObject(position).getString("poster_path"));
                    intent.putExtra("Title",jsonArray.getJSONObject(position).getString("title"));
                    intent.putExtra("description",jsonArray.getJSONObject(position).getString("overview"));
                    intent.putExtra("Rating",jsonArray.getJSONObject(position).getString("vote_average"));
                    intent.putExtra("moiveID",jsonArray.getJSONObject(position).getString("id"));
                    intent.putExtra("Year",jsonArray.getJSONObject(position).getString("release_date"));
                    intent.putExtra("vote",jsonArray.getJSONObject(position).getString("vote_count"));
                    intent.putExtra("lang",jsonArray.getJSONObject(position).getString("original_language"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        asyncTask = new MyAsyncTask();
        if (item.getItemId() == R.id.popular){
            if(connectionChecker.isConnected()) {
                asyncTask.execute("https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=a59665f89fb051a511ba2c8342fb1a14");
            }
            else
            {
                Toast.makeText(MainActivity.this,"No Internet Please try again",Toast.LENGTH_SHORT).show();
                alert();
            }

        }
        else {
            if(connectionChecker.isConnected()) {
                asyncTask.execute("https://api.themoviedb.org/3/discover/movie?sort_by=rating.desc&api_key=a59665f89fb051a511ba2c8342fb1a14");
            }
            else
            {
                Toast.makeText(MainActivity.this,"No Internet Please try again",Toast.LENGTH_SHORT).show();
                alert();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void alert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle("Please Connect to Internet");
        dialog.setMessage("Go to Menu and press Popular or Rating to load movies" );
        dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //Action for "Delete".
            }
        })
                .setNegativeButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Action for "Cancel".
                    }
                });

        final AlertDialog alert = dialog.create();
        alert.show();
    }

    public class MyAsyncTask extends AsyncTask<String, Void, JSONObject> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected JSONObject doInBackground(String... params) {
                //JSONObject jsonObject = null;
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
                    Log.d("Tag", " " + result);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return jsonObject;
            }

            @Override
            protected void onPostExecute(final JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                try {
                    jsonArray = jsonObject.getJSONArray("results");
                } catch (Exception e) {
                    e.printStackTrace();
                }


                class MovieAdapter extends BaseAdapter {


                    @Override
                    public int getCount() {
                        // int count=0;
                        // count=jsonArray.length();
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

                        // Object a = null;
                  /*  try {
                        a = jsonArray.getJSONObject(position);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
*/
                        return a;
                    }

                    @Override
                    public long getItemId(int position) {
                        return 0;
                    }

                    @Override
                    public View getView(int position, View view, ViewGroup viewGroup) {
                        view = getLayoutInflater().inflate(R.layout.movie_frame, null);
                        JSONObject object = new JSONObject();
                        imageView = (ImageView) view.findViewById(R.id.movieImage);

                        try {
                            object = jsonArray.getJSONObject(position);
                            try {
                                posterpath = object.getString("poster_path");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Log.d("poster", "poster" + posterpath);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String url = "http://image.tmdb.org/t/p/w185/" + posterpath;
                       // int width= MainActivity.getResources().getDisplayMetrics().widthPixels;
                        Picasso.with(MainActivity.this).load(url).into(imageView);

                        /*String imageUrl = "xyz.net/abc/img/p/9/0/9/7/9097-tonytheme_product.jpg"; // this is an example, you can get this url from json
                        Picasso.with(this.getActivity()).load(imageUrl).into(imageView);
*/
                        return view;
                    }

                }
                MovieAdapter movieAdapter = new MovieAdapter();
                gridView.setAdapter(movieAdapter);
            }
        }
    }
