<%@page import="uit.cnpm02.dkhp.model.TrainProgram"%>
<%@page import="uit.cnpm02.dkhp.utilities.ClientValidate"%>
<%@page import="uit.cnpm02.dkhp.model.type.AccountType"%>
<%@page import="uit.cnpm02.dkhp.model.Subject"%>
<%@page import="uit.cnpm02.dkhp.model.TrainProDetail"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    // Validate Access role
    ClientValidate.validateAcess(AccountType.ADMIN, session, response);
    
    List<TrainProDetail> trainProDetails = (List<TrainProDetail>) session.getAttribute("lst-train-pro-detail");
    List<Subject> subjects = (List<Subject>) session.getAttribute("all-subjects");
    String trainProgID = (String)session.getAttribute("train-prog-ID");
    String subIds = "";
    String subNames = "";
    if (subjects != null && !subjects.isEmpty()) {
        for(Subject s : subjects) {
            subIds += s.getId() + "--";
            subNames += s.getSubjectName() + "--";
        }
    }
    TrainProgram tp = (TrainProgram) session.getAttribute("trainprog");tp.isIsStarted();
%>
<html>
    <head>
        <link href="../../csss/menu.css" rel="stylesheet" type="text/css" media="screen">
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Cập nhật Chương trình đào tạo</title>
        <style media="all" type="text/css">
            
        </style>
    </head>
    <body onload="addRowFromLast();">
        <!--Div Wrapper-->
        <div id="wrapper">
            <input type="hidden" id="is-started" value="<%= tp != null ? tp.isIsStarted() : false %>" />
            <input type="hidden" id="trainProgId" value="<%= trainProgID %>" />
            <input type="hidden" id="subIds" value="<%= subIds %>" />
            <input type="hidden" id="subNames" value="<%= subNames %>" />
            <%-- Menu --%>
            <%@include file="MenuPDT.jsp" %>
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <!--Main Contents-->
            <div id="content">
                <%-- Title --%>
                <div id="main-title">
                    Cập nhật Chương trình đào tạo <%= " " + trainProgID %>
                </div>
                <%--List file--%>
                <br />
                <%-- Form add subject to Current train program --%>
                <div id="form-add-subject-to-train-pro" class="range">
                    <h3><span id="btn-list-existed-class" class="atag">Cập nhật chương trình đào tạo</span></h3> 
                    <div id="list-existed-class">
                        <div style="float: left;">
                            <div style="font-size: 12px; font-weight: bold; font-style: italic;">
                                Thêm lớp học vào CTĐT (* lớp trùng nhau sẽ tự động được bỏ)
                            </div>
                            <table id="tbl-add" class="general-table" style="float: left; width: 420px; maring-left: 25px;">
                                <tr>
                                    <th>Môn học</th><th>Học kỳ</th><th>Xóa</th>
                                </tr>

                            </table>
                            <div class="clear"></div>
                            <span class="atag" onclick="addRowFromLast()"><img src="../../imgs/icon/add.png" title="add" alt="add"/></span>

                            <%-- Button Add --%>
                            <div class="clear"></div>
                            <div id="btn-add-subject" class="button-1" style="padding: 2px !important; float: left; margin-left: 143px;">
                                <span class="atag" onclick="addSubjectToTrainProg()" ><img src="../../imgs/check.png" />Submit</span>
                            </div>
                            <%-- Add subject to training program result --%>
                            <div class="clear"></div>
                            <div id="add-subject-to-train-prog-result">
                                <%----%>
                            </div>
                        </div>
                        <%-- Button start Training Program --%>
                        <div id="btn-start-trainprog" style="float: left; margin-left: 20%; margin-top: 4%;">
                             <div class="button-1" style="padding: 2px !important;">
                                <span class="atag" onclick="startTrainProgram('<%=trainProgID%>')" ><img src="../../imgs/check.png" />Hoàn tất</span>
                             </div>
                             <div class="clear"></div>
                            <div class="warning">
                                <img src="../../imgs/warning.png"/>
                                <i>
                                    Sau khi xác nhận Hoàn tất cập cho<br />
                                    Chương trình đào tạo. User không<br />
                                    được quyền cập nhật hay hủy bỏ<br />
                                    chương trình đào tạo này<br />
                                </i>
                            </div>
                        </div>
                     </div>
                </div>
                <div class="range">
                    <%-- File from Lecturer --%>
                    <h3><span id="btn-list-existed-training-pro" class="atag">Chương trình đào tạo</span></h3> 
                    <div id="list-existed-training-pro">
                        <%-- DS cac lop chon them vao CTDT --%>
                        <div id="list-all-subject">
                            <table id="tbl-existed" class="general-table" style="float: left; width: 520px; maring-left: 25px;">
                                <tr>
                                    <th>STT</th>
                                    <th>Mã Môn học</th>
                                    <th>Môn học</th>
                                    <th>Học kỳ</th>
                                    <th></th>
                                </tr>
                                <%if (trainProDetails != null && !trainProDetails.isEmpty()) {
                                    int counter = 0;
                                    for (TrainProDetail tpd : trainProDetails) {%>
                                    <tr id="row_tpd<%= tpd.getId().getSubjectID()%>">
                                        <td><%= counter++ %></td>
                                        <td><%= tpd.getId().getSubjectID() %></td>
                                        <td><%= tpd.getSubjectName() %></td>
                                        <td><%= tpd.getSemester() %></td>
                                        <td><input type="checkbox" id="chk<%=counter-1%>" onclick="highLightSelectRow(this, 'row_tpd<%=tpd.getId().getSubjectID()%>')"/></td>
                                    </tr>
                                    <%}
                               }%>
                            </table>
                        </div>
                        <%-- Button Remove --%>
                        <div class="clear"></div>
                        <div id="btn-delete-subject" class="button-1" style="padding: 2px !important; float: left; margin-left: 143px;">
                            <span class="atag" onclick="deleteSubjectFromTrainProg()" ><img src="../../imgs/check.png" />Xóa</span>
                        </div>
                        <div class="clear"></div>
                        <div style="font-size: 12px; font-weight: bold; font-style: italic;">
                            Xóa mục được chon khổi CTĐT
                        </div>
                        
                    </div>
                        
                    <div id="msg-response"></div>
                </div>
                <div class="clear"></div>
                <%-- Helper --%>
                <div class="range">
                    <h3><span id="btn-form-helper" class="atag">Hướng dẫn</span></h3> 
                    <div id="form-helper" style="display: none;">
                        <div style="font-size: 12px; font-weight: bold; font-style: italic;">
                            <p>Trang cập nhật chương trình đào tạo (TCĐT) cho phép Admin thay đổi thông tin của một
                            CTĐT</p>
                            <p>
                                Để thêm môn học vào CTĐT:<br/><br/>
                                - Từ mục Cập nhật Chương trình đào tạo, Admin chọn môn học và học kỳ <br />
                                - Để thêm một môn học khác, Admin click vào nút <img src="../../imgs/icon/add.png" title="add" alt="add"/><br />
                                - Để xóa một môn đã chọn, Admin click vào nút <img src="../../imgs/icon/delete.png" title="Xóa" alt="Xóa"/><br />
                                - Cuối cùng, Admin click vào nút 
                                <span class="button-1" ><img src="../../imgs/check.png" />Submit</span>
                                để hoàn thành việc thêm môn học vào CTĐT.
                            </p>
                            <br/>
                            <p>
                                Để Xóa môn hiện có trong CTĐT:<br/>
                                - Từ mục <b>Chương trình đào tạo</b>, Admin chọn các môn học cần xóa<br />
                                - Admin click button
                                <span class="button-1" ><img src="../../imgs/check.png" />Xóa</span>
                                để hoàn thành việc xóa môn học khỏi CTĐT.
                            </p>
                            <br/>
                            <p>
                                <i>LocNGuyen: locnv.uit@gmail.com</i>
                            </p>
                        </div>
                    </div>
                </div>
                <br />
            </div><!--End Contents-->

            <!--Footer-->
            <div id="footer">
                <%@include file="../Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
        <script src="../../javascripts/AjaxUtil.js"> </script>
        <script src="../../javascripts/UtilTable.js"> </script>
        <script type="text/javascript" src="../../javascripts/jquery-1.7.1.js"></script>
        <script src="../../javascripts/jquery-1.7.1.js"></script>
        <script  type = "text/javascript" >
            window.onload = function() {
                var isStarted = $('#is-started').val();
                if (isStarted == 'true') {
                    $("#btn-start-trainprog").hide();
                    $("#btn-add-subject").hide();
                    $("#btn-delete-subject").hide();
                }
            }
            
            var http = createRequestObject();
            
            $("#btn-list-existed-class").click(function () {
                $('#list-existed-class').slideToggle(500);
            });
            
            $("#btn-list-existed-training-pro").click(function () {
                $('#list-existed-training-pro').slideToggle(500);
            });
            $("#btn-form-helper").click(function () {
                $('#form-helper').slideToggle(500);
            });
            
            function addSubjectToTrainProg() {
                var listSubIDs = "";
                // Get list subject will be add to train program
                var tbl = document.getElementById('tbl-add');
                var tbSize = tbl.rows.length;
                for (var i = 1; i < tbSize; i++) {
                    var cell = tbl.rows[i].cells[0];
                    var sel = cell.getElementsByTagName("select")[0];
                    var subId = sel.value;
                    if (subId != null && subId.length > 0) {
                        listSubIDs += subId + "-";
                    } else {
                        continue;
                    }
                    
                    var cell2 = tbl.rows[i].cells[1];
                    var selSemeter = cell2.getElementsByTagName("select")[0];
                    var semeter = selSemeter.value;
                    listSubIDs += semeter + ";";
                }
                if (listSubIDs == null || listSubIDs.length <= 0) {
                    alert("Vui lòng thêm môn học trước.");
                    return;
                }
                
                var trainProId = document.getElementById("trainProgId").value;
                var controller = "../../TrainingProgramControler?action=add-subject-to-traing-prog"
                        + "&data=" + listSubIDs
                        + "&trainProgID=" + trainProId;
                if(http){
                    http.open("GET", controller, true);
                    http.onreadystatechange = addSubjectToTrainProgHandler;
                    http.send(null);
                } else {
                    alert("Error: http object not found");
                }
            }
            
            function addSubjectToTrainProgHandler() {
                if(http.readyState == 4 && http.status == 200){
                    var detail = document.getElementById("add-subject-to-train-prog-result");
                    detail.innerHTML = http.responseText;
                }
            }
            
            function deleteSubjectFromTrainProg() {
                var listSubIDs = "";
                var tbl = document.getElementById('tbl-existed');
                var tbSize = tbl.rows.length;
                for (var i = 1; i < tbSize; i++) {
                    var cell = tbl.rows[i].cells[4];
                    var checkbox = cell.getElementsByTagName("input")[0];
                    if (checkbox.checked) {
                        var subCell = tbl.rows[i].cells[1];
                        var subId = subCell.innerHTML;
                        listSubIDs += subId + ";";
                    }
                }
                if (listSubIDs == null || listSubIDs.length <= 0) {
                    alert("Không có lớp nào được xóa.");
                    return;
                }
                
                var trainProId = document.getElementById("trainProgId").value;
                var controller = "../../TrainingProgramControler?action=remove-subject-to-traing-prog"
                        + "&data=" + listSubIDs
                        + "&trainProgID=" + trainProId;
                if(http){
                    http.open("GET", controller, true);
                    http.onreadystatechange = deleteSubjectFromTrainProgHandler;
                    http.send(null);
                } else {
                    alert("Error: http object not found");
                }
            }
            
            function deleteSubjectFromTrainProgHandler() {
                if(http.readyState == 4 && http.status == 200){
                    var detail = document.getElementById("list-all-subject");
                    detail.innerHTML = http.responseText;
                }
            }
            
            function addRowFromLast() {
                var tbl = document.getElementById('tbl-add');
                var lastRow = tbl.rows.length;
                var i = 0;
                var row = tbl.insertRow(lastRow);
                var rowId = new Date().getTime();
                row.id = "tbl-row" + rowId;
                
                // DS lop
                var cellSubject = row.insertCell(0);
                var sel = document.createElement('select');
                sel.name = 'subrow' + rowId;
                var subIds = document.getElementById("subIds").value;
                var subNames = document.getElementById("subNames").value;
                if (subIds != null && subNames != null) {
                    var subIdArray = subIds.split("--");
                    var subNameArray = subNames.split("--");
                    for (i = 0; i < subIdArray.length; i++) {
                        sel.options[i+1] = new Option(subNameArray[i], subIdArray[i]);
                    }
                }
                cellSubject.appendChild(sel);

                // Hoc ky
                var cellSemeter = row.insertCell(1);
                var sel = document.createElement('select');
                sel.name = 'semeter' + rowId;
                for (i = 0; i < 10; i++) {
                    sel.options[i] = new Option('Học kỳ ' + (i+1), i+1);
                }
                cellSemeter.appendChild(sel);

                // Button Delete
                var cellLeft = row.insertCell(2);
                var spanTag = document.createElement("span"); 
                spanTag.id = "span1"; 
                spanTag.className = "atag";
                
                spanTag.innerHTML = "<span class=\"atag\" onclick=\"removeRow("+ rowId +")\"><img src=\"../../imgs/icon/delete.png\" title=\"Xóa\" alt=\"Xóa\"/></span>"; 
                cellLeft.appendChild(spanTag);
            }
            
            function removeRow(rowID) {
                var tbl = document.getElementById('tbl-add');
                var lastRow = tbl.rows.length;
                for (var i = 0; i < lastRow; i++) {
                    var row = tbl.rows[i];
                    
                    if (row.id == ("tbl-row" + rowID)) {
                        tbl.deleteRow(i);
                        return;
                    }
                }
            }
            
            function highLightSelectRow(chb, row) {
                // validate condition
                 
                var trobj = document.getElementById(row);
                 
                if(chb.checked){
                    trobj.setAttribute("class", 'datahighlight');
                } else {
                    trobj.removeAttribute("class", 'datahighlight');
                }
            }
            
            function startTrainProgram(progId) {
                if (confirm("CTĐT sau khi hoàn tất sẽ không thể cập nhật hay hủy bỏ.\n\
                            Ấn OK để xác nhận.")) {
                    var controller = "../../TrainingProgramControler?action=start-traing-prog"
                        + "&trainProgID=" + progId;
                    if(http){
                        http.open("GET", controller, true);
                        http.onreadystatechange = startTrainProgHandler;
                        http.send(null);
                    } else {
                        alert("Error: http object not found");
                    }
                    //alert("OK, TrainProg will be finished.");
                }
            }
            
            function startTrainProgHandler() {
                if(http.readyState == 4 && http.status == 200){
                    var responeText = http.responseText;
                    if (responeText.indexOf("OK") != -1) {
                        alert("Cập nhật thành công.");
                        $("#btn-start-trainprog").hide();
                        $("#btn-add-subject").hide();
                        $("#btn-delete-subject").hide();
                        //
                    } else if (responeText.indexOf("NOK") != -1) {
                        alert("Đã có lỗi xảy ra, vui lòng thử lại sau.");
                    } else {
                        alert("Unknown respone from server");
                    }
                }
            }
        </script>
    </body>
</html>