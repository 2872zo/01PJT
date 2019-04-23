package com.model2.mvc.framework;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.common.util.HttpUtil;


public class ActionServlet extends HttpServlet {
	
	private RequestMapping mapper;

	@Override
	public void init() throws ServletException {
		super.init();
		//metadata의 resource.properties 경로 저장
		String resources=getServletConfig().getInitParameter("resources");
		//resource.properties의 정보를 가지고있는 mapper instance 가져옴
		mapper=RequestMapping.getInstance(resources);
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) 
																									throws ServletException, IOException {
		//1. URI 파싱
		String url = request.getRequestURI();
		String contextPath = request.getContextPath();
		String path = url.substring(contextPath.length());
		System.out.println(path);
		
		try{
			//2. RequestMapping를 사용하여 선택한 기능을 수행할 Action 객체 호출
			Action action = mapper.getAction(path);
			action.setServletContext(getServletContext());
			
			//3. resultPage = 전송방식:jspPath의 format으로 기능실행후 결과 반환
			String resultPage=action.execute(request, response);
			String result=resultPage.substring(resultPage.indexOf(":")+1);
			
			//4. HttpUtil을 이용하여 전송기능 수행
			if(resultPage.startsWith("forward:"))
				HttpUtil.forward(request, response, result);
			else
				HttpUtil.redirect(response, result);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}