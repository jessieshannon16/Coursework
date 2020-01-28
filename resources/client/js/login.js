let now = new Date();
let day = now.getDate();
let month = now.getMonth() + 1;
let year = now.getFullYear();
function pageLoad() {
    if(window.location.search === '?logout') {
        document.getElementById('content').innerHTML = '<h1>Logging out, please wait...</h1>';
        logout();
    } else {
        document.getElementById("loginButton").addEventListener("click", login);
        document.getElementById("registerButton").addEventListener("click", register)
    }
    console.log(day + " " + month + " " + year);
}
function register(event){
    event.preventDefault()

    //const form = document.getElementById("register")
    window.location.href = '/client/register.html';
}
function login(event) {
    debugger;

    event.preventDefault();

    const form = document.getElementById("loginForm");
    const formData = new FormData(form);
    formData.append("day", day);
    formData.append("month", month);
    formData.append("year", year);


    fetch("/students/checkLogon", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {
        if (responseData.hasOwnProperty('error')) {

            alert(responseData.error);

        } else {

            Cookies.set("username", responseData.username);
            Cookies.set("token", responseData.token);
            Cookies.set("accountType", responseData.accountType);

            if (responseData.accountType == "adult")
                window.location.href = '/client/dashboard.html';
            else{
                window.location.href = '/client/avatar.html';

            }
        }
    });
}
function logout() {

    fetch("/students/checkLogout", {method: 'post'}
    ).then(response => response.json()
    ).then(responseData => {
        if (responseData.hasOwnProperty('error')) {

            alert(responseData.error);

        } else {

            Cookies.remove("username");
            Cookies.remove("token");

            window.location.href = '/client/login.html';

        }
    });

}
