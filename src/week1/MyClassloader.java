package week1;

import java.io.*;

public class MyClassloader extends ClassLoader{

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        new MyClassloader().findClass("Hello").newInstance();
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException{
        String filePath = "Hello.xlass";
        File file = new File(filePath);

        int length = (int) file.length();
        byte[] bytes = new byte[length];
        try {
            new FileInputStream(file).read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte)(255 - bytes[i]);
        }

        return defineClass(name,bytes,0,bytes.length);
    }


}
