package com.pengllrn.tegm.utils;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author Administrator
 * @version $Rev$
 * @des ${UTODO}
 * @updateAuthor ${Author}$
 * @updateDate2017/9/21.
 */

public class FileCache {
    private File file;
    private Context context;

    public FileCache(Context context) {
        this.context = context;
    }

    public File getFile_FileDir(String filename) {
        file = context.getFilesDir();
        File f = new File(file, filename);
        return f;
    }

    public File getFile_CacheDir(String filename) {
        file = context.getCacheDir();
        File f = new File(file, filename);
        return f;
    }

    public File getFile(String url) {
        file = context.getFilesDir();
        if (!file.exists())
            file.mkdirs();
        String filename = url.replace("/", "") + ".jpg";
        File f = new File(file, filename);
        return f;
    }

    public void clear(String filename) {
        File file = getFile_CacheDir(filename + ".txt");
        if (file != null)
            file.delete();
    }

    public void saveInCacheDir(String data, String filename) {
        File file = getFile_CacheDir(filename + ".txt");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveInFileDir(String data, String filename) {
        File file = getFile_FileDir(filename + ".txt");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String readFromFilrDir(String filename) {
        File file = getFile_FileDir(filename + ".txt");
        StringBuilder sb = new StringBuilder("");
        if (file.exists() && file.length() > 0) {
            try {
                FileInputStream fis = new FileInputStream(file);
                byte[] temp = new byte[1024];
                int len = 0;
                //读取文件内容:
                while ((len = fis.read(temp)) > 0) {
                    sb.append(new String(temp, 0, len));
                }
                //关闭输入流
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public String readFromCacheDir(String filename) {
        File file = getFile_CacheDir(filename + ".txt");
        StringBuilder sb = new StringBuilder("");
        if (file.exists() && file.length() > 0) {
            try {
                FileInputStream fis = new FileInputStream(file);
                byte[] temp = new byte[1024];
                int len = 0;
                //读取文件内容:
                while ((len = fis.read(temp)) > 0) {
                    sb.append(new String(temp, 0, len));
                }
                //关闭输入流
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
