<%-- 
    Document   : jspMainNav
    Created on : 26-04-2011, 21:12:12
    Author     : ngloc_it
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <style media="all" type="text/css">
        
        
    </style>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="../csss/general.css" rel="stylesheet" type="text/css" media="screen">
        <script type="text/javascript" src="../javascripts/calendar/utils.js"></script>
    </head>
    <body>
        <div id="main-navi">
            <div id="range-1">
                <div id="session-counter">
                    Đang truy cập: 1
                </div>
            </div>
            <div id="range-1">
                <canvas id="example" width="200" height="200">Browser not supported!</canvas>
            </div>
        </div>
    </body>
    <script type="application/x-javascript">
        draw();
        
        function draw() {
            var canvas = document.getElementById('example');
            var width = canvas.width;
            var height = canvas.height;
            
            if (canvas.getContext) {
                var c2d = canvas.getContext('2d');
                c2d.clearRect(0, 0, width, height);
                
                //Define gradients for 3D / shadow effect
                var grad1 = c2d.createLinearGradient(0, 0, width, height);
                grad1.addColorStop(0, "#D83040");
                grad1.addColorStop(1, "#801020");
                var grad2 = c2d.createLinearGradient(0, 0, width, height);
                grad2.addColorStop(0, "#801020");
                grad2.addColorStop(1, "#D83040");
                c2d.font = "Bold 20px Arial";
                c2d.textBaseline = "middle";
                c2d.textAlign = "center";
                c2d.lineWidth = 1;
                c2d.save();
                //Outer bezel
                c2d.strokeStyle = grad1;
                c2d.lineWidth = 10;
                c2d.beginPath();
                c2d.arc(width/2, height/2, width/2 - 16, 0, Math.PI*2, true);
                c2d.shadowOffsetX = 4;
                c2d.shadowOffsetY = 4;
                c2d.shadowColor = "rgba(0,0,0,0.6)";
                c2d.shadowBlur = 6;
                c2d.stroke();
                //Inner bezel
                c2d.restore();
                c2d.strokeStyle = grad2;
                c2d.lineWidth = 10;
                c2d.beginPath();
                c2d.arc(width/2, height/2, width/2 - 21, 0, Math.PI*2, true);
                c2d.stroke();
                c2d.strokeStyle = "#222";
                c2d.save();
                c2d.translate(width/2, height/2);
                //Markings/Numerals
                for (i = 1;i <= 60;i++) {
                    ang = Math.PI/30*i;
                    sang = Math.sin(ang);
                    cang = Math.cos(ang);
                    //If modulus of divide by 5 is zero then draw an hour marker/numeral
                    if (i % 5 == 0) {
                        c2d.lineWidth = 4;
                        sx = sang*55;
                        sy = cang*-55;
                        ex = sang*70;
                        ey = cang*-70;
                        nx = sang*45;
                        ny = cang*-45;
                        c2d.fillText(i/5, nx, ny);
                        //Else this is a minute marker
                    } else {
                        c2d.lineWidth = 1;
                        sx = sang*50;
                        sy = cang*50;
                        ex = sang*60;
                        ey = cang*60;
                    }
                    c2d.beginPath();
                    c2d.moveTo(sx,sy);
                    c2d.lineTo(ex,ey);
                    c2d.stroke();
                }
            //Fetch the current time
            var ampm = "AM";
            var now = new Date();
            var hrs = now.getHours();
            var min = now.getMinutes();
            var sec = now.getSeconds();
            c2d.strokeStyle = "#000";
            //Draw AM/PM indicator
            if (hrs >= 12)
                ampm = "PM";
            c2d.lineWidth = 1;
            c2d.strokeRect(15,-14,40,28);
            c2d.fillText(ampm,33,0);

            c2d.lineWidth = 6;
            c2d.save();
            //Draw clock pointers but this time rotate the canvas rather than
            //calculate x/y start/end positions.
            //
            //Draw hour hand
            c2d.rotate(Math.PI/6*(hrs+(min/60)+(sec/3600)));
            c2d.beginPath();
            c2d.moveTo(0, 10);
            c2d.lineTo(0, -40);
            c2d.stroke();
            c2d.restore();
            c2d.save();
            //Draw minute hand
            c2d.rotate(Math.PI/30*(min+(sec/60)));
            c2d.beginPath();
            c2d.moveTo(0, 20);
            c2d.lineTo(0, -60);
            c2d.stroke();
            c2d.restore();
            c2d.save();
            //Draw second hand
            c2d.lineWidth = 3;
            c2d.rotate(Math.PI/30*sec);
            c2d.strokeStyle = "#E33";
            c2d.beginPath();
            c2d.moveTo(0, 20);
            c2d.lineTo(0, -60);
            c2d.stroke();
            c2d.restore();

            //Additional restore to go back to state before translate
            //Alternative would be to simply reverse the original translate
            c2d.restore();
            setTimeout(draw,1000);
        }
    }
</script>
    
</html>

