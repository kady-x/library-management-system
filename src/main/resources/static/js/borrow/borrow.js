async function loadMembers() {
    try {
        const response = await fetch("/api/members");
        const members = await response.json();

        const memberSelect = document.getElementById("memberSelect");
        const memberIdInput = document.getElementById("memberId");

        while (memberSelect.options.length > 1) {
            memberSelect.remove(1);
        }

        members.forEach(member => {
            const option = document.createElement("option");
            option.value = member.memberId;
            option.textContent = `${member.name} (ID: ${member.memberId})`;
            memberSelect.appendChild(option);
        });

        const currentValue = memberIdInput.value;
        if (currentValue && !Array.from(memberSelect.options).some(option => option.value === currentValue)) {
            memberSelect.value = "";
            memberIdInput.value = "";
            console.log("Reset selection - member no longer available:", currentValue);
        }

        if (!memberSelect.hasAttribute('data-listener-attached')) {
            memberSelect.addEventListener("change", (e) => {
                memberIdInput.value = e.target.value;
            });
            memberSelect.setAttribute('data-listener-attached', 'true');
        }
    } catch (error) {
        console.error("Failed to load members:", error);
    }
}

async function loadBooks() {
    try {
        const response = await fetch("/api/books");
        const books = await response.json();

        const bookSelect = document.getElementById("bookSelect");
        const bookIdInput = document.getElementById("bookId");
        const availableBooksTable = document.getElementById("availableBooksTable");

        while (bookSelect.options.length > 1) {
            bookSelect.remove(1);
        }

        availableBooksTable.querySelector("tbody").innerHTML = "";

        books.forEach(book => {
            const quantity = book.quantity != null ? book.quantity : 1;
            const isAvailable = quantity > 0;

            if (isAvailable) {
                const option = document.createElement("option");
                option.value = book.bookID;
                option.textContent = `${book.title} by ${book.author}`;
                bookSelect.appendChild(option);
            }

            const row = document.createElement("tr");
            row.innerHTML = `
                <td>${book.bookID}</td>
                <td>${book.title}</td>
                <td>${book.author}</td>
                <td>${book.genre || 'N/A'}</td>
                <td>
                    ${isAvailable ?
                        `<span style="color: green;">${quantity} available</span> |
                         <button onclick="selectBook(${book.bookID})" class="borrow-button">Select</button>` :
                        '<span style="color: red;">All borrowed</span>'
                    }
                </td>
            `;
            availableBooksTable.querySelector("tbody").appendChild(row);
        });

        if (!bookSelect.hasAttribute('data-listener-attached')) {
            bookSelect.addEventListener("change", (e) => {
                bookIdInput.value = e.target.value;
                console.log("Book dropdown changed:", e.target.value);
            });
            bookSelect.setAttribute('data-listener-attached', 'true');
        }

        const currentValue = bookIdInput.value;
        if (currentValue && !Array.from(bookSelect.options).some(option => option.value === currentValue)) {
            bookSelect.value = "";
            bookIdInput.value = "";
            console.log("Reset selection - book no longer available:", currentValue);
        }

    } catch (error) {
        console.error("Failed to load books:", error);
    }
}

function selectBook(bookId) {
    const bookSelect = document.getElementById("bookSelect");
    const bookIdInput = document.getElementById("bookId");

    bookSelect.value = bookId;
    bookIdInput.value = bookId;
    bookSelect.dispatchEvent(new Event('change'));

    document.querySelector(".borrow-container").scrollIntoView({ behavior: "smooth" });
}

document.getElementById("borrowForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const form = e.target;

    const memberId = form.memberId.value.trim();
    const bookId = form.bookId.value.trim();
    const loanDays = form.loanDays.value.trim();

    if (!memberId || !bookId || !loanDays) {
        showMessage("Please select a member and book, and specify loan days", "error");
        return;
    }

    if (isNaN(memberId) || isNaN(bookId) || isNaN(loanDays)) {
        showMessage("Member ID, Book ID, and Loan Days must be valid numbers", "error");
        return;
    }

    const data = {
        memberId: parseInt(memberId),
        bookId: parseInt(bookId),
        loanDays: parseInt(loanDays)
    };

    if (data.memberId <= 0 || data.bookId <= 0 || data.loanDays <= 0) {
        showMessage("Member ID, Book ID, and Loan Days must be positive integers", "error");
        return;
    }

    try {
        const response = await fetch("/api/borrow/issue", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data)
        });

        if (response.ok) {
            const borrowRecord = await response.json();
            showMessage("Book issued successfully!", "success");
            form.reset();
            loadBooks();
            loadMembers();
        } else {
            const error = await response.text();
            showMessage("Failed to issue book: " + error, "error");
        }
    } catch (error) {
        showMessage("An error occurred: " + error.message, "error");
    }
});

document.getElementById("borrowTab").addEventListener("click", () => {
    switchTab("borrow");
});

document.getElementById("returnTab").addEventListener("click", () => {
    switchTab("return");
});

function switchTab(tabName) {
    document.getElementById("borrowTab").classList.remove("active");
    document.getElementById("returnTab").classList.remove("active");
    document.getElementById(tabName + "Tab").classList.add("active");
    document.getElementById("borrowSection").classList.remove("active");
    document.getElementById("returnSection").classList.remove("active");
    document.getElementById(tabName + "Section").classList.add("active");

    if (tabName === "borrow") {
        loadMembers();
        loadBooks();
    } else if (tabName === "return") {
        loadActiveBorrows();
    }
}

function checkAutoSwitchToReturnTab() {
    const filterMemberId = sessionStorage.getItem('filterMemberId');
    if (filterMemberId) {
        console.log('Auto-switching to return tab with member filter:', filterMemberId);
        window.pendingMemberFilter = filterMemberId;
        sessionStorage.removeItem('filterMemberId');
        
        setTimeout(() => {
            switchTab("return");
        }, 100);
    }
}

function applyPendingMemberFilter() {
    if (window.pendingMemberFilter) {
        console.log('Applying pending member filter:', window.pendingMemberFilter);
        const memberFilterSelect = document.getElementById("memberFilterSelect");
        if (memberFilterSelect) {
            memberFilterSelect.value = window.pendingMemberFilter;
            filterByMember();
            window.pendingMemberFilter = null;
        }
    }
}

async function loadActiveBorrows() {
    const records = await loadActiveBorrowsShared();
    displayBorrowRecords(allBorrowRecords || records || []);
    await loadMembersForFilter();
    applyPendingMemberFilter();
}

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
    const selectedMemberId = memberFilterSelect.value;
    if (selectedMemberId) {
        const selectedMember = Array.from(memberFilterSelect.options).find(option => option.value === selectedMemberId);
        filterStatus.textContent = `Showing borrow records for: ${selectedMember.textContent}`;
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
        borrowRecordSelect.addEventListener("change", (e) => {
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
document.addEventListener('DOMContentLoaded', () => {
    loadMembers();
    loadBooks();
    checkAutoSwitchToReturnTab();

    const returnForm = document.getElementById('returnForm');
    if (returnForm && !returnForm.hasAttribute('data-listener-attached')) {
        returnForm.addEventListener('submit', async (e) => {
            e.preventDefault();

            const idStr = document.getElementById('borrowRecordId').value;
            const id = parseInt(idStr);
            if (!id || id <= 0) {
                showMessage('Invalid borrow ID', 'error');
                return;
            }

            try {
                const res = await fetch(`/api/borrow/return/${id}`, { method: 'POST' });
                if (res.ok) {
                    showMessage('Returned!', 'success');
                    returnForm.reset();
                    await loadActiveBorrows();
                } else {
                    const txt = await res.text();
                    showMessage(txt || 'Failed to return', 'error');
                }
            } catch (err) {
                showMessage(err.message || 'Network error', 'error');
            }
        });
        returnForm.setAttribute('data-listener-attached', 'true');
    }

    document.addEventListener('borrowRecordsUpdated', () => {
        try { displayBorrowRecords(allBorrowRecords); } catch (e) {}
    });
});
