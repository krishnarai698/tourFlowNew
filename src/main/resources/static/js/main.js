/* ============================================
   TourFlow — Core JavaScript
   ============================================ */

document.addEventListener('DOMContentLoaded', () => {
    initNavigation();
    initSearch();
    initCounters();
    initLiveUpdates();
    initAnimations();
});

/* ============================================
   Navigation
   ============================================ */
function initNavigation() {
    const toggle = document.querySelector('.nav-toggle');
    const navLinks = document.querySelector('.nav-links');

    if (toggle && navLinks) {
        toggle.addEventListener('click', () => {
            navLinks.classList.toggle('open');
        });
    }

    // Highlight active nav link
    const currentPath = window.location.pathname;
    document.querySelectorAll('.nav-links a').forEach(link => {
        if (link.getAttribute('href') === currentPath) {
            link.classList.add('active');
        }
    });

    // Navbar background on scroll
    window.addEventListener('scroll', () => {
        const navbar = document.querySelector('.navbar');
        if (navbar) {
            navbar.style.background = window.scrollY > 50
                ? 'rgba(10, 14, 26, 0.95)'
                : 'rgba(10, 14, 26, 0.85)';
        }
    });
}

/* ============================================
   Search with Autocomplete
   ============================================ */
function initSearch() {
    const searchInput = document.getElementById('searchInput');
    const suggestions = document.getElementById('searchSuggestions');

    if (!searchInput || !suggestions) return;

    let debounceTimer;

    searchInput.addEventListener('input', (e) => {
        clearTimeout(debounceTimer);
        const query = e.target.value.trim();

        if (query.length < 2) {
            suggestions.classList.remove('active');
            return;
        }

        debounceTimer = setTimeout(() => {
            fetch(`/api/search?q=${encodeURIComponent(query)}`)
                .then(res => res.json())
                .then(data => {
                    if (data.length === 0) {
                        suggestions.classList.remove('active');
                        return;
                    }

                    suggestions.innerHTML = data.map(d => `
                        <a href="/destination/${d.id}" class="search-suggestion-item">
                            <span>
                                <strong>${d.name}</strong>
                                <small style="color: var(--text-muted); margin-left: 8px;">${d.state} · ${d.type}</small>
                            </span>
                            <span class="badge badge-${d.crowdColor}">
                                <span class="status-dot ${d.crowdColor}"></span>
                                ${d.crowd}% crowd
                            </span>
                        </a>
                    `).join('');

                    suggestions.classList.add('active');
                })
                .catch(() => suggestions.classList.remove('active'));
        }, 300);
    });

    // Close suggestions on outside click
    document.addEventListener('click', (e) => {
        if (!e.target.closest('.search-container')) {
            suggestions.classList.remove('active');
        }
    });

    // Handle form submission
    const searchForm = document.getElementById('searchForm');
    if (searchForm) {
        searchForm.addEventListener('submit', (e) => {
            e.preventDefault();
            const query = searchInput.value.trim();
            if (query) {
                window.location.href = `/search?q=${encodeURIComponent(query)}`;
            }
        });
    }
}

/* ============================================
   Animated Counters
   ============================================ */
function initCounters() {
    const counters = document.querySelectorAll('[data-count]');
    if (counters.length === 0) return;

    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                animateCounter(entry.target);
                observer.unobserve(entry.target);
            }
        });
    }, { threshold: 0.5 });

    counters.forEach(counter => observer.observe(counter));
}

function animateCounter(element) {
    const target = parseInt(element.getAttribute('data-count'));
    const suffix = element.getAttribute('data-suffix') || '';
    const duration = 1500;
    const step = target / (duration / 16);
    let current = 0;

    const timer = setInterval(() => {
        current += step;
        if (current >= target) {
            current = target;
            clearInterval(timer);
        }
        element.textContent = Math.round(current) + suffix;
    }, 16);
}

/* ============================================
   Live Status Updates (AJAX Polling)
   ============================================ */
function initLiveUpdates() {
    const liveElements = document.querySelectorAll('[data-live-update]');
    if (liveElements.length === 0) return;

    // Poll every 30 seconds
    setInterval(fetchLiveData, 30000);
}

function fetchLiveData() {
    fetch('/api/destinations/status')
        .then(res => res.json())
        .then(data => {
            data.forEach(dest => {
                updateDestinationCard(dest);
            });
        })
        .catch(err => console.log('Live update error:', err));
}

function updateDestinationCard(dest) {
    // Update crowd percentage
    const crowdEl = document.querySelector(`[data-dest-crowd="${dest.id}"]`);
    if (crowdEl) {
        crowdEl.textContent = dest.crowd + '%';
        crowdEl.className = 'stat-value num-highlight ' + dest.crowdColor;
    }

    // Update traffic percentage
    const trafficEl = document.querySelector(`[data-dest-traffic="${dest.id}"]`);
    if (trafficEl) {
        trafficEl.textContent = dest.traffic + '%';
    }

    // Update progress bars
    const crowdBar = document.querySelector(`[data-crowd-bar="${dest.id}"]`);
    if (crowdBar) {
        crowdBar.style.width = dest.crowd + '%';
        crowdBar.className = 'progress-bar ' + dest.crowdColor;
    }

    // Update tourism pressure
    const pressureEl = document.querySelector(`[data-dest-pressure="${dest.id}"]`);
    if (pressureEl) {
        pressureEl.textContent = dest.tourismPressure + '/100';
    }
}

/* ============================================
   Scroll Animations
   ============================================ */
function initAnimations() {
    const animatedElements = document.querySelectorAll('.fade-in, .fade-in-delay-1, .fade-in-delay-2, .fade-in-delay-3, .fade-in-delay-4');

    if (animatedElements.length === 0) return;

    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.style.opacity = '';
                entry.target.classList.add('visible');
            }
        });
    }, { threshold: 0.1 });

    animatedElements.forEach(el => observer.observe(el));
}

/* ============================================
   Preference Card Toggle
   ============================================ */
function togglePreference(checkbox) {
    const label = checkbox.nextElementSibling;
    if (checkbox.checked) {
        label.style.borderColor = 'var(--accent-blue)';
        label.style.background = 'rgba(0, 212, 255, 0.1)';
    } else {
        label.style.borderColor = 'var(--border-glass)';
        label.style.background = 'var(--bg-glass)';
    }
}

/* ============================================
   Utility Functions
   ============================================ */
function formatNumber(num) {
    return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
}

function getStatusColor(value) {
    if (value >= 75) return 'red';
    if (value >= 50) return 'yellow';
    return 'green';
}

function showToast(message, type = 'success') {
    const toast = document.createElement('div');
    toast.className = `alert-banner ${type}`;
    toast.style.cssText = 'position: fixed; bottom: 24px; right: 24px; z-index: 9999; max-width: 400px;';
    toast.innerHTML = `
        <span class="alert-icon">${type === 'success' ? '✅' : '⚠️'}</span>
        <span class="alert-text">${message}</span>
    `;
    document.body.appendChild(toast);

    setTimeout(() => {
        toast.style.opacity = '0';
        toast.style.transform = 'translateY(20px)';
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}
