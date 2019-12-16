function pageLoad() {
    if(window.location.search === '?logout') {
        document.getElementById('content').innerHTML = '<h1>Logging out, please wait...</h1>';
        logout();
    } else {
        document.getElementById("loginButton").addEventListener("click", login);
        document.getElementById("registerButton").addEventListener("click", register)
    }

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

    fetch("/students/checkLogon", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {
        if (responseData.hasOwnProperty('error')) {

            alert(responseData.error);

        } else {

            Cookies.set("username", responseData.username);
            Cookies.set("token", responseData.token);

            window.location.href = '/client/dashboard.html';

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

            window.location.href = '/client/index.html';

        }
    });

}
