async function loadMentorData() {
    const response = await fetch('http://localhost:8080/api/mentor/students');
    const data = await response.json();
    
    if(data.success) {
        const table = document.querySelectorAll('table')[1]; // Students table
        // Clear existing rows and append new ones from data.data
    }
}

window.onload = loadMentorData;