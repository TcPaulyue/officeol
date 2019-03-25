package com.example.officeol.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WordFile {
    public ArrayList<NewWord>WordFiles=new ArrayList<>();
    public void addNewFile(Ops ops)
    {
        NewWord newWord=new NewWord(ops.userId,ops.fileId);
        newWord.addContents(ops.message);
        WordFiles.add(newWord);
    }
    public Boolean findWord(Ops ops)
    {
        for(NewWord word:WordFiles) {
            if(word.fileId==ops.fileId) {
                return true;
            }
        }
        return false;
    }
    public Boolean findWordOnOpen(String fileId)
    {
        for(NewWord word:WordFiles) {
            if(word.fileId==fileId) {
                return true;
            }
        }
        return false;
    }
    public Boolean findUserOnOpen(String fileId,String UserId)
    {
        if(findWordOnOpen(fileId)==true) {
            for(NewWord word:WordFiles) {
                if(word.fileId==fileId) {
                    for(User user:word.users) {
                        if(user.getId()==UserId)
                            return true;
                    }
                }
            }
        }
        return false;
    }
    public NewWord getWord(String fileId)
    {
        for(NewWord word:WordFiles) {
            if (word.fileId.equals(fileId))
                return word;
        }
        System.out.println("the word is not existed!");
        return null;
    }
    public void editWord(Ops ops)
    {
        if(findWord(ops)==false)
            this.addNewFile(ops);
        else {
            getWord(ops.fileId).addUsers(ops.userId);
            getWord(ops.fileId).addContents(ops.message);
        }
    }

}
