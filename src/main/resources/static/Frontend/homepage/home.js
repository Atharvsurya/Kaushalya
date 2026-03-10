window.addEventListener("load", () => {

    const profileDiv = document.getElementById("uname");
    const mentorBtn = document.querySelector(".btn.secondary");
    
    const userName = localStorage.getItem("userFName");
    const isLoggedIn = !!userName;

    if (profileDiv) {
        if (isLoggedIn) {
            profileDiv.innerText = userName;
            profileDiv.onclick = () => {
                if(confirm("Logout?")) {
                    localStorage.clear();
                    window.location.reload();
                }
            };
        } else {
            profileDiv.innerText = "Sign In";
            profileDiv.onclick = () => {
                window.location.href = "../loginpage/login.html";
            };
        }
    }

    if (mentorBtn) {
        mentorBtn.onclick = (e) => {
            if (!isLoggedIn) {
                e.preventDefault();
                alert("Please Sign In first.");
                window.location.href = "../loginpage/login.html";
            } else {
                window.location.href = "../mentorpanel/panel.html";
            }
        };
    }
});


let btn = document.getElementById("topBtn");
window.onscroll = function() {
    if (document.documentElement.scrollTop > 200) {
        btn.style.display = "block";
    } else {
        btn.style.display = "none";
    }
}
btn.onclick = function() {
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

let reviews = document.querySelectorAll(".review-card");
let index = 0;
setInterval(() => {
    reviews.forEach(card => card.classList.remove("active"));
    reviews[index].classList.add("active");
    index++;
    if (index >= reviews.length) index = 0;
}, 3000);