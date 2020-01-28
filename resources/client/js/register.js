let now = new Date();
let day = now.getDate();
let month = now.getMonth() + 1;
let year = now.getFullYear();

function pageLoad(){
    document.getElementById("studentButton").addEventListener("click", students);
    document.getElementById("adultButton").addEventListener("click", adults);
    document.getElementById("loginButton").addEventListener("click", login);
}

function students(event){
    debugger;
    event.preventDefault()
    const form = document.getElementById("registerForm");
    const formData = new FormData(form);
    formData.append("day", day);
    formData.append("month", month);
    formData.append("year", year);

    fetch("/students/register", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        }else{

            //automatically logs user on (hopefully)
            fetch("/students/checkLogon", {method:'post', body: formData}
            ).then(response => response.json()
            ).then(responseData => {

                if (responseData.hasOwnProperty('error')) {
                    alert(responseData.error);
                } else {

                    Cookies.set("username", responseData.username);
                    Cookies.set("token", responseData.token);
                    Cookies.set("accountType", responseData.accountType);

                    window.location.href = '/client/avatarnew.html';

                }
            });
        }
    });
}

function adults(event){
    //debugger;
    event.preventDefault()
    const form = document.getElementById("registerForm");
    const formData = new FormData(form);
    formData.append("day", day);
    formData.append("month", month);
    formData.append("year", year);

    fetch("/adults/register", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        }else{
            fetch("/students/checkLogon", {method:'post', body: formData}
            ).then(response => response.json()
            ).then(responseData => {

                if (responseData.hasOwnProperty('error')) {
                    alert(responseData.error);
                } else {

                    Cookies.set("username", responseData.username);
                    Cookies.set("token", responseData.token);
                    Cookies.set("accountType", responseData.accountType)
                    window.location.href = '/client/index.html';

                }
            });
        }
    });
}

function login(event){
    event.preventDefault()
    window.location.href = '/client/login.html.';
}