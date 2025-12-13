let allMembers = [];

async function loadMembers() {
    try {
        const response = await fetch("/api/members");
        const members = await response.json();

        allMembers = members;
        updateStatistics(members);
        renderMembers(members);
    } catch (error) {
        console.error("Failed to load members:", error);
    }
}

function updateStatistics(members) {
    const totalMembersElement = document.getElementById("totalMembers");
    const activeMembersElement = document.getElementById("activeMembers");

    if (totalMembersElement) {
        totalMembersElement.textContent = members.length;
    }

    if (activeMembersElement) {
        activeMembersElement.textContent = members.length;
    }
}

function renderMembers(members) {
    const table = document.getElementById("memberTable");
    const emptyState = document.getElementById("emptyState");

    if (members.length === 0) {
        table.innerHTML = "";
        if (emptyState) {
            emptyState.style.display = "block";
        }
        return;
    }

    if (emptyState) {
        emptyState.style.display = "none";
    }

    const rows = members.map(member => `
        <tr>
            <td>${member.memberId}</td>
            <td>${member.name}</td>
            <td>${member.contactInfo}</td>
            <td>${formatDate(member.membershipDate)}</td>
            <td>
                <a href="/members/edit/${member.memberId}" class="action-link-member">Edit</a>
                <button onclick="deleteMember(${member.memberId})" class="delete-btn">Delete</button>
                <button onclick="viewBorrowedBooks(${member.memberId})" class="view-borrowed-btn">View Borrowed Books</button>
                <button onclick="manageBilling(${member.memberId})" class="billing-btn">Manage Billing</button>
            </td>
        </tr>
    `).join("");

    table.innerHTML = rows;
}

function formatDate(dateString) {
    if (!dateString || dateString === "0001-01-01") {
        return "Not set";
    }

    try {
        const date = new Date(dateString);
        return date.toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'short',
            day: 'numeric'
        });
    } catch (e) {
        return dateString;
    }
}

function manageBilling(memberId) {
    window.location.href = `/members/billing/${memberId}`;
}

function viewBorrowedBooks(memberId) {
    sessionStorage.setItem('filterMemberId', memberId);
    window.location.href = '/borrow';
}

document.getElementById("memberSearch").addEventListener("input", (e) => {
    const searchTerm = e.target.value.toLowerCase().trim();
    const clearBtn = document.getElementById("clearSearch");

    if (searchTerm) {
        clearBtn.style.display = "block";
        const filteredMembers = allMembers.filter(member =>
            member.name.toLowerCase().includes(searchTerm) ||
            member.contactInfo.toLowerCase().includes(searchTerm)
        );
        renderMembers(filteredMembers);
    } else {
        clearBtn.style.display = "none";
        renderMembers(allMembers);
    }
});

document.getElementById("clearSearch").addEventListener("click", () => {
    const searchInput = document.getElementById("memberSearch");
    searchInput.value = "";
    searchInput.dispatchEvent(new Event("input"));
});

document.getElementById("refreshBtn").addEventListener("click", () => {
    loadMembers();
});

document.addEventListener("DOMContentLoaded", () => {
    loadMembers();
});
