<%@page import="com.model2.mvc.service.purchase.vo.PurchaseVO"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>

<%
	PurchaseVO purchaseVO = (PurchaseVO)request.getAttribute("purchaseVO");
%>


<html>
<head>
<title>구매 정보 확인</title>
</head>

<body>

<form name="updatePurchase" action="/updatePurchaseView.do?tranNo=<%=purchaseVO.getTranNo() %>" method="post">

다음과 같이 구매가 되었습니다.

<table border=1>
	<tr>
		<td>물품번호</td>
		<td><%=purchaseVO.getPurchaseProd().getProdNo() %></td>
	</tr>
	<tr>
		<td>구매자아이디</td>
		<td><%=purchaseVO.getBuyer().getUserId() %></td>
	</tr>
	<tr>
		<td>구매방법</td>
		<td>
		
			<%if(purchaseVO.getPaymentOption().equals("1")){ %>
				현금구매
			<%}else if(purchaseVO.getPaymentOption().equals("2")){ %>
				신용구매
			<%} %>
		
		</td>
	</tr>
	<tr>
		<td>구매자이름</td>
		<td><%=purchaseVO.getReceiverName() %></td>
	</tr>
	<tr>
		<td>구매자연락처</td>
		<td><%=purchaseVO.getReceiverPhone() %></td>
	</tr>
	<tr>
		<td>구매자주소</td>
		<td><%=purchaseVO.getDlvyAddr() %></td>
	</tr>
		<tr>
		<td>구매요청사항</td>
		<td><%=purchaseVO.getDlvyRequest() %></td>
	</tr>
	<tr>
		<td>배송희망일자</td>
		<td><%=purchaseVO.getDlvyDate() %></td>
	</tr>
</table>
</form>

</body>
</html>