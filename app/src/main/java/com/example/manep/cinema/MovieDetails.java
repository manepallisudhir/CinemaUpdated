package com.example.manep.cinema;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by manep on 9/3/2017.
 */

public class MovieDetails extends AppCompatActivity {
    JSONObject jsonObject;
    JSONArray jsonArray;
    VideoAsyncTask videoAsyncTask;
    ListView listview;
    String videotrailer;
    String movieid;
    String name;
    String url;
    String title;
    String datee;
    Button clk,review,trailer,db;
    CheckBox checkbox;
    DataBaseHelper DOP = new DataBaseHelper(this);
    private static final String KEY_CHECKED_SAVE = "sp_title";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);
        TextView movietitle = (TextView)findViewById(R.id.movie_title);
        ImageView imagedetail = (ImageView)findViewById(R.id.movie_imagedetail);
        TextView decription = (TextView)findViewById(R.id.Description);
        TextView Rating = (TextView)findViewById(R.id.Rating);
        TextView date = (TextView)findViewById(R.id.Time);
        TextView trai = (TextView)findViewById(R.id.trailer_ss);
        checkbox = (CheckBox)findViewById(R.id.checkbox_star);
        SharedPreferences sharedPreferences1 = getSharedPreferences("sp_title", MovieDetails.MODE_PRIVATE);
        boolean isChecked = sharedPreferences1.getBoolean(KEY_CHECKED_SAVE, false);
        checkbox.setChecked(isChecked);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkbox.isChecked()){
                    DOP.putInformation(title,datee);
                    Toast.makeText(MovieDetails.this, "Data saved", Toast.LENGTH_SHORT).show();
                    saveCheckState();
                }
                else {
                    boolean delstatus = false;
                    saveCheckState();
                    final Cursor cr =DOP.getInfromation();
                    cr.moveToNext();
                    do {
                        if (title.equals(cr.getString(0))||
                                datee.equals(cr.getString(1))){
                            delstatus =true;}
                    }while (cr.moveToNext());

                    if (delstatus == true) {
                        DOP.delInformation(title, datee);
                        Toast.makeText(MovieDetails.this, "Data deleted", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(MovieDetails.this, "Not found", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        onPause();
        db = (Button)findViewById(R.id.buttondatabase);
        db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor c = DOP.getAll();
                if (c.getCount()==0){
                    showMessage("Error","No wish list");
                    return;
                }
                StringBuffer stringBuffer=new StringBuffer();
                while ((c.moveToNext()))
                {
                    stringBuffer.append("USER_NAME :"+c.getString(0)+"\n");
                    stringBuffer.append("PASSWORD:"+c.getString(1)+"\n\n");

                }
                showMessage("Student Details",stringBuffer.toString());


            }
        });
       // trailer =(Button)findViewById(R.id.play_trailer) ;
        review = (Button)findViewById(R.id.Btn_review);
        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlname = "https://api.themoviedb.org/3/movie/"+movieid+"/reviews?api_key=a59665f89fb051a511ba2c8342fb1a14";
                Intent reviewintent = new Intent(MovieDetails.this,Review.class);
                reviewintent.putExtra("Urlname",urlname);
               startActivity(reviewintent);
                //Review.ReviewAsyncTask reviewtask = new Review.ReviewAsyncTask();

                //reviewtask.execute("https://api.themoviedb.org/3/movie/"+movieid+"/reviews?api_key=a59665f89fb051a511ba2c8342fb1a14");
            }
        });
         title =getIntent().getStringExtra("Title");
       movieid = getIntent().getStringExtra("moiveID");
        String movieimagedetail = getIntent().getStringExtra("ImageDetail");
        String rate = getIntent().getStringExtra("Rating");
        String dsc = getIntent().getStringExtra("description");
        String vote = getIntent().getStringExtra("vote");
         datee = getIntent().getStringExtra("Date");
        String imageURL = "http://image.tmdb.org/t/p/w185/"+movieimagedetail;
        movietitle.setText(title);
        decription.setText(dsc);
        date.setText(datee);
        Rating.setText(rate);
        Picasso.with(MovieDetails.this).load(imageURL).into(imagedetail);
       // clk = (Button)findViewById(R.id.play_trailer);
        listview = (ListView)findViewById(R.id.list_trailer);
        String viedoURL = "https://api.themoviedb.org/3/movie/"+movieid+"/videos?api_key=a59665f89fb051a511ba2c8342fb1a14";
        MyTask task = new MyTask();
        task.execute(viedoURL);
        videoAsyncTask = new VideoAsyncTask();
        /*try {
            if (jsonArray ==null) {
                videoAsyncTask.execute("https://api.themoviedb.org/3/movie/\"+movieid+\"/videos?api_key=a59665f89fb051a511ba2c8342fb1a14");

            }
        }catch (Exception e) {
            if (jsonArray != null) {
                videoAsyncTask.execute("https://api.themoviedb.org/3/movie/\"+movieid+\"/videos?api_key=a59665f89fb051a511ba2c8342fb1a14");
            }
        }*/
       /* if (URLUtil.isValidUrl(viedoURL)){
           videoAsyncTask.execute("https://api.themoviedb.org/3/movie/"+movieid+"/videos?api_key=a59665f89fb051a511ba2c8342fb1a14");
            Toast.makeText(MovieDetails.this, "Movie doesn't have any offical trailers", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(MovieDetails.this, "Movie doesn't have any offical trailers", Toast.LENGTH_LONG).show();
            //videoAsyncTask.execute("https://api.themoviedb.org/3/movie/" + movieid + "/videos?api_key=a59665f89fb051a511ba2c8342fb1a14");
            // Context context = this.getApplicationContext();
        }*/
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MovieDetails.this,ShowVideo.class);
                intent.putExtra("URL",url);
                try {
                    intent.putExtra("Name",jsonArray.getJSONObject(position).getString("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });
    }

    private void saveCheckState() {
        SharedPreferences sharedPreferences1 = getSharedPreferences("sp_title", MovieDetails.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences1.edit();
        editor.putBoolean(KEY_CHECKED_SAVE, checkbox.isChecked());
        editor.commit();
    }

    private void showMessage(String title,String response) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MovieDetails.this);
        builder.setTitle(title);
        builder.setMessage(response);
        builder.show();
    }
    private class MyTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                HttpURLConnection.setFollowRedirects(false);
                HttpURLConnection con =  (HttpURLConnection) new URL(params[0]).openConnection();
                con.setRequestMethod("HEAD");
                System.out.println(con.getResponseCode());
                return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
            }
            catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            boolean bResponse = result;
            if (bResponse==true)
            {
                videoAsyncTask.execute("https://api.themoviedb.org/3/movie/"+movieid+"/videos?api_key=a59665f89fb051a511ba2c8342fb1a14");
            }
            else
            {
                //trailer.setEnabled(false);
                Toast.makeText(MovieDetails.this, "Movie doesn't have any offical trailers", Toast.LENGTH_LONG).show();
                review.setEnabled(false);
                Toast.makeText(MovieDetails.this, "No reviews for the movie", Toast.LENGTH_LONG).show();
            }
        }
    }
    public class VideoAsyncTask extends AsyncTask<String,Void,JSONObject>{
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
                //Log.d("Tag", " " + result);

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


            class VideoAdapter extends BaseAdapter {

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
                    return a;
                }

                @Override
                public long getItemId(int position) {
                    return 0;
                }

                @Override
                public View getView(int position, View view, ViewGroup viewGroup) {
                    view = getLayoutInflater().inflate(R.layout.trailertext, null);
                    JSONObject object = new JSONObject();
                    TextView txttrailer = (TextView) view.findViewById(R.id.trailer_text);
                    try {
                        object = jsonArray.getJSONObject(position);
                        try {
                            videotrailer = object.getString("key");
                             name = object.getString("name");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Log.d("key", "key" + videotrailer);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                     url = "https://www.youtube.com/watch?v=" + videotrailer;
                    txttrailer.setText(name);
                    return view;
                }

            }
            VideoAdapter videoadapter = new VideoAdapter();
            listview.setAdapter(videoadapter);
        }
    }

}
