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

var http = createRequestObject();
       
function accept(taskId) {
    var controller = "../../HomepageController?actor=notify-task-to-student"
    + "&taskid=" + taskId;
    if(http){
        http.open("GET", controller ,true);
        http.onreadystatechange = fillTaskTableHandler;
        http.send(null);
    } else {
        alert("Error: http object not found");
    }
}
       
function reject(taskId) {
    alert("Enter Reject... Tobe implemented..." + taskId);
}
    
function hideTask(taskId) {
    alert("AAAAAAAAAAAAAAA " + taskId);
    var controller = "../../HomepageController?actor=hide-task"
    + "&taskid=" + taskId;
    if(http){
        http.open("GET", controller ,true);
        http.onreadystatechange = fillTaskTableHandler;
        http.send(null);
    } else {
        alert("Error: http object not found");
    }
}

function fillTaskTableHandler() {
    if(http.readyState == 4 && http.status == 200){
        var result = http.responseText;
        if (result.substring(0, 5) == "error") {
            alert(result)
        } else {
            var pi = document.getElementById("important-task");
            pi.innerHTML = http.responseText;
        }
    }
}
