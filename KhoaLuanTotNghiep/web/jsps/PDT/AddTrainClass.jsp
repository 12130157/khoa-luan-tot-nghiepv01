<%-- 
    Document   : AddSubject
    Created on : 11-11-2011, 23:45:21
    Author     : LocNguyen
--%>
<%@page import="java.util.List"%>
<%@page import="uit.cnpm02.dkhp.model.web.LecturerWeb"%>
<%@page import="uit.cnpm02.dkhp.model.web.SubjectWeb"%>
<%@include file="MenuPDT.jsp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
  List<LecturerWeb> Lecture = (List<LecturerWeb>) session.getAttribute("lecturers");
  List<SubjectWeb> Subject = (List<SubjectWeb>) session.getAttribute("subjects");
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
        <!--Div Wrapper-->
        <div id="wrapper">

            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->

                <hr/><hr/>
                <div id="title">
                    <u><h3>Mở lớp học mới</h3></u>
                </div>
                <hr/><hr/><br>

                <form id="addclassform" action="../../ManageClassController?action=create" method="post" >
                   <table id="table_mh">
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
                        <td> <input type="text" id="slsv"/> </td>
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
                                <option value="1">Chiều</option>
                            </select>
                        </tr>   
                         <tr>
                        <td> Phòng học: </td>
                        <td> <input type="text" id="room"/> </td>
                        </tr>
                         <tr>
                             <td></td>
                             <td>
                                 <input type="button" name="Check" id="Check" value="  Kiểm tra  " onclick="CheckClass()"/>
                                 <input type="submit" id="Create" name="Create" value="  Tạo lớp học  "/> 
                             </td>
                        </tr>
                    </table>

                </form>
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>
 <script  type = "text/javascript" >
  function ChangeClassCode(){
      var classcode = document.getElementById("subject").value;
      document.getElementById("classcode").value = classcode;
  }   
  function CheckClass(){
      alert("Function to check class");
  }
 </script>
    
</html>
