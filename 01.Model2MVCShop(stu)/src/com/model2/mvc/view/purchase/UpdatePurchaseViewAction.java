package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.purchase.vo.PurchaseVO;

public class UpdatePurchaseViewAction extends Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PurchaseService service = new PurchaseServiceImpl();
		
		PurchaseVO purchaseVO = service.getPurchase(Integer.parseInt(request.getParameter("tranNo")));
		
		if(purchaseVO.getDlvyDate() != null) {
			purchaseVO.setDlvyDate(purchaseVO.getDlvyDate().replaceAll("-", ""));
		}
		request.setAttribute("purchaseVO", purchaseVO);
		
		return "forward:/purchase/updatePurchaseView.jsp";
	}
}
