function deleteMember(id) {
    if (confirm("Are you sure you want to delete this member?")) {
        fetch(`http://localhost:8080/api/members/${id}`, {
            method: "DELETE"
        }).then(response => {
            if (response.ok) { 
                alert("Member deleted successfully");
                loadBooks();
            } else {
                alert("Failed to delete member");
            }
        });
    }
}