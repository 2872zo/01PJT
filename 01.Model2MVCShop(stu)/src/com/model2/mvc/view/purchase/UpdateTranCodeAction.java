package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.purchase.vo.PurchaseVO;

public class UpdateTranCodeAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PurchaseService purchaseService = new PurchaseServiceImpl();

		PurchaseVO purchaseVO = purchaseService.getPurchase(Integer.parseInt(request.getParameter("tranNo")));
		purchaseVO.setTranCode(request.getParameter("tranCode"));

		purchaseService.updateTranCode(purchaseVO);

		return "redirect:/listPurchase.do?page="+request.getParameter("page");
	}

}
