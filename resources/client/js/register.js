function pageLoad(){
    document.getElementById("studentButton").addEventListener("click", students);
    document.getElementById("adultButton").addEventListener("click", adults);
}

function students(event){
    debugger;
    event.preventDefault()
    const form = document.getElementById("registerForm");
    const formData = new FormData(form);

    fetch("/students/register", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        }
    });
}

function adults(event){
    //debugger;
    event.preventDefault()
    const form = document.getElementById("registerFrom");
    const formData = new FormData(form);

    fetch("/adults/register", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        } else {
            Cookies.set("username", responseData.username);
            Cookies.set("token", responseData.token);

            window.location.href = '/client/index.html';
        }
});
}