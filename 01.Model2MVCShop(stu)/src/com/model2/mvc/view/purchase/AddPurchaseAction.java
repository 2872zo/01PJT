package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.product.vo.ProductVO;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.purchase.vo.PurchaseVO;
import com.model2.mvc.service.user.UserService;
import com.model2.mvc.service.user.impl.UserServiceImpl;
import com.model2.mvc.service.user.vo.UserVO;

public class AddPurchaseAction extends Action{

	public AddPurchaseAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PurchaseService purchaseService = new PurchaseServiceImpl();
		ProductService productService = new ProductServiceImpl();
		
		PurchaseVO purchaseVO = new PurchaseVO();
		ProductVO productVO = productService.getProduct(Integer.parseInt(request.getParameter("prodNo")));
		UserVO userVO = (UserVO) request.getSession().getAttribute("user");
		
		//purchaseVO 설정
		purchaseVO.setBuyer(userVO);
		purchaseVO.setPurchaseProd(productVO);
		purchaseVO.setPaymentOption(request.getParameter("paymentOption"));
		purchaseVO.setReceiverName(request.getParameter("receiverName"));
		purchaseVO.setReceiverPhone(request.getParameter("receiverPhone"));
		purchaseVO.setDlvyAddr(request.getParameter("receiverAddr"));
		purchaseVO.setDlvyRequest(request.getParameter("receiverRequest"));
		purchaseVO.setTranCode("1");
		purchaseVO.setDlvyDate(request.getParameter("receiverDate"));
		
		//실행
		purchaseService.addPurchase(purchaseVO);
		
		request.setAttribute("purchaseVO", purchaseVO);
		
		return "forward:/purchase/confirmPurchase.jsp";
	}

}
