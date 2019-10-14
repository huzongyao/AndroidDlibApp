package com.hzy.dlib.simple.app.utils;

import android.os.Environment;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.Utils;

import java.io.File;
import java.util.UUID;

public class SpaceUtils {

    public static final String WORKSPACE_DIR = "workspace";

    /**
     * new some file with random file name
     *
     * @return file object
     */
    public static File newUsableFile() {
        File fileDir = getUsableFilePath();
        if (fileDir.exists()) {
            String randomName = UUID.randomUUID().toString();
            return new File(fileDir, randomName);
        }
        return null;
    }

    public static void clearUsableSpace() {
        File spacePath = getUsableFilePath();
        if (spacePath.exists()) {
            FileUtils.deleteAllInDir(spacePath);
        }
    }

    /**
     * get some available path to store files
     *
     * @return some path
     */
    public static File getUsableFilePath() {
        File fileDir = null;
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                fileDir = Utils.getApp().getExternalFilesDir(WORKSPACE_DIR);
            }
            if (fileDir == null) {
                fileDir = new File(Utils.getApp().getFilesDir(), WORKSPACE_DIR);
            }
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileDir;
    }
}
