document.getElementById("memberForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const memberId = e.target.memberId.value;
    await loadBillingData(memberId);
});

document.getElementById("payForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    try {
        const memberId = document.getElementById("memberId").value;
        const amount = parseFloat(e.target.amount.value);

        if (!amount || amount <= 0) {
            alert("Please enter a valid amount greater than 0");
            return;
        }

        const response = await fetch(`/api/billing/${memberId}/payment`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ amount: amount })
        });

        if (!response.ok) {
            const error = await response.text();
            throw new Error(error || `HTTP error! status: ${response.status}`);
        }

        const result = await response.json();
        alert(`Payment of ${amount.toFixed(2)} EGP processed successfully`);
        e.target.reset();
        await loadBillingData(memberId);

    } catch (error) {
        console.error("Error processing payment:", error);
        alert("Failed to process payment: " + error.message);
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
