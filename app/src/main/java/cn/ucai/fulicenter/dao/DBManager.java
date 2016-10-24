package cn.ucai.fulicenter.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import cn.ucai.fulicenter.Bean.UserAvatar;

/**
 * Created by Administrator on 2016/10/24.
 */

public class DBManager {
    private static DBManager dbmanager = new DBManager();
    private static DBOpenHelper dbHelper;

    void onInit(Context context){
        dbHelper = new DBOpenHelper(context);
    }
    public synchronized void closeDB(){
        if(dbHelper!=null){
            dbHelper.closeDB();
        }
    }

    public static synchronized DBManager getInstance() {
        return dbmanager;
    }

    public synchronized boolean saveUser(UserAvatar user){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserDao.USER_COLUMN_NAME,user.getMuserName());
        values.put(UserDao.USER_COLUMN_NICK,user.getMuserNick());
        values.put(UserDao.USER_COLUMN_AVATAR_ID,user.getMavatarId());
        values.put(UserDao.USER_COLUMN_AVATAR_TYPE,user.getMavatarType());
        values.put(UserDao.USER_COLUMN_AVATAR_PATH,user.getMavatarPath());
        values.put(UserDao.USER_COLUMN_AVATAR_SUFFIX,user.getMavatarSuffix());
        values.put(UserDao.USER_COLUMN_AVATAR_LASTUPDATE_TIME,user.getMavatarLastUpdateTime());
        if(db.isOpen()){
            return db.replace(UserDao.USER_TABLE_NAME,null,values)!=-1;
        }
        return false;
    }

    public  synchronized UserAvatar getUser(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select * from "+UserDao.USER_TABLE_NAME+" where "
                +UserDao.USER_COLUMN_NAME+" =?";
        UserAvatar user  = null;
        Cursor cursor = db.rawQuery(sql,new String[]{username});
        if(cursor.moveToNext()){
            user = new UserAvatar();
            user.setMuserName(username);
            user.setMuserNick(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_NICK)));
            user.setMavatarId(cursor.getInt(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_ID)));
            user.setMavatarType(cursor.getInt(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_TYPE)));
            user.setMavatarPath(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_PATH)));
            user.setMavatarSuffix(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_SUFFIX)));
            user.setMavatarLastUpdateTime(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_LASTUPDATE_TIME)));
        }

        return user;
    }

    public boolean updateUser(UserAvatar user) {
        int result = -1;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = UserDao.USER_COLUMN_NAME+"=?";
        ContentValues values = new ContentValues();
        values.put(UserDao.USER_COLUMN_NICK,user.getMuserNick());
        if(db.isOpen()){
            result = db.update(UserDao.USER_TABLE_NAME,values,sql,new String[]{user.getMuserName()});
        }
        return result>0;
    }
}
