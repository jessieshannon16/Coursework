function pageLoad(){
    debugger;



    fetch("/avatarstats/name", {method:'get'}
    ).then(response => response.json()
    ).then(responseData => {
        if (responseData.AvatarName === 'null') {
            window.location.href = '/client/avatarnew.html';
        } else {
            normalLoad();
        }
    });



}
function normalLoad(){

}