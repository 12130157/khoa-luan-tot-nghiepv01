<%-- 
    Document   : SubjectManager
    Created on : 09-11-2011, 23:08:36
    Author     : LocNguyen
--%>
<%@page import="uit.cnpm02.dkhp.utilities.ClientValidate"%>
<%@page import="uit.cnpm02.dkhp.model.type.AccountType"%>
<%@page import="java.util.List"%>
<%@page import="uit.cnpm02.dkhp.model.Subject"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản lý môn học</title>
        <style media="all" type="text/css">
            <%-- CSS definition --%>
            #btn-to-move-page-add-subject {
                float: right;
                padding-top: 6px;
                margin-right: 20px;
            }
        </style>
    </head>
    <%
        // Validate Access role
        ClientValidate.validateAcess(AccountType.ADMIN, session, response);
        
        int numpage;
        try {
            numpage = (Integer) session.getAttribute("numpage");
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
            <%-- Menu --%>
            <%@include file="MenuPDT.jsp" %>
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <div id="main-title">
                    Quản lý Môn học
                </div>
                <br /><br />
                <div id="control-range">
                    <%--------------Search-------------%>
                    <div id="search-area" style="float: left;">
                        <%--SEARCH FORM--%>
                        <div id="searchbox" action="#">
                            <input id="search" type = "text" placeholder = "Search" onkeypress="txtBoxSearchSubjectKeyPressed()" />
                            <input type="button" id="submit" onclick = "searchSubject()" value = "Tìm" />
                        </div>
                    </div>
                    <div id="btn-to-move-page-add-subject">
                        <span class="atag" type = "button" onclick="preAddSubject()"> <img src="../../imgs/icon/add.png"/>Thêm môn học</span>
                    </div>
                </div>
                    
                <div id = "listsubject">
                    <form id="frm-pre-add-sub" method="post" action="../../ManageSubjectController?function=pre_add_subject">
                    </form>
                    <div id="error">
                        <%
                            if ((error != null) && !error.isEmpty()) { %>
                                <%= error %>
                            <%}
                        %>
                    </div>
                     <table id = "tablelistsubject" class="general-table" name = "tablelistsubject">
                        <tr>
                        <th> STT </th>
                        <th>Mã MH </th>
                        <th> Tên Môn học </th>
                        <th> Khoa </th>
                        <th> Số TC </th>
                        <th> Số TCLT </th>
                        <th> Số TCTH</th>
                        <th>Loại </th>
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
                        <td> <%= subjects.get(i).getFacultyCode()%> </td>
                        <td> <%= subjects.get(i).getnumTC()%> </td>
                        <td> <%= subjects.get(i).getnumTCLT()%> </td>
                        <td> <%= subjects.get(i).getnumTCTH()%> </td>
                        <%if(subjects.get(i).getType()==0){%>
                        <td> Bắt buộc </td>
                        <%}else{%>
                        <td> Tự chọn</td>
                        <%}%>
                        <td><a href = "../../ManageSubjectController?function=edit_subject&ajx=false&subject_code=<%= subjects.get(i).getId()%>"><img src="../../imgs/icon/edit.png" title="Sửa" alt="Sửa"/></a></td>
                        <td> <span class="atag" onclick="deleteSub('<%= subjects.get(i).getId() %>')"><img src="../../imgs/icon/delete.png" title="Xóa" alt="Xóa"/></span> </td>
                        <% }%>
                        </tr>
                        <%}%>
                    </table>
                </div>
                <div id="paggind">
                    <input type="button" value="|<<" onclick="firstPage()"/>- 
                    <input type="button" value="<<" onclick="prePage()"/>-
                    <input type="button" value=">>" onclick="nextPage()"/>-
                    <input type="button" value=">>|" onclick="endPage()"/>
                    <input type="hidden" value="<%= numpage%>" id = "numpage" />
                </div>
                <br/>
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
                var key = document.getElementById("search-box").value;
                submitSearchSubject("../../ManageSubjectController?function=Filter&curentPage="+currentpage + "&key=" + key);
            }
        }
        
        function submitSearchSubject(pagename) {
            if(http){
                http.open("GET", pagename ,true);
                http.onreadystatechange = searchResponeHandler;
                http.send(null);
            }
        }
          function keypressed()
    { 
       if(event.keyCode=='13')
       {
           searchSubject();
       } 
      }
        function txtBoxSearchSubjectKeyPressed() {
            if(event.keyCode == '13') {
                searchSubject();
            }
        }
        
        function searchSubject() {
            currentpage = 1;
            var key = document.getElementById("search").value;
            submitSearchSubject("../../ManageSubjectController?function=search&key=" + key);
        }
        
        function searchResponeHandler() {
            if(http.readyState == 4 && http.status == 200){
                var detail=document.getElementById("tablelistsubject");
                detail.innerHTML=http.responseText;
            }
        }
        function deleteSub(subId){
            var key = document.getElementById("search-box").value;
            submitSearchSubject("../../ManageSubjectController?function=delete&subject_code=" + subId +"&curentPage="+currentpage + "&key=" + key);
        }
        
        function preAddSubject() {
            document.forms["frm-pre-add-sub"].submit();
        }
    </script>
</html>
