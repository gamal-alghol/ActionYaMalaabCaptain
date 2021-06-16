package com.malaab.ya.action.actionyamalaab.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.io.File;

public final class FileUtils {

    private static final String LINE_SEP = System.getProperty("line.separator");


    public static long getFileSizeInKB(final File file) {
        long len = getFileLength(file);
        return len == -1 ? 0 : file.length() / 1024;
    }

    public static long getFileSizeInMB(final File file) {
        return getFileSizeInKB(file) / 1024;
    }


    public static String getFileName(final File file) {
        return file.getName();
    }

    public static String getFileSize(final File file) {
        long len = getFileLength(file);
        return len == -1 ? "" : byte2FitMemorySize(len);
    }

    public static String getFileSize(final String filePath) {
        return getFileSize(getFileByPath(filePath));
    }

    public static String getFileExtension(Activity activity, File file) {
        Uri selectedUri = Uri.fromFile(file);
        //        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);

        return MimeTypeMap.getFileExtensionFromUrl(selectedUri.toString());
    }


    public static File getFileByPath(final String filePath) {
        return isSpace(filePath) ? null : new File(filePath);
    }

    public static long getFileLength(final File file) {
        if (!isFile(file))
            return -1;

        return file.length();
    }

    public static boolean isFile(final File file) {
        return file != null && file.exists() && file.isFile();
    }


    @SuppressLint("DefaultLocale")
    private static String byte2FitMemorySize(final long byteNum) {
        if (byteNum < 0) {
            return "shouldn't be less than zero!";

        } else if (byteNum < 1024) {
            return String.format("%.3f B", (double) byteNum);

        } else if (byteNum < 1048576) {
            return String.format("%.3f KB", (double) byteNum / 1024);

        } else if (byteNum < 1073741824) {
            return String.format("%.3f MB", (double) byteNum / 1048576);

        } else {
            return String.format("%.3f GB", (double) byteNum / 1073741824);
        }
    }


    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    public static boolean deleteFileByPath(String filePath) {
        return deleteFile(getFileByPath(filePath));
    }

    public static boolean deleteFile(File file) {
        return file != null && file.exists() && file.delete();
    }
}