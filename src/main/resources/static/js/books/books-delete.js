function deleteBook(id) {
    if (confirm("Are you sure you want to delete this book?")) {
        fetch(`/api/books/${id}`, {
            method: "DELETE"
        }).then(response => {
            if (response.ok) { 
                alert("Book deleted successfully");
                loadBooks();
            } else {
                alert("Failed to delete book");
            }
        });
    }
}