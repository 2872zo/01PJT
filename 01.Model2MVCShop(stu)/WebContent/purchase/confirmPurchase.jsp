<%@page import="com.model2.mvc.service.purchase.vo.PurchaseVO"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>

<%
	PurchaseVO purchaseVO = (PurchaseVO)request.getAttribute("purchaseVO");
%>


<html>
<head>
<title>���� ���� Ȯ��</title>
</head>

<body>

<form name="updatePurchase" action="/updatePurchaseView.do?tranNo=<%=purchaseVO.getTranNo() %>" method="post">

������ ���� ���Ű� �Ǿ����ϴ�.

<table border=1>
	<tr>
		<td>��ǰ��ȣ</td>
		<td><%=purchaseVO.getPurchaseProd().getProdNo() %></td>
	</tr>
	<tr>
		<td>�����ھ��̵�</td>
		<td><%=purchaseVO.getBuyer().getUserId() %></td>
	</tr>
	<tr>
		<td>���Ź��</td>
		<td>
		
			<%if(purchaseVO.getPaymentOption().equals("1")){ %>
				���ݱ���
			<%}else if(purchaseVO.getPaymentOption().equals("2")){ %>
				�ſ뱸��
			<%} %>
		
		</td>
	</tr>
	<tr>
		<td>�������̸�</td>
		<td><%=purchaseVO.getReceiverName() %></td>
	</tr>
	<tr>
		<td>�����ڿ���ó</td>
		<td><%=purchaseVO.getReceiverPhone() %></td>
	</tr>
	<tr>
		<td>�������ּ�</td>
		<td><%=purchaseVO.getDlvyAddr() %></td>
	</tr>
		<tr>
		<td>���ſ�û����</td>
		<td><%=purchaseVO.getDlvyRequest() %></td>
	</tr>
	<tr>
		<td>����������</td>
		<td><%=purchaseVO.getDlvyDate() %></td>
	</tr>
</table>
</form>

</body>
</html>