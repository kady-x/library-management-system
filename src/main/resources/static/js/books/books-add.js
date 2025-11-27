document.getElementById("addBookForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const form = e.target;
    const data = {
        title: form.title.value,
        author: form.author.value,
        genre: form.genre.value,
        publicationYear: Number(form.publicationYear.value),
        availabilityStatus: form.availabilityStatus.value === "true"
    };

    const response = await fetch("http://localhost:8080/api/books", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    });

    if (response.ok) {
        alert("Book added successfully");
        form.reset();
        window.location.href = "../books";
    } else {
        const error = await response.text();
        alert("Failed to add book: " + error);
    }
});
