document.addEventListener('DOMContentLoaded', function () {
    const sidebar = document.getElementById('sidebar');
    const toggleBtn = document.getElementById('sidebar-toggle');

    toggleBtn.textContent = '»';

    const isMaximized = localStorage.getItem('sidebarMinimized') === 'false';
    if (isMaximized) {
        sidebar.classList.remove('minimized');
        toggleBtn.textContent = '«';
    }

    toggleBtn.addEventListener('click', function () {
        sidebar.classList.toggle('minimized');
        const minimized = sidebar.classList.contains('minimized');
        toggleBtn.textContent = minimized ? '»' : '«';
        localStorage.setItem('sidebarMinimized', minimized);
    });
});
