/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.resume.parser;

import java.util.ArrayList;

/**
 *
 * @author Lokesh
 */
public class trie {
    boolean isTechi;
    boolean isWord;
    int gscore;
    trie[]children;
    trie(){
        isTechi=false;
        isWord=false;
        gscore=0;
        children=new trie[27];
        for(int i=0;i<27;i++)
            children[i]=null;
    }
    void add(String word,int score){
        if(word.length()<1)
            return;
        if(word.length()==1){
            if(word.charAt(0)>='a'&&word.charAt(0)<='z'){
                int i=word.charAt(0)-'a';
                if(children[i]==null)
                    children[i]=new trie();
                children[i].isWord=true;
                children[i].gscore+=score;
                children[i].isTechi=children[i].gscore>=80;
            }else{
                if(children[26]==null)
                    children[26]=new trie();
                children[26].isWord=true;
                children[26].gscore+=score;
                children[26].isTechi=children[26].gscore>=80;
            }
        }
        if(word.charAt(0)>='a'&&word.charAt(0)<='z'){
            int i=word.charAt(0)-'a';
            if(children[i]==null)
                children[i]=new trie();
            children[i].add(word.substring(1),score);
        }
        else{
            if(children[26]==null)
                children[26]=new trie();
            children[26].add(word.substring(1),score);
        }
    }
    void getTechis(String word,ArrayList<String>techTerms){
        if(isWord){
            //System.out.println("trie : "+word+" "+gscore);
            if(isTechi)
                techTerms.add(word);
        }
        for(int i=0;i<26;i++)
            if(children[i]!=null)
                children[i].getTechis(word+(char)('a'+i),techTerms);
        if(children[26]!=null)
                children[26].getTechis(word+"-",techTerms);
    }
}
