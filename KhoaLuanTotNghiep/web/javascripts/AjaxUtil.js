/**
 * Create request object
 */
function createRequestObject(){
    alert('here ');
    var req;
    if(window.XMLHttpRequest){
        req = new XMLHttpRequest();
    } else if(window.ActiveXObject){
        req = new ActiveXObject("Microsoft.XMLHTTP");
    } else{
        alert('Your browser is not IE 5 or higher, or Firefox or Safari or Opera');
    }
    alert('here ' + (req == true));        
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
