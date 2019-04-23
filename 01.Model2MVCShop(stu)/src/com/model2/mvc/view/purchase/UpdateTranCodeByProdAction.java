package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.purchase.vo.PurchaseVO;

public class UpdateTranCodeByProdAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PurchaseService purchaseService = new PurchaseServiceImpl();

		PurchaseVO purchaseVO = purchaseService.getPurchase2(Integer.parseInt(request.getParameter("prodNo")));
		purchaseVO.setTranCode(request.getParameter("tranCode"));

		purchaseService.updateTranCode(purchaseVO);

		String targetURI ="redirect:/listProduct.do?page=" + request.getParameter("page") + "&menu=" + request.getParameter("menu");
		if(request.getParameter("searchCondition")!=null) {
			targetURI += "&searchCondition=" + request.getParameter("searchCondition");
			targetURI += "&searchKeyword=" + request.getParameter("searchKeyword");
		}
		return  targetURI;
	}

}
