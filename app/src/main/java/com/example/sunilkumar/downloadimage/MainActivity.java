package com.example.sunilkumar.downloadimage;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import static android.R.id.progress;

public class MainActivity extends AppCompatActivity {

    Button button;
    ProgressDialog progressdialog;
    public static final int Progress_Dialog_Progress = 0;
    String ImageURL = "http://www.android-examples.com/wp-content/uploads/2016/04/demo_download_image.jpg" ;
   // URL url;
    //URLConnection urlconnection ;
    int FileSize;
   // InputStream inputstream;
   // OutputStream outputstream;
    byte dataArray[] = new byte[1024];
    long totalSize = 0;
    ImageView imageview;
    String GetPath ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button)findViewById(R.id.button1);
        imageview = (ImageView)findViewById(R.id.imageView1);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                new ImageDownloadWithProgressDialog().execute(ImageURL);

            }
        });


    }

    public class ImageDownloadWithProgressDialog extends AsyncTask<String ,String ,String >{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(Progress_Dialog_Progress);
        }

        @Override
        protected void onPostExecute(String s) {

            dismissDialog(Progress_Dialog_Progress);

            GetPath = Environment.getExternalStorageDirectory().toString() + "/demo_photo1.jpg";

            imageview.setImageDrawable(Drawable.createFromPath(GetPath));

            Toast.makeText(MainActivity.this, "Image Downloaded Successfully", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onProgressUpdate(String... values) {

            progressdialog.setProgress(Integer.parseInt(values[0]));
        }

        @Override
        protected String doInBackground(String... strings) {
            int count;

            URL url;
            URLConnection urlConnection;
            InputStream inputStream;
            OutputStream outputStream;


            try {

                url = new URL(strings[0]);
                urlConnection = url.openConnection();
                urlConnection.connect();

                FileSize = urlConnection.getContentLength();
// get the path to sdcard
                File sdCard = Environment.getExternalStorageDirectory();
// to this path add a new directory path
                File dir = new File(sdCard.getAbsolutePath() + "/suneel/");
                // create this directory if not already created
                dir.mkdir();

                inputStream = new BufferedInputStream(url.openStream());

                outputStream = new FileOutputStream("/sdcard/suneel/demo_photo1.jpg");


                while ((count = inputStream.read(dataArray)) != -1) {

                    totalSize += count;

                    publishProgress(""+(int)((totalSize*100)/FileSize));

                    outputStream.write(dataArray, 0, count);
                }

                outputStream.flush();
                outputStream.close();
                inputStream.close();

            }catch (Exception e) {}
            return null;

        }

    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case Progress_Dialog_Progress:

                progressdialog = new ProgressDialog(MainActivity.this);
                progressdialog.setMessage("Downloading Image From Server...");
                progressdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressdialog.setCancelable(false);
                progressdialog.show();
                return progressdialog;

            default:

                return null;
        }

    }
}
