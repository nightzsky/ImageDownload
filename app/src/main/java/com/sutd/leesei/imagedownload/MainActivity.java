package com.sutd.leesei.imagedownload;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    Button downloadButton;
    ImageView imageDownloaded;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ProgressDialog mProgressDialog;
    String userId;
    String imageUrl;
    String imageKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        downloadButton = (Button) findViewById(R.id.downloadBtn);
        imageDownloaded = (ImageView) findViewById(R.id.imageDownloaded);

        database = FirebaseDatabase.getInstance("https://authentication-24160.firebaseio.com/");
        myRef = database.getReference();

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadImage();
            }
        });



    }
    public void downloadImage() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Toast.makeText(MainActivity.this,"hello",Toast.LENGTH_LONG).show();
                    userId = dataSnapshot.child("users").child("0sG4Ejo2MNV6l7sxnkSo4rkPqdl1").child("full_name").getValue(String.class);
                    for (DataSnapshot data: dataSnapshot.child(userId).getChildren()){
                        imageKey = data.getKey();
                        imageUrl = data.getValue(String.class);
                    }

                    //String tempimageUri = dataSnapshot.child("hi2").child("-L5tEwmC3HoV_wO5YQjo").getValue(String.class);
                    //Log.i("Norman", tempimageUri);
                    /*Uri imageUri = Uri.parse(tempimageUri);
                    InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    // Decode the URI into a Bitmap
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    // Rescale the selectedImage to display on the phone
                    Bitmap scaledImage = Bitmap.createScaledBitmap(selectedImage, 512, 512, true);*/


    //photodownloaded.setImageBitmap(scaledImage);
                    new imageDownloading().execute(imageUrl);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private class imageDownloading extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Create a progressdialog
            mProgressDialog = new ProgressDialog(MainActivity.this);
            //Set progressdialog title
            mProgressDialog.setTitle("Download Image Tutorial");
            //Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... URL) {
            String imageURL = URL[0];

            Bitmap bitmap = null;

            try {
                //Download image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                //Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
            }catch (Exception ex){
                ex.printStackTrace();
            }
            return bitmap;


        }

        @Override
        protected void onPostExecute(Bitmap result) {
            //Set the bitmap into ImageView
            imageDownloaded.setImageBitmap(result);
            //Close progress dialog
            mProgressDialog.dismiss();
        }
    }
}

