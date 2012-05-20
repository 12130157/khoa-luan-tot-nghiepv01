<%-- 
    Document   : ManageScore
    Created on : 26-12-2011, 21:40:04
    Author     : LocNguyen
--%>

<%@page import="java.util.List"%>
<%@include file="MenuPDT.jsp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%
    List<String> clazz = (List<String>) session.getAttribute("list-subject");
    String error = (String) session.getAttribute("error");
    if (error != null) {
        //Clear session
        session.removeAttribute("error");
    }

%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <link href="../../csss/menu.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản lý Điểm Sinh Viên</title>
        <style media="all" type="text/css">
            #title{
                text-align: center;
                padding-bottom: 50px;
            }
            #page{
                text-align: center;
            }
            a {
                color: violet;
            }
            #frame-content{
                border: 2 solid #cc0033;
                width: 250px;
                padding-left: 50px;
            }

            #import-result{
                color: #cc0033;
                font-size: 8px;
            }

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
                    Trang quản lý điểm Sinh Viên
                </div>

                <form id="importFromFile" action="../../ManageScoreController?function=import-score-from-file"
                      method="post" name="importFromFile" enctype="multipart/form-data">
                    <div id="frame-content">
                        <div style="border-color: #cc0033; padding-top: 10px;">
                            <u style="font-size: 12px" >Chọn môn học</u> <br />
                            <select id="select_class"
                                    name="select_class"
                                    style="width: 250px;">
                                <% if ((clazz != null) && (!clazz.isEmpty())) {
                                    for (int i = 0; i < clazz.size(); i++) {%>
                                <option <% if (i == 1) {%> selected <% }%> >
                                    <%= clazz.get(i)%>
                                </option>
                                <% } /*End for*/%>
                                <% } /*End if*/%>
                            </select>
                        </div>
                        <div style="border-color: #cc0033; padding-top: 10px;">
                            <u style="font-size: 12px" >Chọn học kỳ</u> <br />
                            <select id="select_course"
                                    name="select_course"
                                    style="width: 250px;">
                                <option> Học kỳ 1 </option>
                                <option> Học kỳ 2 </option>
                            </select>
                        </div>
                        <div style="border-color: #cc0033; padding-top: 10px;">
                            <u style="font-size: 12px" >Chọn năm học</u> <br />
                            <select id="select_year"
                                    name="select_year"
                                    style="width: 250px;">
                                <% for (int i = 0; i < 50; i++) {%>
                                <option>
                                    <%= (2010 + i)%>
                                </option>
                                <% } /*End for*/%>
                            </select>
                        </div>
                        <div style="border-color: #cc0033; padding-top: 10px;">
                            <u style="font-size: 12px" >Chọn file điểm</u> <br />
                            <input type="file" name="txtPath" id="txtPath">
                            <!--input type="button" onclick="submitInsertScoreFromFile('../../ManageScoreController?function=import-score-from-file')" value="Hoàn thành"-->
                            <input type="submit" value="Hoàn thành">
                        </div>
                    </div>
                            
                    <div id="import-result">
                        <% if (error != null) {%>
                        <%= error%>
                        <% }%>
                    </div>
                    
                </form>


                <div style="border: 2 solid #cc0033;
                     font-size: 10px;
                     padding-left: 100px;
                     width: 450px;
                     padding-top: 45px;
                     padding-bottom: 75px;">

                    <div>
                        <a href="DownloadScoreTableByClass.jsp"> Download bảng điểm theo SV </a>
                    </div>
                    <div>
                        <a href="DownloadScoreTableByClass.jsp"> Download bảng điểm theo lớp học </a>
                    </div>
                    <div>
                        <a href="UpdateScoreTableManualMode.jsp"> Cập nhật điểm SV </a>
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
    <script  type = "text/javascript" >
        var http = createRequestObject();
         
         //==> Shared be remove
        function submitInsertScoreFromFile(pagename){
            var clazz = document.getElementById("select_class").value;
            var course = document.getElementById("select_course").value;
            var year = document.getElementById("select_year").value;
            var datas = "&clazz=" + clazz;
            datas += "&course=" + course;
            datas += "&year=" + year;
            var controller = pagename + datas;
             
            if(http){
                http.open("GET", controller ,true);
                http.onreadystatechange = handleResponse;
                http.send(null);
                 
            }
        }
         
        function handleResponse() {
            if(http.readyState == 4 && http.status == 200){
                var detail=document.getElementById("import-result");
                detail.innerHTML=http.responseText;
            }
        }
    </script>
</html>
