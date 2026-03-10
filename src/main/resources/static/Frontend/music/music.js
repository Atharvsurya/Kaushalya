const profileBtn = document.getElementById("profileBtn");
const profileMenu = document.getElementById("profileMenu");

profileBtn.addEventListener("click", () => {

profileMenu.style.display =
profileMenu.style.display === "block" ? "none" : "block";

});

window.addEventListener("click", function(e){

if(!profileBtn.contains(e.target) && !profileMenu.contains(e.target)){

profileMenu.style.display = "none";

}

});


const userEmail = localStorage.getItem('userEmail');

async function loadProfile() {
    if(!userEmail) return;

    const response = await fetch(`http://localhost:8080/api/auth/profile/${userEmail}`);
    const data = await response.json();

    if(data.success) {
        // Update UI with user name
        document.getElementById('profileBtn').title = `Logged in as ${data.data.name}`;
    }
}

// Toggle Profile Menu
document.getElementById('profileBtn').addEventListener('click', () => {
    document.getElementById('profileMenu').classList.toggle('active');
});

window.onload = loadProfile;