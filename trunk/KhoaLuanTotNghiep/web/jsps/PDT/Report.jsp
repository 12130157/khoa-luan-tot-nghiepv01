<%-- 
    Document   : Report
    Created on : 22-04-2012, 22:54:46
    Author     : LocNguyen
--%>

<%@page import="java.util.Calendar"%>
<%@include file="MenuPDT.jsp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Report</title>
        <style media="all" type="text/css">

            #report-range {
                width: 100%;
                padding-left: 10px;
                padding-right: 10px;
                padding-bottom: 25px;
                text-align: center;

            }
            
            #report-range td {
                text-align: left;
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
            #createLabel{
                padding-right: 15px;
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
                <div id="report-range">
                    THONG KE THEO SINH VIEN
                </div>
                <div id="search">
                    <table>
                        <tr>
                            <td>
                                <b>Nhập họ tên SV or MSSV </b>
                                <input id="search-student" type="text" value=""/>
                            </td>
                            <td>
                                <input type="button" onclick="SendRequestCreateNewTrainClass()" value="Tìm"/>
                            </td>
                        </tr>
                    </table>
                </div>
                <div id="list-student">
                    <table>
                        <tr>
                            <td>
                                
                            </td>
                        </tr>
                    </table>
                </div>
                <hr/><hr/><br>

                <%--Form add new Train subject--%>
                           
                <div id = "report-range">
                    THONG KE THEO LOP HOC
                    <table>
                        <tr>
                            <th></th>
                            <th></th>
                            <th></th>
                        </tr>
                        <tr>
                            <td>
                                <b>Năm học: </b>
                                <select id="year" name="year" onchange="">
                                <%
                                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                                if (currentYear <= 2007)
                                    currentYear = 2050;
                                int delta = currentYear - 2007;
                                for(int i = 0; i < delta; i++){
                                    String year = Integer.toString(2007 + i) +
                                            "-" + Integer.toString(2007 + i + 1);
                                %>
                                <option value="<%= year %>"><%= year %></option>
                                <%}
                                %>
                                </select>
                            </td>
                            <td>
                                <b>Học Kỳ: </b>
                                <select id="semeter" name="semeter" onchange="">
                                    <option value="1"> 1 </option>
                                    <option value="2"> 2 </option>
                                </select>
                            </td>
                            <td>
                                <input type="button" value="Tìm" onclick="" />
                            </td>
                        </tr>
                    </table>
                    
                    <div id="result">
                        <table>
                            <tr>
                                <th></th>
                                <th></th>
                            </tr>
                            <tr>
                                <td>Tổng số lớp đã tạo: </td>
                                <td>120</td>
                            </tr>
                            <tr>
                                <td>Tổng số lớp hủy do không đủ điều kiện: </td>
                                <td>100</td>
                            </tr>
                            <tr>
                                <td>Tổng số lớp chưa kết thúc: </td>
                                <td>20</td>
                            </tr>
                        </table>
                    </div>
                    
                        
                </div>
                
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
            var search = document.getElementById("search-student").value;
            alert(search);
             if (http) {
                http.open("GET", "../../ReportController?action=search_student&value="
                    + search, true);
                http.onreadystatechange = handleResponseFindStudent;
                http.send(null);
              }
            }
        
         function handleResponseFindStudent() {
             if(http.readyState == 4 && http.status == 200){
                 var detail=document.getElementById("list-student");
                 detail.innerHTML=http.responseText;
             }
         }
    </script>
</html>
