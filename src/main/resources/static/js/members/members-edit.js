const pathParts = window.location.pathname.split('/');
const memberId = pathParts[pathParts.length - 1];

async function loadMember(id) {
    const response = await fetch(`http://localhost:8080/api/members/${id}`);

    if (!response.ok) {
        alert("Member not found");
        return;
    }

    const member = await response.json();
    document.getElementById("memberID").value = member.memberID;
    document.querySelector("input[name='name']").value = member.name;
    document.querySelector("input[name='contactInfo']").value = member.contactInfo;
}

if (memberId) {
    loadMember(memberId);
}

document.getElementById("editMemberForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const form = e.target;
    const id = document.getElementById("memberID").value;
    const data = {
        name: form.name.value,
        contactInfo: form.contactInfo.value,
    };

    const response = await fetch(`http://localhost:8080/api/members/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    });

    if (response.ok) {
        alert("Book updated successfully");
        window.location.href = "../../books";
    } else {
        alert("Failed to update book");
    }
});
