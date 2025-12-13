// Enhanced form validation
function validateMemberForm(form) {
    const name = form.name.value.trim();
    const contactInfo = form.contact.value.trim();
    const membershipDate = form.membershipDate.value;

    if (!name) {
        showError("Name is required");
        return false;
    }

    if (name.length < 2) {
        showError("Name must be at least 2 characters long");
        return false;
    }

    if (!contactInfo) {
        showError("Contact information is required");
        return false;
    }

    if (!membershipDate) {
        showError("Membership date is required");
        return false;
    }

    // Check if membership date is not in the future
    const selectedDate = new Date(membershipDate);
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    if (selectedDate > today) {
        showError("Membership date cannot be in the future");
        return false;
    }

    return true;
}

function showError(message) {
    // Remove any existing error messages
    const existingError = document.querySelector('.error-message');
    if (existingError) {
        existingError.remove();
    }

    // Create and show error message
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

    const form = document.getElementById("addMemberForm");
    form.parentNode.insertBefore(errorDiv, form);

    // Auto-hide after 5 seconds
    setTimeout(() => {
        if (errorDiv.parentNode) {
            errorDiv.remove();
        }
    }, 5000);
}

function showSuccess(message) {
    // Remove any existing messages
    const existingMessage = document.querySelector('.success-message, .error-message');
    if (existingMessage) {
        existingMessage.remove();
    }

    // Create and show success message
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

    // Auto-hide after 3 seconds
    setTimeout(() => {
        if (successDiv.parentNode) {
            successDiv.remove();
        }
    }, 3000);
}

function setLoadingState(loading) {
    const submitBtn = document.querySelector('.add-btn-member');
    const resetBtn = document.querySelector('.reset-btn-member');
    const inputs = document.querySelectorAll('input');

    if (loading) {
        submitBtn.disabled = true;
        submitBtn.textContent = 'Adding Member...';
        resetBtn.disabled = true;
        inputs.forEach(input => input.disabled = true);
    } else {
        submitBtn.disabled = false;
        submitBtn.textContent = 'Add Member';
        resetBtn.disabled = false;
        inputs.forEach(input => input.disabled = false);
    }
}

// Set default membership date to today
document.addEventListener('DOMContentLoaded', () => {
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('membershipDate').value = today;
});

document.getElementById("addMemberForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const form = e.target;

    // Validate form
    if (!validateMemberForm(form)) {
        return;
    }

    // Set loading state
    setLoadingState(true);

    const data = {
        name: form.name.value.trim(),
        contactInfo: form.contact.value.trim(),
        membershipDate: form.membershipDate.value
    };

    try {
        const response = await fetch("/api/members", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data)
        });

        if (response.ok) {
            showSuccess("Member added successfully!");
            form.reset();

            // Reset to today's date
            const today = new Date().toISOString().split('T')[0];
            document.getElementById('membershipDate').value = today;

            // Redirect after a short delay to show success message
            setTimeout(() => {
                window.location.href = "/members";
            }, 1500);
        } else {
            const error = await response.text();
            showError("Failed to add member: " + error);
        }
    } catch (error) {
        console.error("Network error:", error);
        showError("Network error occurred. Please try again.");
    } finally {
        setLoadingState(false);
    }
});
