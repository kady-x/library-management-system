document.getElementById("addMemberForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const form = e.target;
    const data = {
        name: form.name.value,
        contactInfo: form.contactInfo.value
    };

    const response = await fetch("http://localhost:8080/api/members", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    });

    if (response.ok) {
        alert("Member added successfully");
        form.reset();
        window.location.href = "../members-list.html";
    } else {
        const error = await response.text();
        alert("Failed to add member: " + error);
    }
});
