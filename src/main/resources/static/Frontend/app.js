/* ============================================================
   KAUSHALYA — Shared Utilities v2
   ============================================================ */

const API = 'kaushalya-production.up.railway.app/api';
const SESSION_DURATION = 7 * 24 * 60 * 60 * 1000; // 7 days

const Auth = {
  save(user) {
    localStorage.setItem('kaushalya_user', JSON.stringify(user));
    localStorage.setItem('kaushalya_login_time', Date.now().toString());
  },
  get() {
    try {
      const loginTime = parseInt(localStorage.getItem('kaushalya_login_time') || '0');
      if (Date.now() - loginTime > SESSION_DURATION) { this.clear(); return null; }
      return JSON.parse(localStorage.getItem('kaushalya_user'));
    } catch { return null; }
  },
  clear()     { localStorage.removeItem('kaushalya_user'); localStorage.removeItem('kaushalya_login_time'); },
  isLoggedIn(){ return !!this.get(); },
  role()      { return this.get()?.role || null; },
};

const Toast = {
  container: null,
  init() {
    this.container = document.getElementById('toast-container');
    if (!this.container) {
      this.container = document.createElement('div');
      this.container.id = 'toast-container';
      this.container.className = 'toast-container';
      document.body.appendChild(this.container);
    }
  },
  show(msg, type = 'default', duration = 3500) {
    this.init();
    const t = document.createElement('div');
    t.className = `toast ${type}`;
    t.innerHTML = `<span>${msg}</span>`;
    this.container.appendChild(t);
    setTimeout(() => {
      t.style.animation = 'toastOut 0.3s ease forwards';
      setTimeout(() => t.remove(), 300);
    }, duration);
  },
  success(msg) { this.show('✓ ' + msg, 'success'); },
  error(msg)   { this.show('✕ ' + msg, 'error'); },
  info(msg)    { this.show('ℹ ' + msg, 'default'); }
};

async function apiFetch(path, opts = {}) {
  try {
    const res = await fetch(API + path, {
      headers: { 'Content-Type': 'application/json', ...(opts.headers || {}) },
      ...opts
    });
    const data = await res.json();
    return { ok: res.ok, status: res.status, data };
  } catch (e) {
    return { ok: false, status: 0, data: { message: 'Network error. Is the server running on port 8080?' } };
  }
}

function initials(name = '') {
  return name.trim().split(/\s+/).map(w => w[0]).join('').toUpperCase().slice(0, 2) || '?';
}

function initMobileNav() {
  const hamburger = document.querySelector('.hamburger');
  const mobileNav = document.querySelector('.mobile-nav');
  if (hamburger && mobileNav) {
    hamburger.addEventListener('click', () => mobileNav.classList.toggle('open'));
    document.addEventListener('click', (e) => {
      if (!hamburger.contains(e.target) && !mobileNav.contains(e.target)) mobileNav.classList.remove('open');
    });
  }
}

function initUserDropdown() {
  const user = Auth.get();
  const loginBtn  = document.getElementById('nav-login-btn');
  const signupBtn = document.getElementById('nav-signup-btn');
  const userMenu  = document.getElementById('nav-user-menu');
  const userAvatar= document.getElementById('nav-user-avatar');
  const userName  = document.getElementById('nav-user-name');
  const logoutBtn = document.getElementById('nav-logout');
  const adminLink = document.getElementById('nav-admin-link');
  const panelLink = document.getElementById('nav-panel-link');
  const becomeMentorLink = document.getElementById('nav-become-mentor-link');

  if (user) {
    if (loginBtn)  loginBtn.style.display  = 'none';
    if (signupBtn) signupBtn.style.display = 'none';
    if (userMenu)  userMenu.style.display  = 'flex';
    if (userAvatar) userAvatar.textContent = initials(user.name);
    if (userName)   userName.textContent   = user.name;
    if (user.role === 'ADMIN'   && adminLink)          adminLink.style.display = 'flex';
    if (user.role === 'MENTOR'  && panelLink)          panelLink.style.display = 'flex';
    if (user.role === 'STUDENT' && becomeMentorLink)   becomeMentorLink.style.display = 'flex';
  } else {
    if (userMenu) userMenu.style.display = 'none';
  }

  const dropTrigger = document.getElementById('nav-user-trigger');
  const dropMenu    = document.getElementById('nav-user-dropdown');
  if (dropTrigger && dropMenu) {
    dropTrigger.addEventListener('click', (e) => { e.stopPropagation(); dropMenu.classList.toggle('open'); });
    document.addEventListener('click', () => dropMenu.classList.remove('open'));
  }

  if (logoutBtn) {
    logoutBtn.addEventListener('click', () => {
      Auth.clear(); Toast.info('Logged out');
      setTimeout(() => window.location.href = '../index.html', 800);
    });
  }
}

function setLoading(btn, loading, text = '') {
  if (loading) { btn.disabled = true; btn._origText = btn.innerHTML; btn.innerHTML = `<span class="spinner"></span>`; }
  else { btn.disabled = false; btn.innerHTML = text || btn._origText || ''; }
}

function requireRole(role) {
  const user = Auth.get();
  if (!user) { window.location.href = 'login.html'; return false; }
  if (role && user.role !== role) { window.location.href = '../index.html'; return false; }
  return true;
}

function fmtDate(str) {
  if (!str) return '—';
  try { return new Date(str).toLocaleDateString('en-IN', { day:'numeric', month:'short', year:'numeric' }); }
  catch { return str; }
}

function fmtDateTime(str) {
  if (!str) return '—';
  try { return new Date(str).toLocaleString('en-IN', { day:'numeric', month:'short', year:'numeric', hour:'2-digit', minute:'2-digit' }); }
  catch { return str; }
}

function validateField(input, rules = {}) {
  const val = input.value.trim();
  let error = '';
  if (rules.required && !val) error = rules.requiredMsg || 'This field is required.';
  else if (rules.minLength && val.length < rules.minLength) error = rules.minLengthMsg || `Minimum ${rules.minLength} characters.`;
  else if (rules.email && val && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(val)) error = 'Enter a valid email address.';
  else if (rules.match && val !== rules.match()) error = rules.matchMsg || 'Fields do not match.';

  let errEl = input.closest('.form-group')?.querySelector('.field-error');
  if (!errEl) {
    errEl = document.createElement('span');
    errEl.className = 'field-error form-error';
    input.closest('.form-group')?.appendChild(errEl);
  }
  errEl.textContent = error;
  errEl.style.display = error ? 'block' : 'none';
  input.classList.toggle('error', !!error);
  return !error;
}

function initPasswordStrength(inputId, containerId) {
  const input = document.getElementById(inputId);
  const container = document.getElementById(containerId);
  if (!input || !container) return;
  input.addEventListener('input', () => {
    const val = input.value;
    let score = 0;
    if (val.length >= 6) score++;
    if (val.length >= 10) score++;
    if (/[A-Z]/.test(val)) score++;
    if (/[0-9]/.test(val)) score++;
    if (/[^A-Za-z0-9]/.test(val)) score++;
    const labels = ['', 'Very Weak', 'Weak', 'Fair', 'Strong', 'Very Strong'];
    const colors = ['', '#e74c3c', '#e67e22', '#f1c40f', '#27ae60', '#1a7a4a'];
    container.innerHTML = val ? `
      <div style="display:flex;align-items:center;gap:10px;margin-top:6px;">
        <div style="flex:1;height:4px;background:var(--cream-dark);border-radius:2px;overflow:hidden;">
          <div style="height:100%;width:${Math.min((score/5)*100,100)}%;background:${colors[score]||'#1a7a4a'};border-radius:2px;transition:all 0.3s;"></div>
        </div>
        <span style="font-size:0.72rem;font-weight:700;color:${colors[score]||'#1a7a4a'};min-width:74px;">${labels[score]||'Very Strong'}</span>
      </div>` : '';
  });
}

function paginate(items, page, perPage = 10) {
  return { items: items.slice((page-1)*perPage, page*perPage), totalPages: Math.ceil(items.length/perPage), page, total: items.length };
}

function renderPagination(container, currentPage, totalPages, onPageChange) {
  if (!container || totalPages <= 1) { if(container) container.innerHTML=''; return; }
  let html = `<div class="pagination">`;
  html += `<button class="page-btn" onclick="(${onPageChange.toString()})(${currentPage-1})" ${currentPage===1?'disabled':''}>← Prev</button>`;
  for (let i=1; i<=totalPages; i++) {
    if (i===1||i===totalPages||Math.abs(i-currentPage)<=1) html += `<button class="page-btn ${i===currentPage?'active':''}" onclick="(${onPageChange.toString()})(${i})">${i}</button>`;
    else if (Math.abs(i-currentPage)===2) html += `<span class="page-dots">…</span>`;
  }
  html += `<button class="page-btn" onclick="(${onPageChange.toString()})(${currentPage+1})" ${currentPage===totalPages?'disabled':''}>Next →</button>`;
  html += `</div>`;
  container.innerHTML = html;
}

function checkSessionExpiry() {
  const loginTime = parseInt(localStorage.getItem('kaushalya_login_time')||'0');
  if (loginTime && Date.now()-loginTime > SESSION_DURATION) {
    Auth.clear(); Toast.info('Session expired. Please log in again.');
    setTimeout(() => window.location.href='./login.html', 1500);
    return false;
  }
  return true;
}

document.addEventListener('DOMContentLoaded', () => {
  checkSessionExpiry();
  initMobileNav();
  initUserDropdown();
});
