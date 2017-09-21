package me.zaheenchoudhry.rideandgo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class SetProfileImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

    private ImageView image;

    public SetProfileImageAsyncTask(ImageView image) {
        this.image = image;
    }

    protected Bitmap doInBackground(String... urls) {
        String url = urls[0];
        Bitmap imageBitmap = null;
        try {
            InputStream inputStream = new java.net.URL(url).openStream();
            imageBitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return imageBitmap;
    }

    protected void onPostExecute(Bitmap result) {
        image.setImageBitmap(result);
    }
}
