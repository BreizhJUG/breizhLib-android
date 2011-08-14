package org.breizhjug.breizhlib.utils;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import org.breizhjug.breizhlib.BreizhLib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ISBNImageCache {

    private static ImageDownloader imageDownloader = BreizhLib.getImageDownloader();

    private static final String SD_PATH = Environment.getExternalStorageDirectory().toString() + "/breizhlib/";

    public static void init(){
        File dir = new File(SD_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static void clearCache(){
       File dir = new File(SD_PATH);
        if (dir.exists()) {
            File[] fileList = dir.listFiles();
            for (File file : fileList) {
                 file.delete();
            }
        }
    }

    public static void getIsbnImageFromCache(String isbn, String url, ImageView imageView) {


        init();

        File[] fileList = new File(SD_PATH).listFiles();

        Bitmap bitmap = null;
        for (File file : fileList) {
            if (file.getName().contains(isbn)) {
                bitmap = BitmapFactory.decodeFile(SD_PATH+file.getName());
                imageView.setImageBitmap(bitmap);
                Log.d("IMG", file.getName() +" "+bitmap );
                return;
            }
        }

        if (bitmap == null) {
            bitmap = imageDownloader.download(url, imageView);
            if (bitmap != null) {
                try {
                    saveImage(bitmap, isbn);
                } catch (FileNotFoundException e) {

                }
            }
        }
    }

    private static void saveImage(Bitmap bitmap, String isbn) throws FileNotFoundException {
        File file = new File(SD_PATH + isbn + ".jpg");
        Bitmap thumb = Bitmap.createScaledBitmap(bitmap,75, 100,true);
        thumb.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
    }
}
