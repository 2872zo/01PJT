<%@page import="java.net.URLEncoder"%>
<%@page import="com.model2.mvc.service.user.vo.UserVO"%>
<%@page import="com.model2.mvc.service.product.vo.ProductVO"%>
<%@page import="com.model2.mvc.common.SearchVO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@ page language="java"%>
<%@ page contentType="text/html; charset=EUC-KR"%>
<%@ page pageEncoding="EUC-KR"%>

<%
	SearchVO searchVO = (SearchVO)request.getAttribute("searchVO");
	System.out.println("jspSearchVO : " + searchVO);

	Map<String, Object> map = (HashMap<String, Object>)request.getAttribute("map");
	List<ProductVO> list = (ArrayList<ProductVO>)map.get("list");
	UserVO userVO = (UserVO)session.getAttribute("user");
	String menu = request.getParameter("menu");
	int totalUnit = (Integer)map.get("count");
	
	int firstPage = (Integer)request.getAttribute("firstPage");
	int lastPage = (Integer)request.getAttribute("lastPage");
	int totalPage = (Integer)request.getAttribute("totalPage");
%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>��ǰ �����ȸ</title>

<link rel="stylesheet" href="/css/admin.css" type="text/css">

<script type="text/javascript">
<!--
function fncGetProductList(){
	var searchCondition = document.detailForm.searchCondition.value;
	var searchKeyword = document.detailForm.searchKeyword.value
	
	if(searchKeyword != null){
		if(searchCondition != 1){
			if(isNaN(searchKeyword)){
				alert("���ڸ� �����մϴ�.")
				return;
			} 
		}
	}
	
	document.detailForm.submit();
}
-->



</script>
</head>

<body bgcolor="#ffffff" text="#000000">

<div style="width:98%; margin-left:10px;">

<form name="detailForm" action="<%=request.getParameter("menu").equals("search")?"/listProduct.do?menu=search":"/listProduct.do?menu=manage" %>" method="post">

<table width="100%" height="37" border="0" cellpadding="0"	cellspacing="0">
	<tr>
		<td width="15" height="37">
			<img src="/images/ct_ttl_img01.gif" width="15" height="37"/>
		</td>
		<td background="/images/ct_ttl_img02.gif" width="100%" style="padding-left:10px;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="93%" class="ct_ttl01">
					
						<%if(menu.equals("search")) {%>
							��ǰ �����ȸ
						<%}else if(menu.equals("manage")){ %>
							��ǰ ����
						<%} %>
					
					</td>
				</tr>
			</table>
		</td>
		<td width="12" height="37">
			<img src="/images/ct_ttl_img03.gif" width="12" height="37"/>
		</td>
	</tr>
</table>


<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
		
		<td align="right">
			<select name="searchCondition" class="ct_input_g" style="width:80px" id="searchCondition">
				<option value="0" <%if(searchVO.getSearchCondition()!=null&&searchVO.getSearchCondition().equals("0")){%>selected="selected"<%} %>>��ǰ��ȣ</option>
				<option value="1" <%if(searchVO.getSearchCondition()!=null&&searchVO.getSearchCondition().equals("1")){%>selected="selected"<%} %>>��ǰ��</option>
				<option value="2" <%if(searchVO.getSearchCondition()!=null&&searchVO.getSearchCondition().equals("2")){%>selected="selected"<%} %>>��ǰ����</option>
			</select>
			<input type="text" name="searchKeyword" id="searchKeyword" class="ct_input_g" style="width:200px; height:19px" value="<%if(searchVO.getSearchKeyword()!=null){ %><%=searchVO.getSearchKeyword()%><%} %>"/>
		</td>
	
		
		<td align="right" width="70">
			<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="17" height="23">
						<img src="/images/ct_btnbg01.gif" width="17" height="23">
					</td>
					<td background="/images/ct_btnbg02.gif" class="ct_btn01" style="padding-top:3px;">
						<a href="javascript:fncGetProductList();">�˻�</a>
					</td>
					<td width="14" height="23">
						<img src="/images/ct_btnbg03.gif" width="14" height="23">
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>


<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
		<td colspan="11" >��ü <%=totalUnit%> �Ǽ�, ���� <%=searchVO.getPage()%> ������</td>
	</tr>
	<tr>
		<td class="ct_list_b" width="100">No</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">��ǰ��</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">����</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">�����</td>	
		<td class="ct_line02"></td>
		<td class="ct_list_b">�������</td>	
	</tr>
	<tr>
		<td colspan="11" bgcolor="808285" height="1"></td>
	</tr>
		
	<%for(int i=0;i<list.size();i++){%>
	<tr class="ct_list_pop">
	
		<td align="center"><%=i+1%></td>
		<td></td>
				<td align="left"><a href="/getProduct.do?prodNo=<%=list.get(i).getProdNo()%>&menu=<%=menu%>"><%=list.get(i).getProdName()%></a></td>
		<td></td>
		<td align="left"><%=list.get(i).getPrice()%></td>
		<td></td>
		<td align="left"><%=list.get(i).getRegDate()%></td>
		<td></td>
		<td align="left">
		
		<%if(list.get(i).getProTranCode()!=null){%>
			<%if(list.get(i).getProTranCode().equals("1")){if(userVO != null && userVO.getRole().equals("admin")){%>����غ���&nbsp;<%if (menu.equals("manage")){ %><a href="/updateTranCodeByProd.do?page=<%=searchVO.getPage() %>&prodNo=<%=list.get(i).getProdNo()%>&tranCode=2<%if(searchVO.getSearchCondition() != null){%>&searchCondition=<%=searchVO.getSearchCondition() %>&searchKeyword=<%=searchVO.getSearchKeyword() %><%}%>">��ǰ�߼�</a><%}}else{ %>������<%}} %>
			<%if(list.get(i).getProTranCode().equals("2")){if(userVO != null && userVO.getRole().equals("admin")){%>����� <%}else{ %>������<%}} %>
			<%if(list.get(i).getProTranCode().equals("3")){if(userVO != null && userVO.getRole().equals("admin")){%>�ŷ��Ϸ� <%}else{ %>������<%}} %>
		<%}else{ %>
			�Ǹ���
		<%} %>
		
		</td>	
	</tr>	
	<tr>
		<td colspan="11" bgcolor="D6D7D6" height="1"></td>
	</tr>	
	<%} %>
	
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
		<td align="center">
		<%if(firstPage != 1){ %>
			<a href="/listProduct.do?page=<%=firstPage-1%>&menu=<%=menu%>
				<%if(searchVO.getSearchKeyword()!=null){%>
					&searchCondition=<%=searchVO.getSearchCondition()%>
					&searchKeyword=<%=searchVO.getSearchKeyword()%>
				<%}%>"
			>
			��
			</a>
		<%}else{%>
			��
		<%}%>
		
		<%for(int i = firstPage; i <= lastPage; i++){%>
			<%if(i != searchVO.getPage()){%>
				<a href="/listProduct.do?page=<%=i%>&menu=<%=menu%>
					<%if(searchVO.getSearchKeyword()!=null){%>
						&searchCondition=<%=searchVO.getSearchCondition()%>
						&searchKeyword=<%=searchVO.getSearchKeyword()%>
					<%}%>"
				>
				<%=i%>
				</a>
			<%}else{%>
				<font style="font-weight: bold">
					<%=i%>
				</font>
			<%}%>
		<%}%>
		
		<%if(lastPage != totalPage){ %>
			<a href="/listProduct.do?page=<%=lastPage+1%>&menu=<%=menu%>
				<%if(searchVO.getSearchKeyword()!=null){%>
					&searchCondition=<%=searchVO.getSearchCondition()%>
					&searchKeyword=<%=searchVO.getSearchKeyword()%>
				<%}%>"
			>
			��
			</a>
		<%}else{%>
			��
		<%}%>
		
		</td>
	</tr>
</table>
<!--  ������ Navigator �� -->

</form>

</div>
</body>
</html>
