package com.cnlive.media.media.loader;


import com.cnlive.media.media.entity.Folder;

import java.util.ArrayList;

/**
 * Created by dmcBig on 2017/7/20.
 */

public abstract class LoaderM {

    public String getParent(String path) {
        String[] sp = path.split("/");
        return sp.length == 0 ? "" : sp[sp.length - 2];
    }

    public int hasDir(ArrayList<Folder> folders, String dirName) {
        for (int i = 0; i < folders.size(); i++) {
            Folder folder = folders.get(i);
            if (folder.name.equals(dirName)) {
                return i;
            }
        }
        return -1;
    }

    public abstract void onDestory();
}
