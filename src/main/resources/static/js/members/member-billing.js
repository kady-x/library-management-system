const pathParts = window.location.pathname.split('/');
const memberId = pathParts[pathParts.length - 1];

function showError(message) {
    const existingError = document.querySelector('.error-message');
    if (existingError) {
        existingError.remove();
    }

    const errorDiv = document.createElement('div');
    errorDiv.className = 'error-message';
    errorDiv.style.cssText = `
        background: #ff4757;
        color: white;
        padding: 12px;
        border-radius: 6px;
        margin-bottom: 20px;
        text-align: center;
        font-weight: bold;
    `;
    errorDiv.textContent = message;

    const form = document.getElementById("billingForm");
    form.parentNode.insertBefore(errorDiv, form);

    setTimeout(() => {
        if (errorDiv.parentNode) {
            errorDiv.remove();
        }
    }, 5000);
}

function showSuccess(message) {
    const existingMessage = document.querySelector('.success-message, .error-message');
    if (existingMessage) {
        existingMessage.remove();
    }

    const successDiv = document.createElement('div');
    successDiv.className = 'success-message';
    successDiv.style.cssText = `
        background: #2ed573;
        color: white;
        padding: 12px;
        border-radius: 6px;
        margin-bottom: 20px;
        text-align: center;
        font-weight: bold;
    `;
    successDiv.textContent = message;

    const container = document.querySelector('.container');
    container.insertBefore(successDiv, container.firstChild);

    setTimeout(() => {
        if (successDiv.parentNode) {
            successDiv.remove();
        }
    }, 3000);
}

function setLoadingState(loading) {
    const submitBtn = document.querySelector('.add-btn-member');
    const addPaymentBtn = document.getElementById('addPaymentBtn');
    const resetBtn = document.querySelector('.reset-btn-member');
    const inputs = document.querySelectorAll('input, textarea');

    if (loading) {
        submitBtn.disabled = true;
        addPaymentBtn.disabled = true;
        resetBtn.disabled = true;
        inputs.forEach(input => input.disabled = true);
    } else {
        submitBtn.disabled = false;
        addPaymentBtn.disabled = false;
        resetBtn.disabled = false;
        inputs.forEach(input => input.disabled = false);
    }
}

async function loadMemberAndBilling(memberId) {
    try {
        setLoadingState(true);

        const memberResponse = await fetch(`/api/members/${memberId}`);
        if (!memberResponse.ok) {
            showError("Member not found");
            setTimeout(() => {
                window.location.href = "/members";
            }, 2000);
            return;
        }

        const member = await memberResponse.json();
        
        document.getElementById("memberId").value = member.memberId;
        document.getElementById("memberName").value = member.name;
        document.getElementById("memberContact").value = member.contactInfo;

        try {
            const billingResponse = await fetch(`/api/billing/status/${memberId}`);
            if (billingResponse.ok) {
                const billingData = await billingResponse.json();
                
                const totalFinesInput = document.getElementById('totalFines');
                if (totalFinesInput && billingData.totalFines !== undefined) {
                    totalFinesInput.value = billingData.totalFines;
                }

                const paymentHistoryTextarea = document.getElementById('paymentHistoryText');
                if (paymentHistoryTextarea && billingData.paymentHistoryText) {
                    const historyLines = billingData.paymentHistoryText.split(';').filter(line => line.trim());
                    paymentHistoryTextarea.value = historyLines.join('\n');
                }
                
                showSuccess("Billing information loaded successfully");
            } else {
                document.getElementById('totalFines').value = 0;
                document.getElementById('paymentHistoryText').value = '';
                showSuccess("No existing billing data found. Ready to create new billing record.");
            }
        } catch (error) {
            console.error("Failed to load billing data:", error);
            document.getElementById('totalFines').value = 0;
            document.getElementById('paymentHistoryText').value = '';
        }

    } catch (error) {
        console.error("Failed to load member:", error);
        showError("Failed to load member data");
    } finally {
        setLoadingState(false);
    }
}

async function saveBillingInformation() {
    const memberId = document.getElementById("memberId").value;
    const totalFines = parseFloat(document.getElementById("totalFines").value) || 0;
    const paymentHistoryText = document.getElementById("paymentHistoryText").value.trim();

    const formattedPaymentHistory = paymentHistoryText ? 
        paymentHistoryText.split('\n').filter(line => line.trim()).join(';') : null;

    const data = {
        totalFines: totalFines,
        paymentHistoryText: formattedPaymentHistory
    };

    setLoadingState(true);

    try {
        const response = await fetch(`/api/billing/${memberId}/info`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data)
        });

        if (response.ok) {
            showSuccess("Billing information saved successfully!");
        } else {
            const error = await response.text();
            showError("Failed to save billing information: " + error);
        }
    } catch (error) {
        console.error("Network error:", error);
        showError("Network error occurred. Please try again.");
    } finally {
        setLoadingState(false);
    }
}

async function addPayment() {
    const paymentAmount = prompt("Enter payment amount (EGP):");
    if (paymentAmount === null || paymentAmount === "") {
        return;
    }

    const amount = parseFloat(paymentAmount);
    if (isNaN(amount) || amount <= 0) {
        showError("Please enter a valid payment amount");
        return;
    }

    const memberId = document.getElementById("memberId").value;

    setLoadingState(true);

    try {
        const response = await fetch(`/api/billing/${memberId}/payment`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ amount: amount })
        });

        if (response.ok) {
            showSuccess(`Payment of ${amount} EGP recorded successfully!`);
            setTimeout(() => {
                window.location.reload();
            }, 1500);
        } else {
            const error = await response.text();
            showError("Failed to record payment: " + error);
        }
    } catch (error) {
        console.error("Network error:", error);
        showError("Network error occurred. Please try again.");
    } finally {
        setLoadingState(false);
    }
}

document.getElementById("billingForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    await saveBillingInformation();
});

document.getElementById("addPaymentBtn").addEventListener("click", addPayment);

if (memberId) {
    loadMemberAndBilling(memberId);
} else {
    showError("Invalid member ID");
    setTimeout(() => {
        window.location.href = "/members";
    }, 2000);
}
