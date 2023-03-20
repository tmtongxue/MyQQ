package utils;

import java.io.*;

public class StreamUtils {
	public static byte[] streamToByteArray(InputStream is) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(); // 创建输出流对象
		byte[] b = new byte[1024]; // 字节数组
		int len;
		while ((len = is.read(b)) != -1) { // 读取循环
			bos.write(b, 0, len); // 把读取到的数据， 写入bos
		}
		byte[] array = bos.toByteArray(); // 然后将bos转成字节数组
		bos.close();
		return array;
	}
	
	public static String streamToString(InputStream is) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line).append("\r\n");
		}
		return sb.toString();
	}
}
