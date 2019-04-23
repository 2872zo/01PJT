package com.model2.mvc.view.product;

import java.net.URLDecoder;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.common.util.HttpUtil;
import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;

public class ListProductAction extends Action {

	public ListProductAction() {
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SearchVO searchVO = new SearchVO();

		int page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}

		searchVO.setPage(page);
		searchVO.setSearchCondition(request.getParameter("searchCondition"));
		if (request.getParameter("searchKeyword") != null) {
			if (request.getMethod().equals("GET")) {
				searchVO.setSearchKeyword(HttpUtil.convertKo(request.getParameter("searchKeyword")));
			} else {
				searchVO.setSearchKeyword(request.getParameter("searchKeyword"));
			}
		}

		int pageUnit = Integer.parseInt(getServletContext().getInitParameter("pageSize"));
		searchVO.setPageUnit(pageUnit);

		System.out.println(searchVO);

		ProductService service = new ProductServiceImpl();
		HashMap<String, Object> map = service.getProductList(searchVO);

		request.setAttribute("map", map);
		request.setAttribute("searchVO", searchVO);

		// 전체 페이지 구하기
		int totalUnit = (Integer) map.get("count");
		int totalPage = 0;
		if (totalUnit > 0) {
			totalPage = totalUnit / searchVO.getPageUnit();
			if (totalUnit % searchVO.getPageUnit() > 0)
				totalPage += 1;
		}

		// 출력 페이지 구하기
		int pageShowSize = 5;
		int firstPage = ((searchVO.getPage() - 1) / pageShowSize) * pageShowSize + 1;
		int lastPage = firstPage + pageShowSize - 1;
		if(lastPage > totalPage) {
			lastPage = totalPage;
		}
		request.setAttribute("totalPage", totalPage);
		request.setAttribute("firstPage", firstPage);
		request.setAttribute("lastPage", lastPage);

		return "forward:/product/listProduct.jsp";
	}

}
