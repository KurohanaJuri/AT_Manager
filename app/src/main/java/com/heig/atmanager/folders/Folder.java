package com.heig.atmanager.folders;

import android.util.Log;

import com.heig.atmanager.DrawerObject;
import com.heig.atmanager.taskLists.TaskList;

import java.util.ArrayList;

/**
 *  Author : Teo Ferrari
 *  Date   : 06.04.2020
 *
 *  Class folder representing a folder containing different tasklists for organisation
 */
public class Folder extends DrawerObject {

    private long id;
    private ArrayList<TaskList> taskLists;

    public Folder(String name){
        this(-1, name, new ArrayList<TaskList>());
    }

    public Folder(long id, String name, ArrayList<TaskList> taskLists){
        super(name);
        this.id = id;
        this.taskLists = taskLists;
    }

    /**
     * Deletes a list from folder
     *
     * @param name Name of the list to delete
     * @return success of the operation
     */
    public boolean deleteList(String name){
        for(TaskList list : taskLists){
            if(list.getName().equals(name)){
                taskLists.remove(list);
                return true;
            }
        }
        return false;
    }

    private static final String TAG = "Folder";

    /**
     * Adds a list from folder
     *
     * @param list : tasklist to add
     */
    public void addList(TaskList list){
        int recurrences = 0;
        String original = list.getName();
        for(int i = 0; i < taskLists.size(); ++i) {
            if(taskLists.get(i).getName().equals(list.getName())){
                recurrences++;
                list.setName(original + " (" + recurrences + ")");
                i = 0;
            }
        }

        taskLists.add(list);
        list.setFolderId(this.id);
    }

    public ArrayList<TaskList> getTaskLists() {
        return taskLists;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean isFolder() {
        return true;
    }
}