async function loadMembersForFilter() {
    try {
        const response = await fetch("/api/members");
        const members = await response.json();

        const memberFilterSelect = document.getElementById("memberFilterSelect");

        while (memberFilterSelect.options.length > 1) {
            memberFilterSelect.remove(1);
        }

        members.forEach(member => {
            const option = document.createElement("option");
            option.value = member.memberId;
            option.textContent = `${member.name} (ID: ${member.memberId})`;
            memberFilterSelect.appendChild(option);
        });

        if (!memberFilterSelect.hasAttribute('data-listener-attached')) {
            memberFilterSelect.addEventListener("change", filterByMember);
            memberFilterSelect.setAttribute('data-listener-attached', 'true');
        }

        const clearFilterBtn = document.getElementById("clearFilterBtn");
        if (clearFilterBtn && !clearFilterBtn.hasAttribute('data-listener-attached')) {
            clearFilterBtn.addEventListener("click", clearFilter);
            clearFilterBtn.setAttribute('data-listener-attached', 'true');
        }
    } catch (error) {
        console.error("Failed to load members for filter:", error);
    }
}

function displayBorrowRecords(records) {
    const borrowRecordSelect = document.getElementById("borrowRecordSelect");
    const borrowRecordIdInput = document.getElementById("borrowRecordId");
    const activeBorrowsTable = document.getElementById("activeBorrowsTable");
    const filterStatus = document.getElementById("filterStatus");

    while (borrowRecordSelect.options.length > 1) {
        borrowRecordSelect.remove(1);
    }

    activeBorrowsTable.querySelector("tbody").innerHTML = "";

    const memberFilterSelect = document.getElementById("memberFilterSelect");
    const selectedMemberId = memberFilterSelect ? memberFilterSelect.value : "";
    if (selectedMemberId) {
        const selectedMember = Array.from(memberFilterSelect.options).find(option => option.value === selectedMemberId);
        filterStatus.textContent = `Showing borrow records for: ${selectedMember ? selectedMember.textContent : selectedMemberId}`;
        filterStatus.style.display = 'block';
    } else {
        filterStatus.textContent = '';
        filterStatus.style.display = 'none';
    }

    records.forEach(record => {
        if (!record.returnStatus) {
            const dueDate = new Date(record.dueDate);
            const today = new Date();
            const isOverdue = dueDate < today;
            const daysOverdue = isOverdue ? Math.floor((today - dueDate) / (1000 * 60 * 60 * 24)) : 0;

            const option = document.createElement("option");
            option.value = record.borrowID;
            option.textContent = `ID ${record.borrowID}: ${record.book.title} by ${record.member.name}${isOverdue ? ' (OVERDUE)' : ''}`;
            borrowRecordSelect.appendChild(option);

            const row = document.createElement("tr");
            row.className = isOverdue ? 'overdue-row' : '';
            row.innerHTML = `
                <td>${record.borrowID}</td>
                <td>${record.member.name}</td>
                <td>${record.book.title}</td>
                <td>${record.borrowDate}</td>
                <td class="${isOverdue ? 'overdue-date' : ''}">${record.dueDate}${isOverdue ? ` (${daysOverdue} days overdue)` : ''}</td>
                <td>
                    <button onclick="selectBorrowRecordShared(${record.borrowID})" class="borrow-button">Return</button>
                    <button onclick="renewBorrowRecord(${record.borrowID})" class="renew-button">Renew</button>
                </td>
            `;
            activeBorrowsTable.querySelector("tbody").appendChild(row);
        }
    });

    if (!borrowRecordSelect.hasAttribute('data-listener-attached')) {
        borrowRecordSelect.addEventListener('change', (e) => {
            borrowRecordIdInput.value = e.target.value;
        });
        borrowRecordSelect.setAttribute('data-listener-attached', 'true');
    }
}

function filterByMember() {
    const memberFilterSelect = document.getElementById("memberFilterSelect");
    const selectedMemberId = memberFilterSelect.value;

    if (!selectedMemberId) {
        displayBorrowRecords(allBorrowRecords);
    } else {
        const filteredRecords = allBorrowRecords.filter(record => 
            record.member.memberId == selectedMemberId
        );
        displayBorrowRecords(filteredRecords);
    }
}

function clearFilter() {
    const memberFilterSelect = document.getElementById("memberFilterSelect");
    memberFilterSelect.value = "";
    displayBorrowRecords(allBorrowRecords);
}

async function loadActiveBorrows() {
    const records = await loadActiveBorrowsShared();
    displayBorrowRecords(records || allBorrowRecords || []);
    await loadMembersForFilter();
}

document.getElementById("returnForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const form = e.target;

    const borrowRecordId = form.borrowRecordId.value.trim();

    if (!borrowRecordId) {
        showMessage("Please select a borrow record to return", "error");
        return;
    }

    if (isNaN(borrowRecordId)) {
        showMessage("Borrow Record ID must be a valid number", "error");
        return;
    }

    const id = parseInt(borrowRecordId);
    if (id <= 0) {
        showMessage("Borrow Record ID must be a positive integer", "error");
        return;
    }

    const submitButton = form.querySelector('button[type="submit"]');
    const originalText = submitButton.textContent;
    submitButton.textContent = "Processing...";
    submitButton.disabled = true;

    try {
        const response = await fetch(`/api/borrow/return/${borrowRecordId}`, { method: "POST" });

        if (response.ok) {
            const borrowRecord = await response.json();
            showMessage("Book returned successfully!", "success");
            form.reset();
            await loadActiveBorrows();
        } else {
            const errorText = await response.text();
            showMessage("Failed to return book: " + errorText, "error");
        }
    } catch (error) {
        console.error("Return error:", error);
        showMessage("An error occurred: " + error.message, "error");
    } finally {
        submitButton.textContent = originalText;
        submitButton.disabled = false;
    }
});

document.addEventListener("DOMContentLoaded", () => {
    loadActiveBorrows();
    document.addEventListener('borrowRecordsUpdated', () => {
        try { displayBorrowRecords(allBorrowRecords); } catch (e) { /* ignore */ }
    });
});

function displayBorrowRecords(records) {
    const borrowRecordSelect = document.getElementById("borrowRecordSelect");
    const borrowRecordIdInput = document.getElementById("borrowRecordId");
    const activeBorrowsTable = document.getElementById("activeBorrowsTable");
    const filterStatus = document.getElementById("filterStatus");

    while (borrowRecordSelect.options.length > 1) {
        borrowRecordSelect.remove(1);
    }

    activeBorrowsTable.querySelector("tbody").innerHTML = "";

    const memberFilterSelect = document.getElementById("memberFilterSelect");
    const selectedMemberId = memberFilterSelect ? memberFilterSelect.value : "";
    if (selectedMemberId) {
        const selectedMember = Array.from(memberFilterSelect.options).find(option => option.value === selectedMemberId);
        filterStatus.textContent = `Showing borrow records for: ${selectedMember ? selectedMember.textContent : selectedMemberId}`;
        filterStatus.style.display = 'block';
    } else {
        filterStatus.textContent = '';
        filterStatus.style.display = 'none';
    }

    records.forEach(record => {
        if (!record.returnStatus) {
            const dueDate = new Date(record.dueDate);
            const today = new Date();
            const isOverdue = dueDate < today;
            const daysOverdue = isOverdue ? Math.floor((today - dueDate) / (1000 * 60 * 60 * 24)) : 0;

            const option = document.createElement("option");
            option.value = record.borrowID;
            option.textContent = `ID ${record.borrowID}: ${record.book.title} by ${record.member.name}${isOverdue ? ' (OVERDUE)' : ''}`;
            borrowRecordSelect.appendChild(option);

            const row = document.createElement("tr");
            row.className = isOverdue ? 'overdue-row' : '';
            row.innerHTML = `
                <td>${record.borrowID}</td>
                <td>${record.member.name}</td>
                <td>${record.book.title}</td>
                <td>${record.borrowDate}</td>
                <td class="${isOverdue ? 'overdue-date' : ''}">${record.dueDate}${isOverdue ? ` (${daysOverdue} days overdue)` : ''}</td>
                <td>
                    <button onclick="selectBorrowRecordShared(${record.borrowID})" class="borrow-button">Return</button>
                    <button onclick="renewBorrowRecord(${record.borrowID})" class="renew-button">Renew</button>
                </td>
            `;
            activeBorrowsTable.querySelector("tbody").appendChild(row);
        }
    });

    if (!borrowRecordSelect.hasAttribute('data-listener-attached')) {
        borrowRecordSelect.addEventListener('change', (e) => {
            borrowRecordIdInput.value = e.target.value;
        });
        borrowRecordSelect.setAttribute('data-listener-attached', 'true');
    }
}

function filterByMember() {
    const memberFilterSelect = document.getElementById("memberFilterSelect");
    const selectedMemberId = memberFilterSelect.value;

    if (!selectedMemberId) {
        displayBorrowRecords(allBorrowRecords);
    } else {
        const filteredRecords = allBorrowRecords.filter(record => 
            record.member.memberId == selectedMemberId
        );
        displayBorrowRecords(filteredRecords);
    }
}

function clearFilter() {
    const memberFilterSelect = document.getElementById("memberFilterSelect");
    memberFilterSelect.value = "";
    displayBorrowRecords(allBorrowRecords);
}

async function loadActiveBorrows() {
    const records = await loadActiveBorrowsShared();
    displayBorrowRecords(records || allBorrowRecords || []);
    await loadMembersForFilter();
}

document.getElementById("returnForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const form = e.target;

    const borrowRecordId = form.borrowRecordId.value.trim();

    if (!borrowRecordId) {
        showMessage("Please select a borrow record to return", "error");
        return;
    }

    if (isNaN(borrowRecordId)) {
        showMessage("Borrow Record ID must be a valid number", "error");
        return;
    }

    const id = parseInt(borrowRecordId);
    if (id <= 0) {
        showMessage("Borrow Record ID must be a positive integer", "error");
        return;
    }

    const submitButton = form.querySelector('button[type="submit"]');
    const originalText = submitButton.textContent;
    submitButton.textContent = "Processing...";
    submitButton.disabled = true;

    try {
        const response = await fetch(`/api/borrow/return/${borrowRecordId}`, { method: "POST" });

        if (response.ok) {
            const borrowRecord = await response.json();
            showMessage("Book returned successfully!", "success");
            form.reset();
            await loadActiveBorrows();
        } else {
            const errorText = await response.text();
            showMessage("Failed to return book: " + errorText, "error");
        }
    } catch (error) {
        console.error("Return error:", error);
        showMessage("An error occurred: " + error.message, "error");
    } finally {
        submitButton.textContent = originalText;
        submitButton.disabled = false;
    }
});

document.addEventListener("DOMContentLoaded", () => {
    loadActiveBorrows();
    document.addEventListener('borrowRecordsUpdated', () => {
        try { displayBorrowRecords(allBorrowRecords); } catch (e) {}
    });
});

document.getElementById("returnForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const form = e.target;

    const borrowRecordId = form.borrowRecordId.value.trim();

    if (!borrowRecordId) {
        showMessage("Please select a borrow record to return", "error");
        return;
    }

    if (isNaN(borrowRecordId)) {
        showMessage("Borrow Record ID must be a valid number", "error");
        return;
    }

    const id = parseInt(borrowRecordId);
    if (id <= 0) {
        showMessage("Borrow Record ID must be a positive integer", "error");
        return;
    }

    const submitButton = form.querySelector('button[type="submit"]');
    const originalText = submitButton.textContent;
    submitButton.textContent = "Processing...";
    submitButton.disabled = true;

    try {
        const response = await fetch(`/api/borrow/return/${borrowRecordId}`, { method: "POST" });

        if (response.ok) {
            const borrowRecord = await response.json();
            showMessage("Book returned successfully!", "success");
            form.reset();
            await loadActiveBorrows();
        } else {
            const errorText = await response.text();
            showMessage("Failed to return book: " + errorText, "error");
        }
    } catch (error) {
        console.error("Return error:", error);
        showMessage("An error occurred: " + error.message, "error");
    } finally {
        submitButton.textContent = originalText;
        submitButton.disabled = false;
    }
});

document.addEventListener("DOMContentLoaded", () => {
    loadActiveBorrows();
});
