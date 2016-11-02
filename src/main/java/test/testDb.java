package test;

import org.junit.Test;

import java.io.UnsupportedEncodingException;

/**
 * Created by chenhongyu on 2016/10/31.
 */
public class testDb {
    @Test
    public void testReplaceImgFilesUrl() {
        byte bytes[] = {(byte) 0xc2,(byte) 0xa0};
        try {
            String UTFSpace = new String(bytes, "UTF-8");
            System.out.println("UTFSpace + \";\" = " + UTFSpace + ";");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte bytes2[] = {(byte) 0xc2};
        try {
            String UTFSpace = new String(bytes2, "UTF-8");
            System.out.println("UTFSpace + \";\" = " + UTFSpace + ";");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte bytes3[] = {(byte) 0xa0};
        try {
            String UTFSpace = new String(bytes3, "UTF-8");
            System.out.println("UTFSpace + \";\" = " + UTFSpace + ";");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
