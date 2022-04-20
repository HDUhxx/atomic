package com.developer.filepicker.controller;

/**
 * DialogSelectionListener
 *
 * @author akshay sunil masram
 * @since xxxx年x月x日
 */
public interface DialogSelectionListener {
    /**
     * 选择的文件
     *
     * @param files 文件
     */
    void onSelectedFilePaths(String[] files);
}