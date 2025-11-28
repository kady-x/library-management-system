async function loadBooks() {
    const response = await fetch("http://localhost:8080/api/books");
    const books = await response.json();

    const table = document.getElementById("bookTable");
    table.innerHTML = "";

    const rows = books.map(book => `
        <tr>
            <td>${book.bookID}</td>
            <td>${book.title}</td>
            <td>${book.author}</td>
            <td>${book.genre}</td>
            <td>${book.publicationYear}</td>
            <td>${book.availabilityStatus}</td>
            <td>
                <a href="/books/edit/${book.bookID}" class="edit-btn">Edit</a>
                <button onclick="deleteBook(${book.bookID})">Delete</button>
            </td>
        </tr>
    `).join("");

    table.innerHTML = rows;
}

function deleteBook(id) {
    if (confirm("Are you sure you want to delete this book?")) {
        fetch(`http://localhost:8080/api/books/${id}`, {
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

loadBooks();