/**
 * Create request object
 */
function createRequestObject(){
    var req;
    if(window.XMLHttpRequest){
        req = new XMLHttpRequest();
    } else if(window.ActiveXObject){
        req = new ActiveXObject("Microsoft.XMLHTTP");
    } else{
        alert('Your browser is not IE 5 or higher, or Firefox or Safari or Opera');
    }
    return req;
}

/**
 * Submit request.
 * 
 * @Param pagename point to controller.
 * @handleResponse response handler.
 */
function submit(pagename, handleResponse){
    if(http){
        http.open("GET", pagename ,true);
        http.onreadystatechange = handleResponse;
        http.send(null);
    }
}

/**
 * stuffId: which will be showed
 * btnId: button use to controll action
 * btn Label: new label of button.
 */
function showStuff(stuffId, btnId, btnShowLabel, btnHideLabel) {
    var btnHidden = "<span onclick=\"hideStuff('" + stuffId + "','" + btnId + "','" + btnShowLabel + "','" + btnHideLabel + "')\" class=\"atag\">"
    + btnHideLabel + "</span>";
    document.getElementById(btnId).innerHTML = btnHidden;
    document.getElementById(stuffId).style.display = 'block';
}
function hideStuff(stuffId, btnId, btnShowLabel, btnHideLabel) {
    var btnShow = "<span onclick=\"showStuff('" + stuffId + "','" + btnId + "','" + btnShowLabel + "','" + btnHideLabel + "')\" class=\"atag\">"
    + btnShowLabel + "</span>";
    document.getElementById(btnId).innerHTML = btnShow;
    document.getElementById(stuffId).style.display = 'none';
}

/*function timeOut(timeOutFunction, delay) {
    setTimeout(func, delay, param1, param2)
}
*/

function highLightSelectRow(chb, row) {
    // validate condition

    var trobj = document.getElementById(row);

    if(chb.checked){
        trobj.setAttribute("class", 'datahighlight');
    } else {
        trobj.removeAttribute("class", 'datahighlight');
    }
}

$(document).ready(function(){
    $('.general-table tr').hover(function(){
        $(this).children().addClass('mouseover_tr');
    },function(){
        $(this).children().removeClass('mouseover_tr');
    });
});


/**
* Hide specified element by id
**/
function hideMe(id) {
    var divObj = document.getElementById(id);
    $(divObj).fadeOut('slow', function() {
        // Animation complete
    });
}

// Show pop up window
function initPopupWindowAtMousePossition(popupId) {
    var detailForm = document.getElementById(popupId);
    var posx = 0;
    var posy = 0;
    if (!e) var e = window.event;
    if (e.pageX || e.pageY) {
        posx = e.pageX;
        posy = e.pageY;
    } else if (e.clientX || e.clientY) {
        posx = e.clientX;
        posy = e.clientY;
    }
    detailForm.style.left = posx - 5;
    detailForm.style.top = posy - 5;
    
    detailForm.innerHTML = "<img src = '../../imgs/icon/loading.gif' />";
}

// Set time out for element by id
function setTimeOut(id, seconds) {
    var object = document.getElementById(id);
    setTimeout(function() {
        $(object).fadeOut('slow');
    }, seconds * 1000); // <-- time in milliseconds
}