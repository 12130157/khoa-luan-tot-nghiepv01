<%-- 
    Document   : TrangChu
    Created on : Apr 23, 2011, 10:59:14 PM
    Author     : ngloc_it
--%>

<%@page import="uit.cnpm02.dkhp.utilities.ClientValidate"%>
<%@page import="uit.cnpm02.dkhp.model.type.AccountType"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="uit.cnpm02.dkhp.model.Task"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
    // Validate access role
    ClientValidate.validateAcess(AccountType.STUDENT, session, response);
    
    List<Task> tasks = (List<Task>) session.getAttribute("tasks");
%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <style media="all" type="text/css">
            .max-width-content {
                width: 450px;
            }
        </style>
        <title>Trang sinh viên</title>
    </head>
    <body>
        <!--Div Wrapper-->
        <div id="wrapper">  
            <%@include file="MenuSV.jsp"%>
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <div id="important-task">
                    <%
                    if ((tasks != null) && !tasks.isEmpty()) {
                    %>
                    <u><b>Tin quan trọng:</b></u>
                    <table class="general-table" style="width: 800px;">
                        <tr>
                            <th> STT </th>
                            <th class="max-width-content"> Nội dung </th>
                            <th> Người gửi </th>
                            <th> Ngày gửi </th>
                        </tr>
                        <%
                        for (int i = 0; i < tasks.size(); i++) {
                            Task t = tasks.get(i);
                            SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIME_PARTERM_DEFAULT);
                            String content = t.getContent();
                            if (content.length() > 200) {
                                content = content.substring(0, 200) + "...";
                            }
                        %>
                            <tr id="<%= t.getId() %>">
                                <td> <%= (i + 1) %> </td>
                                <td> <span class="atag" onclick="showTaskProcess(<%= t.getId() %>);"> <%= content %> </span></td>
                                <td> <%= t.getSender() %> </td>
                                <td> <%= sdf.format(t.getCreated()) %> </td>
                            </tr>
                        <%
                        }
                        %>
                    </table>
                    <%-- A popup show option process task --%>
                    <div class="clear"></div>
                    <div id="popup-process-task" style="border: solid 1px #ff0; border-radius: 5px; display: none;" onClick="hideMe('popup-process-task');">
                        <img src = '../../imgs/icon/loading.gif' />
                    </div>
                    <div class="clear"></div>
                    <%
                    }
                    %>
                </div>
                <%-- Msg process result --%>
                <div class="clear"></div>
                <div id="msg-process-task-response" class="msg-response">
                </div>
                <%@include file="News.jsp" %>
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    
    <script src="../javascripts/News.js"></script>
    <script src="../../javascripts/AjaxUtil.js"></script>
    <script  type = "text/javascript" >
        action = "search";
        var http = createRequestObject();
        function search(){
            name=document.getElementById("subname").value;
            ajaxfunction("../servSubject?action="+action+"&name="+name);
        }
        
        function showTaskProcess(taskId) {
            $('#popup-process-task').fadeIn('slow', function() {
                // Animation complete
                var controller = '../../RegistryController?action=student-process-task' 
                            + '&taskid=' + taskId;
                if(http){
                    http.open("GET", controller, true);
                    http.onreadystatechange = showTaskProcessHandler;
                    http.send(null);
                } else {
                    alert("Lỗi gửi yêu cầu tới Server thất bại");
                }
            });
        }
        function showTaskProcessHandler() {
            if(http.readyState == 4 && http.status == 200){
                 var detail = document.getElementById("popup-process-task");
                 detail.innerHTML = http.responseText;
            }
        }
        
        function confirm(taskid, agree) {
            var controller = '../../RegistryController?action=student-confirm-process-task' 
                            + '&taskid=' + taskid
                            + '&yesno=' + agree;
            if(http){
                http.open("GET", controller, true);
                http.onreadystatechange = confirmHandler;
                http.send(null);
            } else {
                alert("Lỗi gửi yêu cầu tới Server thất bại");
            }
            
            // Display the task table
            var tblTask = document.getElementById('important-task').getElementsByTagName('table')[0];
            var tblLength = tblTask.rows.length;

            for (var i = 1; i < tblLength; i++) {
                var id = tblTask.rows[i].id;
                if (id == taskid) {
                    tblTask.deleteRow(i);
                    tblLength--;
                    break;
                }
            }
            if (tblLength <= 1) {
                document.getElementById('important-task').style.display = 'none';
            }
            
        }
        
        function confirmHandler() {
            if(http.readyState == 4 && http.status == 200){
                $('#msg-process-task-response').show('slow');
                var detail = document.getElementById("msg-process-task-response");
                detail.innerHTML = http.responseText;
                setTimeOut("msg-process-task-response", AjaxConstants.SHORT_DELAY);
            }
        }
        //msg-process-task-response
    </script>
    </body>
</html>
