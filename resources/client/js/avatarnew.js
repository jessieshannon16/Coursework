let colour = "brown";
function pageLoad(){

    document.getElementById("brownAlpaca").addEventListener("click", brown);
    document.getElementById("greenAlpaca").addEventListener("click", green);
    document.getElementById("pinkAlpaca").addEventListener("click", pink);
    document.getElementById("purpleAlpaca").addEventListener("click", purple);
    document.getElementById("tealAlpaca").addEventListener("click", teal);


    document.getElementById("confirmButton").addEventListener("click", submit);
}
function submit(){
    debugger;
    event.preventDefault();

    let formData = new FormData();
    formData.append("colour", colour);

    fetch("/avatartype/choose", {method:'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {

        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        } else {
            const form = document.getElementById("nameForm");
            const nameData = new FormData(form);

            fetch("/avatarstats/setName", {method:'post', body: nameData}
            ).then(response => response.json()
            ).then(responseData => {

                if (responseData.hasOwnProperty('error')) {
                    alert(responseData.error);
                } else {

                    window.location.href = '/client/avatar.html';

                }
            });
        }
    });



}
function brown(){
    event.preventDefault();

    colour = "brown";
}
function green(){
    event.preventDefault();

    colour = "green";
}
function pink(){
    event.preventDefault();

    colour = "pink";
}
function purple(){
    event.preventDefault();
    colour = "purple";
}
function teal(){
    event.preventDefault();

    colour = "teal";
}


