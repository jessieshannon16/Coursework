function pageLoad() {

    let accountType = Cookies.get("accountType");
    if (accountType == 'adult'){
        window.location.href = '/client/dashboard.html';
    }

    fetch('/avatarstats/view', {method: 'get'}
    ).then(response => response.json()
    ).then(stats => {

        let imageHTML = `<div>`;

        imageHTML +=
            `<img src='/client/img/${stats.Image}' alt='${stats.Image}' id="AlpacaImage" width="400px" height="500">`;

        imageHTML += `</div>`;
        console.log(imageHTML);
        document.getElementById("test").innerHTML = imageHTML;

        let statsHTML = `<div>`

        statsHTML +=
            `<h1>${stats.AvatarName}</h1>`+
            `<div>Hunger: ${stats.Hunger}` +
            `<button id='feedButton' data-id='Feed'>Feed</button></div>` +
            `<div>Cleanliness: ${stats.Cleanliness}` +
            `<button id='cleanButton' data-id='Clean'>Clean</button></div>` +
            `<div>Intelligence: ${stats.Intelligence}` +
            `<button id='learnButton' data-id='Learn'>Learn</button></div>` ;
        statsHTML += `</div>`;
        document.getElementById("stats").innerHTML = statsHTML;

        document.getElementById("feedButton").addEventListener("click", feed);
        document.getElementById("cleanButton").addEventListener("click", clean);
        document.getElementById("learnButton").addEventListener("click", learn);
        document.getElementById("dashboardButton").addEventListener("click", dashboard);
        document.getElementById("logoutButton").addEventListener("click", logout);

    });


}
function feed(event){
    event.preventDefault()
    window.location.href = '/client/feed.html';

}
function clean(event){
    event.preventDefault()

    window.location.href = '/client/clean.html';

}
function learn(event){
    event.preventDefault()

    window.location.href = '/client/learn.html';

}
function dashboard(event){
    event.preventDefault()

    window.location.href = '/client/dashboard.html';

}
function logout() {

    fetch("/students/logout", {method: 'post'}
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
