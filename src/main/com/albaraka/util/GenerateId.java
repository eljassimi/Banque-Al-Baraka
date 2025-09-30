package main.com.albaraka.util;

public class GenerateId {

    public static Long generateId(){
        return Math.round(Math.random()*10000L);
    }
}
