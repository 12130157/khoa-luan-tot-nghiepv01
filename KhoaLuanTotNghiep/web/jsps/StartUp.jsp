<%-- 
    Document   : jspTrangChu
    Created on : Apr 23, 2011, 10:59:14 PM
    Author     : ngloc_it
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <link href="../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <script src="../javascripts/simplegallery.js" type="text/javascript"></script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Trang Chủ</title>
        <style media="all" type="text/css">
            
        </style>
    </head>
    <body>
        <!--Div Wrapper-->
        <div id="wrapper">
            <%@include file="Menu.jsp" %>
            <div id="mainNav"><!--Main Navigation-->
                <%@include file="MainNav.jsp" %>
            </div><!--End Navigation-->
            <div id="content"><!--Main Contents-->
                <div id="simplegallery1">
                    
                </div>
                <%@include file="News.jsp" %>
            </div><!--End Contents-->

            <div id="footer"><!--Footer-->
                <%@include file="Footer.jsp" %>
            </div><!--End footer-->
        </div>
        <!--End Wrapper-->
        <script type="text/javascript">
            var mygallery = new simpleGallery({
                wrapperid: "simplegallery1", //ID of main gallery container,
                dimensions: ['97.5%', 343], //width/height of gallery in pixels. Should reflect dimensions of the images exactly
                    imagearray: [
                        ["../imgs/gallery/BoNhiemHT.jpg", "", "new", "Hoi nghi cap cao UIT"],
                        ["../imgs/gallery/HoatDong_8_3_2012.jpg", "", "", "Hoat dong chao mung 8.3"],
                        ["../imgs/gallery/HoiThaoCanTho.jpg", "", "", "Tham gia hoi thao o Can Tho"],
                        ["../imgs/gallery/TS2012_TX.png", "", "", "Thong tin tuyen sinh dai hoc he chinh qui 2012"],
                        ["../imgs/gallery/LeTNDot1-2012.jpg", "", "", "Le tot nghiep dot 1-2012 He ky su"],
                        ["../imgs/gallery/NgayNhaGiao.jpg", "", "", "Ngay nha giao viet nam 20-11-2012"],
                        ["../imgs/gallery/TruongUIT.jpg", "", "", "TRUONG DAI HOC CONG NGHE THONG TIN"]
                    ],
                autoplay: [true, 4500, 2], //[auto_play_boolean, delay_btw_slide_millisec, cycles_before_stopping_int]
                persist: false, //remember last viewed slide and recall within same session?
                fadeduration: 1000, //transition duration (milliseconds)
                oninit:function(){ //event that fires when gallery has initialized/ ready to run
                    //Keyword "this": references current gallery instance (ie: try this.navigate("play/pause"))
                },
                onslide:function(curslide, i){ //event that fires after each slide is shown
                    //Keyword "this": references current gallery instance
                    //curslide: returns DOM reference to current slide's DIV (ie: try alert(curslide.innerHTML)
                    //i: integer reflecting current image within collection being shown (0=1st image, 1=2nd etc)
                }
            })
        </script>
    </body>
</html>