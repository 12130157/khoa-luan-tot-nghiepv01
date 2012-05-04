<%-- 
    Document   : SubjectManager
    Created on : 09-11-2011, 23:08:36
    Author     : LocNguyen
--%>
<%@page import="java.util.List"%>
<%@page import="uit.cnpm02.dkhp.model.Subject"%>
<%@include file="MenuPDT.jsp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản lý tài khoản</title>
        <style media="all" type="text/css">

            #tablelistsubject, #tableaddsubject{
                width: 100%;
                padding-left: 10px;
                padding-right: 10px;
                text-align: center;

            }
            #tablelistsubject th, #tableaddsubject th{
                background-color:#00ff00;
                height: 30px;
                border-color: black;
            }

            #tablelistsubject td, #tableaddsubject td{
                text-align: center;
                background-color: #5F676D;
            }
            #title{
                background-color: #2f4e3d;
                text-align: center;
                padding-top: 12px;
                padding-bottom: 10px;
            }
            #page{
                text-align: center;
            }
            #sidebar {
                height:250px;
                overflow:auto;
            }
            a {
                color: violet;
            }
        </style>
    </head>
    <%
        int numpage;
        try {
            numpage = (Integer) session.getAttribute("numpage_sub");
        } catch (Exception ex) {
            numpage = 1;
        }
        
        String error = (String)session.getAttribute("error");
        session.removeAttribute("error");

        List<Subject> subjects = (List<Subject>) session.getAttribute("list_subject");

    %>
    <body>
        <!--Div Wrapper-->
        <div id="wrapper">

            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <div id="title">
                    <u><h3>Quản lý Môn học</h3></u>
                </div>
                <hr/><hr/><br>

                <div id = "listsubject">
                    <div style="padding-bottom: 10px;
                         padding-left: 10px;
                         ">
                        <input type = "text" placeholder = "Nhập thông tin tìm kiếm" id="search-box" />
                        <input type = "button" onclick = "searchSubject()" value = "Tìm">
                    </div>
                    <form method="post" action="../../ManageSubjectController?function=pre_add_subject">
                        <div style="padding-bottom: 5px;
                             float: right; padding-right: 15px;
                             ">
                            <input 
                                style="background-color: #f40f0f;"
                                type = "submit" value = "Thêm môn học">
                        </div>
                    </form>
                    <div id="error">
                        <%
                            if ((error != null) && !error.isEmpty()) { %>
                                <%= error %>
                            <%}
                        %>
                    </div>
                    <table id = "tablelistsubject" name = "tablelistsubject">
                        <tr>
                        <th> STT </th>
                        <th><a href="#" onclick="sort('MaMH', '')"> Mã MH </a></th>
                        <th><a href="#" onclick="sort('TenMH', '')"> Tên Môn học </a></th>
                        <th><a href="#" onclick="sort('SoTC', '')"> Số TC </a></th>
                        <th><a href="#" onclick="sort('SoTCLT', '')"> Số TCLT </a></th>
                        <th><a href="#" onclick="sort('SoTCTH', '')"> Số TCTH </a></th>
                        <th> Sửa </th>
                        <th> Xóa </th>
                        <%--Should be sorted when click on table's header--%>
                        </tr>
                        <%if ((subjects != null) && !subjects.isEmpty()) {%>
                        <% for (int i = 0; i < subjects.size(); i++) {%>
                        <tr>
                        <td> <%= (i + 1)%> </td>
                        <td> <%= subjects.get(i).getId()%> </td> 
                        <td> <%= subjects.get(i).getSubjectName()%> </td>
                        <td> <%= subjects.get(i).getnumTC()%> </td>
                        <td> <%= subjects.get(i).getnumTCLT()%> </td>
                        <td> <%= subjects.get(i).getnumTCTH()%> </td>
                        <td><a href = "../../ManageSubjectController?function=edit_subject&ajx=false&subject_code=<%= subjects.get(i).getId()%>">Sửa</a></td>
                        <td><a href = "../../ManageSubjectController?function=delete_single_subject&ajax=false&currentpage=1&subject_code=<%= subjects.get(i).getId()%>">Xóa</a></td>
                        <% }%>
                        </tr>
                        <%}%>
                    </table>
                    <div id="page">
                        <input type="button" value="|<<" onclick="firstPage()"/>- 
                        <input type="button" value="<<" onclick="prePage()"/>-
                        <input type="button" value=">>" onclick="nextPage()"/>-
                        <input type="button" value=">>|" onclick="endPage()"/>
                        <input type="hidden" value="<%= numpage%>" id = "numpage" />
                    </div>
                    <br/>
                </div>

                <div id="editsubject">
                    <table id="tableeditsubject">
                    </table>
                </div>

            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>

    <script src="../../javascripts/UtilTable.js"></script>
    <script src="../../javascripts/AjaxUtil.js"></script>
    <script  type = "text/javascript" >
        var currentpage = 1;
        var http = createRequestObject();
        var numpage = document.getElementById("numpage").value;
        var sortType = "ASC";
        function firstPage(){
            currentpage = 1;
            sendRequest();
        }
        function prePage(){
            currentpage--;
            if(currentpage < 1)
                currentpage = 1;
            sendRequest();
        }
        function nextPage(){
            currentpage ++;
            if(currentpage > numpage)
                currentpage = numpage;
            sendRequest();
        }
        function endPage(){
            currentpage = numpage;
            sendRequest();
        }
        function sendRequest(){
            if(http){
                submitSearchSubject("../../ManageSubjectController?function=list_subject&ajax=true&currentpage=" + currentpage);
            }
        }
        
        function submitSearchSubject(pagename) {
            if(http){
                http.open("GET", pagename ,true);
                http.onreadystatechange = searchResponeHandler;
                http.send(null);
            }
        }
        
        function searchSubject() {
            var key = document.getElementById("search-box").value;
            submitSearchSubject("../../ManageSubjectController?function=search&key=" + key);
        }
        
        function searchResponeHandler() {
            if(http.readyState == 4 && http.status == 200){
                var detail=document.getElementById("tablelistsubject");
                detail.innerHTML=http.responseText;
            }
        }
        
        function sort(by, type) {
            if (sortType == "ASC")
                sortType = "DES";
            else 
                sortType = "ASC";
            submitSearchSubject("../../ManageSubjectController?function=sort&by="
                + by + "&type=" + sortType);
        }
    </script>
</html>