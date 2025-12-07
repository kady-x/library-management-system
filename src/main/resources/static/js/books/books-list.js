async function loadBooks(query = null) {
    let url = "http://localhost:8080/api/books";
    if (query) {
        url = `http://localhost:8080/api/books/search?query=${encodeURIComponent(query)}`;
    }
    const response = await fetch(url);
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

document.getElementById("searchBtn").addEventListener("click", () => {
    const query = document.getElementById("searchQuery").value.trim();
    loadBooks(query || null);
});

document.getElementById("showAllBtn").addEventListener("click", () => {
    document.getElementById("searchQuery").value = "";
    loadBooks();
});

loadBooks();
