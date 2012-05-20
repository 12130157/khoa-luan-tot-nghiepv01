<%-- 
    Document   : RuleManager
    Created on : 06-05-2012, 10:57:42
    Author     : LocNguyen
--%>
<%@page import="uit.cnpm02.dkhp.model.Rule"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    List<Rule> rules = (List<Rule>) session.getAttribute("rules");
%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản lý qui định</title>
        <style media="all" type="text/css">
            
        </style>
    </head>
    
    <body>
        <!--Div Wrapper-->
        <div id="wrapper">
            <%-- Menu --%>
            <%@include file="MenuPDT.jsp" %>
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <div id="title">
                    Quản lý Qui định
                </div>
                <hr/><hr/><br>
                
                <div id="add-rule-resule">
                </div>
                
                <b><u>Danh sách qui định hiện tại:</u></b>
                <div id = "list-rules">
                    <table id="table-rules" class="general-table" style="width: 750px !important">
                        <tr> 
                            <th> STT </th> 
                            <th> Mã </th> 
                            <th> Tên </th> 
                            <th> Giá trị </th> 
                            <th> </th>
                        </tr>
                        <%
                        if ((rules != null) && !rules.isEmpty()) {
                            for (int i = 0; i < rules.size(); i++) {
                                Rule r = rules.get(i);
                        %>
                            <tr> 
                                <td> <%= (i + 1) %> </td>
                                <td> <%= r.getId() %> </td>
                                <td id="td-description<%=i%>"> <%= r.getDescription() %> </td>
                                <td id="td-value<%=i%>"> 
                                    <%= r.getValue() %> 
                                 
                                </td>
                                <td id="td-edit<%=i%>">
                                    <%--<a href="#" onclick="update('<%= r.getId()%>', '<%= r.getValue()%>')"> Sửa </a>--%>
                                    <a href="#" onclick="edit(<%=i%>, '<%=r.getId()%>')"> Sửa </a>
                                    - <a href="#" onclick="deleteRule('<%= r.getId()%>')"> Xóa </a>
                                </td>
                            </tr>
                            <%}
                       }%>
                        
                    </table>
                </div>
                       
                   <div id="add-rule">
                       </br></br>
                    <b><u>Thêm qui định mới</u></b>
                    <table class="general-table" style="width:250px !important;">
                        <tr>
                            <td width="125px"> Mã qui định </td>
                            <td width="72px"> <input type="text" id="txt-new-rule-id"/> </td>
                        </tr>
                        <tr>
                            <td> Giá trị </td>
                            <td> <input type="text" id="txt-new-rule-value"/> </td>
                        </tr>
                        <tr>
                            <td> Mô tả </td>
                            <td> <input type="text" id="txt-new-rule-description"/> </td>
                        </tr>
                    </table>
                    <input type="button" value="Thêm" onclick="addRule()"/>
                </div>

            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>

    <script src="../../javascripts/AjaxUtil.js"></script>
    <script  type = "text/javascript" >
        var http = createRequestObject();
        
        function edit(index, ruleId) {
            var controlRange = document.getElementById("td-edit" + index);
            controlRange.innerHTML = "<a href=\"#\" onclick=save(" + index 
                + ",'" + ruleId + "')>Lưu</a>";
            
            var editRange = document.getElementById("td-value" + index);
            var currentValue = editRange.innerHTML;
            var editField = "<input type=\"text\" id=\"txt-value\" value=\"" + currentValue + "\"/>";
            
            var editDescription = document.getElementById("td-description" + index);
            var currentDescription = editDescription.innerHTML;
            var editDescroptionField = "<input type=\"text\" id=\"txt-description\" value=\"" + currentDescription + "\"/>";
            
            editRange.innerHTML = editField;
            editDescription.innerHTML = editDescroptionField;
        }
        
        function save(index, ruleId) {
            var editValueRange = document.getElementById("td-value" + index);
            var newValue = document.getElementById("txt-value").value;
            editValueRange.innerHTML = newValue;
            
            var descriptionRange = document.getElementById("td-description" + index);
            var newDescription = document.getElementById("txt-description").value;
            descriptionRange.innerHTML = newDescription;
            
            if(http){
                http.open("GET", "../../ManageRuleController?action=edit&id=" 
                    + ruleId + "&new_value=" + newValue 
                    + "&new_description=" + newDescription
                    ,true);
                http.onreadystatechange = editResponeHandler;
                http.send(null);
            }
            
            var controlRange = document.getElementById("td-edit" + index);
            var controleRangeValue = "<a href=\"#\" onclick=edit(" + index 
                + ",'" + ruleId + "')>Sửa</a>" 
                + " - <a href=\"#\" onclick=\"deleteRule('" + ruleId + "')\"> Xóa </a>";
            controlRange.innerHTML = controleRangeValue;
        }

        function addRule() {
            var id = "";
            var value = "";
            var description = "";
            try {
                id = document.getElementById("txt-new-rule-id").value;
                value = document.getElementById("txt-new-rule-value").value;
                description = document.getElementById("txt-new-rule-description").value;
            } catch(err) {
                alert("Lỗi");
                return;
            }
            
            if ((id.length <= 0) 
                    || (value.length <= 0) 
                    || (description.length <= 0)) {
                    alert("Vui lòng nhập đầy đủ thông tin.");
                    return;
            }
                
            if(http){
                http.open("GET", "../../ManageRuleController?action=add&id=" 
                    + id + "&value=" + value + "&description=" + description ,true);
                http.onreadystatechange = addRoleResponeHandler;
                http.send(null);
            }
        }
        
        function deleteRule(ruleId) {
            if(http){
                http.open("GET", "../../ManageRuleController?action=delete&id=" 
                    + ruleId, true);
                http.onreadystatechange = listRoleResponeHandler;
                http.send(null);
            }

        }
        
        function listRoleResponeHandler() {
            if(http.readyState == 4 && http.status == 200){
                var detail=document.getElementById("list-rules");
                detail.innerHTML=http.responseText;
            }
        }

        function updateRules(tableId) {
            if(http){
                http.open("GET", "" ,true);
                http.onreadystatechange = responeHandler;
                http.send(null);
            }
        }
        
        function responeHandler() {
            if(http.readyState == 4 && http.status == 200){
                var detail=document.getElementById("some-id");
                detail.innerHTML=http.responseText;
            }
        }
        
        function addRoleResponeHandler() {
            if(http.readyState == 4 && http.status == 200){
                var detail=document.getElementById("add-rule-resule");
                detail.innerHTML=http.responseText;
            }
        }
        
        function editResponeHandler() {
            /*if(http.readyState == 4 && http.status == 200){
                var detail=document.getElementById("add-rule-resule");
                detail.innerHTML=http.responseText;
            }*/
        }
        
    </script>
</html>
