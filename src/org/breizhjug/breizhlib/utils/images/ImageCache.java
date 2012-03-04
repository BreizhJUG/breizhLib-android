package org.breizhjug.breizhlib.utils.images;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import com.google.inject.Inject;
import org.breizhjug.breizhlib.guice.AppPath;
import org.breizhjug.breizhlib.utils.Cache;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ImageCache implements Cache {

    private static final String TAG = "Breizhlib.ImageCache";

    private static final String JPG_EXT = ".jpg";

    private static ImageDownloader imageDownloader = ImageDownloader.getInstance();

    private String SD_PATH = Environment.getExternalStorageDirectory().toString();

    @Inject
    public ImageCache(@AppPath String appfolder) {
        SD_PATH += "/" + appfolder + "/";
    }

    public void init() {
        File dir = new File(SD_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public void clearCache() {
        File dir = new File(SD_PATH);
        if (dir.exists()) {
            File[] fileList = dir.listFiles();
            for (File file : fileList) {
                file.delete();
            }
        }
    }

    @Override
    public void clearDBCache() {
        clearCache();
    }

    public void getFromCache(String name, String url, ImageView imageView) {


        init();

        File[] fileList = new File(SD_PATH).listFiles();

        Bitmap bitmap = null;
        if (fileList != null) {
            for (File file : fileList) {
                if (file.getName().contains(name)) {
                    bitmap = BitmapFactory.decodeFile(SD_PATH + file.getName());
                    imageView.setImageBitmap(bitmap);
                    Log.d(TAG, file.getName() + " " + bitmap);
                    return;
                }
            }
        }

        if (bitmap == null && url != null) {
            bitmap = imageDownloader.download(url, imageView);
            if (bitmap != null) {
                try {
                    saveImage(bitmap, name);
                } catch (FileNotFoundException e) {

                }
            }
        }
    }

    public void download(String url, ImageView imageView) {
        imageDownloader.download(url, imageView);
    }

    private void saveImage(Bitmap bitmap, String isbn) throws FileNotFoundException {
        File file = new File(SD_PATH + isbn + JPG_EXT);
        Bitmap thumb = Bitmap.createScaledBitmap(bitmap, 75, 100, true);
        thumb.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
    }
}
