const pathParts = window.location.pathname.split('/');
const memberId = pathParts[pathParts.length - 1];

async function loadMember() {
    const response = await fetch(`/api/members/${memberId}`);
    const member = await response.json();

    const table = document.getElementById("memberTable");
    table.innerHTML = "";

    const rows = `
        <tr>
            <td>${member.memberId}</td>
            <td>${member.name}</td>
            <td>${member.contactInfo}</td>
            <td>
                <a href="/members/edit/${member.memberId}" class="edit-btn">Edit</a>
                <button onclick="deleteMember(${member.memberId})">Delete</button>
            </td>
        </tr>
    `;

    table.innerHTML = rows;
}

loadMember();
