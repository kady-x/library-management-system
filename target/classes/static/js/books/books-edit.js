const pathParts = window.location.pathname.split('/');
const bookId = pathParts[pathParts.length - 1];

async function loadBook(id) {
    const response = await fetch(`http://localhost:8080/api/books/${id}`);

    if (!response.ok) {
        alert("Book not found");
        return;
    }

    const book = await response.json();

    document.getElementById("bookID").value = book.bookID;
    document.querySelector("input[name='title']").value = book.title;
    document.querySelector("input[name='author']").value = book.author;
    document.querySelector("input[name='genre']").value = book.genre;
    document.querySelector("input[name='coverUrl']").value = book.coverUrl;
    document.querySelector("input[name='publicationYear']").value = book.publicationYear;
    document.querySelector("select[name='availabilityStatus']").value = book.availabilityStatus;
}

if (bookId) {
    loadBook(bookId);
}

document.getElementById("editBookForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const form = e.target;
    const id = document.getElementById("bookID").value;

    const data = {
        title: form.title.value,
        author: form.author.value,
        genre: form.genre.value,
        coverUrl: form.coverUrl.value,
        publicationYear: Number(form.publicationYear.value),
        availabilityStatus: form.availabilityStatus.value === "true"
    };

    const response = await fetch(`http://localhost:8080/api/books/${id}`, {
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
