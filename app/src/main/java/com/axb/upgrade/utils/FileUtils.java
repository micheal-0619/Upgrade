package com.axb.upgrade.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileUtils {

    private final int FILESIZE = 40960;
    public static FileUtils instance;
    private static final String TAG="FileUtils";

    private FileUtils() {
    }

    public static FileUtils getInstance() {
        if (instance == null) {
            instance = new FileUtils();
        }

        return instance;
    }

    public static String extractFileExt(String filename) {
        if (filename != null && filename.length() > 0) {
            int dot = filename.lastIndexOf(46);
            if (dot > -1 && dot < filename.length() - 1) {
                return filename.substring(dot + 1);
            }
        }

        return filename;
    }

    public static String extractFileName(String filename) {
        if (filename != null && filename.length() > 0) {
            int dot = filename.lastIndexOf(46);
            if (dot > -1 && dot < filename.length()) {
                return filename.substring(0, dot);
            }
        }

        return filename;
    }

    public static String getNewFileName(String folderName, String prefix, String ext) {
        folderName = folderName.replaceAll("\\\\", File.separator);
        if (!File.separator.equals(folderName.subSequence(folderName.length() - 1, folderName.length()))) {
            folderName = folderName + File.separator;
        }

        File file = new File(folderName);
        if (!file.exists()) {
            return folderName;
        } else {
            Date now = new Date(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String fileName = prefix + sdf.format(now);
            String fullName = folderName + fileName + ext;

            while(true) {
                file = new File(fullName);
                if (!file.exists()) {
                    return fullName;
                }

                fileName = fileName + "_1";
                fullName = folderName + fileName + ext;
            }
        }
    }

    public static String extractDirectoryName(String fileName) {
        if (fileName != null && fileName.length() > 0) {
            int splash = fileName.lastIndexOf(File.separator);
            if (splash > -1 && splash < fileName.length()) {
                return fileName.substring(0, splash);
            }
        }

        return fileName;
    }

    public File createSDFile(String fileName) throws IOException {
        File file = new File(fileName);
        file.createNewFile();
        return file;
    }

    public File createSDDir(String dirName) {
        File dir = new File(dirName);
        dir.mkdir();
        return dir;
    }

    public static boolean isFileExist(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }

    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        return file.isFile() && file.exists() ? file.delete() : false;
    }

    public static File[] getFiles(String aDirName) {
        File file = new File(aDirName);
        return file.exists() && file.isDirectory() ? file.listFiles() : null;
    }

    public static boolean saveBitmap(Bitmap bitmap, String fileName) throws IOException {
        return saveBitmap(bitmap, fileName, 0, Bitmap.CompressFormat.PNG, 100);
    }

    public static boolean saveBitmap(Bitmap bitmap, String fileName, int scale, Bitmap.CompressFormat format, int quality) throws IOException {
        File file = new File(fileName);
        if (!file.exists() && !file.createNewFile()) {
            return false;
        } else {
            if (scale != 0) {
                float ratio = (float)scale / 100.0F;
                Matrix matrix = new Matrix();
                matrix.postScale(ratio, ratio);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }

            FileOutputStream os = new FileOutputStream(file);

            try {
                bitmap.compress(format, quality, os);
                os.flush();
            } finally {
                os.close();
            }

            return true;
        }
    }

    public boolean deleteDirectory(String path) {
        boolean flag = false;
        if (!path.endsWith(File.separator)) {
            path = path + File.separator;
        }

        File dirFile = new File(path);
        if (dirFile.exists() && dirFile.isDirectory()) {
            flag = true;
            File[] files = dirFile.listFiles();

            for(int i = 0; i < files.length; ++i) {
                if (files[i].isFile()) {
                    flag = deleteFile(files[i].getAbsolutePath());
                    if (!flag) {
                        break;
                    }
                } else {
                    flag = this.deleteDirectory(files[i].getAbsolutePath());
                    if (!flag) {
                        break;
                    }
                }
            }

            return !flag ? false : dirFile.delete();
        } else {
            return false;
        }
    }

    public boolean DeleteFolder(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        } else {
            return file.isFile() ? deleteFile(filePath) : this.deleteDirectory(filePath);
        }
    }

    public void appendText(String fileName, String line) {
        try {
            File file = new File(fileName);
            if (!file.exists() && !file.createNewFile()) {
                return;
            }

            FileWriter writer = new FileWriter(file, true);
            if (!line.endsWith("\r\n")) {
                line = line + "\r\n";
            }

            writer.write(line);
            writer.flush();
            writer.close();
        } catch (IOException var5) {
            Log.e(TAG, "getBase64Str: ", var5);
        }

    }

    public static String getBase64Str(Context context, Uri fileUri) {
        try {
            InputStream is = context.getContentResolver().openInputStream(fileUri);
            int len = is.available();
            byte[] content = new byte[len];
            is.read(content);
            return Base64.encodeToString(content, 0);
        } catch (IOException var5) {
            Log.e(TAG, "getBase64Str: ", var5);
            return "";
        }
    }

    public static List<String> readTextFromAsset(Context context, String fileName, Charset charset) {
        List<String> result = new ArrayList();
        InputStream is = null;

        try {
            is = context.getAssets().open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, charset));

            String line;
            while((line = reader.readLine()) != null) {
                result.add(line);
            }
        } catch (IOException var15) {
            Log.e(TAG, "getBase64Str: ", var15);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException var14) {
                    Log.e(TAG, "getBase64Str: ", var14);
                }
            }

        }

        return result;
    }
}
