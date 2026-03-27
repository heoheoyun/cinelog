/* ── CineLog 테마 관리 ── */
(function () {
    // 페이지 로드 시 저장된 테마 즉시 적용 (FOUC 방지)
    var saved = localStorage.getItem('cinelog-theme') || 'dark';
    document.documentElement.setAttribute('data-theme', saved);
})();

function toggleTheme() {
    var current = document.documentElement.getAttribute('data-theme');
    var next = current === 'light' ? 'dark' : 'light';
    document.documentElement.setAttribute('data-theme', next);
    localStorage.setItem('cinelog-theme', next);
}
