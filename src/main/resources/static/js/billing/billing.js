document.getElementById("memberForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const memberId = e.target.memberId.value;
    await loadBillingData(memberId);
});

document.getElementById("payForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const memberId = document.getElementById("memberId").value;
    const amount = parseFloat(e.target.amount.value);

    const response = await fetch(`/api/billing/${memberId}?amount=${amount}`, {
        method: "POST"
    });

    if (response.ok) {
        alert("Payment processed successfully");
        e.target.reset();
        await loadBillingData(memberId);
    } else {
        const error = await response.text();
        alert("Failed to process payment: " + error);
    }
});

async function loadBillingData(memberId) {
    try {
        const response = await fetch(`/api/billing/status/${memberId}`);

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();

        document.getElementById("totalFinesAmount").textContent = data.totalFines.toFixed(2) + " EGP";
        document.getElementById("statusMessage").textContent = data.status;
        document.getElementById("totalFinesSection").style.display = "block";

        const tbody = document.getElementById("paymentHistoryTable");
        tbody.innerHTML = "";
        if (data.paymentHistory && data.paymentHistory.length > 0) {
            data.paymentHistory.forEach(entry => {
                const row = document.createElement("tr");
                const cell = document.createElement("td");
                cell.textContent = entry;
                row.appendChild(cell);
                tbody.appendChild(row);
            });
        } else {
            const row = document.createElement("tr");
            const cell = document.createElement("td");
            cell.textContent = "No payment history";
            row.appendChild(cell);
            tbody.appendChild(row);
        }
        document.getElementById("paymentHistorySection").style.display = "block";

        document.getElementById("payFormSection").style.display = "block";

    } catch (error) {
        alert("Failed to load billing data: " + error.message);
        console.error("Error loading billing data:", error);
    }
}
