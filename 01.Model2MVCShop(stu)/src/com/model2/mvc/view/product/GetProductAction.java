package com.model2.mvc.view.product;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.product.vo.ProductVO;

public class GetProductAction extends Action {

	public GetProductAction() {
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String targetURI = null;
		int prodNo = Integer.parseInt(request.getParameter("prodNo"));

		ProductService service = new ProductServiceImpl();
		ProductVO productVO = service.getProduct(prodNo);

		System.out.println(request.getParameter("menu"));
		request.setAttribute("productVO", productVO);

		if (request.getParameter("menu").equals("search")) {
			targetURI = "forward:/product/getProduct.jsp";
		} else if (request.getParameter("menu").equals("manage")) {
			targetURI = "forward:/updateProductView.do";
		}

		String history = null;
		Cookie[] cookies = request.getCookies();
		Cookie cookie = null;
		if (cookies != null && cookies.length > 0) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie tempCookie = cookies[i];
				if (tempCookie.getName().equals("history")) {
					history = tempCookie.getValue();
				}
			}
			boolean isEqual = false;
			if (history != null) {
				if(history.indexOf(String.valueOf(productVO.getProdNo())) != -1) {
					isEqual = true;
				}
				if (!isEqual) {
					cookie = new Cookie("history", history + "," + String.valueOf(productVO.getProdNo()));
				}
			} else {
				history = String.valueOf(productVO.getProdNo());
				cookie = new Cookie("history", history);
			}
			
			if(cookie != null) {
				response.addCookie(cookie);
			}
		}
		return targetURI;
	}

}
