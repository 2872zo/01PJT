package com.model2.mvc.service.product.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.product.vo.ProductVO;

public class ProductDAO {

	public ProductDAO() {
	}

	public void insertProduct(ProductVO productVO) throws Exception {
		// 1.DBUtil을 이용하여 DB연결
		Connection con = DBUtil.getConnection();

		// 2.DB에 보낼 DML 작성 및 값 설정
		String sql = "INSERT INTO Product VALUES(seq_product_prod_no.NEXTVAL,?,?,?,?,?,sysdate)";
		PreparedStatement stmt = con.prepareStatement(sql);
		System.out.println(productVO.getManuDate());
		stmt.setString(1, productVO.getProdName());
		stmt.setString(2, productVO.getProdDetail());
		stmt.setString(3, productVO.getManuDate().replaceAll("\\-", ""));
		stmt.setInt(4, productVO.getPrice());
		stmt.setString(5, productVO.getFileName());

		// 3.실행
		System.out.println("실행 : " + stmt.executeUpdate());

		con.close();
	}

	public ProductVO findProduct(int prodNo) throws Exception {
		// 1.DBUtil을 이용하여 DB연결
		Connection con = DBUtil.getConnection();

		// 2.DB에 보낼 DML 작성 및 값 설정
		String sql = "SELECT"
				+ " p.prod_no, p.prod_name, p.prod_detail, p.manufacture_day, p.price, p.image_file, p.reg_date, t.tran_status_code"
				+ " FROM product p, transaction t"
				+ " WHERE p.prod_no = t.prod_no(+)"
				+ " AND p.prod_no = ?";
		PreparedStatement stmt = con.prepareStatement(sql);

		stmt.setInt(1, prodNo);
		ResultSet rs = stmt.executeQuery();

		// 3. 결과값을 ProductVO에 담아서 전달
		ProductVO productVO = null;
		if (rs.next()) {
			productVO = new ProductVO();
			productVO.setProdNo(rs.getInt("prod_no"));
			productVO.setProdName(rs.getString("prod_name"));
			productVO.setProdDetail(rs.getString("prod_detail"));
			productVO.setManuDate(rs.getString("manufacture_day"));
			productVO.setPrice(rs.getInt("price"));
			productVO.setFileName(rs.getString("image_file"));
			productVO.setRegDate(rs.getDate("reg_date"));
			productVO.setProTranCode(rs.getString("tran_status_code"));
		}

		con.close();

		return productVO;
	}

	public HashMap<String, Object> getProductList(SearchVO searchVO) throws Exception {
		System.out.println("getProductList 시작");
		// 1.DBUtil을 이용하여 DB연결
		Connection con = DBUtil.getConnection();

		// 2.DB에 보낼 DML 작성 및 값 설정
		
		//전체 줄 수 확인
//		String = "SELECT"
//		+"COUNT(*)"
//		+"FROM"
//		+"(SELECT  "
//		+"	ROWNUM AS rn, p.*, t.tran_status_code"
//		+"	FROM product p, transaction t"
//		+"	WHERE p.prod_no = t.prod_no(+)";
//		if (searchVO.getSearchCondition() != null) {
//			if (searchVO.getSearchCondition().equals("0")) {
//				sql += " AND p.prod_no = '" + searchVO.getSearchKeyword() + "'";
//			} else if (searchVO.getSearchCondition().equals("1")) {
//				sql += " AND p.prod_name like '%" + searchVO.getSearchKeyword() + "%'";
//			}else if (searchVO.getSearchCondition().equals("2")) {
//				sql += " AND p.price = '" + searchVO.getSearchKeyword() + "'";
//			}
//		}
		
		//필요한 위치만 골라옴
//		String = "SELECT"
//		+"*"
//		+"FROM"
//		+"(SELECT  "
//		+"	ROWNUM AS rn, p.*, t.tran_status_code"
//		+"	FROM product p, transaction t"
//		+"	WHERE p.prod_no = t.prod_no(+)";
//		if (searchVO.getSearchCondition() != null) {
//			if (searchVO.getSearchCondition().equals("0")) {
//				sql += " AND p.prod_no = '" + searchVO.getSearchKeyword() + "'";
//			} else if (searchVO.getSearchCondition().equals("1")) {
//				sql += " AND p.prod_name like '%" + searchVO.getSearchKeyword() + "%'";
//			}else if (searchVO.getSearchCondition().equals("2")) {
//				sql += " AND p.price = '" + searchVO.getSearchKeyword() + "'";
//			}
//		}
//		sql += ")WHERE rn BETWEEN searchVO.getPage()*searchVO.getPageUnit()-searchVO.getPageUnit()+1 AND searchVO.getPage()*searchVO.getPageUnit()+1";

		String sql = "SELECT "
				+ " p.*, t.tran_status_code"
				+ " FROM product p, transaction t"
				+ " WHERE p.prod_no = t.prod_no(+)";
		if (searchVO.getSearchCondition() != null) {
			if (searchVO.getSearchCondition().equals("0")) {
				sql += " AND p.prod_no = '" + searchVO.getSearchKeyword() + "'";
			} else if (searchVO.getSearchCondition().equals("1")) {
				sql += " AND p.prod_name like '%" + searchVO.getSearchKeyword() + "%'";
			}else if (searchVO.getSearchCondition().equals("2")) {
				sql += " AND p.price = '" + searchVO.getSearchKeyword() + "'";
			}
		}
		PreparedStatement stmt = con.prepareStatement(sql, 
				ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_UPDATABLE);

		ResultSet rs = stmt.executeQuery();

		//3. 총 Row수 구함
		rs.last();
		int totalUnit = rs.getRow();
		System.out.println("상품 총 줄수 : " + totalUnit);

		//4. 반환할 map에 총 Row수를 저장
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("count", new Integer(totalUnit));
		
		//5. 커서를 목표 페이지에 맞게 이동시킴
		rs.absolute(searchVO.getPage() * searchVO.getPageUnit() - searchVO.getPageUnit() + 1);
		System.out.println("searchVO.getPage():" + searchVO.getPage());
		System.out.println("searchVO.getPageUnit():" + searchVO.getPageUnit());

		//목표 page에 출력하기위해 pageUnit만큼 list에 ProductVO저장
		List<ProductVO> list = new ArrayList<ProductVO>();
		if (totalUnit > 0) {
			for (int i = 0; i < searchVO.getPageUnit(); i++) {
				ProductVO productVO = new ProductVO();
				productVO.setProdNo(rs.getInt("prod_no"));
				productVO.setProdName(rs.getString("prod_name"));
				productVO.setProdDetail(rs.getString("prod_detail"));
				productVO.setManuDate(rs.getString("manufacture_day"));
				productVO.setPrice(rs.getInt("price"));
				productVO.setFileName(rs.getString("image_file"));
				productVO.setRegDate(rs.getDate("reg_date"));
				productVO.setProTranCode(rs.getString("tran_status_code"));
				list.add(productVO);
				//목표 page의 pageUnit만큼보다 DB에 저장된 개수가 적을 경우
				if (!rs.next())
					break;
			}
		}
		System.out.println("list.size() : " + list.size());
		map.put("list", list);
		System.out.println("map.size() : " + map.size());

		con.close();

		return map;
	}

	public void updateProduct(ProductVO productVO) throws Exception {
		// 1.DBUtil을 이용하여 DB연결
		Connection con = DBUtil.getConnection();

		// 2.DB에 보낼 DML 작성 및 값 설정
		String sql = "UPDATE product set prod_name = ?, prod_detail = ?, manufacture_day =?, price = ?, image_file = ? WHERE prod_no = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, productVO.getProdName());
		stmt.setString(2, productVO.getProdDetail());
		stmt.setString(3, productVO.getManuDate());
		stmt.setInt(4, productVO.getPrice());
		stmt.setString(5, productVO.getFileName());
		stmt.setInt(6, productVO.getProdNo());

		stmt.executeUpdate();
		
		con.close();
	}
}
