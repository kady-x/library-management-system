// State management
let currentReport = 'overdue';
let currentData = [];
let sortConfig = { field: '', order: 'asc' };

// Sort options for each report type
const sortOptions = {
    'overdue': [
        { value: '', label: 'Default' },
        { value: 'title', label: 'Book Title' },
        { value: 'author', label: 'Author' },
        { value: 'member', label: 'Member' },
        { value: 'dueDate', label: 'Due Date' },
        { value: 'daysOverdue', label: 'Days Overdue' }
    ],
    'most-borrowed': [
        { value: '', label: 'Default' },
        { value: 'title', label: 'Book Title' },
        { value: 'author', label: 'Author' },
        { value: 'borrowCount', label: 'Borrow Count' }
    ],
    'member-activity': [
        { value: '', label: 'Default' },
        { value: 'memberName', label: 'Member Name' },
        { value: 'memberEmail', label: 'Email' },
        { value: 'activityCount', label: 'Activity Count' }
    ]
};

// Initialize page
document.addEventListener('DOMContentLoaded', () => {
    initializeEventListeners();
    updateSortOptions();
    loadReport('overdue');
});

// Set up event listeners
function initializeEventListeners() {
    // Report tab buttons
    document.querySelectorAll('.report-tab').forEach(tab => {
        tab.addEventListener('click', (e) => {
            const reportType = e.target.dataset.report;
            setActiveTab(reportType);
            currentReport = reportType; // Update currentReport before calling updateSortOptions
            updateSortOptions();
            loadReport(reportType);
        });
    });

    // Apply filters button
    document.getElementById('applyFiltersBtn').addEventListener('click', () => {
        loadReport(currentReport);
    });

    // Clear filters button
    document.getElementById('clearFiltersBtn').addEventListener('click', () => {
        document.getElementById('startDate').value = '';
        document.getElementById('endDate').value = '';
        document.getElementById('limitResults').value = '10';
        loadReport(currentReport);
    });

    // Sort button
    document.getElementById('applySortBtn').addEventListener('click', () => {
        sortConfig.field = document.getElementById('sortBy').value;
        sortConfig.order = document.getElementById('sortOrder').value;
        sortAndDisplayData();
    });

    // Clickable table headers for sorting
    document.querySelectorAll('th[data-sort]').forEach(th => {
        th.style.cursor = 'pointer';
        th.addEventListener('click', () => {
            const field = th.dataset.sort;
            if (sortConfig.field === field) {
                sortConfig.order = sortConfig.order === 'asc' ? 'desc' : 'asc';
            } else {
                sortConfig.field = field;
                sortConfig.order = 'asc';
            }
            document.getElementById('sortBy').value = field;
            document.getElementById('sortOrder').value = sortConfig.order;
            sortAndDisplayData();
        });
    });
}

// Set active tab styling
function setActiveTab(reportType) {
    document.querySelectorAll('.report-tab').forEach(tab => {
        tab.classList.remove('active');
        if (tab.dataset.report === reportType) {
            tab.classList.add('active');
        }
    });
}

// Update sort dropdown options based on report type
function updateSortOptions() {
    const sortBySelect = document.getElementById('sortBy');
    sortBySelect.innerHTML = sortOptions[currentReport]
        .map(opt => `<option value="${opt.value}">${opt.label}</option>`)
        .join('');
    sortConfig.field = '';
    sortConfig.order = 'asc';
    document.getElementById('sortOrder').value = 'asc';
}

// Load report data from API
async function loadReport(reportType) {
    currentReport = reportType;
    showLoading(true);
    hideError();
    hideAllTables();

    const startDate = document.getElementById('startDate').value;
    const endDate = document.getElementById('endDate').value;
    const limit = document.getElementById('limitResults').value;

    // Comment 1: Use overdue-filtered endpoint when overdue report has date filters
    let url;
    if (reportType === 'overdue' && (startDate || endDate)) {
        url = '/api/reports/overdue-filtered';
    } else {
        url = `/api/reports/${reportType}`;
    }
    
    const params = new URLSearchParams();

    // For overdue-filtered, pass startDate and endDate if present
    if (url.includes('/overdue-filtered')) {
        if (startDate) params.append('startDate', startDate);
        if (endDate) params.append('endDate', endDate);
        if (limit) params.append('limit', limit);
    } else {
        // For other endpoints, pass all parameters
        if (startDate) params.append('startDate', startDate);
        if (endDate) params.append('endDate', endDate);
        if (limit) params.append('limit', limit);
    }

    if (params.toString()) url += '?' + params.toString();

    try {
        const response = await fetch(url);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        currentData = data;
        
        showLoading(false);
        updateReportSummary(reportType, data.length);
        
        if (data.length === 0) {
            showNoData(true);
            return;
        }

        showNoData(false);
        displayReport(reportType, data);
    } catch (error) {
        showLoading(false);
        showError(`Failed to load report: ${error.message}`);
        console.error('Error loading report:', error);
    }
}

// Display report based on type
function displayReport(reportType, data) {
    if (reportType === 'overdue') {
        displayOverdueBooks(data);
    } else if (reportType === 'most-borrowed') {
        displayMostBorrowedBooks(data);
    } else if (reportType === 'member-activity') {
        displayMemberActivity(data);
    }
}

// Display overdue books
function displayOverdueBooks(books) {
    const table = document.getElementById('overdueTable');
    const tbody = document.getElementById('overdueTableBody');
    
    tbody.innerHTML = books.map(record => {
        const daysOverdue = calculateDaysOverdue(record.dueDate);
        return `
            <tr class="${daysOverdue > 14 ? 'severe-overdue' : daysOverdue > 7 ? 'moderate-overdue' : ''}">
                <td>${record.book?.title || 'N/A'}</td>
                <td>${record.book?.author || 'N/A'}</td>
                <td>${record.member?.name || 'N/A'}</td>
                <td>${formatDate(record.dueDate)}</td>
                <td>${daysOverdue} days</td>
            </tr>
        `;
    }).join('');
    
    table.style.display = 'table';
}

// Display most borrowed books
function displayMostBorrowedBooks(data) {
    const table = document.getElementById('mostBorrowedTable');
    const tbody = document.getElementById('mostBorrowedTableBody');
    
    tbody.innerHTML = data.map((item, index) => `
        <tr class="${index < 3 ? 'top-borrowed' : ''}">
            <td>${item.title || 'N/A'}</td>
            <td>${item.author || 'N/A'}</td>
            <td><strong>${item.borrowCount || 0}</strong></td>
        </tr>
    `).join('');
    
    table.style.display = 'table';
}

// Display member activity
function displayMemberActivity(data) {
    const table = document.getElementById('memberActivityTable');
    const tbody = document.getElementById('memberActivityTableBody');
    
    tbody.innerHTML = data.map((item, index) => `
        <tr class="${index < 3 ? 'top-activity' : ''}">
            <td>${item.memberName || 'N/A'}</td>
            <td>${item.memberEmail || 'N/A'}</td>
            <td><strong>${item.activityCount || 0}</strong></td>
        </tr>
    `).join('');
    
    table.style.display = 'table';
}

// Sort and redisplay data
function sortAndDisplayData() {
    if (!sortConfig.field || currentData.length === 0) {
        displayReport(currentReport, currentData);
        return;
    }

    const sortedData = [...currentData].sort((a, b) => {
        let valueA, valueB;

        // Get values based on report type and field
        switch (currentReport) {
            case 'overdue':
                valueA = getOverdueValue(a, sortConfig.field);
                valueB = getOverdueValue(b, sortConfig.field);
                break;
            case 'most-borrowed':
                valueA = getMostBorrowedValue(a, sortConfig.field);
                valueB = getMostBorrowedValue(b, sortConfig.field);
                break;
            case 'member-activity':
                valueA = getMemberActivityValue(a, sortConfig.field);
                valueB = getMemberActivityValue(b, sortConfig.field);
                break;
            default:
                return 0;
        }

        // Handle null/undefined
        if (valueA == null) return 1;
        if (valueB == null) return -1;

        // Compare values
        let comparison = 0;
        if (typeof valueA === 'number' && typeof valueB === 'number') {
            comparison = valueA - valueB;
        } else {
            comparison = String(valueA).localeCompare(String(valueB));
        }

        return sortConfig.order === 'desc' ? -comparison : comparison;
    });

    displayReport(currentReport, sortedData);
}

// Get value from overdue record for sorting
function getOverdueValue(record, field) {
    switch (field) {
        case 'title': return record.book?.title;
        case 'author': return record.book?.author;
        case 'member': return record.member?.name;
        case 'dueDate': return new Date(record.dueDate).getTime();
        case 'daysOverdue': return calculateDaysOverdue(record.dueDate);
        default: return null;
    }
}

// Get value from most borrowed record for sorting
function getMostBorrowedValue(record, field) {
    switch (field) {
        case 'title': return record.title;
        case 'author': return record.author;
        case 'borrowCount': return record.borrowCount;
        default: return null;
    }
}

// Get value from member activity record for sorting
function getMemberActivityValue(record, field) {
    switch (field) {
        case 'memberName': return record.memberName;
        case 'memberEmail': return record.memberEmail;
        case 'activityCount': return record.activityCount;
        default: return null;
    }
}

// Helper functions
function calculateDaysOverdue(dueDateStr) {
    if (!dueDateStr) return 0;
    const dueDate = new Date(dueDateStr);
    const today = new Date();
    const diffTime = today - dueDate;
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    return diffDays > 0 ? diffDays : 0;
}

function formatDate(dateStr) {
    if (!dateStr) return 'N/A';
    const date = new Date(dateStr);
    return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
    });
}

function getReportTitle(type) {
    switch (type) {
        case 'overdue': return 'Overdue Books Report';
        case 'most-borrowed': return 'Most Borrowed Books Report';
        case 'member-activity': return 'Member Activity Report';
        default: return 'Report';
    }
}

// UI helper functions
function showLoading(show) {
    document.getElementById('loadingIndicator').style.display = show ? 'block' : 'none';
}

function showError(message) {
    const errorEl = document.getElementById('errorMessage');
    errorEl.textContent = message;
    errorEl.style.display = 'block';
}

function hideError() {
    document.getElementById('errorMessage').style.display = 'none';
}

function showNoData(show) {
    document.getElementById('noDataMessage').style.display = show ? 'block' : 'none';
}

function hideAllTables() {
    document.getElementById('overdueTable').style.display = 'none';
    document.getElementById('mostBorrowedTable').style.display = 'none';
    document.getElementById('memberActivityTable').style.display = 'none';
}

function updateReportSummary(reportType, count) {
    document.getElementById('reportTitle').textContent = getReportTitle(reportType);
    document.getElementById('recordCount').textContent = `${count} record${count !== 1 ? 's' : ''} found`;
}
