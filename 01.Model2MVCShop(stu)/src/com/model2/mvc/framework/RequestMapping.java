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
			//resource의 위치를 parameter로 받고있기에 동적으로 Runtime에서 inputstream 생성 ==> URI로 파일 경로 검색
			//비 종속적이 구조를 가지기 위해 file시스템이 아닌 package구조를 사용한다.
			//in = new FileInputStream("C:\\workspace\\01.Model2MVCShop(stu)\\src\\com\\model2\\mvc\\resources\\actionmapping.properties");
			//안됨in = new FileInputStream("src/com/model2/mvc/resources/actionmapping.properties");
			//안됨in = new FileInputStream("actionmapping.properties");
			//in = getClass().getResourceAsStream("/com/model2/mvc/resources/actionmapping.properties");
			in = getClass().getClassLoader().getResourceAsStream(resources);
			properties = new Properties();
			properties.load(in);
		}catch(Exception ex){
			System.out.println(ex);
			throw new RuntimeException("actionmapping.properties 파일 로딩 실패 :"  + ex);
		}finally{
			if(in != null){
				try{ in.close(); } catch(Exception ex){}
			}
		}
	}
	
	//하나의 instance만 만듦
	public synchronized static RequestMapping getInstance(String resources){
		if(requestMapping == null){
			requestMapping = new RequestMapping(resources);
		}
		return requestMapping;
	}
	
	//기능 판별하여 기능에 따른 Action 반환
	public Action getAction(String path){
		Action action = map.get(path);
		//action도 각기 하나씩만 instance화하여 map에 저장하는 구조
		//기존에 생성된 action이 없어야만 새로 생성한다.
		if(action == null){
			String className = properties.getProperty(path);
			System.out.println("prop : " + properties);
			System.out.println("path : " + path);			
			System.out.println("className : " + className);
			className = className.trim();
			try{
				//생성할 Class가 동적으로 결정되므로 Class.forName()을 이용하여 생성할 Class의 정보를 가져옴
				//따라서 className이 수정되더라도 코드를 변경할필요가 없어진다. 
				Class c = Class.forName(className);
				Object obj = c.newInstance();
				//생성된 obj의 validation check
				if(obj instanceof Action){
					map.put(path, (Action)obj);
					action = (Action)obj;
				}else{
					throw new ClassCastException("Class형변환시 오류 발생  ");
				}
			}catch(Exception ex){
				System.out.println(ex);
				throw new RuntimeException("Action정보를 구하는 도중 오류 발생 : " + ex);
			}
		}
		return action;
	}
}