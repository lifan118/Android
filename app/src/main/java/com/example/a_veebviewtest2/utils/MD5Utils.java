package com.example.a_veebviewtest2.utils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class MD5Utils {
    //md5 �����㷨
    public static String md5(String text) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("md5");
            // ���� byte[] result -> digest.digest( );  �ı� text.getBytes(); 
            byte[] result = digest.digest(text.getBytes());
            //����StringBuilder���� Ȼ����StringBuffer����ȫ�Ը�
            //StringBuilder sb = new StringBuilder();
            StringBuffer sb = new StringBuffer();
            // result���飬digest.digest ( ); -> text.getBytes();
            // for ѭ������byte[] result;
            for (byte b : result){
                // 0xff Ϊ16����
                int number = b & 0xff;
                // numberֵ ת�� �ַ��� Integer.toHexString( );
                String hex = Integer.toHexString(number);
                if (hex.length() == 1){
                    sb.append("0"+hex);
                }else {
                    sb.append(hex);
                }
            }
            //sb StringBuffer sb = new StringBuffer();����ʵ����
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            //�����쳣return���ַ���
            return "";
        }
    }
}