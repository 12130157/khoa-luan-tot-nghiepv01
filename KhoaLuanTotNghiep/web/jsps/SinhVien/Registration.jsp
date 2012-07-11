<%-- 
    Document   : jspXemChuongTringDaoTao
    Created on : Apr 23, 2011, 4:33:27 PM
    Author     : ngloc_it
--%>

<%@page import="uit.cnpm02.dkhp.utilities.StringUtils"%>
<%@page import="uit.cnpm02.dkhp.model.TrainClass"%>
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
    List<TrainClass> trainClass = (List<TrainClass>) session.getAttribute("trainClass");
    List<TrainClass> extTrainClasses = (List<TrainClass>) session.getAttribute("ExtTrainClass");
    String year = (String) session.getAttribute("year");
    Integer semester = (Integer) session.getAttribute("semester");
    Student student = (Student) session.getAttribute("student");
    Class classes = (Class) session.getAttribute("classes");
    Faculty faculty = (Faculty) session.getAttribute("faculty");
    
    // Danh sach lop ma SV da dk
    List<String> registried = (List<String>) session.getAttribute("registriedID");
    boolean nonStudy=(Boolean) session.getAttribute("non-study-student");
%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Đăng ký môn học</title>
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
            #info{
                width: 100%;
            }
            #formdetail{
                width: 99%;
            }
         </style>
        
    </head>
    <body>
        <!--Div Wrapper-->
        <div id="wrapper">
            <%@include file="MenuSV.jsp" %>
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <div id="main-title">Đăng ký học phần hoc kỳ <%=semester%> năm học <%=year%></div>
                <div class="clear"></div>
                <br />
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
                    </table>
                </form>
                </div>
               <hr/><hr/>
               <form id="formdetail" name="formdetail" action="../../RegistryController?action=registry" method="post">
                    <div style="float: left; font-size: 12px; font-weight: bold; font-style: italic;">
                        (*) Sinh viên môn học muốn đk và click nút Đăng Ký
                    </div>
                   <%--<div id="btn-chkbox_external_trainclass" class="chkbox_external_trainclass">
                       <input type="checkbox" class="button-1" value="Hiển thị lớp thuộc khoa khác" />
                    </div>--%>
                   
                   <div class="clear"></div>
                    <table id="detail" name="detail" class="general-table" >
                     <tr>
                         <th width="10px">STT</th>
                         <th width="70px">Mã lớp</th>
                         <th width="200px">Môn học</th>
                         <th width="10px">TC</th>
                         <th width="200px">Giảng viên</th>
                         <th width="10px">Thứ</th>
                         <th width="50px">Ca</th>
                         <th width="50px">Phòng</th>
                         <th width="50px">Tối đa</th>
                         <th width="50px">Đã ĐK</th>
                         <th width="10px"><INPUT type="checkbox" name="checkAll" onclick="selectAll('detail',10)" /></th>
                     </tr>   
                     <%for(int i = 0; i < trainClass.size(); i++){
                         boolean checked = StringUtils.checkStringExitList(trainClass.get(i).getId().getClassCode(), registried);
                         String clazz = "class='"+ (checked ? "datahighlight" : "") + "'";
                     %>
                     <tr id="tr_main_<%=i%>" <%=clazz%>>
                         
                         <%-- STT --%>
                         <td><%=i+1%></td>
                         <%-- Ma lop --%>
                         <td><a href="../../RegistryController?action=detail&classCode=<%=trainClass.get(i).getId().getClassCode()%>&semester=<%=trainClass.get(i).getId().getSemester()%>&year=<%=trainClass.get(i).getId().getYear()%>"><%=trainClass.get(i).getId().getClassCode()%></a></td>
                         <td><%=trainClass.get(i).getSubjectName()%></td>
                         <td><%=trainClass.get(i).getNumTC()%></td>
                         <td><%=trainClass.get(i).getLectturerName()%></td>
                         <td><%=trainClass.get(i).getStudyDate()%></td>
                         <%-- Ca hoc cua lop <Sang/Chieu> --%>
                         <%if(trainClass.get(i).getShift()==1){%>
                             <td>Sáng</td>
                             <%}else{%>
                             <td>Chiều</td>
                         <%}%>
                         <%-- Phon hoc --%>
                         <td><%=trainClass.get(i).getClassRoom()%></td>
                         <%-- SLSV toi da cua lop --%>
                         <td><%=trainClass.get(i).getNumOfStudent()%></td>
                         <%-- SLSV da dang ky cua lop --%>
                         <td><%=trainClass.get(i).getNumOfStudentReg()%></td>
                         <%-- Check box cho phep SV chon co dk hoc lop nay hay khong
                            SV ko dc dang ky neu SLSV da dk vuot qua SLSV toi da
                            SV ko dc dang ky neu ko du cac dk theo qui che Tin Chi: ...
                         --%>
                         <%if(checked){%>
                             <td width="10px">
                                 <input type="checkbox" name="check" checked="true" value="<%=trainClass.get(i).getId().getClassCode()%>" onclick="validateSelectTrainClass(this, 'tr_main_<%=i%>')"/>
                             </td>
                         <%}else {
                             if(trainClass.get(i).getNumOfStudentReg() >= trainClass.get(i).getNumOfStudent()){%>
                             <td width="10px"><input type="checkbox" disabled name="check" value="<%=trainClass.get(i).getId().getClassCode()%>"/></td>
                             <%}else{%>
                             <td width="10px"><input type="checkbox" name="check" value="<%=trainClass.get(i).getId().getClassCode()%>" onclick="validateSelectTrainClass(this, 'tr_main_<%=i%>')"/></td>
                             <%}
                         }%>
                     </tr>
                     <%}%>
                     </table>
                    <span class="atag" id="btn-chkbox_external_trainclass" style="">
                        Danh sách mở rộng (<i>Gồm các lớp không thuộc khoa SV đang theo học</i>)
                    </span>
                     <%-- External trainclass - Danh sach lop hoc thuoc khoa khac khoa cua SV --%>
                     <table id="ext-detail" name="detail" class="general-table" style="display: none;">
                     <tr>
                         <th width="10px">STT</th>
                         <th width="70px">Mã lớp</th>
                         <th width="200px">Môn học</th>
                         <th width="10px">TC</th>
                         <th width="200px">Giảng viên</th>
                         <th width="10px">Thứ</th>
                         <th width="50px">Ca</th>
                         <th width="50px">Phòng</th>
                         <th width="50px">Tối đa</th>
                         <th width="50px">Đã ĐK</th>
                         <th width="10px"><%--<INPUT type="checkbox" name="checkAll" onclick="selectAll('detail',10)" />--%></th>
                     </tr>
                     <%for(int i = 0; i < extTrainClasses.size(); i++){
                         TrainClass tcTemp = extTrainClasses.get(i);
                         boolean checked1 = StringUtils.checkStringExitList(tcTemp.getId().getClassCode(), registried);
                         String clazz = "class='"+ (checked1 ? "datahighlight" : "") + "'";
                         %>
                         <tr id="tr_<%=i%>" <%=clazz%>>
                         <%-- STT --%>
                         <td><%=i+1%></td>
                         <%-- Ma lop --%>
                         <td><a href="../../RegistryController?action=detail&classCode=<%=tcTemp.getId().getClassCode()%>&semester=<%=tcTemp.getId().getSemester()%>&year=<%=tcTemp.getId().getYear()%>"><%=tcTemp.getId().getClassCode()%></a></td>
                         <td><%=tcTemp.getSubjectName()%></td>
                         <td><%=tcTemp.getNumTC()%></td>
                         <td><%=tcTemp.getLectturerName()%></td>
                         <td><%=tcTemp.getStudyDate()%></td>
                         <%-- Ca hoc cua lop <Sang/Chieu> --%>
                         <%if(tcTemp.getShift() == 1){%>
                             <td>Sáng</td>
                             <%}else{%>
                             <td>Chiều</td>
                         <%}%>
                         <%-- Phon hoc --%>
                         <td><%=tcTemp.getClassRoom()%></td>
                         <%-- SLSV toi da cua lop --%>
                         <td><%=tcTemp.getNumOfStudent()%></td>
                         <%-- SLSV da dang ky cua lop --%>
                         <td><%=tcTemp.getNumOfStudentReg()%></td>
                         <%-- Check box cho phep SV chon co dk hoc lop nay hay khong
                            SV ko dc dang ky neu SLSV da dk vuot qua SLSV toi da
                            SV ko dc dang ky neu ko du cac dk theo qui che Tin Chi: ...
                         --%>
                         <%if(checked1){%>
                             <td width="10px">
                                 <input type="checkbox" name="check" checked="true" value="<%=tcTemp.getId().getClassCode()%>" onclick="validateSelectTrainClass(this, 'tr_<%=i%>')"/>
                             </td>
                         <%}else {
                             if(tcTemp.getNumOfStudentReg() >= tcTemp.getNumOfStudent()){%>
                             <td width="10px">
                                 <input type="checkbox" disabled name="check" value="<%=tcTemp.getId().getClassCode()%>" />
                             </td>
                             <%}else{%>
                             <td width="10px">
                                 <input type="checkbox" name="check" value="<%=tcTemp.getId().getClassCode()%>" onclick="validateSelectTrainClass(this, 'tr_<%=i%>')"/>
                             </td>
                             <%}
                         }%>
                     </tr>
                     <%}%>
                     </table>
                     <br />
                      <%
                    if (nonStudy) {
                    %>
                        <div class="clear"></div>
                        <b>Sinh viên <b><%=" " + student.getFullName() + " "%></b> đang tạm thời bị đình chỉ học tập không được phép đăng ký học phần</b> 
                    <%} else {%>
                        <input type="submit" class="button-1" style="margin-left: 45%;" value="Đăng ký" />
                    <%}%>
                 </form>
                     <div class="clear"></div>
                     <br />
                </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
    
        <script src="../../javascripts/AjaxUtil.js"> </script>
        <script type="text/javascript" src="../../javascripts/jquery-1.7.1.js"></script>
        <script  type = "text/javascript" >
            //var http = createRequestObject();
            
            $("#btn-chkbox_external_trainclass").click(function () {
                $('#ext-detail').slideToggle(50);
            });

             function validateSelectTrainClass(chb, row) {
                // validate condition
                 
                var trobj = document.getElementById(row);
                 
                if(chb.checked){
                    trobj.setAttribute("class", 'datahighlight');
                } else {
                    trobj.removeAttribute("class", 'datahighlight');
                }
            }
        </script>
    </body>
 </html>
