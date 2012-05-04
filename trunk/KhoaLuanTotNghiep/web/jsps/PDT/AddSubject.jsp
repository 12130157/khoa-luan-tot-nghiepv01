<%-- 
    Document   : AddSubject
    Created on : 11-11-2011, 23:45:21
    Author     : LocNguyen
--%>
<%@page import="uit.cnpm02.dkhp.model.Faculty"%>
<%@page import="java.util.List"%>
<%@page import="uit.cnpm02.dkhp.model.Subject"%>
<%@include file="MenuPDT.jsp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
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

            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <div id="title">
                    <u><h3>Thêm môn học</h3></u>
                </div>
                <br>
                <hr/><hr/><br>
                
                <div id="add-subject">
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
                            <td> <input type="text" id="txt-tclt"> </td>
                        </tr>
                        <tr>
                            <td> Số TCTH </td>
                            <td> <input type="text" id="txt-tcth"> </td>
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
                                    
                    <div id="add-subject-button">
                        <input type="button" onclick="addSubject()" value="Thêm"/>
                        <input type="button" onclick="moveBack()" value="Quay lại"/>
                        <%----%>
                    </div>
                </div>
                    <div id="add-result">
                        
                    </div>
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>
    <script src="../../javascripts/AjaxUtil.js"></script>
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
            alert("Called move back");
        }
        
        function addSubjectRespone() {
            if(http.readyState == 4 && http.status == 200){
                var detail=document.getElementById("add-result");
                detail.innerHTML=http.responseText;
            }
        }
        
    </script>
</html>