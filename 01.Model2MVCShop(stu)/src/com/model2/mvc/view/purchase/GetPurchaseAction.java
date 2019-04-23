package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.purchase.vo.PurchaseVO;

public class GetPurchaseAction extends Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PurchaseService service = new PurchaseServiceImpl();
		
		PurchaseVO purchaseVO = service.getPurchase(Integer.parseInt(request.getParameter("tranNo")));
		request.setAttribute("purchaseVO", purchaseVO);
		System.out.println("getPurchaseActionÀÇ getTranCode°ª : "+purchaseVO.getTranCode());
		return "forward:/purchase/getPurchase.jsp";
	}
}
