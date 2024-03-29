<%-- 
    Document   : AddSubject
    Created on : 11-11-2011, 23:45:21
    Author     : LocNguyen
--%>
<%@page import="uit.cnpm02.dkhp.model.type.AccountType"%>
<%@page import="uit.cnpm02.dkhp.utilities.ClientValidate"%>
<%@page import="uit.cnpm02.dkhp.model.Faculty"%>
<%@page import="java.util.List"%>
<%@page import="uit.cnpm02.dkhp.model.Subject"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // Validate access role
    ClientValidate.validateAcess(AccountType.ADMIN, session, response);
    List<Faculty> faculities = (List<Faculty>)session.getAttribute("faculty");
    /**
    * For adding request subject ... TODO later.....
    */
    List<Subject> subjects = (List<Subject>)session.getAttribute("subjects");
%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Thêm môn học</title>
        <style media="all" type="text/css">
        </style>
    </head>
    <body>
        <!--Div Wrapper-->
        <div id="wrapper">
            <%@include file="MenuPDT.jsp"%>
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <div id="main-title">
                    Thêm môn học
                </div>
                <br /><br />
                
                <div id="add-subject" class="wrapper-border">
                    <table>
                        <tr>
                            <td> Mã MH </td>
                            <td> <input type="text" id="txt-subject-code"> </td>
                        </tr>
                        <tr>
                            <td> Tên MH </td>
                            <td> <input type="text" id="txt-subject-name"> </td>
                        </tr>
                        <tr>
                            <td> Số TCLT </td>
                            <td> <input type="text" id="txt-tclt" onkeypress="return checknumber(event)"> </td>
                        </tr>
                        <tr>
                            <td> Số TCTH </td>
                            <td> <input type="text" id="txt-tcth" onkeypress="return checknumber(event)"> </td>
                        </tr>
                        <tr>
                            <td> Khoa </td>
                            <td>
                                <select id="txt-faculty">
                                    <%
                                        if ((faculities != null) && !faculities.isEmpty()) {
                                            for (Faculty f : faculities) {
                                                %>
                                                <option value="<%=f.getId()%>"> <%=f.getFacultyName()%> </option>
                                                <%
                                            }
                                        }
                                    %>
                                    
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td> Loại </td>
                            <td>
                                <select id="txt-type">
                                    <option value="0"> Bắt buộc </option>
                                    <option value="1"> Tự chọn </option>
                                </select>
                            </td>
                        </tr>
                        
                        <%--- Mon hoc tien quet --%>
                        <%
                        for (int i = 0; i < 3; i++) {
                        %>
                        <tr>
                            <td> Môn học tiên quyết 1 </td>
                            <td>
                                <select id="pre-sub<%=(i+1)%>">
                                    <option value="X"> X </option>
                                <%
                                if ((subjects != null) && !subjects.isEmpty()) {
                                    for (int j = 0; j < subjects.size(); j++) {
                                        Subject s = subjects.get(j);
                                    %>
                                        <option value="<%=s.getId()%>"> <%=s.getSubjectName()%> </option>
                                    <%
                                    }%>
                                <%}%>
                        <%}%>
                                </select>
                            </td>
                        </tr>
                        
                    </table>
                    <div class="hint-box">
                        Form nhập thông tin môn học mới, người dùng vui lòng cung cấp thông tin về môn học
                        như: mã, tên, khoa, môn học tiên quyết...
                        <br /> Mặc đinh, hệ thống hỗ trợ thêm 3 môn tiên quyết, các môn trùng nhau sẽ bị
                        loại bỏ, chọn X để bỏ qua.
                        <br /> Để thêm môn tiên quyết khác, vui lòng truy cập trang quản lý môn học
                        <br />
                    </div>
                    <div style="margin-left: 5px; width: 265px;">
                        <div id="btn-check-add-one-student" style="float: left;">
                            <div class="button-1" style="padding: 2px 2px;margin-top: 15px;">
                                <span class="atag" onclick="addSubject()" ><img src="../../imgs/check.png" />Thêm</span>
                            </div>
                        </div>
                        <div id="btn-add-one-student" style="float: left; padding-left: 12px;">
                            <div class="button-1" style="padding: 2px 2px;margin-top: 15px;">
                                <span class="atag" onclick="moveBack()" ><img src="../../imgs/check.png" />Quay lại</span>
                            </div>
                        </div>
                    </div>
                    <br /><br />
                    <div id="add-result" class="msg-response" style="margin-left: 10%; width: 80%; margin-top: 22px;">
                    </div>
                </div>
                    
            </div><!--End Contents-->
            <form id="back" action="../../ManageSubjectController?function=list_subject&ajax=false" method="post">
                
            </form>
            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    
    <%--<script src="../../javascripts/AjaxUtil.js"></script>--%>
    <script type = "text/javascript">
        var http = createRequestObject();
        function addSubject() {
            var subjectId = document.getElementById("txt-subject-code").value;
            var subjectName = document.getElementById("txt-subject-name").value;
            var tclt = document.getElementById("txt-tclt").value;
            var tcth = document.getElementById("txt-tcth").value;
            var faculty = document.getElementById("txt-faculty").value;
            var subType = document.getElementById("txt-type").value;
            var preSubject1 = document.getElementById("pre-sub1").value;
            var preSubject2 = document.getElementById("pre-sub2").value;
            var preSubject3 = document.getElementById("pre-sub3").value;
            var preSubject = 'X';
            if(subjectId.length == 0 || subjectName.length==0 || tclt.length==0 || tcth.length ==0 )
            {
                alert("Vui lòng nhập đủ thông tin");
                return;
            }
           if ((preSubject1 != null) && (preSubject1.length > 0) && (preSubject1 != "X")) {
                preSubject = preSubject1;
            }
            if ((preSubject2 != null) && (preSubject2.length > 0) && (preSubject2 != "X")) {
                preSubject += "-"
                preSubject += preSubject2;
            }
            if ((preSubject3 != null) && (preSubject3.length > 0) && (preSubject3 != "X")) {
                preSubject += "-"
                preSubject += preSubject3;
            }
            var data = subjectId + ";"
                        + subjectName + ";"
                        + tclt + ";"
                        + tcth + ";"
                        + faculty + ";"
                        + subType + ";"
                        + preSubject;
                  
            if(http){
                http.open("GET", "../../ManageSubjectController?function=add_subject&data="
                    + data ,true);
                http.onreadystatechange = addSubjectRespone;
                http.send(null);
            } else {
                alert("Error: No HTTP object");
            }
            
        }
        
        function moveBack() {
           document.forms["back"].submit();
        }
        
        function addSubjectRespone() {
            if(http.readyState == 4 && http.status == 200){
                $('#add-result').show('slow');
                var detail=document.getElementById("add-result");
                detail.innerHTML=http.responseText;
            }
        }
        function checknumber(evt){
           var e = event || evt; // for trans-browser compatibility
	var charCode = e.which || e.keyCode;

	if (charCode > 31 && (charCode < 48 || charCode > 57))
		return false;

	return true;
        }
    </script>
    </body>
</html>