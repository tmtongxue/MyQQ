package utils;

import java.io.*;

public class StreamUtils {
	public static byte[] streamToByteArray(InputStream is) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(); // �������������
		byte[] b = new byte[1024]; // �ֽ�����
		int len;
		while ((len = is.read(b)) != -1) { // ��ȡѭ��
			bos.write(b, 0, len); // �Ѷ�ȡ�������ݣ� д��bos
		}
		byte[] array = bos.toByteArray(); // Ȼ��bosת���ֽ�����
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
