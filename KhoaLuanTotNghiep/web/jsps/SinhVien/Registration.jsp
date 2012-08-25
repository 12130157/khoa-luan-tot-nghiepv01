<%-- 
    Document   : jspXemChuongTringDaoTao
    Created on : Apr 23, 2011, 4:33:27 PM
    Author     : ngloc_it
--%>

<%@page import="uit.cnpm02.dkhp.utilities.ClientValidate"%>
<%@page import="uit.cnpm02.dkhp.model.type.AccountType"%>
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
    // Validate access role
    ClientValidate.validateAcess(AccountType.STUDENT, session, response);
    
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
    
    int min_tc = (Integer) session.getAttribute("min-tc");
    int max_tc = (Integer) session.getAttribute("max-tc");
%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="../../javascripts/jquery-1.7.1.js"></script>
        <script src="../../javascripts/chart/RGraph.common.core.js"> </script>
        <script src="../../javascripts/chart/RGraph.common.dynamic.js"> </script>
        <script src="../../javascripts/chart/RGraph.common.effects.js"> </script>
        <script src="../../javascripts/chart/RGraph.common.key.js"> </script>
        <script src="../../javascripts/chart/RGraph.common.tooltips.js"> </script>
        <script src="../../javascripts/chart/RGraph.line.js"> </script>
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
                <input type="hidden" id="my_list" value="<%= registried.toString() %>"/>
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
               <div class="clear"></div>
                 <%-- A popup show list of student already registered on a class --%>
                 <div id="popup-reg-support" style="border: solid 2px #666; border-radius: 5px; display: none;">
                     <%--onClick="hideMe('popup-reg-support');"--%>
                     <%-- List student registered --%>
                     <div id="lst-student-reg" class="support">
                        <img src = '../../imgs/icon/loading.gif' />
                    </div>
                     <div class="clear"></div>
                     <%-- List pre subject of selected subject --%>
                     <div id="lst-pre-subject" class="support hide">
                         <div id="popup-title">DS Môn học tiên quyết</div> <br />
                         <div class="support-data">
                             <ul>
                             </ul>
                         </div>
                    </div>
                     <div class="clear"></div>
                     <%-- The chart report the subject's history --%>
                     <div id="chart-subject" class="support hide">
                         <div id="popup-title">Lịch sử môn học</div> <br />
                        <canvas id="sub_history_chart" class="support-chart" width="700" height="280">
                            Your browser does not support the HTML5 canvas tag.
                        </canvas>
                     </div>
                     <div class="clear"></div>
                     <%-- The chart report the lecturer & subject 's history --%>
                     <div id="chart-lecturer-subject" class="support hide">
                         <div id="popup-title">Một vài thống kê theo GV</div> <br />
                         <canvas id="lecturer_chart" class="support-chart" width="700" height="280">
                            Your browser does not support the HTML5 canvas tag.
                        </canvas>
                         <%-- Select year --%>
                         <div style="position: absolute; right: 52px; bottom: 63px;">
                             <%
                             int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                             %>
                             <select id="select-year" style="width: 120px;">
                                 <%for (int i = 0; i < 5; i++) {
                                     String yearStr = (currentYear-i-1) +"-" + (currentYear-i);
                                     %>
                                 <option value="<%= yearStr %>"><%= yearStr %></option> 
                                 <%}%>
                             </select>
                         </div>
                     </div>
                     <%-- Control table --%>
                     <div id="tbl-support-control">
                         <table>
                             <tr>
                                 <td><span id="lst-reg-student" class="atag1 button-3 option-selected">DS SV đã ĐK</span></td>
                                 <td><span id="lst-pre-subject" class="atag1 button-3">Môn học TQ</span></td>
                                 <td><span id="subject-history" class="atag1 button-3">Lịch sử môn học</span></td>
                                 <td><span  id="subject-lecturer-history" class="atag1 button-3">Thống kể theo GV</span></td>
                                 <td><span  id="hide-support" class="atag1 button-3">Ẩn</span></td>
                             </tr>
                         </table>
                     </div>
                </div>
                 <%-- Main form --%>
               <form id="formdetail" name="formdetail" action="../../RegistryController?action=registry" method="post">
                    <div id="reg-info" style="float: left; font-size: 12px; font-weight: bold; font-style: italic;">
                        <p>(*) Sinh viên môn học muốn đk và click nút Đăng Ký <br /></p>
                        <p>(*) Số TC tối thiểu - Tối đa cho phép đăng ký:<b> [<%= min_tc %> - <%= max_tc%>]</b></p>
                        <div id="tc-reged"></div>
                    </div>
                    <%-- Popup --%>
                    <input type="hidden" id="min-tc" value="<%= min_tc %>"/>
                    <input type="hidden" id="max-tc" value="<%= max_tc %>"/>
                    <div id="warning-window" style="display: none;">
                       <%----%>
                    </div>
                    <div class="btn-refresh">
                       <img src="../../imgs/icon/refresh.gif"/>
                       <input type="hidden" id="remaining-time" value="30"/>
                       <span id="remaining-time-display">(30 s)</span>
                    </div>
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
                         TrainClass tc = trainClass.get(i);
                     %>
                     <tr id="tr_main_<%=i%>" <%=clazz%>>
                         <input type="hidden" class="tcId" value="<%=tc.getId().getClassCode() %>"/>
                         <input type="hidden" class="semester" value="<%=tc.getId().getSemester() %>"/>
                         <input type="hidden" class="year" value="<%=tc.getId().getYear() %>"/>
                         <%-- STT --%>
                         <td><%=i+1%></td>
                         <%-- Ma lop --%>
                         <%--<td><a href="../../RegistryController?action=detail&classCode=<%=tc.getId().getClassCode()%>&semester=<%=tc.getId().getSemester()%>&year=<%=tc.getId().getYear()%>"><%=tc.getId().getClassCode()%></a></td>--%>
                         <td>
                             <span class="atag" onclick="getListStudentReged('<%=tc.getSubjectCode() %>', '<%=tc.getLecturerCode() %>', '<%=tc.getId().getClassCode()%>',<%=tc.getId().getSemester()%>,'<%=tc.getId().getYear()%>')"><%=tc.getId().getClassCode()%></span>
                         </td>
                         <td><%=tc.getSubjectName()%></td>
                         <td><%=tc.getNumTC()%></td>
                         <td><%=tc.getLectturerName()%></td>
                         <td><%=tc.getStudyDate()%></td>
                         <%-- Ca hoc cua lop <Sang/Chieu> --%>
                         <%if(tc.getShift() == 1){%>
                             <td>Sáng</td>
                             <%}else{%>
                             <td>Chiều</td>
                         <%}%>
                         <%-- Phon hoc --%>
                         <td><%=tc.getClassRoom()%></td>
                         <%-- SLSV toi da cua lop --%>
                         <td class="number_max"><%=tc.getNumOfStudent()%></td>
                         <%-- SLSV da dang ky cua lop --%>
                         <td class="current_number_reg"><%=tc.getNumOfStudentReg()%></td>
                         <%-- Check box cho phep SV chon co dk hoc lop nay hay khong
                            SV ko dc dang ky neu SLSV da dk vuot qua SLSV toi da
                            SV ko dc dang ky neu ko du cac dk theo qui che Tin Chi: ...
                         --%>
                         <%if(checked){%>
                             <td width="10px">
                                 <input type="checkbox" name="check" checked="true" value="<%=tc.getId().getClassCode()%>" onclick="validateSelectTrainClass(this, 'tr_main_<%=i%>')"/>
                             </td>
                         <%}else {
                             if(tc.getNumOfStudentReg() >= tc.getNumOfStudent()){%>
                             <td width="10px"><input type="checkbox" disabled name="check" value="<%=tc.getId().getClassCode()%>"/></td>
                             <%}else{%>
                             <td width="10px"><input type="checkbox" name="check" value="<%=tc.getId().getClassCode()%>" onclick="validateSelectTrainClass(this, 'tr_main_<%=i%>')"/></td>
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
                             <input type="hidden" class="tcId" value="<%=tcTemp.getId().getClassCode() %>"/>
                            <input type="hidden" class="semester" value="<%=tcTemp.getId().getSemester() %>"/>
                            <input type="hidden" class="year" value="<%=tcTemp.getId().getYear() %>"/>
                         <%-- STT --%>
                         <td><%=i+1%></td>
                         <%-- Ma lop --%>
                         <td>
                            <span class="atag" onclick="getListStudentReged('<%=tcTemp.getSubjectCode() %>', '<%=tcTemp.getLecturerCode() %>','<%=tcTemp.getId().getClassCode()%>',<%=tcTemp.getId().getSemester()%>,'<%=tcTemp.getId().getYear()%>')"><%=tcTemp.getId().getClassCode()%></span>
                         </td>
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
                         <td class="number_max"><%=tcTemp.getNumOfStudent()%></td>
                         <%-- SLSV da dang ky cua lop --%>
                         <td class="current_number_reg"><%=tcTemp.getNumOfStudentReg()%></td>
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
        <script src="../../javascripts/Registration.js"> </script>
        <script  type = "text/javascript" >
            //var http = createRequestObject();
            
            $("#btn-chkbox_external_trainclass").click(function () {
                $('#ext-detail').slideToggle(50);
            });
            
            window.onload = function() {
                refressNumberRegistrated();
            }

            var time_counter = 30;
            function refressNumberRegistrated() {
                setTimeout(function() {
                    refressNumberRegistrated();
                    
                    var remainingTime = $('#remaining-time').val();
                    if (remainingTime <= 0) {
                        var json = getNumberRegistrated();
                        if ((json != null) && (json.length > 0)) {
                            updateNumberRegistrationStatus(json);
                        }
                        
                        remainingTime = 30;
                    } else {
                        remainingTime--;
                    }
                    //console.log(remainingTime);
                    $('#remaining-time').val(remainingTime);
                    $('#remaining-time-display').html('(' +remainingTime + ' s)');
                }, 1000);
            }

            var waringPopupDisplayed = false;
            function validateSelectTrainClass(chb, row) {
                // validate condition
                var trobj = document.getElementById(row);
                 
                if(chb.checked){
                    trobj.setAttribute("class", 'datahighlight');
                } else {
                    trobj.removeAttribute("class", 'datahighlight');
                }
                
                var min_tc = $('#min-tc').val();
                var max_tc = $('#max-tc').val();
                var num_tc = getNumberRegs();
                var content = '';
                if (num_tc < min_tc) {
                    content = 'Số TC chưa đủ';
                } else if (num_tc > max_tc) {
                    content = 'Số TC vượt quá qui định';
                }
                var popup = $('#warning-window');
                if (waringPopupDisplayed && content == '') {
                    //popup.innerHTML = content;
                    $(popup).slideToggle(100);
                    waringPopupDisplayed = false;
                } else if (!waringPopupDisplayed && content != '') {
                    popup.html(content);
                    $(popup).slideToggle(100);
                    waringPopupDisplayed = true;
                }
                
                // Update information
                var numRegInfo = $('#tc-reged');
                numRegInfo.html("(*) Tổng số TC đã chọn: " + num_tc);
                //reg-info
            }
            
            function getNumberRegs() {
                var num_main = getNumberRegsOnTable('detail');
                var num_ext = getNumberRegsOnTable('ext-detail');
                
                return num_main + num_ext;
            }
            
            function getNumberRegsOnTable(id) {
                var tbl = document.getElementById(id);
                var tbSize = tbl.rows.length;
                var total = 0;
                for (var i = 1; i < tbSize; i++) {
                    var cell = tbl.rows[i].cells[10];
                    var checkbox = cell.getElementsByTagName("input")[0];
                    if (checkbox.checked) {
                        var subCell = tbl.rows[i].cells[3]; // get number TC per selected row
                        var tc = parseInt(subCell.innerHTML);
                        total += tc;
                    }
                }
                
                return total;
            }
            
            function getListStudentReged(subId, lecturerId, trainclassID, semeter, year) {
                bindSupportEvent(subId, lecturerId);
                $('#popup-reg-support').fadeIn('slow', function() {
                // Animation complete
                    var controller = '../../RegistryController?action=get-reged-students' 
                                + '&trainclassid=' + trainclassID
                                + '&semeter=' + semeter
                                + '&year=' + year;
                    if(http){
                        http.open("GET", controller, true);
                        http.onreadystatechange = getListStudentRegedHandler;
                        http.send(null);
                    } else {
                        alert("Lỗi gửi yêu cầu tới Server thất bại");
                    }
                });
            }
            
            function getListStudentRegedHandler() {
                if(http.readyState == 4 && http.status == 200){
                     var detail = document.getElementById("lst-student-reg");
                     detail.innerHTML = http.responseText;
                }  
            }
        </script>
        
    </body>
 </html>
