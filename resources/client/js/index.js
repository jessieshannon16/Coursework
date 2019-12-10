function pageLoad() {

    let now = new Date();

    let myHTML = '<div style="text-align:center;">'
        + '<h1>Pacos n Tacos</h1>'
        + '<img src="/client/img/alpaca.png"  alt="Logo"/>'
        + '<div style="font-style: italic;">'
        + 'Generated at ' + now.toLocaleTimeString()
        + '</div>'
        + '</div>';

    //document.getElementById("testDiv").innerHTML = myHTML;
    checkLogin();

}
function checkLogin() {

    let username = Cookies.get("username");

    let logInHTML = '';

    if (username === undefined) {

        logInHTML = "Not logged in. <a href='/client/login.html'>Log in</a>";

    } else {

        logInHTML = "Logged in as " + username + ". <a href='/client/login.html?logout'>Log out</a>";

    }

    document.getElementById("loggedInDetails").innerHTML = logInHTML;

}

