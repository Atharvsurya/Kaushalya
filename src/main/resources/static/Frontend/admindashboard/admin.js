async function loadDashboard() {
    // 1. Fetch Stats
    const statsRes = await fetch('http://localhost:8080/api/admin/stats');
    const statsData = await statsRes.json();
    if(statsData.success) {
        // Update cards (Example: Total Users)
        document.querySelectorAll('.card p')[0].innerText = statsData.data.totalUsers;
    }

    // 2. Fetch Users List
    const usersRes = await fetch('http://localhost:8080/api/admin/users');
    const usersData = await usersRes.json();
    // Logic to map usersData.data to your table rows...
}

async function deleteUser(id) {
    if(confirm("Delete user?")) {
        await fetch(`http://localhost:8080/api/admin/users/${id}`, { method: 'DELETE' });
        loadDashboard(); // Refresh
    }
}

window.onload = loadDashboard;