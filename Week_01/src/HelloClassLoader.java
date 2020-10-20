import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;

public class HelloClassLoader extends ClassLoader {
    public static void main(String[] args) throws Exception {
        String path = HelloClassLoader.class.getClassLoader().getResource("Hello.xlass").getPath();
        System.out.println(path);
        Class c = new HelloClassLoader().findClass(path);
        Object obj = c.newInstance();
        System.out.println(obj.getClass().getName());
        Method m = c.getMethod("hello");
        m.invoke(obj);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bytes = null;
        try {
            bytes = read_class(name);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert bytes != null;
        return defineClass(null,bytes,0,bytes.length);
    }

    protected static byte[] read_class(String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int len;

        byte[] bArray = new byte[1024];

        while ((len=fis.read(bArray)) != -1){
//            for (byte b : bArray) {
//                System.out.println(b);
//            }
            bos.write(bArray,0,len);
        }

        fis.close();
        bArray = bos.toByteArray();

        for (int i = 0; i < bArray.length; i++) {
            bArray[i] = (byte) (255 - bArray[i]);
        }

        return bArray;
    }
}