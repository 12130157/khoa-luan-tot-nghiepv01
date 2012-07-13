<%-- 
    Document   : AddSubject
    Created on : 11-11-2011, 23:45:21
    Author     : LocNguyen
--%>
<%@page import="uit.cnpm02.dkhp.utilities.DateTimeUtil"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.List"%>
<%@page import="uit.cnpm02.dkhp.model.TrainClass"%>
<%@page import="uit.cnpm02.dkhp.utilities.Constants"%>
<%@page import="uit.cnpm02.dkhp.model.web.LecturerWeb"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
  TrainClass trainClass = (TrainClass) session.getAttribute("trainclass");
  List<LecturerWeb> Lecture = (List<LecturerWeb>) session.getAttribute("lecturers");
  List<String> roomList = Constants.ROOM_LISS;
  SimpleDateFormat sdf= new SimpleDateFormat(Constants.DATETIME_PARTERM_DEFAULT);
%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Cập nhật thông tin lớp học</title>
        <style media="all" type="text/css">
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
                <div id="main-title">
                    Cập nhật thông tin lớp học: <%=trainClass.getId().getClassCode()%>
                </div>
                <form id="classDetail" action="../../ManageClassController?action=update&classId=<%=trainClass.getId().getClassCode()%>&year=<%= trainClass.getId().getYear()%>&semester=<%= trainClass.getId().getSemester()%>" method="post" >
                    <table id="table_mh" class="general-table" style="width: 350px;">
                        <tr>
                            <td width="100px"> Mã lớp </td>
                            <td> <b> <%=trainClass.getId().getClassCode()%> </b></td>
                        </tr>
                        <tr>
                            <td> Môn học </td>
                            <td><%=trainClass.getSubjectName()%> </td>
                        </tr>
                        <tr>
                            <td> Giảng viên </td>
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
                            <td> Ngày học </td>
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
                            <td> Ca học </td>
                             <td> 
                            <select id="Shift" name="Shift">
                                <option value="1">Sáng</option>
                                <option value="2">Chiều</option>
                            </select>
                        </tr> 
                        <tr>
                            <td>Phòng học</td>
                            <td> 
                                <select id="room" name="room">
                                    <%for(int i=0; i<roomList.size();i++){%>
                                        <option value="<%=roomList.get(i)%>"><%=roomList.get(i)%></option>
                                    <%}%>  
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td>Ngày thi</td>
                            <td>
                                <input type="text" id="testDate" name="testDate" readonly="readonly" value="<%=DateTimeUtil.format(trainClass.getTestDate())%>">
                                <img src="../../imgs/cal.gif" style="cursor: pointer;" onclick="javascript:NewCssCal('testDate','YYMMMDD')" /> 
                            </td> 
                        </tr>
                        <tr>
                            <td>Phòng thi</td>
                            <td> 
                            <select id="testroom" name="testroom">
                              <%for(int i=0; i<roomList.size();i++){%>
                                <option value="<%=roomList.get(i)%>"><%=roomList.get(i)%></option>
                                <%}
                                %>  
                            </select>
                        </td>
                        </tr>
                        <tr>
                            <td>Giờ thi</td>
                            <td>
                                <select id="hh" name="hh">
                                    <%for(int i =8; i<16; i++){%>
                                    <option value="<%=i%>"><%=i%></option>
                                    <%}%>
                                </select> hh
                                <select id="mm" name="mm">
                                    <%for(int i =0; i<60; i+=5){%>
                                    <option value="<%=i%>"><%=i%></option>
                                    <%}%>
                                </select>
                                
                            </td>
                        </tr>
                        <tr>
                            <td>Ngày bắt đầu học</td>
                            <td>
                                <input type="text" id="startDate" name="startDate" readonly="readonly" value="<%=DateTimeUtil.format(trainClass.getStartDate())%>">
                                <img src="../../imgs/cal.gif" style="cursor: pointer;" onclick="javascript:NewCssCal('startDate','YYMMMDD')" /> 
                            </td> 
                        </tr>
                        <tr>
                            <td>Ngày kết thúc</td>
                            <td>
                                <input type="text" id="endDate" name="endDate" readonly="readonly" value="<%=DateTimeUtil.format(trainClass.getEndDate())%>">
                                <img src="../../imgs/cal.gif" style="cursor: pointer;" onclick="javascript:NewCssCal('endDate','YYMMMDD')" /> 
                            </td> 
                        </tr>
                    </table>
                  <input type="submit" id="submit" name="update" value="Cập nhật"/> 
                </form>
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>
   
</html>
