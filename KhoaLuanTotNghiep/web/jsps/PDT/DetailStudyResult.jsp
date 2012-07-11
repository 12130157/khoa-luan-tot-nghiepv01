<%-- 
    Document   : jspXemChuongTringDaoTao
    Created on : Apr 23, 2011, 4:33:27 PM
    Author     : ngloc_it
--%>

<%@page import="uit.cnpm02.dkhp.DAO.SubjectDAO"%>
<%@page import="uit.cnpm02.dkhp.model.StudyResult"%>
<%@page import="java.util.List"%>
<%@page import="uit.cnpm02.dkhp.model.Faculty"%>
<%@page import="uit.cnpm02.dkhp.model.Student"%>
<%@page import="uit.cnpm02.dkhp.model.Class"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
List<StudyResult> studyResult=(List<StudyResult>) session.getAttribute("studyResult");
List<String> yearList=(List<String>) session.getAttribute("yearList");
Student student=(Student) session.getAttribute("student");
Class classes=(Class) session.getAttribute("classes");
Faculty faculty=(Faculty)session.getAttribute("faculty");
    SubjectDAO subjectDao=new SubjectDAO();
    int m = studyResult.size();
    int i, j;
    int numTC = 0;
    float SumMark = 0;
    float Average = 0;
    for (j = 0; j < m; j++) {
        numTC += subjectDao.findById(studyResult.get(j).getId().getSubjectCode()).getnumTC();
        SumMark += (subjectDao.findById(studyResult.get(j).getId().getSubjectCode()).getnumTC() * studyResult.get(j).getMark());
        Average = (float) Math.round(SumMark * 100 / numTC) / 100;
    }
%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Kết quả học tập</title>
        <style media="all" type="text/css">

            #formstudent{
                margin-left: 20px;
                margin-top: 10px;
                padding-top: 20px;
                padding-bottom: 20px;
                padding-right: 10px;
                padding-left: 10px;                                
                width: 100%;
            }
            #formstudent table{
                width: 100%;
                padding-left: 5px;
                padding-right: 5px;
            }
            #formstudent table td{
                width: 100px;
            }
            #formstudent table th{
                width: 200px;
                text-align: left;
            }
                            
            #form-result{
                margin-left: 20px;
                margin-bottom: 20px;
                padding-top: 20px;
                padding-bottom: 20px;
                padding-right: 10px;
                padding-left: 10px;                
                border: 3px solid #7F38EC;
                width: 99%;
            }
            #info{
                width: 100%;
            }
            #formdetail{
                width: 99%;
            }
            #detail{
                width: 90%;
            }
             #detail th{
               text-align: center;
            }
            
        </style>
     </head>
    <body>
        <!--Div Wrapper-->
        <div id="wrapper">
            <%@include file="MenuPDT.jsp" %>
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <div id="main-title">KẾT QUẢ HỌC TẬP</div>
                <div>
                    <form action="" name="formstudent"  id="formstudent">
                        <table id="info">
                        <tr>
                            <td width="200px">MSSV: </td>
                            <th width="100px"><%=student.getId()%></th>
                            <td width="200px">Họ và tên: </td>
                            <th><%=student.getFullName()%></th>
                        </tr>
                        <tr>
                            <td width="200px">Lớp: </td>
                            <th width="100px"><%=classes.getClassName()%></th>
                            <td width="200px">Khoa: </td>
                            <th><%=faculty.getFacultyName()%></th>
                        </tr>
                        <tr>
                            <td width="200px">Số tín chỉ đã tích lũy: </td>
                            <th width="100px"><%=numTC%></th>
                            <td width="200px">Điểm trung bình:</td>
                            <th><%=Average%></th>
                        </tr>
                        <tr>
                            <td width="50px">Năm học:  </td>
                            <td>
                                <select style="width:90px" name="year" id="year" onchange="reloadResult()">
                                    <option value="All">Tất cả</option>
                                    <% for (i = 0; i < yearList.size(); i++) {%>
                                    <option value="<%=yearList.get(i)%>"><%=yearList.get(i)%></option>
                                    <%}%>
                                </select>
                            </td>
                            <td width="50px">Học kỳ:</td>
                            <td>
                                <select style="width:70px" name="semester" onchange="reloadResult()">
                                    <option value="0">Tất cả</option>
                                    <option value="1">1</option>
                                    <option value="2">2</option>
                                </select>
                            </td>
                        </tr>

                    </table>
                </form>
                </div>
               <hr/><hr/>
                 <form id="formdetail" name="formdetail">
                    <u>Chi tiết</u>
                    <table id="detail" name="detail" class="general-table" >
                     <tr>
                            <th width="100px">Năm học</th><th width="70px">Học kỳ</th>
                            <th width="100px">Mã môn</th><th width="300px">Tên môn học</th>
                            <th width="70px">Số TC</th><th width="80px">Điểm</th>
                            <th width="100px">Nhân hệ số</th>
                            <th width="50px">Sửa</th>
                     </tr>   
                      <%
                                 numTC = 0;
                                 SumMark = 0;
                                 Average = 0;
                                 for (j = 0; j < m; j++) {
                                  int numTCSubject=subjectDao.findById(studyResult.get(j).getId().getSubjectCode()).getnumTC();
                                  float markSubject=(subjectDao.findById(studyResult.get(j).getId().getSubjectCode()).getnumTC() * studyResult.get(j).getMark());
                        %>
                        <tr>
                            <td><%=studyResult.get(j).getYear()%></td>
                            <td><%=studyResult.get(j).getSemester()%></td>
                            <td><%=studyResult.get(j).getId().getSubjectCode()%></td>
                            <td><%=studyResult.get(j).getSubjectName()%></td>
                            <td><%=numTCSubject%></td>
                            <td><%=studyResult.get(j).getMark()%></td>
                            <td><%=markSubject%></td>
                            <td><a href="../../StudyResultManager?action=update&studentCode=<%=student.getId()%>&subjectCode=<%=studyResult.get(j).getId().getSubjectCode()%>">Sửa</a></td>
                        </tr>
                        <%
                         numTC += numTCSubject;
                         SumMark += markSubject;
                         Average = (float) Math.round(SumMark * 100 / numTC) / 100;
                         }%>
                        <tr>
                            <th align="center">
                                Tổng kết
                            </th>
                            <th ></th>
                            <th></th>
                            <th align="center">Trung bình: <%= Average%></th>
                            <th align="center"><%=numTC%></th>
                            <th></th>
                            <th align="center"><%=SumMark%></th>
                            <th align="center"></th>
                        </tr>
                    </table>
                 </form>
                <form action="" method="post" id="frmexport">
                    <div style="margin-left: 40%;">
                        <a class="button-1" href="../../DownloadController?action=studentresult&mssv=<%=student.getId()%>"><img src="../../imgs/download.png"/>Tải bảng điểm</a>
                        <input type="hidden" id="studentCode" name="studentCode" value="<%=student.getId()%>"/>
                    </div>
                </form>
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    </body>
    <script src="../../javascripts/jquery-1.7.1.js"></script>
    <script src="../../javascripts/StudyResult.js"></script>
    <script  type = "text/javascript" >
        var http = createRequestObject();
        var year="2007-2008";
        var semester=1;
        var studentCode ="";
        function reloadResult(){
             year=document.formstudent.year.value;
             semester=document.formstudent.semester.value;
             studentCode= document.getElementById("studentCode").value;
            SendRequest();
        }
        function SendRequest(){
              if(http){
              ajaxfunction("../../StudyResultManager?action=reload&studentCode=" + studentCode + "&year="+year+"&semester="+semester );
             }
         }
   </script>
</html>
