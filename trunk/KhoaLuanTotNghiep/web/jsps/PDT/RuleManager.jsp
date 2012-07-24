<%-- 
    Document   : RuleManager
    Created on : 06-05-2012, 10:57:42
    Author     : LocNguyen
--%>
<%@page import="uit.cnpm02.dkhp.utilities.ClientValidate"%>
<%@page import="uit.cnpm02.dkhp.model.type.AccountType"%>
<%@page import="uit.cnpm02.dkhp.model.Rule"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // Validate Access role
    ClientValidate.validateAcess(AccountType.ADMIN, session, response);
    
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
                <div id="main-title">
                    Quản lý Qui định
                </div>
                <br /><br />
                
                
                <div class="range">
                    <h3><span id="btn-list-existed-rule" class="atag">
                            Qui định hiện tại
                    </span></h3>
                    <div id = "list-rules">
                        <table id="table-rules" class="general-table">
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
                                        <span onclick="edit(<%=i%>, '<%=r.getId()%>')"> <img src="../../imgs/icon/edit.png" title="Sửa" alt="Sửa"/> </span>
                                        - <span onclick="deleteRule('<%= r.getId()%>')"> <img src="../../imgs/icon/delete.png" title="Xóa" alt="Xóa" /> </span>
                                    </td>
                                </tr>
                                <%}
                           }%>

                        </table>
                    </div>
               </div>
               <%-- Add new rule --%>
               <div class="range">
                   <h3><span id="btn-add-new-rule" class="atag">
                            Thêm qui định mới
                    </span></h3>
                   <div id="add-rule" style="display: none;">
                        <br />
                        <table class="general-table" style="width:282px !important;">
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
                        <div class="button-1" style="float: left; margin-left: 25px;">
                            <span class="atag" onclick="addRule()" ><img src="../../imgs/check.png" />Thêm</span>
                        </div>
                        <div class="clear"> </div>
                        <div id="add-rule-resule" style="font-size: 11px; font-weight: bold;">
                        </div>
                    </div>
               </div>
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>

    <script src="../../javascripts/AjaxUtil.js"></script>
    <script type="text/javascript" src="../../javascripts/jquery-1.7.1.js"></script>
    <script  type = "text/javascript" >
        $("#btn-list-existed-rule").click(function () {
            $('#list-rules').slideToggle(500);
        });
        $("#btn-add-new-rule").click(function () {
            $('#add-rule').slideToggle(500);
        });
        
        
        var http = createRequestObject();
        
        function edit(index, ruleId) {
            var controlRange = document.getElementById("td-edit" + index);
            controlRange.innerHTML = "<span onclick=save(" + index 
                + ",'" + ruleId + "')><img src=\"../../imgs/icon/save.png\" title=\"Lưu\" alt=\"Lưu\" /></span>";
            
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
            var controleRangeValue = "<span onclick=edit(" + index 
                + ",'" + ruleId + "')><img src=\"../../imgs/icon/edit.png\" title=\"Sửa\" alt=\"Sửa\" /></span>" 
                + " - <span onclick=\"deleteRule('" + ruleId + "')\"> <img src=\"../../imgs/icon/delete.png\" title=\"Xóa\" alt=\"Xóa\" /> </span>";
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
