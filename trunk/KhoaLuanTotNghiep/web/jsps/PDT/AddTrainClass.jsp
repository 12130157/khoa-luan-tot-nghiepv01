<%-- 
    Document   : AddSubject
    Created on : 11-11-2011, 23:45:21
    Author     : LocNguyen
--%>
<%@page import="uit.cnpm02.dkhp.model.Faculty"%>
<%@page import="java.util.Date"%>
<%@page import="uit.cnpm02.dkhp.utilities.DateTimeUtil"%>
<%@page import="java.util.List"%>
<%@page import="uit.cnpm02.dkhp.model.web.LecturerWeb"%>
<%@page import="uit.cnpm02.dkhp.model.web.SubjectWeb"%>
<%@page import="uit.cnpm02.dkhp.utilities.Constants"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
  List<LecturerWeb> Lecture = (List<LecturerWeb>) session.getAttribute("lecturers");
  List<SubjectWeb> Subject = (List<SubjectWeb>) session.getAttribute("subjects");
  List<Faculty> faculty = (List<Faculty>) session.getAttribute("faculty");
  List<String> roomList = Constants.ROOM_LISS;
%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Mở lớp học</title>
        <style media="all" type="text/css">

            #table{
                width: 100%;
                padding-left: 10px;
                padding-right: 10px;
                text-align: center;

            }
            #table th{
                background-color:#00ff00;
                height: 30px;
                border-color: black;
            }

            #table td{
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

            #error_code, #error_name, #error_tclt, #error_tcth {
                font-size: 10px;
                color: #cc0033;
            }
        </style>
    </head>
    <body>
        <script src="../../javascripts/DateTimePicker.js" type="text/javascript"></script>
        <!--Div Wrapper-->
        <div id="wrapper">
            <%@include file="MenuPDT.jsp"%>
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->

               <div id="title">
                    <u><h3>Mở lớp học mới</h3></u>
                </div>
                <hr/><hr/><br>

                <form id="addclassform" method="post">
                   <table id="table_mh">
                        <tr>
                        <td> Khoa: </td>
                        <td>
                            <select id="faculty" name="faculty" onchange="ChangeClassList()">
                                <%for(int i=0; i<faculty.size();i++){%>
                                <option value="<%=faculty.get(i).getId()%>"><%=faculty.get(i).getFacultyName()%></option>
                                <%}
                                %>
                            </select>
                        </td>
                        </tr>
                        <tr>
                        <td> Mã lớp: </td>
                        <td> <input type="text" id="classcode" name="classcode"/> </td>
                        </tr>
                        <tr>
                        <td> Môn học: </td>
                        <td> 
                            <select id="subject" name="subject" onchange="ChangeClassCode()">
                                <%for(int i=0; i<Subject.size();i++){%>
                                <option value="<%=Subject.get(i).getId()%>"><%=Subject.get(i).getName()%></option>
                                <%}
                                %>
                            </select>
                        </td>
                        </tr>
                        <tr>
                        <td> Giảng viên: </td>
                        <td> 
                            <select id="lecturer" name="lecturer">
                                <%for(int i=0; i<Lecture.size();i++){%>
                                <option value="<%=Lecture.get(i).getId()%>"><%=Lecture.get(i).getName()%></option>
                                <%}
                                %>
                            </select>
                        </td>
                       </tr>
                        <tr>
                        <td> Số sinh viên tối đa: </td>
                        <td> <input type="text" id="slsv" onkeypress="return checknumber(event)"/> </td>
                        </tr>
                         <tr>
                        <td> Ngày học: </td>
                        <td> 
                           <select id="Date" name="Date">
                                <option value="2">Thứ 2</option>
                                <option value="3">Thứ 3</option>
                                <option value="4">Thứ 4</option>
                                <option value="5">Thứ 5</option>
                                <option value="6">Thứ 6</option>
                                <option value="7">Thứ 7</option>
                            </select>
                        </td>
                        </tr>
                        <tr>
                        <td> Ca học: </td>
                        <td> 
                            <select id="Shift" name="Shift">
                                <option value="1">Sáng</option>
                                <option value="2">Chiều</option>
                            </select>
                        </tr>   
                         <tr>
                        <td> Phòng học: </td>
                        <td> 
                            <select id="room" name="room">
                              <%for(int i=0; i<roomList.size();i++){%>
                                <option value="<%=roomList.get(i)%>"><%=roomList.get(i)%></option>
                                <%}
                                %>  
                            </select>
                        </td>
                        <tr>
                            <td>Ngày bắt đầu học:</td>
                            <td>
                                <input type="text" id="startDate" name="startDate" readonly="readonly" value="<%=DateTimeUtil.format(new Date())%>">
                                <img src="../../imgs/cal.gif" style="cursor: pointer;" onclick="javascript:NewCssCal('startDate','YYMMMDD')" /> 
                            </td> 
                        </tr>
                        <tr>
                            <td>Ngày kết thúc:</td>
                            <td>
                                <input type="text" id="endDate" name="endDate" readonly="readonly" value="<%=DateTimeUtil.format(new Date())%>">
                                <img src="../../imgs/cal.gif" style="cursor: pointer;" onclick="javascript:NewCssCal('endDate','YYMMMDD')" /> 
                            </td> 
                        </tr>
                        </tr>
                         <tr>
                             <td></td>
                             <td>
                                 <input type="button" name="Check" id="Check" value="  Kiểm tra  " onclick="CheckClass()"/>
                                 <input type="button" id="Create" onclick="SendRequestCreateNewTrainClass()" name="Create" value="  Tạo lớp học  "/> 
                             </td>
                        </tr>
                    </table>
                     
                    <div id="error">
                        
                    </div>
                             
                    <div id="createNew-result">
                        
                    </div>

                </form>
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
        //numpage = document.getElementById("numpage").value;
        function SendRequestCreateNewTrainClass(){
            var classCode = document.getElementById("classcode").value;
            var lecturer = document.getElementById("lecturer").value;
            var subject = document.getElementById("subject").value;
            var slsv = document.getElementById("slsv").value;
            var date = document.getElementById("Date").value;
            var shift = document.getElementById("Shift").value;
            var room = document.getElementById("room").value;
            var startDate = document.getElementById("startDate").value;
            var endDate = document.getElementById("endDate").value;
            if(classCode.length == 0){
                alert("Hãy nhập mã lớp học");
            }else if(slsv.length ==0 ){
                alert("Hãy nhập số sinh viên tối đa cho lớp");
            }else{
             if (http) {
               http.open("GET", "../../ManageClassController?action=create" +
                "&classcode=" + classCode +
                "&subject=" + subject +
                "&lecturer="+ lecturer +
                "&slsv=" + slsv +
                "&Date=" + date +
                "&Shift=" + shift +
                "&room=" + room +
                "&startDate=" + startDate +
                "&endDate=" + endDate,true);
                http.onreadystatechange = handleResponseForCreateNew;
                http.send(null);
              }
            }
        }
        
    function CheckClass() {
        var classCode = document.getElementById("classcode").value;
        var lecturer = document.getElementById("lecturer").value;
        var subject = document.getElementById("subject").value;
        var slsv = document.getElementById("slsv").value;
        var date = document.getElementById("Date").value;
        var shift = document.getElementById("Shift").value;
        var room = document.getElementById("room").value;
        var startDate = document.getElementById("startDate").value;
            var endDate = document.getElementById("endDate").value;
        
        if(classCode.length == 0){
            alert("Hãy nhập mã lớp học");
        }else if(slsv.length == 0 ){
            alert("Hãy nhập số sinh viên tối đa cho lớp");
        } else {
            if (http) {
                 http.open("GET", "../../ManageClassController?action=check_create" +
                    "&classcode=" + classCode +
                    "&subject=" + subject +
                    "&lecturer="+ lecturer +
                    "&slsv=" + slsv +
                    "&Date=" + date +
                    "&Shift=" + shift +
                    "&room=" + room +
                    "&startDate=" + startDate +
                     "&endDate=" + endDate,true);
                http.onreadystatechange = handleResponseForCreateNew;
                http.send(null);
            }
        }
    }
 
        function handleResponseForCreateNew() {
             if(http.readyState == 4 && http.status == 200){
                 var detail=document.getElementById("createNew-result");
                 detail.innerHTML=http.responseText;
             }
         }
        
         function handleResponse() {
             if(http.readyState == 4 && http.status == 200){
                 var detail=document.getElementById("error");
                 detail.innerHTML=http.responseText;
             }
         }

         function ChangeClassCode(){
                  var subjectCode = document.getElementById("subject").value;
                  document.getElementById("classcode").value = subjectCode;
             if(http){
                    http.open("GET", "../../ManageClassController?action=FilterLecturerBySubject&subjetCode="+subjectCode, true);
                    http.onreadystatechange = filterLecturer;
                    http.send(null);
                 }
         }
          function filterLecturer() {
             if(http.readyState == 4 && http.status == 200){
                 var detail=document.getElementById("lecturer");
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
        function ChangeClassList(){
           var facultyCode = document.getElementById("faculty").value; 
           if(http){
                    http.open("GET", "../../ManageClassController?action=FilterClassByFaculty&facultyCode="+facultyCode, true);
                    http.onreadystatechange = filterSubjectName;
                    http.send(null);
                 }
        }
        function filterSubjectName() {
             if(http.readyState == 4 && http.status == 200){
                 var detail=document.getElementById("subject");
                 detail.innerHTML=http.responseText;
             }
         }
    </script>
    
</html>
