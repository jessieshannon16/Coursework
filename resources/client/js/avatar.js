function pageLoad() {


    fetch('/avatarstats/view', {method: 'get'}
    ).then(response => response.json()
    ).then(stats => {

        let statsHTML = `<div>`

        statsHTML +=
            `<img src='/client/img/${stats.Image}' alt='${stats.Image}' id="AlpacaImage" width="400px" height="500">`;

        statsHTML += `</div>`;
        console.log(statsHTML);
        document.getElementById("test").innerHTML = statsHTML;

    });



}

