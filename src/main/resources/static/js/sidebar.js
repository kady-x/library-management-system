document.addEventListener('DOMContentLoaded', function () {
    const sidebar = document.getElementById('sidebar');
    const toggleBtn = document.getElementById('sidebar-toggle');

    const isMinimized = localStorage.getItem('sidebarMinimized') === 'true';
    if (isMinimized) {
        sidebar.classList.add('minimized');
        toggleBtn.textContent = '>';
    }

    toggleBtn.addEventListener('click', function () {
        sidebar.classList.toggle('minimized');
        const minimized = sidebar.classList.contains('minimized');
        toggleBtn.textContent = minimized ? '>' : '<';
        localStorage.setItem('sidebarMinimized', minimized);
    });
});