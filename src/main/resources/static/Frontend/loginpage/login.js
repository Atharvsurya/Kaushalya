document.querySelector('.login-btn').addEventListener('click', async (e) => {
    e.preventDefault();
    
    const email = document.querySelector('input[type="email"]').value;
    const password = document.querySelector('input[type="password"]').value;

    const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email: email, password: password })
    });

    const result = await response.json(); 

    if (result.success) {
        const user = result.data; 

        localStorage.setItem("userEmail", user.email);
        localStorage.setItem("userRole", user.role);
        localStorage.setItem("userFName", user.name);
        console.log(user.name);

        if(user.role === 'ADMIN') window.location.href = "admin.html";
            else window.location.href = "../homepage/home.html";
    }
    else{
        alert("Invalid Credentials");
    }
});