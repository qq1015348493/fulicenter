package cn.ucai.fulicenter.net;

import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;

import cn.ucai.fulicenter.Bean.PhotoBean;

/**
 * Created by Administrator on 2016/10/25.
 */

public class ImageFind {
    public static ArrayList<PhotoBean> readImageFilesInfo(Context context) {
        ArrayList<PhotoBean> list = new ArrayList<>();
        String[]projection={
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.HEIGHT,
                MediaStore.Images.Media.WIDTH
        };
        ContentResolver resolver = context.getContentResolver();
        Cursor c = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,projection,null,null,null);
        while(c.moveToNext()){
            int id = c.getInt(c.getColumnIndex(MediaStore.Images.Media._ID));
            String displayName = c.getString(c.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
            String path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
            int size = c.getInt(c.getColumnIndex(MediaStore.Images.Media.SIZE));
            int height = c.getInt(c.getColumnIndex(MediaStore.Images.Media.HEIGHT));
            int width = c.getInt(c.getColumnIndex(MediaStore.Images.Media.HEIGHT));
            PhotoBean photo = new PhotoBean(id,displayName,path,size,height,width);
            list.add(photo);
        }
        return list;
    }



    ;
    PhotoBean photo = null;


}
