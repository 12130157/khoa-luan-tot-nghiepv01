<%-- 
    Document   : AddSubject
    Created on : 11-11-2011, 23:45:21
    Author     : LocNguyen
--%>
<%@page import="java.util.List"%>
<%@page import="uit.cnpm02.dkhp.model.Subject"%>
<%@include file="MenuPDT.jsp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <link href="../../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Qu?n l? t᩠kho?n</title>
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
    <%
        String subjectStr = "";
        List<String> subject = (List<String>) session.getAttribute("subjects");

        if (subject != null) {
            for (int i = 0; i < subject.size(); i++) {
                subjectStr += subject.get(i);
                if (i < (subject.size() - 1)) {
                    subjectStr += ";";
                }
            }
        }


    %>
    <body>
        <!--Div Wrapper-->
        <div id="wrapper">

            <div id="mainNav"><!--Main Navigation-->
                <%@include file="../MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->

                <hr/><hr/>
                <div id="title">
                    <u><h3>Th뭠m��?c</h3></u>
                </div>
                <hr/><hr/><br>

                <input type="hidden" id="subs" value="<%= subjectStr%>">
                <form action="" method="post">
                    <div style="padding-bottom: 20px;
                         margin-left: 40px;">
                        <input type="button"
                               onclick="submitAddSubject('../../ManageSubjectController?function=add_subject')"
                               value="Hoᮠthᯨ" />
                        <input type="button" onclick="clearData()" value="X