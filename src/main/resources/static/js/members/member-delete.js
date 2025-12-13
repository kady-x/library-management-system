function deleteMember(id) {
    if (confirm("Are you sure you want to delete this member?")) {
        fetch(`/api/members/${id}`, {
            method: "DELETE"
        }).then(response => {
            if (response.ok) {
                alert("Member deleted successfully");
                loadMembers();
            } else {
                alert("Failed to delete member");
            }
        });
    }
}
