function pageLoad(){
    let accountType = Cookies.get("accountType");
    if (accountType == 'adult'){
        window.location.href = '/client/dashboard.html';
    }

    fetch('/avatarstats/view', {method: 'get'}
    ).then(response => response.json()
    ).then(stats => {
        if (stats.hasOwnProperty('error')) {
            alert(stats.error);
        }else {
            document.getElementById("avatar").innerHTML = `<img src='/client/img/${stats.Image}' alt='${stats.Image}' id="AlpacaImage" width="400px" height="500">`;
            document.getElementById("stats").innerHTML = `<div>Cleanliness: ${stats.Cleanliness}`;

            document.getElementById("spongeButton").addEventListener("click", clean);
            document.getElementById("avatarButton").addEventListener("click", avatar);
        }
    });
}
function clean(event){
    event.preventDefault();

    fetch('/avatarstats/clean', {method: 'get'}
    ).then(response => response.json()
    ).then(stats => {
        if (stats.hasOwnProperty('error')) {
            alert(stats.error);
        }else {
            pageLoad();
        }
    });
}
function avatar(event){
    event.preventDefault();
    window.location.href = '/client/avatar.html';
}