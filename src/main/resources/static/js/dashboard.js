async function fadeInBooks() {
    const cards = document.querySelectorAll('.card-animation');

    cards.forEach((card, index) => {
        card.style.animationDelay = `${index * 0.08}s`;
    });
}

async function loadBooks() {
    const response = await fetch("http://localhost:8080/api/books");
    const books = await response.json();

    const cards = document.getElementById("book-grid");
    const title = document.getElementById("title");
    cards.innerHTML = "No data";
    title.innerHTML = "";

    const dashboardRows = books.map(book => `
        <div class="card-animation">
            <div class="book-card">
                <img src="${book.coverUrl}" alt="No Book">
                <h2>${book.title}</h2>
                <h3><b>Author: </b> <span>${book.author}</span></h3>
            </div>
        </div>
    `).join("");

    if (books[0] == null){
        title.innerHTML = "<p>No books available.</p><p>Be the First to <a href=\"books\" class=\"submit-btn\">Submit</a> a Book!</p>";
    }

    cards.innerHTML = dashboardRows;

    fadeInBooks()
}

loadBooks();