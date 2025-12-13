const pathParts = window.location.pathname.split('/');
const bookID = pathParts[pathParts.length - 1];

async function loadBook() {
    const response = await fetch(`/api/books/${bookID}`);
    const book = await response.json();
    const table = document.getElementById("bookTable");
    table.innerHTML = "";

    const rows = `
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
    `;

    table.innerHTML = rows;
}

loadBook();