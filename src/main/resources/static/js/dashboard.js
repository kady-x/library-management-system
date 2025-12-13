let currentReport = null;

async function fadeInBooks() {
    const cards = document.querySelectorAll('.card-animation');

    cards.forEach((card, index) => {
        card.style.animationDelay = `${index * 0.08}s`;
    });
}

async function loadBooks() {
    const response = await fetch("/api/books");
    const books = await response.json();

    const cards = document.getElementById("book-grid");
    const title = document.getElementById("title");
    cards.innerHTML = "No data";
    title.innerHTML = "";

    const dashboardRows = books.map(book => `
        <div class="card-animation">
            <div class="book-card">
                <img src="${book.coverUrl}" alt="No Book">
                <h2>${book.title}</h2>
                <h3><b>Author: </b> <span>${book.author}</span></h3>
            </div>
        </div>
    `).join("");

    if (books[0] == null){
        title.innerHTML = "<p>No books available.</p><p>Be the First to <a href=\"books\" class=\"submit-btn\">Submit</a> a Book!</p>";
    }

    cards.innerHTML = dashboardRows;

    fadeInBooks()
}

async function loadReport(reportType, startDate = null, endDate = null) {
    currentReport = reportType;
    let url = `/api/reports/${reportType}`;
    const params = new URLSearchParams();
    if (startDate) params.append('startDate', startDate);
    if (endDate) params.append('endDate', endDate);
    if (params.toString()) url += '?' + params.toString();

    const response = await fetch(url);
    const data = await response.json();

    const preview = document.getElementById("report-preview");
    preview.innerHTML = `<h3 class="preview-title">${getReportTitle(reportType)}</h3>`;

    if (reportType === 'overdue') {
        displayOverdueBooks(data);
    } else if (reportType === 'most-borrowed') {
        displayMostBorrowedBooks(data);
    } else if (reportType === 'member-activity') {
        displayMemberActivity(data);
    }
}

function getReportTitle(type) {
    switch(type) {
        case 'overdue': return 'Overdue Books';
        case 'most-borrowed': return 'Most Borrowed Books';
        case 'member-activity': return 'Member Activity';
        default: return 'Report';
    }
}

function displayOverdueBooks(books) {
    const preview = document.getElementById("report-preview");
    if (books.length === 0) {
        preview.innerHTML += '<p class="muted">No overdue books.</p>';
        return;
    }

    const table = document.createElement('table');
    table.className = 'report-table';
    table.innerHTML = `
        <thead>
            <tr>
                <th>Book Title</th>
                <th>Author</th>
                <th>Member</th>
                <th>Due Date</th>
                <th>Days Overdue</th>
            </tr>
        </thead>
        <tbody>
            ${books.map(book => `
                <tr>
                    <td>${book.title}</td>
                    <td>${book.author}</td>
                    <td>${book.memberName}</td>
                    <td>${book.dueDate}</td>
                    <td>${calculateDaysOverdue(book.dueDate)}</td>
                </tr>
            `).join('')}
        </tbody>
    `;
    preview.appendChild(table);
}

function displayMostBorrowedBooks(data) {
    const preview = document.getElementById("report-preview");
    if (data.length === 0) {
        preview.innerHTML += '<p class="muted">No borrowing data available.</p>';
        return;
    }

    const table = document.createElement('table');
    table.className = 'report-table';
    table.innerHTML = `
        <thead>
            <tr>
                <th>Book Title</th>
                <th>Author</th>
                <th>Borrow Count</th>
            </tr>
        </thead>
        <tbody>
            ${data.map(item => `
                <tr>
                    <td>${item.title}</td>
                    <td>${item.author}</td>
                    <td>${item.borrowCount}</td>
                </tr>
            `).join('')}
        </tbody>
    `;
    preview.appendChild(table);
}

function displayMemberActivity(data) {
    const preview = document.getElementById("report-preview");
    if (data.length === 0) {
        preview.innerHTML += '<p class="muted">No member activity data available.</p>';
        return;
    }

    const table = document.createElement('table');
    table.className = 'report-table';
    table.innerHTML = `
        <thead>
            <tr>
                <th>Member Name</th>
                <th>Email</th>
                <th>Activity Count</th>
            </tr>
        </thead>
        <tbody>
            ${data.map(item => `
                <tr>
                    <td>${item.memberName}</td>
                    <td>${item.memberEmail}</td>
                    <td>${item.activityCount}</td>
                </tr>
            `).join('')}
        </tbody>
    `;
    preview.appendChild(table);
}

function calculateDaysOverdue(dueDateStr) {
    const dueDate = new Date(dueDateStr);
    const today = new Date();
    const diffTime = today - dueDate;
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    return diffDays > 0 ? diffDays : 0;
}

loadBooks();

document.querySelectorAll('.report-btn').forEach(btn => {
    btn.addEventListener('click', (e) => {
        e.preventDefault();
        const reportType = btn.getAttribute('data-report');
        loadReport(reportType);
    });
});

document.getElementById('report-filters').addEventListener('submit', (e) => {
    e.preventDefault();
    const startDate = document.getElementById('startDate').value;
    const endDate = document.getElementById('endDate').value;
    if (currentReport) {
        loadReport(currentReport, startDate || null, endDate || null);
    }
});
