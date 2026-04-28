/* ============================================================
   KAUSHALYA — Navbar injection v2 (role-aware)
   ============================================================ */
function injectNavbar(activePage = '') {
  const nav = `
  <nav class="navbar">
    <a href="../index.html" class="nav-logo">Kaushal<span>ya</span></a>
    <div class="nav-links">
      <a href="../index.html" class="${activePage === 'home' ? 'active' : ''}">Home</a>
      <a href="/Frontend/mentors.html" class="${activePage === 'mentors' ? 'active' : ''}">Mentors</a>
      <a href="/Frontend/skills.html" class="${activePage === 'skills' ? 'active' : ''}">Skills</a>
      <a href="/Frontend/become-mentor.html" class="${activePage === 'become-mentor' ? 'active' : ''}">Become a Mentor</a>
      <a href="/Frontend/about.html" class="${activePage === 'about' ? 'active' : ''}">About</a>
      <a href="/Frontend/contact.html" class="${activePage === 'contact' ? 'active' : ''}">Contact</a>
    </div>
    <div class="nav-cta">
      <a href="/Frontend/login.html" id="nav-login-btn" class="btn btn-ghost">Log in</a>
      <a href="/Frontend/signup.html" id="nav-signup-btn" class="btn btn-primary">Sign up</a>
      <div id="nav-user-menu" class="dropdown" style="display:none;">
        <div id="nav-user-trigger" style="display:flex;align-items:center;gap:10px;cursor:pointer;padding:6px 10px;border-radius:8px;border:1.5px solid var(--border);background:var(--white);transition:all 0.2s;">
          <div class="avatar avatar-sm" id="nav-user-avatar"></div>
          <span id="nav-user-name" style="font-size:0.875rem;font-weight:500;color:var(--ink);max-width:110px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;"></span>
          <svg width="11" height="11" viewBox="0 0 12 12" fill="none"><path d="M2 4l4 4 4-4" stroke="var(--muted)" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/></svg>
        </div>
        <div id="nav-user-dropdown" class="dropdown-menu">
          <a href="./profile.html" class="dropdown-item">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="8" r="4"/><path d="M20 21a8 8 0 10-16 0"/></svg>
            My Profile
          </a>
          <a href="./dashboard.html" class="dropdown-item">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="7" height="7"/><rect x="14" y="3" width="7" height="7"/><rect x="3" y="14" width="7" height="7"/><rect x="14" y="14" width="7" height="7"/></svg>
            Dashboard
          </a>
          <a href="./mentor-panel.html" id="nav-panel-link" class="dropdown-item" style="display:none;">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 00-3-3.87M16 3.13a4 4 0 010 7.75"/></svg>
            Mentor Panel
          </a>
          <a href="./admin.html" id="nav-admin-link" class="dropdown-item" style="display:none;">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/></svg>
            Admin Console
          </a>
          <a href="./become-mentor.html" id="nav-become-mentor-link" class="dropdown-item" style="display:none;">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M22 10v6M2 10l10-5 10 5-10 5z"/><path d="M6 12v5c3 3 9 3 12 0v-5"/></svg>
            Become a Mentor
          </a>
          <div class="dropdown-divider"></div>
          <div id="nav-logout" class="dropdown-item danger">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M9 21H5a2 2 0 01-2-2V5a2 2 0 012-2h4M16 17l5-5-5-5M21 12H9"/></svg>
            Log out
          </div>
        </div>
      </div>
    </div>
    <div class="hamburger" aria-label="Menu"><span></span><span></span><span></span></div>
  </nav>
  <div class="mobile-nav">
    <a href="../index.html">Home</a>
    <a href="/Frontend/mentors.html">Mentors</a>
    <a href="/Frontend/skills.html">Skills</a>
    <a href="/Frontend/become-mentor.html">Become a Mentor</a>
    <a href="/Frontend/about.html">About</a>
    <a href="/Frontend/contact.html">Contact</a>
    <a href="/Frontend/login.html">Log in</a>
    <a href="/Frontend/signup.html">Sign up</a>
  </div>`;
  document.body.insertAdjacentHTML('afterbegin', nav);
}
