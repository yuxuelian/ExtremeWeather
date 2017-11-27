package com.as.weather.util.cache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 作者：王开波
 * 邮箱：568966289@qq.com
 * 公司：山东宇易信息科技
 * 创建时间：2017/3/28
 */

public class DiskObjectCache<T> {

    private DiskLruCache mDiskLruCache = null;

    public DiskObjectCache(Context context, String cacheName) {
        try {
            File cacheDir = getDiskCacheDir(context, cacheName);
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(context), 1, 10 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存对象到缓存
     *
     * @param key
     * @param value
     */
    public void saveObjectCache(String key, T value) {
        try {
            key = hashKeyForDisk(key);
            DiskLruCache.Editor editor = mDiskLruCache.edit(key);
            if (editor != null) {
                //打开输出流
                OutputStream outputStream = editor.newOutputStream(0);
                //将对象转成字节数组
                byte[] data = objectToByte(value);

                outputStream.write(data);

                editor.commit();
            }
            mDiskLruCache.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将对象转换成字节
     *
     * @param value
     * @return
     * @throws IOException
     */
    private byte[] objectToByte(T value) throws IOException {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
        objectOutputStream.writeObject(value);
        objectOutputStream.flush();
        byte data[] = arrayOutputStream.toByteArray();
        objectOutputStream.close();
        arrayOutputStream.close();
        return data;
    }

    public T readObjectCache(String key) {
        try {
            key = hashKeyForDisk(key);
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
            InputStream inputStream = snapshot.getInputStream(0);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            T object = (T) objectInputStream.readObject();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteObject(String key) {
        key = hashKeyForDisk(key);
        try {
            if (mDiskLruCache.remove(key)) {
                //删除成功
                mDiskLruCache.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            mDiskLruCache.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    private int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getApplicationContext().getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    private String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
