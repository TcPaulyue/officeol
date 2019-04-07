package com.example.officeol.bean;

import java.util.ArrayList;

public class JupiterWordList {
    public ArrayList<JupiterWord> jupiterWordArrayList=new ArrayList<>();
    public boolean addNewJupiterWordorNot(String fileId)
    {
        for(JupiterWord jupiterWord:jupiterWordArrayList)
        {
            if(jupiterWord.fileId.equals(fileId))
            {
                return false;
            }
        }
        return true;
    }
    public JupiterWord getJupiterWord(String fileId)
    {
        for(JupiterWord jupiterWord:jupiterWordArrayList)
        {
            if(jupiterWord.fileId.equals(fileId))
                return jupiterWord;
        }
        System.err.println("getJupiterWord error!");
        return null;
    }
    public void addNewJupiterWord(String fileId,String userId)
    {
        JupiterWord jupiterWord=new JupiterWord();
        jupiterWord.fileId=fileId;
        JupiterUser jupiterUser=new JupiterUser(userId,fileId);
        jupiterWord.jupiterUsers.add(jupiterUser);
        jupiterWordArrayList.add(jupiterWord);
    }
    public void addNewJupiterUser(String fileId,String userId)
    {
        if(addNewJupiterWordorNot(fileId)==false)
        {
            for(JupiterWord jupiterWord:jupiterWordArrayList)
            {
                if(jupiterWord.fileId.equals(fileId))
                {
                    JupiterUser jupiterUser=new JupiterUser(userId,fileId);
                    jupiterWord.jupiterUsers.add(jupiterUser);
                }
            }
        }
    }
    public void displayJupiterWordList()
    {
        for(JupiterWord jupiterWord:jupiterWordArrayList)
            System.out.println(jupiterWord.fileId+"     "+jupiterWord.jupiterUsers);
    }
}
