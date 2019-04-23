package com.model2.mvc.framework;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class RequestMapping {
	
	private static RequestMapping requestMapping;
	private Map<String, Action> map;
	private Properties properties;
	
	private RequestMapping(String resources) {
		map = new HashMap<String, Action>();
		InputStream in = null;
		try{
			//resource�� ��ġ�� parameter�� �ް��ֱ⿡ �������� Runtime���� inputstream ���� ==> URI�� ���� ��� �˻�
			//�� �������� ������ ������ ���� file�ý����� �ƴ� package������ ����Ѵ�.
			//in = new FileInputStream("C:\\workspace\\01.Model2MVCShop(stu)\\src\\com\\model2\\mvc\\resources\\actionmapping.properties");
			//�ȵ�in = new FileInputStream("src/com/model2/mvc/resources/actionmapping.properties");
			//�ȵ�in = new FileInputStream("actionmapping.properties");
			//in = getClass().getResourceAsStream("/com/model2/mvc/resources/actionmapping.properties");
			in = getClass().getClassLoader().getResourceAsStream(resources);
			properties = new Properties();
			properties.load(in);
		}catch(Exception ex){
			System.out.println(ex);
			throw new RuntimeException("actionmapping.properties ���� �ε� ���� :"  + ex);
		}finally{
			if(in != null){
				try{ in.close(); } catch(Exception ex){}
			}
		}
	}
	
	//�ϳ��� instance�� ����
	public synchronized static RequestMapping getInstance(String resources){
		if(requestMapping == null){
			requestMapping = new RequestMapping(resources);
		}
		return requestMapping;
	}
	
	//��� �Ǻ��Ͽ� ��ɿ� ���� Action ��ȯ
	public Action getAction(String path){
		Action action = map.get(path);
		//action�� ���� �ϳ����� instanceȭ�Ͽ� map�� �����ϴ� ����
		//������ ������ action�� ����߸� ���� �����Ѵ�.
		if(action == null){
			String className = properties.getProperty(path);
			System.out.println("prop : " + properties);
			System.out.println("path : " + path);			
			System.out.println("className : " + className);
			className = className.trim();
			try{
				//������ Class�� �������� �����ǹǷ� Class.forName()�� �̿��Ͽ� ������ Class�� ������ ������
				//���� className�� �����Ǵ��� �ڵ带 �������ʿ䰡 ��������. 
				Class c = Class.forName(className);
				Object obj = c.newInstance();
				//������ obj�� validation check
				if(obj instanceof Action){
					map.put(path, (Action)obj);
					action = (Action)obj;
				}else{
					throw new ClassCastException("Class����ȯ�� ���� �߻�  ");
				}
			}catch(Exception ex){
				System.out.println(ex);
				throw new RuntimeException("Action������ ���ϴ� ���� ���� �߻� : " + ex);
			}
		}
		return action;
	}
}