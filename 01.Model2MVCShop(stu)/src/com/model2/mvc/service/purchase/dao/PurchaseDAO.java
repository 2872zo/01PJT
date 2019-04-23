package com.model2.mvc.service.purchase.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.product.vo.ProductVO;
import com.model2.mvc.service.purchase.vo.PurchaseVO;
import com.model2.mvc.service.user.UserService;
import com.model2.mvc.service.user.impl.UserServiceImpl;
import com.model2.mvc.service.user.vo.UserVO;

public class PurchaseDAO {

	public PurchaseDAO() {
	}

	// 구매 정보 상세조회
	public PurchaseVO findPurchase(int tranNo) throws Exception {
		Connection con = DBUtil.getConnection();

		String sql = "SELECT * FROM transaction WHERE tran_no = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, tranNo);

		ResultSet rs = stmt.executeQuery();
		PurchaseVO purchaseVO = null;
		if (rs.next()) {
			UserService userService = new UserServiceImpl();
			UserVO userVO = userService.getUser(rs.getString("buyer_id"));
			ProductService productService = new ProductServiceImpl();
			ProductVO productVO = productService.getProduct(rs.getInt("prod_no"));

			purchaseVO = new PurchaseVO();

			purchaseVO.setTranNo(tranNo);
			purchaseVO.setPurchaseProd(productVO);
			purchaseVO.setBuyer(userVO);
			purchaseVO.setPaymentOption(rs.getString("payment_option"));
			purchaseVO.setReceiverName(rs.getString("receiver_name"));
			purchaseVO.setReceiverPhone(rs.getString("receiver_phone"));
			purchaseVO.setDlvyAddr(rs.getString("demailaddr"));// dlvy_addr
			purchaseVO.setDlvyRequest(rs.getString("dlvy_request"));
			purchaseVO.setTranCode(rs.getString("tran_status_code"));
			purchaseVO.setDlvyDate((rs.getString("dlvy_date") != null)? rs.getString("dlvy_date").substring(0, 10):rs.getString("dlvy_date"));
			purchaseVO.setOrderDate(rs.getDate("order_data"));// order_date
		}

		con.close();

		return purchaseVO;
	}

	public PurchaseVO findPurchase2(int prodNo) throws Exception {
		Connection con = DBUtil.getConnection();

		String sql = "SELECT * FROM transaction WHERE prod_no = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, prodNo);

		ResultSet rs = stmt.executeQuery();
		PurchaseVO purchaseVO = null;
		if (rs.next()) {
			UserService userService = new UserServiceImpl();
			UserVO userVO = userService.getUser(rs.getString("buyer_id"));
			ProductService productService = new ProductServiceImpl();
			ProductVO productVO = productService.getProduct(prodNo);

			purchaseVO = new PurchaseVO();
			purchaseVO.setTranNo(rs.getInt("tran_no"));
			purchaseVO.setPurchaseProd(productVO);
			purchaseVO.setBuyer(userVO);
			purchaseVO.setPaymentOption(rs.getString("payment_option"));
			purchaseVO.setReceiverName(rs.getString("receiver_name"));
			purchaseVO.setReceiverPhone(rs.getString("receiver_phone"));
			purchaseVO.setDlvyAddr(rs.getString("demailaddr"));// dlvy_addr
			purchaseVO.setDlvyRequest(rs.getString("dlvy_request"));
			purchaseVO.setDlvyDate((rs.getString("dlvy_date") != null)? rs.getString("dlvy_date").substring(0, 10):rs.getString("dlvy_date"));
			purchaseVO.setOrderDate(rs.getDate("order_data"));// order_date
		}

		con.close();

		return purchaseVO;
	}

	// 구매 목록 조회
	public HashMap<String, Object> getPurchaseList(SearchVO searchVO, String buyerId) throws Exception {
		System.out.println("::getPurchaseList 시작");
		Connection con = DBUtil.getConnection();

		String sql = "SELECT * FROM transaction WHERE buyer_id = ?";
		PreparedStatement stmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		stmt.setString(1, buyerId);

		ResultSet rs = stmt.executeQuery();

		rs.last();
		int totalUnit = rs.getRow();
		System.out.println("구매목록  : " + totalUnit);

		rs.absolute(searchVO.getPage() * searchVO.getPageUnit() - searchVO.getPageUnit() + 1);

		List<PurchaseVO> list = new ArrayList<PurchaseVO>();
		PurchaseVO purchaseVO = null;
		if (totalUnit != 0) {
			for (int i = 0; i < searchVO.getPageUnit(); i++) {
				UserService userService = new UserServiceImpl();
				UserVO userVO = userService.getUser(rs.getString("buyer_id"));
				ProductService productService = new ProductServiceImpl();
				ProductVO productVO = productService.getProduct(rs.getInt("prod_no"));

				purchaseVO = new PurchaseVO();
				purchaseVO.setTranNo(rs.getInt("tran_no"));
				purchaseVO.setPurchaseProd(productVO);
				purchaseVO.setBuyer(userVO);
				purchaseVO.setPaymentOption(rs.getString("payment_option"));
				purchaseVO.setReceiverName(rs.getString("receiver_name"));
				purchaseVO.setReceiverPhone(rs.getString("receiver_phone"));
				purchaseVO.setDlvyAddr(rs.getString("demailaddr"));// dlvy_addr
				purchaseVO.setDlvyRequest(rs.getString("dlvy_request"));
				purchaseVO.setTranCode(rs.getString("tran_status_code"));
				purchaseVO.setDlvyDate((rs.getString("dlvy_date") != null)? rs.getString("dlvy_date").substring(0, 10):rs.getString("dlvy_date"));
				purchaseVO.setOrderDate(rs.getDate("order_data"));
				list.add(purchaseVO);
				
				if (!rs.next()) {
					break;
				}
			}
			con.close();
		}

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("searchVO", searchVO);
		map.put("list", list);
		map.put("count", totalUnit);
		
		con.close();
			
		return map;		
	}

	// 판매목록 보기
	public HashMap<String, Object> getSaleList(SearchVO searchVO) throws Exception {

		return null;
	}

	// 구매
	public void insertPurchase(PurchaseVO purchaseVO) throws Exception {
		System.out.println("::insertPurchase 시작");
		Connection con = DBUtil.getConnection();
		String sql = "INSERT INTO transaction VALUES(seq_transaction_tran_no.NEXTVAL, ?,?,?,?,?,?,?,?,sysdate,?)";

		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, purchaseVO.getPurchaseProd().getProdNo());
		stmt.setString(2, purchaseVO.getBuyer().getUserId());
		stmt.setString(3, purchaseVO.getPaymentOption());
		stmt.setString(4, purchaseVO.getReceiverName());
		stmt.setString(5, purchaseVO.getReceiverPhone());
		stmt.setString(6, purchaseVO.getDlvyAddr());
		stmt.setString(7, purchaseVO.getDlvyRequest());
		stmt.setString(8, purchaseVO.getTranCode());
		stmt.setString(9, purchaseVO.getDlvyDate());

		System.out.println("insertPurchase : " + stmt.executeUpdate());
		
		con.close();
	}

	// 구매 정보 수정
	public void updatePurchase(PurchaseVO purchaseVO) throws Exception {
		System.out.println("::updatePurchase 시작");
		Connection con = DBUtil.getConnection();

		String sql = "UPDATE transaction" 
				+ " SET"
				+ " payment_option = ?, receiver_name = ?, receiver_phone = ?, demailaddr = ?,"
				+ " dlvy_request = ?, dlvy_date = ?"
				+ " WHERE tran_no = ?";
		PreparedStatement stmt = con.prepareStatement(sql);

		stmt.setString(1, purchaseVO.getPaymentOption());
		stmt.setString(2, purchaseVO.getReceiverName());
		stmt.setString(3, purchaseVO.getReceiverPhone());
		stmt.setString(4, purchaseVO.getDlvyAddr());
		stmt.setString(5, purchaseVO.getDlvyRequest());
		stmt.setString(6, purchaseVO.getDlvyDate());
		stmt.setInt(7, purchaseVO.getTranNo());

		System.out.println("uptdate중 purchaseVO : " + purchaseVO);
		
		System.out.println("UpdatePurchase로 변경된 줄 수 : " + stmt.executeUpdate());

		con.close();
	}

	// 판매 상태 코드 수정
	public void updateTranCode(PurchaseVO purchaseVO) throws Exception {
		Connection con = DBUtil.getConnection();

		String sql = "UPDATE transaction SET tran_status_code = ? WHERE tran_no = ?";

		PreparedStatement stmt = con.prepareStatement(sql);

		stmt.setString(1, purchaseVO.getTranCode());
		stmt.setInt(2, purchaseVO.getTranNo());

		System.out.println("Update로 변경된 줄 수 : " + stmt.executeUpdate());

		con.close();
	}
}
