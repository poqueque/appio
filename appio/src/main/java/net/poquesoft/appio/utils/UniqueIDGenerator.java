package net.poquesoft.appio.utils;

public class UniqueIDGenerator {

    public static final String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

    public static String getId(){
        long ts = System.currentTimeMillis()*1000;
        ts += (Math.random()*1000);
        int b = charSet.length();
        String id = "";
        while (ts > 0){
            id = Character.toString(charSet.charAt((int) (ts % b))) + id;
            ts = ts / b;
        }
        return id;
    }

}
