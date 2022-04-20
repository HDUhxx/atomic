package com.developer.filepicker.model;

import ohos.app.Context;
import ohos.global.resource.RawFileEntry;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author akshay sunil masram
 */
public class DialogProperties {

    public int selection_mode;
    public int selection_type;
    public File root;
    public File error_dir;
    public File offset;
    public String[] extensions;
    public boolean show_hidden_files;
    public Context context;

    public DialogProperties(Context context) {
        this.context = context;
        selection_mode = DialogConfigs.SINGLE_MODE;
        selection_type = DialogConfigs.FILE_SELECT;
        root = new File(getPath());
        error_dir = new File(getPath());
        offset = new File(getPath());
        extensions = null;
        show_hidden_files = false;
    }

    public String getPath(){
        RawFileEntry fileEntry = context.getResourceManager().getRawFileEntry("resources/rawfile/text.txt");
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileEntry.openRawFile()));
            return bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}