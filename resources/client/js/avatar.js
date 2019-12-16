function pageLoad(){

    const form = document.getElementById("AvatarForm");
    const formData = new FormData(form);
    fetch("/avatarstats/name", {method:'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {

        if (responseData.hasOwnProperty('error')) {
            firstTime();
        } else {
            normalLoad();
        }
    });
}
function firstTime(){
    window.location.href = '/client/avatar.html';

}
function normalLoad(){

}