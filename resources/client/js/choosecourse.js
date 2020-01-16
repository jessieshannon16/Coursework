function pageLoad(){

    let accountType = Cookies.get("accountType");
    console.log(accountType);
    debugger;

    let coursesHTML = `<table>` +
        '<tr>' +
        '<th>Course Name</th>' +
        `<th></th>` +
        '</tr>';

    fetch('/courses/list', {method: 'get'}
    ).then(response => response.json()
    ).then(coursesData => {

        for (let courseData of coursesData) {

            coursesHTML += `<tr>` +

                `<td>${courseData.CourseName}</td>` +
                `<td>` +
                `<button class='addButton' data-id='${courseData.CourseID}'>ADD!</button>` +
                `</td>` +
                `</tr>`;
        }


        coursesHTML += '</table>';
        document.getElementById("tableDiv").innerHTML = coursesHTML;

        let addButtons = document.getElementsByClassName("addButton");
        for (let button of addButtons) {
            button.addEventListener("click", addCourse);
        }
    });

    document.getElementById("logoutButton").addEventListener("click", logout);
    document.getElementById("dashboardButton").addEventListener("click", dashboard);

}

function addCourse(event){
    debugger;
    event.preventDefault();

    const id = event.target.getAttribute("data-id");
    let accountType = Cookies.get("accountType");
    if (accountType=="adult"){

        const form = document.getElementById("usernameform");
        const formData = new FormData(form);
        formData.append("CourseID", id);

        fetch("/adults/choosecourse", {method: 'post', body: formData}
        ).then(response => response.json()
        ).then(responseData => {
            if (responseData.hasOwnProperty('error')) {

                alert(responseData.error);

            } else {

                alert("Course added!");

            }
        });

    }else{

        let formData = new FormData();
        formData.append("CourseID", id);

        fetch("/students/choosecourse", {method: 'post', body: formData}
        ).then(response => response.json()
        ).then(responseData => {
            if (responseData.hasOwnProperty('error')) {

                alert(responseData.error);

            } else {

                alert("Course added!");

            }
        });
    }
}
function dashboard(){
    window.location.href = '/client/dashboard.html';

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