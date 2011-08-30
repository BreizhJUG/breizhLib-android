package org.breizhjug.breizhlib.database;

import android.content.*;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import java.io.*;


public class DBContext extends Context {

    private static String DB_PHONE_PATH = "/data/data/org.breizhjug.breizhlib/databases/";
    private static String DB_SDCARD_PATH = "/.breizhlib/";
    private static final String DATABASE_NAME = "breizhlib.db";

    Context context;


    public DBContext(Context context) {
        this.context = context;
    }

    protected void clean() {
        File sdDatabase = new File(DB_SDCARD_PATH + DATABASE_NAME);
        File phoneDatabase = new File(DB_PHONE_PATH + DATABASE_NAME);
        if (sdDatabase.exists() && phoneDatabase.exists()) {
            phoneDatabase.delete();
        }
    }

    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        return context.bindService(service, conn, flags);
    }

    public int checkCallingOrSelfPermission(String permission) {
        return context.checkCallingOrSelfPermission(permission);
    }

    public int checkCallingOrSelfUriPermission(Uri uri, int modeFlags) {
        return context.checkCallingOrSelfUriPermission(uri, modeFlags);
    }

    public int checkCallingPermission(String permission) {
        return context.checkCallingPermission(permission);
    }

    public int checkCallingUriPermission(Uri uri, int modeFlags) {
        return context.checkCallingUriPermission(uri, modeFlags);
    }

    public int checkPermission(String permission, int pid, int uid) {
        return context.checkPermission(permission, pid, uid);
    }

    public int checkUriPermission(Uri uri, int pid, int uid, int modeFlags) {
        return context.checkUriPermission(uri, pid, uid, modeFlags);
    }

    public int checkUriPermission(Uri uri, String readPermission, String writePermission, int pid, int uid,
                                  int modeFlags) {
        return context.checkUriPermission(uri, readPermission, writePermission, pid, uid, modeFlags);
    }

    public void clearWallpaper() throws IOException {
        context.clearWallpaper();
    }

    public Context createPackageContext(String packageName, int flags) throws PackageManager.NameNotFoundException {
        return context.createPackageContext(packageName, flags);
    }

    public String[] databaseList() {
        return context.databaseList();
    }

    public boolean deleteDatabase(String name) {
        return context.deleteDatabase(name);
    }

    public boolean deleteFile(String name) {
        return context.deleteFile(name);
    }

    public void enforceCallingOrSelfPermission(String permission, String message) {
        context.enforceCallingOrSelfPermission(permission, message);
    }

    public void enforceCallingOrSelfUriPermission(Uri uri, int modeFlags, String message) {
        context.enforceCallingOrSelfUriPermission(uri, modeFlags, message);
    }

    public void enforceCallingPermission(String permission, String message) {
        context.enforceCallingPermission(permission, message);
    }

    public void enforceCallingUriPermission(Uri uri, int modeFlags, String message) {
        context.enforceCallingUriPermission(uri, modeFlags, message);
    }

    public void enforcePermission(String permission, int pid, int uid, String message) {
        context.enforcePermission(permission, pid, uid, message);
    }

    public void enforceUriPermission(Uri uri, int pid, int uid, int modeFlags, String message) {
        context.enforceUriPermission(uri, pid, uid, modeFlags, message);
    }

    public void enforceUriPermission(Uri uri, String readPermission, String writePermission, int pid, int uid,
                                     int modeFlags, String message) {
        context.enforceUriPermission(uri, readPermission, writePermission, pid, uid, modeFlags, message);
    }

    public boolean equals(Object o) {
        return context.equals(o);
    }

    public String[] fileList() {
        return context.fileList();
    }

    public Context getApplicationContext() {
        return context.getApplicationContext();
    }

    public ApplicationInfo getApplicationInfo() {
        return context.getApplicationInfo();
    }

    public AssetManager getAssets() {
        return context.getAssets();
    }

    public File getCacheDir() {
        return context.getCacheDir();
    }

    public ClassLoader getClassLoader() {
        return context.getClassLoader();
    }

    public ContentResolver getContentResolver() {
        return context.getContentResolver();
    }

    public File getDatabasePath(String name) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File root = Environment.getExternalStorageDirectory();
            File repertoire = new File(root, DB_SDCARD_PATH);
            File outputFile = new File(repertoire, name);
            return outputFile;
        } else {
            File repertoire = new File(DB_PHONE_PATH);
            File outputFile = new File(repertoire, name);
            return outputFile;
        }
    }

    public File getDir(String name, int mode) {
        return context.getDir(name, mode);
    }

    public File getExternalCacheDir() {
        return context.getExternalCacheDir();
    }

    public File getExternalFilesDir(String type) {
        return context.getExternalFilesDir(type);
    }

    public File getFileStreamPath(String name) {
        return context.getFileStreamPath(name);
    }

    public File getFilesDir() {
        return context.getFilesDir();
    }

    public Looper getMainLooper() {
        return context.getMainLooper();
    }

    public String getPackageCodePath() {
        return context.getPackageCodePath();
    }

    public PackageManager getPackageManager() {
        return context.getPackageManager();
    }

    public String getPackageName() {
        return context.getPackageName();
    }

    public String getPackageResourcePath() {
        return context.getPackageResourcePath();
    }

    public Resources getResources() {
        return context.getResources();
    }

    public SharedPreferences getSharedPreferences(String name, int mode) {
        return context.getSharedPreferences(name, mode);
    }

    public Object getSystemService(String name) {
        return context.getSystemService(name);
    }

    public Resources.Theme getTheme() {
        return context.getTheme();
    }

    public Drawable getWallpaper() {
        return context.getWallpaper();
    }

    public int getWallpaperDesiredMinimumHeight() {
        return context.getWallpaperDesiredMinimumHeight();
    }

    public int getWallpaperDesiredMinimumWidth() {
        return context.getWallpaperDesiredMinimumWidth();
    }

    public void grantUriPermission(String toPackage, Uri uri, int modeFlags) {
        context.grantUriPermission(toPackage, uri, modeFlags);
    }

    public int hashCode() {
        return context.hashCode();
    }

    public boolean isRestricted() {
        return context.isRestricted();
    }

    public FileInputStream openFileInput(String name) throws FileNotFoundException {
        return context.openFileInput(name);
    }

    public FileOutputStream openFileOutput(String name, int mode) throws FileNotFoundException {
        return context.openFileOutput(name, mode);
    }

    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        File dbFile = getDatabasePath(name);
        dbFile.getParentFile().mkdirs();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, factory);
        return db;
    }

    public Drawable peekWallpaper() {
        return context.peekWallpaper();
    }

    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter, String broadcastPermission,
                                   Handler scheduler) {
        return context.registerReceiver(receiver, filter, broadcastPermission, scheduler);
    }

    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        return context.registerReceiver(receiver, filter);
    }

    public void removeStickyBroadcast(Intent intent) {
        context.removeStickyBroadcast(intent);
    }

    public void revokeUriPermission(Uri uri, int modeFlags) {
        context.revokeUriPermission(uri, modeFlags);
    }

    public void sendBroadcast(Intent intent, String receiverPermission) {
        context.sendBroadcast(intent, receiverPermission);
    }

    public void sendBroadcast(Intent intent) {
        context.sendBroadcast(intent);
    }

    public void sendOrderedBroadcast(Intent intent, String receiverPermission, BroadcastReceiver resultReceiver,
                                     Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
        context.sendOrderedBroadcast(intent, receiverPermission, resultReceiver, scheduler, initialCode, initialData,
                initialExtras);
    }

    public void sendOrderedBroadcast(Intent intent, String receiverPermission) {
        context.sendOrderedBroadcast(intent, receiverPermission);
    }

    public void sendStickyBroadcast(Intent intent) {
        context.sendStickyBroadcast(intent);
    }

    public void sendStickyOrderedBroadcast(Intent intent, BroadcastReceiver resultReceiver, Handler scheduler,
                                           int initialCode, String initialData, Bundle initialExtras) {
        context.sendStickyOrderedBroadcast(intent, resultReceiver, scheduler, initialCode, initialData, initialExtras);
    }

    public void setTheme(int resid) {
        context.setTheme(resid);
    }

    public void setWallpaper(Bitmap bitmap) throws IOException {
        context.setWallpaper(bitmap);
    }

    public void setWallpaper(InputStream data) throws IOException {
        context.setWallpaper(data);
    }

    public void startActivity(Intent intent) {
        context.startActivity(intent);
    }

    public boolean startInstrumentation(ComponentName className, String profileFile, Bundle arguments) {
        return context.startInstrumentation(className, profileFile, arguments);
    }

    public void startIntentSender(IntentSender intent, Intent fillInIntent, int flagsMask, int flagsValues,
                                  int extraFlags) throws IntentSender.SendIntentException {
        context.startIntentSender(intent, fillInIntent, flagsMask, flagsValues, extraFlags);
    }

    public ComponentName startService(Intent service) {
        return context.startService(service);
    }

    public boolean stopService(Intent service) {
        return context.stopService(service);
    }

    public String toString() {
        return context.toString();
    }

    public void unbindService(ServiceConnection conn) {
        context.unbindService(conn);
    }

    public void unregisterReceiver(BroadcastReceiver receiver) {
        context.unregisterReceiver(receiver);
    }
}
