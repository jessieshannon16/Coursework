let score = 0;
function pageLoad(){
    let accountType = Cookies.get("accountType");
    if (accountType == 'adult'){
        window.location.href = '/client/dashboard.html';
    }

    fetch("/avatarstats/question", {method: 'get'}
    ).then(response => response.json()
    ).then(question => {
        if (question.hasOwnProperty('error')) {
            alert(question.error);
        }

        document.getElementById("titles").innerHTML = `<h3>Course Name: ` + `${question.CourseName}</h3>` +
        `<h2>Question: ` + `${question.Question}</h2>`;



        let questionHTML = `<div>`;


        questionHTML +=
            `<button id='Answer1Button' >${question.CorrectAnswer}</button></div>` +
            `<button id='Answer2Button' >${question.InorrectAnswer1}</button></div>` +
            `<button id='Answer3Button' >${question.InorrectAnswer2}</button></div>` +
            `<button id='Answer4Button' >${question.InorrectAnswer3}</button></div>` ;

        questionHTML += `</div>`;
        document.getElementById("questions").innerHTML = questionHTML;
        document.getElementById("score").innerHTML = `<div>Score: ` + score + `</div>`;

        document.getElementById("Answer1Button").addEventListener("click", correct);
        document.getElementById("Answer2Button").addEventListener("click", wrong);
        document.getElementById("Answer3Button").addEventListener("click", wrong);
        document.getElementById("Answer4Button").addEventListener("click", wrong);

    });
    document.getElementById("avatarButton").addEventListener("click", Avatar);

}
function correct(event){
    event.preventDefault();
    score ++;
    pageLoad();
}
function wrong(event){
    event.preventDefault();
    pageLoad();

}
function Avatar(event){
    event.preventDefault();

    let formData = new FormData();
    formData.append("Score", score);
    fetch("/avatarstats/learn", {method: 'Post', body: formData}
    ).then(response => response.json()
    ).then(question => {
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        }
        window.location.href = '/client/avatar.html';
    });

}