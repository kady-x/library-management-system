async function loadBooks() {
    const response = await fetch("http://localhost:8080/api/books");
    const books = await response.json();

    const table = document.getElementById("bookTable");
    table.innerHTML = "";

    books.forEach(book => {
        const row = `
            <tr>
                <td>${book.bookID}</td>
                <td>${book.title}</td>
                <td>${book.author}</td>
                <td>${book.genre}</td>
                <td>${book.publicationYear}</td>
                <td>${book.availabilityStatus}</td>
            </tr>
        `;
        table.innerHTML += row;
    });
}

loadBooks();