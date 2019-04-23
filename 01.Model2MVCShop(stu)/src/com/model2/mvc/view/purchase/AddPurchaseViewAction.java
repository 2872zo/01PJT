package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.product.vo.ProductVO;
import com.model2.mvc.service.purchase.vo.PurchaseVO;
import com.model2.mvc.service.user.vo.UserVO;

public class AddPurchaseViewAction extends Action{

	public AddPurchaseViewAction() {
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ProductService productService = new ProductServiceImpl();
		HttpSession session = request.getSession();
		
		
		PurchaseVO purchaseVO = new PurchaseVO();
		ProductVO productVO = productService.getProduct(Integer.parseInt(request.getParameter("prodNo")));
		UserVO userVO = (UserVO)session.getAttribute("user");
				
		//purchaseVO ¼³Á¤
		purchaseVO.setBuyer(userVO);
		purchaseVO.setPurchaseProd(productVO);
		
		request.setAttribute("purchaseVO", purchaseVO);
		
		return "forward:/purchase/addPurchaseView.jsp";
	}
}
