package com.example.android.popularmovies;

import android.app.Application;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by siddharth.thakrey on 26-09-2016.
 */
public class Utility extends Application {



    public static String getSortedSetting(Context c)
    {
        String SORT_ORDER_PREFERENCE = c.getString(R.string.sort_preference_key);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(c);
        String category = pref.getString(SORT_ORDER_PREFERENCE, c.getString(R.string.popular_preference_key));
        return category;


    }

    public static void saveImage(Context context, Bitmap b, String name, String extension , String setting){
        File path=new File(context.getFilesDir(),setting);
        File mypath=new File(path,name);
        mypath.getParentFile().mkdirs();
        try {
            mypath.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out;
        try {
            out = new FileOutputStream(mypath);
            b.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void getImageBitmap(Context context, String name, String setting, ImageView i ){
        try{

            File f1 = context.getFilesDir();
            File f2 =  new File(f1,context.getString(R.string.slash)+setting+context.getString(R.string.slash)+name);
            Picasso.with(context).load(f2).into(i);
            return;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return;
    }

    public static void copyImageBitmap(Context context, String name, String srcFolder, String destFolder) throws FileNotFoundException {
        File f1 = context.getFilesDir();
        File f2 =  new File(f1,context.getString(R.string.slash)+srcFolder+context.getString(R.string.slash)+name);
        try {
            FileInputStream fis = new FileInputStream(f2.getPath());
            Bitmap b = BitmapFactory.decodeStream(fis);
            saveImage(context,b, name, "",destFolder);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        }



}
