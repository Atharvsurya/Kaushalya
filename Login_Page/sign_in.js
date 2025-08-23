function login(){

    let email = document.getElementById("email").value;
    let password = document.getElementById("password").value;

    if(email == "admin@admin.com" && password == "admin123"){
        alert("Login Successful");
        window.location.href = "../Dashboard/home.html";
        return false;
    }
    else{
        alert("Invalid email or password. Please try again.");
        return false;
    }
}