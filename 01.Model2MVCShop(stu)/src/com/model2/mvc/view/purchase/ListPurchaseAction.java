package com.model2.mvc.view.purchase;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.framework.Action;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.user.vo.UserVO;

public class ListPurchaseAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PurchaseService service = new PurchaseServiceImpl();
		UserVO userVO = (UserVO) (request.getSession().getAttribute("user"));

		SearchVO searchVO = new SearchVO();

		int page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		searchVO.setPage(page);
		searchVO.setSearchCondition("searchCondition");
		searchVO.setSearchKeyword(request.getParameter("searchKeyword"));

		int pageUnit = Integer.parseInt(getServletContext().getInitParameter("pageSize"));
		searchVO.setPageUnit(pageUnit);

		HashMap<String, Object> map = service.getPurchaseList(searchVO, userVO.getUserId());
		request.setAttribute("map", map);

		// 전체 페이지 구하기
		int totalUnit = (Integer) map.get("count");
		int totalPage = 0;
		if (totalUnit > 0) {
			totalPage = totalUnit / searchVO.getPageUnit();
			if (totalUnit % searchVO.getPageUnit() > 0)
				totalPage += 1;
		}
		// 출력 페이지 구하기
		int showPageSize = 2;
		if (totalPage <= showPageSize * 2 + 1) {
			request.setAttribute("firstPage", 1);
			request.setAttribute("lastPage", totalPage);
		} else {
			int firstPage = searchVO.getPage() - showPageSize;
			int lastPage = searchVO.getPage() + showPageSize;

			if (firstPage < 1) {
				lastPage += Math.abs(firstPage) + 1;
				firstPage = 1;
			}

			if (lastPage > totalPage) {
				firstPage -= Math.abs(totalPage - Math.abs(lastPage));
				lastPage = totalPage;
			}
			request.setAttribute("firstPage", firstPage);
			request.setAttribute("lastPage", lastPage);
		}

		return "forward:/purchase/listPurchase.jsp";
	}
}
