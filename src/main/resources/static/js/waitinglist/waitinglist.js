async function loadBooks() {
    try {
        const response = await fetch("/api/books");
        if (!response.ok) {
            throw new Error('Failed to fetch books');
        }
        const books = await response.json();
        return books;
    } catch (error) {
        console.error('Error loading books:', error);
        return [];
    }
}

async function loadMembers() {
    try {
        const response = await fetch("/api/members");
        if (!response.ok) {
            throw new Error('Failed to fetch members');
        }
        const members = await response.json();
        return members;
    } catch (error) {
        console.error('Error loading members:', error);
        return [];
    }
}

async function populateDropdowns() {
    const books = await loadBooks();
    const members = await loadMembers();
    
    const bookSelect = document.getElementById("bookSelect");
    const memberSelect = document.getElementById("memberSelect");
    bookSelect.innerHTML = '<option value="">Select Book</option>';
    memberSelect.innerHTML = '<option value="">Select Member</option>';
    
    books.forEach(book => {
        const option = document.createElement('option');
        option.value = book.bookID;
        option.textContent = `${book.title} (${book.bookID})`;
        bookSelect.appendChild(option);
    });
    
    members.forEach(member => {
        const option = document.createElement('option');
        option.value = member.memberId;
        option.textContent = `${member.name} (ID: ${member.memberId})`;
        memberSelect.appendChild(option);
    });
}

async function loadWaitingList() {
    try {
        const response = await fetch("/api/waiting-list");
        if (!response.ok) {
            const errorMessage = await response.text();
            alert(errorMessage);
            return;
        }
        const entries = await response.json();

        const table = document.getElementById("waitingListTable");
        table.innerHTML = "";

        if (entries.length === 0) {
            table.innerHTML = '<tr><td colspan="7" style="text-align: center; padding: 20px;">No waiting list entries found</td></tr>';
            return;
        }

        const rows = entries.map(entry => `
            <tr>
                <td>${entry.id}</td>
                <td>${entry.book ? entry.book.title : 'Unknown'}</td>
                <td>${entry.book ? entry.book.bookID : 'Unknown'}</td>
                <td>${entry.member ? entry.member.name : 'Unknown'}</td>
                <td>${entry.member ? entry.member.memberId : 'Unknown'}</td>
                <td>${entry.requestDate ? new Date(entry.requestDate).toLocaleDateString() : 'N/A'}</td>
                <td>
                    <button onclick="removeFromWaitingList(${entry.id})" class="remove-btn">Remove</button>
                </td>
            </tr>
        `).join("");

        table.innerHTML = rows;
    } catch (error) {
        console.error('Error loading waiting list:', error);
        alert('Failed to load waiting list entries');
    }
}

async function addToWaitingList() {
    const bookSelect = document.getElementById("bookSelect");
    const memberSelect = document.getElementById("memberSelect");
    
    const bookId = bookSelect.value;
    const memberId = memberSelect.value;

    if (!bookId || !memberId) {
        alert('Please select both a book and a member');
        return;
    }

    try {
        const response = await fetch(`/api/waiting-list/add/${bookId}?memberId=${memberId}`, {
            method: 'POST'
        });

        if (response.ok) {
            bookSelect.value = '';
            memberSelect.value = '';
            loadWaitingList();
            showSuccessMessage('Successfully added to waiting list');
        } else {
            const error = await response.text();
            showErrorMessage('Failed to add to waiting list: ' + error);
        }
    } catch (error) {
        console.error('Error adding to waiting list:', error);
        showErrorMessage('Failed to add to waiting list');
    }
}

function showSuccessMessage(message) {
    const notification = document.createElement('div');
    notification.className = 'success-notification';
    notification.textContent = message;
    
    document.body.appendChild(notification);
    setTimeout(() => {
        notification.remove();
    }, 3000);
}

function showErrorMessage(message) {
    const notification = document.createElement('div');
    notification.className = 'error-notification';
    notification.textContent = message;
    
    document.body.appendChild(notification);
    setTimeout(() => {
        notification.remove();
    }, 5000);
}

async function removeFromWaitingList(entryId) {
    if (!confirm('Are you sure you want to remove this entry from the waiting list?')) {
        return;
    }

    try {
        const response = await fetch(`/api/waiting-list/remove/${entryId}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            loadWaitingList();
            showSuccessMessage('Successfully removed from waiting list');
        } else {
            const error = await response.text();
            showErrorMessage('Failed to remove from waiting list: ' + error);
        }
    } catch (error) {
        console.error('Error removing from waiting list:', error);
        showErrorMessage('Failed to remove from waiting list');
    }
}

document.addEventListener('DOMContentLoaded', async function() {
    await populateDropdowns();
    loadWaitingList();
    document.getElementById("addBtn").addEventListener("click", addToWaitingList);
});
