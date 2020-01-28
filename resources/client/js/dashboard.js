function pageLoad() {

    let accountType = Cookies.get("accountType");
    console.log(accountType);

    if (accountType == "adult") {

        let adultsHTML = `<table>` +
            '<tr>' +
            '<th>Student Username</th>' +
            '<th>Course Name</th>' +
            '<th>Score</th>' +
            '</tr>';

        fetch('/adults/courses', {method: 'post'}
        ).then(response => response.json()
        ).then(coursesData => {

            for (let courseData of coursesData) {

                adultsHTML += `<tr>` +

                    `<td>${courseData.StudentUsername}</td>` +
                    `<td>${courseData.CourseName}</td>` +
                    `<td>${courseData.Score}</td>` +
                    `</tr>`;
            }

            adultsHTML += '</table>';
            document.getElementById("tableDiv").innerHTML = adultsHTML;

        });

    } else {

        document.getElementById("studentButton").innerHTML = `<div><button id='statsButton' data-id='stats'>Avatar stats</button></div>`;
        document.getElementById("studentButton").addEventListener("click", student);

        let studentsHTML = `<table>` +
            '<tr>' +
            '<th>Course Name</th>' +
            '<th>Score</th>' +
            '</tr>';

        fetch('/students/courses', {method: 'post'}
        ).then(response => response.json()
        ).then(studentsData => {

            for (let studentData of studentsData) {

                studentsHTML += `<tr>` +
                    `<td>${studentData.CourseName}</td>` +
                    `<td>${studentData.Score}</td>` +
                    `</tr>`;
            }

            studentsHTML += '</table>';
            document.getElementById("tableDiv").innerHTML = studentsHTML;
        });

        document.getElementById("learn").innerHTML = `<div></div><button id='learnButton' data-id='learn'>Learn!</button></div>`;
        document.getElementById("learn").addEventListener("click", learn);


        fetch('/avatarstats/view', {method: 'get'}
        ).then(response => response.json()
        ).then(image => {

            document.getElementById("image").innerHTML = `<div><img src='/client/img/${image.Image}' alt='${image.Image}' id="AlpacaImage" width="400px" height="500"></div>`;

        });
    }
    document.getElementById("courseButton").addEventListener("click", course);
    document.getElementById("logoutButton").addEventListener("click", logout);

}

function course(){
    event.preventDefault()
    window.location.href = '/client/chooseCourse.html';

}
function learn(){
    event.preventDefault()
    window.location.href = '/client/learn.html';

}
function student(){
    event.preventDefault()
    window.location.href = '/client/avatar.html';

}
function logout() {
    let accountType = Cookies.get("accountType");

    if (accountType=="adult") {
        fetch("/adults/logout", {method: 'post'}
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
    }else{
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
}