function topfun() {
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

window.onload = function() {
    updateProfileUI();
};

function updateProfileUI() {
    const name = localStorage.getItem("userFName");
    const profileBtn = document.getElementById("profile");
    const menu = document.getElementById("logoutMenu");

    if (name) {
        // User State: Show name, click to swap for Logout
        profileBtn.innerText = name;
        profileBtn.style.display = "inline-block";
        profileBtn.classList.remove("hidden");
        menu.style.display = "none";
        menu.classList.remove("show-logout");

        profileBtn.onclick = function(event) {
            event.stopPropagation();
            profileBtn.classList.add("hidden");
            profileBtn.style.display = "none";
            menu.classList.add("show-logout");
            menu.style.display = "inline-block";
        };
    } else {
        // Guest State: Show "Sign In", click to redirect
        profileBtn.innerText = "Sign In";
        profileBtn.style.display = "inline-block";
        profileBtn.classList.remove("hidden");
        menu.style.display = "none";
        menu.classList.remove("show-logout");

        profileBtn.onclick = function() {
            window.location.href = "../Login_Page/sign_in.html";
        };
    }
}

function logout(event) {
    if (event) event.stopPropagation();

    // 1. Clear data
    localStorage.removeItem("userFName");

    // 2. Reset UI state through the main function
    updateProfileUI();

    alert("Logged out successfully");
}

window.onclick = function() {
    const profileBtn = document.getElementById("profile");
    const menu = document.getElementById("logoutMenu");
    
    // Swap back to name if the user clicks anywhere else
    if (menu && menu.classList.contains("show-logout")) {
        menu.classList.remove("show-logout");
        menu.style.display = "none";
        profileBtn.classList.remove("hidden");
        profileBtn.style.display = "inline-block";
    }
};